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

    /* ��������id */
    private java.lang.Integer secondLandId;

    /* �ؿ����ͣ�1լ���أ�2�����õأ� */
    private java.lang.Integer landType;

    /* ���ر��� */
    private java.lang.String landCode;

    /* ����ʱ�� */
    private java.sql.Timestamp updateTime;

    /* ���� */
    private java.lang.String title;

    /* ��; */
    private java.lang.String usage;

    /* ���ؼ۸� */
    private java.lang.String landPrice;

    /* ��ת���� */
    private java.lang.String transferType;

    /* ��ת���� */
    private java.lang.String transferTime;

    /* ������� */
    private java.lang.String totalArea;

    /* ����λ�� */
    private java.lang.String regionalLocation;

    /* ��ϵ�� */
    private java.lang.String contacts;

    /* ��ϵ�绰 */
    private java.lang.String contactsPhone;

    /*  */
    private java.sql.Timestamp createTime;

    /* ��Ϣ��Դ */
    private java.lang.String from;


    /* get  */
    public java.lang.Integer getId() {
        return id;
    }

    /* set  */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }
    /* get ��������id */
    public java.lang.Integer getSecondLandId() {
        return secondLandId;
    }

    /* set ��������id */
    public void setSecondLandId(java.lang.Integer secondLandId) {
        this.secondLandId = secondLandId;
    }
    /* get �ؿ����ͣ�1լ���أ�2�����õأ� */
    public java.lang.Integer getLandType() {
        return landType;
    }

    /* set �ؿ����ͣ�1լ���أ�2�����õأ� */
    public void setLandType(java.lang.Integer landType) {
        this.landType = landType;
    }
    /* get ���ر��� */
    public java.lang.String getLandCode() {
        return landCode;
    }

    /* set ���ر��� */
    public void setLandCode(java.lang.String landCode) {
        this.landCode = landCode;
    }
    /* get ����ʱ�� */
    public java.sql.Timestamp getUpdateTime() {
        return updateTime;
    }

    /* set ����ʱ�� */
    public void setUpdateTime(java.sql.Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    /* get ���� */
    public java.lang.String getTitle() {
        return title;
    }

    /* set ���� */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    /* get ��; */
    public java.lang.String getUsage() {
        return usage;
    }

    /* set ��; */
    public void setUsage(java.lang.String usage) {
        this.usage = usage;
    }
    /* get ���ؼ۸� */
    public java.lang.String getLandPrice() {
        return landPrice;
    }

    /* set ���ؼ۸� */
    public void setLandPrice(java.lang.String landPrice) {
        this.landPrice = landPrice;
    }
    /* get ��ת���� */
    public java.lang.String getTransferType() {
        return transferType;
    }

    /* set ��ת���� */
    public void setTransferType(java.lang.String transferType) {
        this.transferType = transferType;
    }
    /* get ��ת���� */
    public java.lang.String getTransferTime() {
        return transferTime;
    }

    /* set ��ת���� */
    public void setTransferTime(java.lang.String transferTime) {
        this.transferTime = transferTime;
    }
    /* get ������� */
    public java.lang.String getTotalArea() {
        return totalArea;
    }

    /* set ������� */
    public void setTotalArea(java.lang.String totalArea) {
        this.totalArea = totalArea;
    }
    /* get ����λ�� */
    public java.lang.String getRegionalLocation() {
        return regionalLocation;
    }

    /* set ����λ�� */
    public void setRegionalLocation(java.lang.String regionalLocation) {
        this.regionalLocation = regionalLocation;
    }
    /* get ��ϵ�� */
    public java.lang.String getContacts() {
        return contacts;
    }

    /* set ��ϵ�� */
    public void setContacts(java.lang.String contacts) {
        this.contacts = contacts;
    }
    /* get ��ϵ�绰 */
    public java.lang.String getContactsPhone() {
        return contactsPhone;
    }

    /* set ��ϵ�绰 */
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
    /* get ��Ϣ��Դ */
    public java.lang.String getFrom() {
        return from;
    }

    /* set ��Ϣ��Դ */
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