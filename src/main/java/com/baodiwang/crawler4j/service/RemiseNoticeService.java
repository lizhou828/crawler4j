/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.service;

import com.baodiwang.crawler4j.model.RemiseNotice;

import java.util.List;

public interface RemiseNoticeService extends GenericIService<RemiseNotice,Integer>{

    public List<RemiseNotice> findNoticeWithoutDetail();

    public List<RemiseNotice> findNoticeWithoutDetail( int pageNum , int pageSize);


    public List<RemiseNotice> findNoticeMissDetail( int pageNum , int pageSize);

    public List<RemiseNotice> findNoticeWithoutContent();

    public List<RemiseNotice> findNoticeWithoutContent( long startId , int pageSize);

    public List<RemiseNotice> findNoticeWithoutContentById(Long startId,Long endId, int pageNum ,int pageSize);

    public long findMaxId();

    public long findMinId();

    public void batchInsertWithId(List<RemiseNotice> remiseNotices);

    public long findMinIdWithoutContent(Long lastDataCount);

}
