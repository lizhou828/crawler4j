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
public class RemiseNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /*  */
    private Long id;

    /* 类型（招标、拍卖、挂牌） */
    private Integer type;

    /* 标题 */
    private String title;

    /* 公告号 */
    private String noticeNum;

    /* 发布时间 */
    private java.sql.Timestamp publishTime;

    /* 所在地区 */
    private String areaName;

    /* 地区id */
    private Integer areaId;

    /* 网页内容 */
    private String content;

    /* 源地址 */
    private String href;

    /*  */
    private java.sql.Timestamp createTime;

    /*  */
    private String creator;


    /* get  */
    public Long getId() {
        return id;
    }

    /* set  */
    public void setId(Long id) {
        this.id = id;
    }
    /* get 类型（招标、拍卖、挂牌） */
    public Integer getType() {
        return type;
    }

    /* set 类型（招标、拍卖、挂牌） */
    public void setType(Integer type) {
        this.type = type;
    }
    /* get 标题 */
    public String getTitle() {
        return title;
    }

    /* set 标题 */
    public void setTitle(String title) {
        this.title = title;
    }
    /* get 公告号 */
    public String getNoticeNum() {
        return noticeNum;
    }

    /* set 公告号 */
    public void setNoticeNum(String noticeNum) {
        this.noticeNum = noticeNum;
    }
    /* get 发布时间 */
    public java.sql.Timestamp getPublishTime() {
        return publishTime;
    }

    /* set 发布时间 */
    public void setPublishTime(java.sql.Timestamp publishTime) {
        this.publishTime = publishTime;
    }
    /* get 所在地区 */
    public String getAreaName() {
        return areaName;
    }

    /* set 所在地区 */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    /* get 地区id */
    public Integer getAreaId() {
        return areaId;
    }

    /* set 地区id */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }
    /* get 网页内容 */
    public String getContent() {
        return content;
    }

    /* set 网页内容 */
    public void setContent(String content) {
        this.content = content;
    }
    /* get 源地址 */
    public String getHref() {
        return href;
    }

    /* set 源地址 */
    public void setHref(String href) {
        this.href = href;
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
        return "RemiseNotice {" +
                    " id = " + id +
                    " , type = " + type +
                    " , title = " + title +
                    " , noticeNum = " + noticeNum +
                    " , publishTime = " + publishTime +
                    " , areaName = " + areaName +
                    " , areaId = " + areaId +
                    " , content = " + content +
                    " , href = " + href +
                    " , createTime = " + createTime +
                    " , creator = " + creator +
            "}";
        }
}