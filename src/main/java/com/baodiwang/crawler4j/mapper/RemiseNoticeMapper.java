/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.mapper;


import com.baodiwang.crawler4j.model.RemiseNotice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RemiseNoticeMapper extends GenericIBatisMapper <RemiseNotice, Integer>{

    public List<RemiseNotice> findNoticeWithoutDetail(@Param("pageNum") int pageNum ,@Param("pageSize") int pageSize);

    public List<RemiseNotice> findNoticeWithoutContent(@Param("pageNum") int pageNum ,@Param("pageSize") int pageSize);

    public List<RemiseNotice> findNoticeWithoutContentById(@Param("startId")Long startId,@Param("endId")Long endId,@Param("pageNum") int pageNum ,@Param("pageSize") int pageSize);



}