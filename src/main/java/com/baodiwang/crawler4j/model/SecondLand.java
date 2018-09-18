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
public class SecondLand implements Serializable {

    private static final long serialVersionUID = 1L;

    /*  */
    private java.lang.Integer id;

    /* 标题 */
    private java.lang.String title;

    /*  */
    private java.lang.String href;

    /*  */
    private java.lang.String content;

    /*  */
    private java.sql.Timestamp createTime;


    /* get  */
    public java.lang.Integer getId() {
        return id;
    }

    /* set  */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }
    /* get 标题 */
    public java.lang.String getTitle() {
        return title;
    }

    /* set 标题 */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    /* get  */
    public java.lang.String getHref() {
        return href;
    }

    /* set  */
    public void setHref(java.lang.String href) {
        this.href = href;
    }
    /* get  */
    public java.lang.String getContent() {
        return content;
    }

    /* set  */
    public void setContent(java.lang.String content) {
        this.content = content;
    }
    /* get  */
    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }

    /* set  */
    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }

    public String toString() {
        return "SecondLand {" +
                " id = " + id +
                " , title = " + title +
                " , href = " + href +
                " , content = " + content +
                " , createTime = " + createTime +
                "}";
    }
}