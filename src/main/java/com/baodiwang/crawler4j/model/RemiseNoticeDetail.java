/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */
package com.baodiwang.crawler4j.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class RemiseNoticeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 主键 */
    private Long id;

    /* 公告id */
    private Long noticeId;

    /* 宗地编号 */
    private String landSn;

    /* 宗地总面积 */
    private String landTotalArea;

    /* 宗地坐落 */
    private String landLocation;

    /* 出让年限 */
    private String saleTime;

    /* 容积率 */
    private String plotRatio;

    /* 建筑密度 */
    private String buildingDensity;

    /* 绿化率  */
    private String greeningRate;

    /* 建筑限高 */
    private String buildingLimitedHeight;

    /* 土地用途明细 */
    private String landUseDetails;

    /* 投资强度 */
    private String investmentIntensity;

    /* 保证金 */
    private String cashDeposit;

    /* 估价报告备案号 */
    private String valuationReportNum;

    /* 现状土地条件 */
    private String currentLandConditions;

    /* 起始价 */
    private String startingPrice;

    /* 加价幅度 */
    private String priceIncrease;

    /* 挂牌开始时间 */
    private String openStartTime;

    /* 挂牌截止时间 */
    private String openEndTime;

    /* 备注 */
    private String remark;

    /*  */
    private java.sql.Timestamp createTime;

    /*  */
    private String creator;


    /* get 主键 */
    public Long getId() {
        return id;
    }

    /* set 主键 */
    public void setId(Long id) {
        this.id = id;
    }
    /* get 公告id */
    public Long getNoticeId() {
        return noticeId;
    }

    /* set 公告id */
    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }
    /* get 宗地编号 */
    public String getLandSn() {
        return landSn;
    }

    /* set 宗地编号 */
    public void setLandSn(String landSn) {
        this.landSn = landSn;
    }
    /* get 宗地总面积 */
    public String getLandTotalArea() {
        return landTotalArea;
    }

    /* set 宗地总面积 */
    public void setLandTotalArea(String landTotalArea) {
        this.landTotalArea = landTotalArea;
    }
    /* get 宗地坐落 */
    public String getLandLocation() {
        return landLocation;
    }

    /* set 宗地坐落 */
    public void setLandLocation(String landLocation) {
        this.landLocation = landLocation;
    }
    /* get 出让年限 */
    public String getSaleTime() {
        return saleTime;
    }

    /* set 出让年限 */
    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
    }
    /* get 容积率 */
    public String getPlotRatio() {
        return plotRatio;
    }

    /* set 容积率 */
    public void setPlotRatio(String plotRatio) {
        this.plotRatio = plotRatio;
    }
    /* get 建筑密度 */
    public String getBuildingDensity() {
        return buildingDensity;
    }

    /* set 建筑密度 */
    public void setBuildingDensity(String buildingDensity) {
        this.buildingDensity = buildingDensity;
    }
    /* get 绿化率  */
    public String getGreeningRate() {
        return greeningRate;
    }

    /* set 绿化率  */
    public void setGreeningRate(String greeningRate) {
        this.greeningRate = greeningRate;
    }
    /* get 建筑限高 */
    public String getBuildingLimitedHeight() {
        return buildingLimitedHeight;
    }

    /* set 建筑限高 */
    public void setBuildingLimitedHeight(String buildingLimitedHeight) {
        this.buildingLimitedHeight = buildingLimitedHeight;
    }
    /* get 土地用途明细 */
    public String getLandUseDetails() {
        return landUseDetails;
    }

    /* set 土地用途明细 */
    public void setLandUseDetails(String landUseDetails) {
        this.landUseDetails = landUseDetails;
    }
    /* get 投资强度 */
    public String getInvestmentIntensity() {
        return investmentIntensity;
    }

    /* set 投资强度 */
    public void setInvestmentIntensity(String investmentIntensity) {
        this.investmentIntensity = investmentIntensity;
    }
    /* get 保证金 */
    public String getCashDeposit() {
        return cashDeposit;
    }

    /* set 保证金 */
    public void setCashDeposit(String cashDeposit) {
        this.cashDeposit = cashDeposit;
    }
    /* get 估价报告备案号 */
    public String getValuationReportNum() {
        return valuationReportNum;
    }

    /* set 估价报告备案号 */
    public void setValuationReportNum(String valuationReportNum) {
        this.valuationReportNum = valuationReportNum;
    }
    /* get 现状土地条件 */
    public String getCurrentLandConditions() {
        return currentLandConditions;
    }

    /* set 现状土地条件 */
    public void setCurrentLandConditions(String currentLandConditions) {
        this.currentLandConditions = currentLandConditions;
    }
    /* get 起始价 */
    public String getStartingPrice() {
        return startingPrice;
    }

    /* set 起始价 */
    public void setStartingPrice(String startingPrice) {
        this.startingPrice = startingPrice;
    }
    /* get 加价幅度 */
    public String getPriceIncrease() {
        return priceIncrease;
    }

    /* set 加价幅度 */
    public void setPriceIncrease(String priceIncrease) {
        this.priceIncrease = priceIncrease;
    }
    /* get 挂牌开始时间 */
    public String getOpenStartTime() {
        return openStartTime;
    }

    /* set 挂牌开始时间 */
    public void setOpenStartTime(String openStartTime) {
        this.openStartTime = openStartTime;
    }
    /* get 挂牌截止时间 */
    public String getOpenEndTime() {
        return openEndTime;
    }

    /* set 挂牌截止时间 */
    public void setOpenEndTime(String openEndTime) {
        this.openEndTime = openEndTime;
    }
    /* get 备注 */
    public String getRemark() {
        return remark;
    }

    /* set 备注 */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /* get  */
    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }

    /* set  */
    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }
    /* get  */
    public String getCreator() {
        return creator;
    }

    /* set  */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String toString() {
        return "RemiseNoticeDetail {" +
                    " id = " + id +
                    " , noticeId = " + noticeId +
                    " , landSn = " + landSn +
                    " , landTotalArea = " + landTotalArea +
                    " , landLocation = " + landLocation +
                    " , saleTime = " + saleTime +
                    " , plotRatio = " + plotRatio +
                    " , buildingDensity = " + buildingDensity +
                    " , greeningRate = " + greeningRate +
                    " , buildingLimitedHeight = " + buildingLimitedHeight +
                    " , landUseDetails = " + landUseDetails +
                    " , investmentIntensity = " + investmentIntensity +
                    " , cashDeposit = " + cashDeposit +
                    " , valuationReportNum = " + valuationReportNum +
                    " , currentLandConditions = " + currentLandConditions +
                    " , startingPrice = " + startingPrice +
                    " , priceIncrease = " + priceIncrease +
                    " , openStartTime = " + openStartTime +
                    " , openEndTime = " + openEndTime +
                    " , remark = " + remark +
                    " , createTime = " + createTime +
                    " , creator = " + creator +
            "}";
        }
}