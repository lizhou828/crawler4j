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

    @Transactional
    public Boolean compareContent(Integer remiseNoticeId){
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
        String webContent = landChinaHttpBreaker3.breakBarrierGet(remiseNotice.getHref(), null);
        if(StringUtils.isEmpty(webContent) || webContent.length() < 8000){
            log.error(logMessage + "抓取到的网页异常！webContent=" + webContent);
            return false;
        }


        RemiseNotice temp = new RemiseNotice();
        temp.setId(remiseNotice.getId());
        if(!webContent.equals(remiseNotice.getContent())){
            temp.setContent(webContent);
            int result = remiseNoticeService.update(temp);
            return result > 0;
        }
        return null;
    }


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

        //一、详情页数据条数长度不一样
        if(remiseNoticeDetailList.size() != remiseNoticeVo.getRemiseNoticeDetailList().size()){
            if(autoUpdate){
                /* 校对数据后，更新remiseNoticeDetail表 */
                updateAfterCompareDetail(remiseNotice, remiseNoticeVo.getRemiseNoticeDetailList());

                /* 校对数据后，更新remiseNotice表 */
                Boolean update = updateRemiseNotice(remiseNotice, remiseNoticeVo, webContent);
                if(null == update){
                    log.info(logMessage + "详情页数据条数长度不一样,remiseNotice数据无需更新");
                } else if(update){
                    log.info(logMessage + "详情页数据条数长度不一样,更新remiseNotice表成功");
                } else {
                    log.error(logMessage + "详情页数据条数长度不一样,更新remiseNotice表失败，remiseNotice=" + remiseNotice);
                }
                log.info(logMessage + "详情页数据条数长度不一样,已更新数据库");
                return true;
            }else{
                log.warn(logMessage + "详情页数据条数长度不一样，需要更新数据库");
                return true;
            }
        }

        //二、详情页数据条数长度一样的情况
        /* 校对数据后，更新remiseNoticeDetail表 */
        Boolean updateResult = compareListWithSameLength(remiseNotice, remiseNoticeDetailList, remiseNoticeVo.getRemiseNoticeDetailList(), autoUpdate);

        /* 校对数据后，更新remiseNotice表（详情页其他内容可能会有改动、乱码等。所以需要更新） */
        Boolean update = updateRemiseNotice(remiseNotice, remiseNoticeVo, webContent);
        if(null == update){
            log.info(logMessage + "详情页数据条数长度一样的情况,remiseNotice数据无需更新");
        } else if(update){
            log.info(logMessage + "详情页数据条数长度一样的情况,更新remiseNotice表成功");
        }else {
            log.error(logMessage + "详情页数据条数长度一样的情况,更新remiseNotice表失败.......remiseNotice=" + remiseNotice);
        }

        if(null == updateResult){
            log.info(logMessage + "详情页数据条数长度一样的情况,详情页数据无需更新");
            return null;
        }else if(updateResult){
            log.info(logMessage + "详情页数据条数长度一样的情况,详情页个别数据不一样，已更新数据库");
            return true;
        }else {
            log.error(logMessage + "详情页数据条数长度一样的情况,详情页个别数据不一样，更新remiseNoticeDetai失败.......remiseNotice=" + remiseNotice);
            return false;
        }
    }

    private Boolean compareListWithSameLength(RemiseNotice remiseNotice ,List<RemiseNoticeDetail> listInDB ,List<RemiseNoticeDetail> listInPage ,boolean autoUpdate){
        if(!autoUpdate){
            return autoUpdate;
        }
        if (CollectionUtils.isEmpty(listInDB) && CollectionUtils.isEmpty(listInPage)){
            return null ;//无需处理
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
        return null;
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

    /**
     * 校对数据成功后，更新remiseNotice的相关字段
     * @param remiseNotice
     * @param remiseNoticeVo
     * @param webContent
     * @return
     */
    private Boolean updateRemiseNotice(RemiseNotice remiseNotice, RemiseNoticeVo remiseNoticeVo, String webContent){
        if(StringUtils.isEmpty(webContent) || webContent.length() < 8000){
            return false;
        }
        Boolean needUpdate = false;
        RemiseNotice temp = new RemiseNotice();
        temp.setId(remiseNotice.getId());
        if(!webContent.equals(remiseNotice.getContent())){
            temp.setContent(webContent);
            needUpdate = true;
        }

        RemiseNotice remiseNoticeInVo  = remiseNoticeVo.getRemiseNotice();
        if(null != remiseNoticeInVo  && StringUtils.isNotEmpty(remiseNoticeInVo.getTitle()) &&  !remiseNoticeInVo.getTitle().equals(remiseNotice.getTitle())){
            temp.setTitle(remiseNoticeInVo.getTitle());
            needUpdate = true;
        }
        if(null != remiseNoticeInVo  && StringUtils.isNotEmpty(remiseNoticeInVo.getNoticeNum()) &&  !remiseNoticeInVo.getNoticeNum().equals(remiseNotice.getNoticeNum())){
            temp.setNoticeNum(remiseNoticeInVo.getNoticeNum());
            needUpdate = true;
        }
        if(needUpdate){
            int result = remiseNoticeService.update(temp);
            return result > 0;
        }else{
            return null;
        }
    }
}
