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
public class SecondLandDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /*  */
    private java.lang.Integer id;

    /* 二手土地id */
    private java.lang.Integer secondLandId;

    /* 地块类型（1宅基地，2工商用地） */
    private java.lang.Integer landType;

    /* 土地编码 */
    private java.lang.String landCode;

    /* 更新时间 */
    private java.sql.Timestamp updateTime;

    /* 标题 */
    private java.lang.String title;

    /* 用途 */
    private java.lang.String usage;

    /* 土地价格 */
    private java.lang.String landPrice;

    /* 流转类型 */
    private java.lang.String transferType;

    /* 流转年限 */
    private java.lang.String transferTime;

    /* 土地面积 */
    private java.lang.String totalArea;

    /* 地区位置 */
    private java.lang.String regionalLocation;

    /* 联系人 */
    private java.lang.String contacts;

    /* 联系电话 */
    private java.lang.String contactsPhone;

    /*  */
    private java.sql.Timestamp createTime;

    /* 信息来源 */
    private java.lang.String from;


    /* get  */
    public java.lang.Integer getId() {
        return id;
    }

    /* set  */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }
    /* get 二手土地id */
    public java.lang.Integer getSecondLandId() {
        return secondLandId;
    }

    /* set 二手土地id */
    public void setSecondLandId(java.lang.Integer secondLandId) {
        this.secondLandId = secondLandId;
    }
    /* get 地块类型（1宅基地，2工商用地） */
    public java.lang.Integer getLandType() {
        return landType;
    }

    /* set 地块类型（1宅基地，2工商用地） */
    public void setLandType(java.lang.Integer landType) {
        this.landType = landType;
    }
    /* get 土地编码 */
    public java.lang.String getLandCode() {
        return landCode;
    }

    /* set 土地编码 */
    public void setLandCode(java.lang.String landCode) {
        this.landCode = landCode;
    }
    /* get 更新时间 */
    public java.sql.Timestamp getUpdateTime() {
        return updateTime;
    }

    /* set 更新时间 */
    public void setUpdateTime(java.sql.Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    /* get 标题 */
    public java.lang.String getTitle() {
        return title;
    }

    /* set 标题 */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    /* get 用途 */
    public java.lang.String getUsage() {
        return usage;
    }

    /* set 用途 */
    public void setUsage(java.lang.String usage) {
        this.usage = usage;
    }
    /* get 土地价格 */
    public java.lang.String getLandPrice() {
        return landPrice;
    }

    /* set 土地价格 */
    public void setLandPrice(java.lang.String landPrice) {
        this.landPrice = landPrice;
    }
    /* get 流转类型 */
    public java.lang.String getTransferType() {
        return transferType;
    }

    /* set 流转类型 */
    public void setTransferType(java.lang.String transferType) {
        this.transferType = transferType;
    }
    /* get 流转年限 */
    public java.lang.String getTransferTime() {
        return transferTime;
    }

    /* set 流转年限 */
    public void setTransferTime(java.lang.String transferTime) {
        this.transferTime = transferTime;
    }
    /* get 土地面积 */
    public java.lang.String getTotalArea() {
        return totalArea;
    }

    /* set 土地面积 */
    public void setTotalArea(java.lang.String totalArea) {
        this.totalArea = totalArea;
    }
    /* get 地区位置 */
    public java.lang.String getRegionalLocation() {
        return regionalLocation;
    }

    /* set 地区位置 */
    public void setRegionalLocation(java.lang.String regionalLocation) {
        this.regionalLocation = regionalLocation;
    }
    /* get 联系人 */
    public java.lang.String getContacts() {
        return contacts;
    }

    /* set 联系人 */
    public void setContacts(java.lang.String contacts) {
        this.contacts = contacts;
    }
    /* get 联系电话 */
    public java.lang.String getContactsPhone() {
        return contactsPhone;
    }

    /* set 联系电话 */
    public void setContactsPhone(java.lang.String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }
    /* get  */
    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }

    /* set  */
    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }
    /* get 信息来源 */
    public java.lang.String getFrom() {
        return from;
    }

    /* set 信息来源 */
    public void setFrom(java.lang.String from) {
        this.from = from;
    }

    public String toString() {
        return "SecondLandDetail {" +
                " id = " + id +
                " , secondLandId = " + secondLandId +
                " , landType = " + landType +
                " , landCode = " + landCode +
                " , updateTime = " + updateTime +
                " , title = " + title +
                " , usage = " + usage +
                " , landPrice = " + landPrice +
                " , transferType = " + transferType +
                " , transferTime = " + transferTime +
                " , totalArea = " + totalArea +
                " , regionalLocation = " + regionalLocation +
                " , contacts = " + contacts +
                " , contactsPhone = " + contactsPhone +
                " , createTime = " + createTime +
                " , from = " + from +
                "}";
    }
}