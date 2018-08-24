/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.controller.detailPage.RemiseNoticeDetailParser.java <2018年08月23日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.controller.detailPage;

import com.baodiwang.crawler4j.VO.RemiseNoticeVo;
import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.model.RemiseNoticeDetail;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 出让公告详情页解析器
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月23日 11时03分
 */

public class RemiseNoticeDetailParser {
    private static final Logger log = LogManager.getLogger(RemiseNoticeDetailParser.class);

    public static RemiseNoticeVo parseHtml(String pageContent){
        RemiseNoticeVo remiseNoticeVo = new RemiseNoticeVo();
        RemiseNotice remiseNotice = new RemiseNotice();

        Document doc = Jsoup.parse(pageContent);
        Element tileEle = doc.getElementById("lblTitle");
        if(null != tileEle && tileEle.hasText()){
            log.debug("标题：" + tileEle.text());
        }
        Element lblCreateDate = doc.getElementById("lblCreateDate");
        if(null != lblCreateDate && lblCreateDate.hasText()){
            log.debug(lblCreateDate.text());//发布时间
        }
        Element lblXzq = doc.getElementById("lblXzq");
        if(null != lblXzq && lblXzq.hasText()){
            String str = lblXzq.text();
//            log.info(str);
//            if(StringUtils.isNotEmpty(str)){
//                str = str.replace("行政区：","");
//            }
//            if(StringUtils.isNotEmpty(str)){
//
//                remiseNotice.setAreaName(str);//行政区
//            }
        }

        Element tdContent = doc.getElementById("tdContent");//网页中的正文部分
        Element contentTable = null;
        if(null != tdContent && !tdContent.children().isEmpty()){
            contentTable = tdContent.children().get(0);
        }
        if(null == contentTable){
            return null;
        }
        Elements trEleList = contentTable.children().select("tr");
        if(null != trEleList && !trEleList.isEmpty()){
            for(int i= 0;i < trEleList.size();i++){
                String title = trEleList.get(0).text();
                String noticeNum = StringUtils.getNoticeNumFromTitle(title);
                if(StringUtils.isNotEmpty(noticeNum)){
                    remiseNotice.setNoticeNum(noticeNum);
                }
            }
        }
        remiseNoticeVo.setRemiseNotice(remiseNotice);

        /* 解析表格（地块的基本情况和规划指标要求） */
        Elements tableEleList = contentTable.children().select("table");
        if(null == tableEleList || tableEleList.isEmpty()){
            return null;
        }
        List<RemiseNoticeDetail> remiseNoticeDetailList = new ArrayList<>();
        for(Element table : tableEleList){
            RemiseNoticeDetail remiseNoticeDetail = parseTable(table);
            if(null != remiseNoticeDetail){
                remiseNoticeDetailList.add(remiseNoticeDetail);
            }
        }

        remiseNoticeVo.setRemiseNoticeDetailList(remiseNoticeDetailList);
        return remiseNoticeVo;
    }

    private static  RemiseNoticeDetail  parseTable(Element table) {
        RemiseNoticeDetail remiseNoticeDetail = null;
        if(null == table ){
            return remiseNoticeDetail;
        }
        Elements trElementList = table.select("tr");
        remiseNoticeDetail = new RemiseNoticeDetail();
        for(int i = 0 ;i< trElementList.size();i++){
            Element trElement = trElementList.get(i);
            if(null == trElement || trElement.children().size() == 0){
                continue;
            }

            /* tr 只有一个td的情况（跨列） */
            if(trElement.children().size() == 1){
                if(trElement.text().contains("土地用途明细")){
                    if( i+1 < trElementList.size() && trElementList.get(i+1).children().size() == 1){
                        remiseNoticeDetail.setLandUseDetails(trElementList.get(i+1).children().text());
                    }
                }else if (trElement.text().contains("现状土地条件")){
//                    现状土地条件：净地；
                    String str = trElement.text();
                    str = str.replace("现状土地条件","");
                    if(str.contains("：")){
                        str = str.replace("：","");
                    }
                    if(str.contains("；")){
                        str = str.replace("；","");
                    }
                    remiseNoticeDetail.setCurrentLandConditions(str);
                }
                continue;
            }

            /* tr 只有一个td的情况（跨列） */
            if(trElement.children().size() == 2){
                if(StringUtils.isNotEmpty(trElement.text()) && trElement.text().contains("备注")){
                    remiseNoticeDetail.setRemark(trElement.child(1).text());;
                }
                continue;
            }

            /* tr 有多个td的情况(第一列) */
            Element ele0 = trElement.child(0);
            if(null != ele0 && ele0.text().contains("宗地编号")){
                int currentEleIndex = ele0.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" :e.text() );
                    if(StringUtils.isEmpty(str)){
                        log.warn("宗地编号解析失败！");
                    }else{
                        remiseNoticeDetail.setLandSn(str);
                    }
                }
            }else if(null != ele0 && ele0.text().contains("出让年限")){
                int currentEleIndex = ele0.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" :e.text() );
                    if(StringUtils.isEmpty(str)){
                        log.warn("出让年限解析失败！");
                    }else{
                        remiseNoticeDetail.setSaleTime(str);
                    }
                }
            }else if(null != ele0 && ele0.text().contains("绿化率")){
                int currentEleIndex = ele0.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" :e.text() );
                    if(StringUtils.isEmpty(str)){
                        log.warn("绿化率解析失败！");
                    }else{
                        remiseNoticeDetail.setGreeningRate(str.contains("%") ? str : str + "%");
                    }
                }
            }else if(null != ele0 && ele0.text().contains("投资强度")){
                int currentEleIndex = ele0.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" :e.text() );
                    if(StringUtils.isEmpty(str)){
                        log.warn("绿化率解析失败！");
                    }else{
                        remiseNoticeDetail.setInvestmentIntensity(str);
                    }
                }
            }else if(null != ele0 && ele0.text().contains("起始价")){
                int currentEleIndex = ele0.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" :e.text() );
                    if(StringUtils.isEmpty(str)){
                        log.warn("起始价解析失败！");
                    }else{
                        remiseNoticeDetail.setStartingPrice(str);
                    }
                }
            }else if(null != ele0 && ele0.text().contains("挂牌开始时间")){
                int currentEleIndex = ele0.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" :e.text() );
                    if(StringUtils.isEmpty(str)){
                        log.warn("挂牌开始时间解析失败！");
                    }else{
                        remiseNoticeDetail.setOpenStartTime(str);
                    }
                }
            }

            /* tr 有多个td的情况(第三列) */
            Element ele2 = trElement.child(2);
            if(null != ele2 && ele2.text().contains("宗地总面积")) {
                int currentEleIndex = ele2.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if (nextEleIndex  <= trElement.children().size()) {
                    Element e = trElement.child(nextEleIndex );
                    String str = removeInvalidStr(null == e ? "" : e.text());
                    if (StringUtils.isEmpty(str)) {
                        log.warn("宗地总面积解析失败！");
                    } else {
                        remiseNoticeDetail.setLandTotalArea(str);
                    }
                }
            }else   if(null != ele2 && ele2.text().contains("容积率")){
                int currentEleIndex = ele2.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" : e.text());
                    if(StringUtils.isEmpty(str)){
                        log.warn("容积率解析失败！");
                    }else{
                        remiseNoticeDetail.setPlotRatio(str);
                    }
                }
            }else   if(null != ele2 && ele2.text().contains("建筑限高(米)")){
                int currentEleIndex = ele2.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" : e.text());
                    if(StringUtils.isEmpty(str)){
                        log.warn("建筑限高(米)解析失败！");
                    }else{
                        remiseNoticeDetail.setBuildingLimitedHeight(str.contains("米") ? str : str + "米");
                    }
                }
            }else   if(null != ele2 && ele2.text().contains("保证金")){
                int currentEleIndex = ele2.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" : e.text());
                    if(StringUtils.isEmpty(str)){
                        log.warn("保证金解析失败！");
                    }else{
                        remiseNoticeDetail.setCashDeposit(str);
                    }
                }
            }else   if(null != ele2 && ele2.text().contains("加价幅度")){
                int currentEleIndex = ele2.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" : e.text());
                    if(StringUtils.isEmpty(str)){
                        log.warn("加价幅度解析失败！");
                    }else{
                        remiseNoticeDetail.setPriceIncrease(str);
                    }
                }
            }else   if(null != ele2 && ele2.text().contains("挂牌截止时间")){
                int currentEleIndex = ele2.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" : e.text());
                    if(StringUtils.isEmpty(str)){
                        log.warn("挂牌截止时间解析失败！");
                    }else{
                        remiseNoticeDetail.setOpenEndTime(str);
                    }
                }
            }

            if(trElement.children().size() <= 4){
                continue;
            }
            Element ele4 = ele4 = trElement.child(4);
            if(null != ele4 && ele4.text().contains("宗地坐落")) {
                int currentEleIndex = ele4.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex + 1;
                if (nextEleIndex <= trElement.children().size()) {
                    Element e = trElement.child(nextEleIndex);
                    String str = removeInvalidStr(null == e ? "" : e.text());
                    if (StringUtils.isEmpty(str)) {
                        log.warn("宗地坐落解析失败！");
                    } else {
                        remiseNoticeDetail.setLandLocation(str);
                    }
                }
            }else  if(null != ele4 && ele4.text().contains("建筑密度")){
                int currentEleIndex = ele4.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" : e.text());
                    if(StringUtils.isEmpty(str)){
                        log.warn("建筑密度解析失败！");
                    }else{
                        remiseNoticeDetail.setBuildingDensity(str.contains("%") ? str : str+ "%");
                    }
                }
            }else  if(null != ele4 && ele4.text().contains("估价报告备案号")){
                int currentEleIndex = ele4.elementSiblingIndex(); //position in element sibling list
                int nextEleIndex = currentEleIndex +1;
                if(nextEleIndex <= trElement.children().size()){
                    Element e = trElement.child(nextEleIndex);
                    String str =removeInvalidStr(null == e ? "" : e.text());
                    if(StringUtils.isEmpty(str)){
                        log.warn("估价报告备案号解析失败！");
                    }else{
                        remiseNoticeDetail.setValuationReportNum(str);
                    }
                }
            }
        }
        return remiseNoticeDetail;
    }

    private static String removeInvalidStr(String str){
        if(StringUtils.isEmpty(str)){
            return str;
        }
        if(StringUtils.isNotEmpty(str)) {
            str = str.trim();
            if (str.contains("&nbsp;")) {
                str.replace("&nbsp;", "");
            }
        }
        return str;
    }
}
