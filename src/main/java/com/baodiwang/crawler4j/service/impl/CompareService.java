/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.service.impl.CompareService.java <2018年09月13日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.service.impl;

import com.baodiwang.crawler4j.VO.RemiseNoticeVo;
import com.baodiwang.crawler4j.controller.detailPage.RemiseNoticeDetailParser;
import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.model.RemiseNoticeDetail;
import com.baodiwang.crawler4j.service.RemiseNoticeDetailService;
import com.baodiwang.crawler4j.service.RemiseNoticeService;
import com.baodiwang.crawler4j.utils.LandChinaHttpBreaker3;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月13日 09时55分
 */
@Service
public class CompareService {

    private static final Logger log = LogManager.getLogger(CompareService.class);

    @Autowired
    RemiseNoticeService remiseNoticeService;

    @Autowired
    RemiseNoticeDetailService remiseNoticeDetailService;

    @Autowired
    LandChinaHttpBreaker3 landChinaHttpBreaker3;


    /**
     * 比对数据
     * @param remiseNoticeId remiseNoticeId
     * @return
     */
    @Transactional
    public Boolean compare(Integer remiseNoticeId){
        return compare(remiseNoticeId,true);
    }
    /**
     * 比对数据
     * @param remiseNoticeId remiseNoticeId
     * @param autoUpdate 数据不一致时，是否需要自动更新数据
     * @return
     */
    @Transactional
    public Boolean compare(Integer remiseNoticeId,boolean autoUpdate){
        String logMessage = "比对数据功能remiseNoticeId="+remiseNoticeId+",===================================================";
        if(null == remiseNoticeId || remiseNoticeId <= 0){
            log.error(logMessage + "非法id:"+remiseNoticeId) ;
            return false;
        }
        RemiseNotice remiseNotice = remiseNoticeService.getByPK(remiseNoticeId);
        if(null == remiseNotice || StringUtils.isEmpty(remiseNotice.getHref())){
            log.error(logMessage + "没有该id的数据");
            return false;
        }
        if(StringUtils.isEmpty(remiseNotice.getContent())){
            log.error(logMessage + "没有抓取到该id的网页内容");
            return false;
        }
        RemiseNoticeDetail remiseNoticeDetail = new RemiseNoticeDetail();
        remiseNoticeDetail.setNoticeId((long)remiseNoticeId);
        List<RemiseNoticeDetail> remiseNoticeDetailList = remiseNoticeDetailService.listByProperty(remiseNoticeDetail);
        if(CollectionUtils.isEmpty(remiseNoticeDetailList)){
            log.error(logMessage + "该id的网页内容还未解析");
            return false;
        }

        String webContent = landChinaHttpBreaker3.breakBarrierGet(remiseNotice.getHref(), null);
        if(StringUtils.isEmpty(webContent) || webContent.length() < 8000){
            log.error(logMessage + ",抓取到的网页内容异常，webContent.length()=" + (StringUtils.isEmpty(webContent) ? 0 : webContent.length()) + ",webContent=" + webContent);
            return false;
        }

        RemiseNoticeVo remiseNoticeVo = RemiseNoticeDetailParser.parseHtml(webContent);
        if(null == remiseNoticeVo || CollectionUtils.isEmpty(remiseNoticeVo.getRemiseNoticeDetailList())){
            log.info(logMessage + "解析刚抓取的网页内容异常!");
            return false;

        }

        //详情页数据条数长度不一样
        if(remiseNoticeDetailList.size() != remiseNoticeVo.getRemiseNoticeDetailList().size()){
            if(autoUpdate){
                updateAfterCompareDetail(remiseNotice, remiseNoticeVo.getRemiseNoticeDetailList());
                log.info(logMessage + "详情页条数不一样，已更新数据库");
                return true;
            }else{
                log.warn(logMessage + "详情页条数不一样，需要更新数据库");
                return true;
            }
        }

        boolean updateResult = compareList(remiseNotice,remiseNoticeDetailList ,remiseNoticeVo.getRemiseNoticeDetailList(),autoUpdate);
        if(updateResult){
            log.info(logMessage + "详情页个别数据不一样，已更新数据库");
            return true;
        }else {
            log.info(logMessage + "详情页数据无需更新");
            return null;
        }
    }

    private boolean compareList(RemiseNotice remiseNotice ,List<RemiseNoticeDetail> listInDB ,List<RemiseNoticeDetail> listInPage ,boolean autoUpdate){
        if(!autoUpdate){
            return autoUpdate;
        }
        if(CollectionUtils.isEmpty(listInDB) || CollectionUtils.isEmpty(listInPage) || listInDB.size() != listInPage.size() ){
            return false;
        }
        boolean isSame = true;
        for(int i = 0 ;i <listInDB.size();i++){
            isSame = compareRemiseNotice(listInDB.get(i), listInPage.get(i));
            if(!isSame){
                log.error("isSame = " + isSame + " \n listInDB.get(i)=" + listInDB.get(i) + " \n listInPage.get(i)=" + listInPage.get(i));
                break;
            }
        }
        if(autoUpdate && !isSame){
            return updateAfterCompareDetail(remiseNotice,listInPage);
        }
        return false;
    }

    private boolean updateAfterCompareDetail(RemiseNotice remiseNotice,List<RemiseNoticeDetail> listInPage){
        RemiseNoticeDetail temp = new RemiseNoticeDetail();
        temp.setNoticeId(remiseNotice.getId());
        remiseNoticeDetailService.deleteByProperty(temp);

        for(RemiseNoticeDetail detail : listInPage ){
            if(null == detail ){
                continue;
            }
            detail.setNoticeId(remiseNotice.getId());
        }
        int count = remiseNoticeDetailService.batchInsert(listInPage);
        return count > 0;
    }

    /**
     * 只要有一个属性值不同，则直接返回
     * @param detailInDB
     * @param detailInPage
     * @return
     */
    private boolean compareRemiseNotice(RemiseNoticeDetail detailInDB, RemiseNoticeDetail detailInPage) {
        boolean isSame = (StringUtils.isEmpty(detailInDB.getLandSn()) && StringUtils.isEmpty(detailInPage.getLandSn()))  ||  detailInDB.getLandSn().equals(detailInPage.getLandSn());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getLandTotalArea()) && StringUtils.isEmpty(detailInPage.getLandTotalArea()))  ||  detailInDB.getLandTotalArea().equals(detailInPage.getLandTotalArea());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getLandLocation()) && StringUtils.isEmpty(detailInPage.getLandLocation()))  ||  detailInDB.getLandLocation().equals(detailInPage.getLandLocation());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getSaleTime()) && StringUtils.isEmpty(detailInPage.getSaleTime()))  ||  detailInDB.getSaleTime().equals(detailInPage.getSaleTime());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getPlotRatio()) && StringUtils.isEmpty(detailInPage.getPlotRatio()))  ||  detailInDB.getPlotRatio().equals(detailInPage.getPlotRatio());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getBuildingDensity()) && StringUtils.isEmpty(detailInPage.getBuildingDensity()))  ||  detailInDB.getBuildingDensity().equals(detailInPage.getBuildingDensity());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getGreeningRate()) && StringUtils.isEmpty(detailInPage.getGreeningRate()))  ||  detailInDB.getGreeningRate().equals(detailInPage.getGreeningRate());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getBuildingLimitedHeight()) && StringUtils.isEmpty(detailInPage.getBuildingLimitedHeight()))  ||  detailInDB.getBuildingLimitedHeight().equals(detailInPage.getBuildingLimitedHeight());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getLandUseDetails()) && StringUtils.isEmpty(detailInPage.getLandUseDetails()))  ||  detailInDB.getLandUseDetails().equals(detailInPage.getLandUseDetails());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getInvestmentIntensity()) && StringUtils.isEmpty(detailInPage.getInvestmentIntensity()))  ||  detailInDB.getInvestmentIntensity().equals(detailInPage.getInvestmentIntensity());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getCashDeposit()) && StringUtils.isEmpty(detailInPage.getCashDeposit()))  ||  detailInDB.getCashDeposit().equals(detailInPage.getCashDeposit());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getValuationReportNum()) && StringUtils.isEmpty(detailInPage.getValuationReportNum()))  ||  detailInDB.getValuationReportNum().equals(detailInPage.getValuationReportNum());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getCurrentLandConditions()) && StringUtils.isEmpty(detailInPage.getCurrentLandConditions()))  ||  detailInDB.getCurrentLandConditions().equals(detailInPage.getCurrentLandConditions());
        if( !isSame ){
            return false;
        }
        isSame = (StringUtils.isEmpty(detailInDB.getStartingPrice()) && StringUtils.isEmpty(detailInPage.getStartingPrice()))  ||  detailInDB.getStartingPrice().equals(detailInPage.getStartingPrice());
        if( !isSame ){
            return false;
        }
        isSame = (StringUtils.isEmpty(detailInDB.getPriceIncrease()) && StringUtils.isEmpty(detailInPage.getPriceIncrease()))  ||  detailInDB.getPriceIncrease().equals(detailInPage.getPriceIncrease());
        if( !isSame ){
            return false;
        }
        isSame = (StringUtils.isEmpty(detailInDB.getOpenStartTime()) && StringUtils.isEmpty(detailInPage.getOpenStartTime()))  ||  detailInDB.getOpenStartTime().equals(detailInPage.getOpenStartTime());
        if( !isSame ){
            return false;
        }
        isSame = (StringUtils.isEmpty(detailInDB.getOpenEndTime()) && StringUtils.isEmpty(detailInPage.getOpenEndTime()))  ||  detailInDB.getOpenEndTime().equals(detailInPage.getOpenEndTime());
        if( !isSame ){
            return false;
        }

        isSame = (StringUtils.isEmpty(detailInDB.getRemark()) && StringUtils.isEmpty(detailInPage.getRemark()))  ||  detailInDB.getRemark().equals(detailInPage.getRemark());
        return isSame;
    }

}
