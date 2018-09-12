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

    /**
     * 查询没有解析详情页的数据（查询速度快）
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<RemiseNotice> findNoticeWithoutDetail(@Param("pageNum") int pageNum ,@Param("pageSize") int pageSize);

    /**
     * 查询遗漏的、没有解析的详情页的数据（（查询速度慢，用作查询因网络等原因没有抓取到的数据）,用左连接取差集）
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<RemiseNotice> findNoticeMissDetail(@Param("pageNum") int pageNum ,@Param("pageSize") int pageSize);

    /**
     * 查询没有抓取到详情页内容的数据
     * @param startId
     * @param pageSize
     * @return
     */
    public List<RemiseNotice> findNoticeWithoutContent(@Param("startId") long startId,@Param("pageSize") int pageSize);

    /**
     * 通过指定id来查询没有详情页网页内容的数据
     * @param startId
     * @param endId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<RemiseNotice> findNoticeWithoutContentById(@Param("startId")Long startId,@Param("endId")Long endId,@Param("pageNum") int pageNum ,@Param("pageSize") int pageSize);


    public long findMaxId();

    public long findMinId();

    public void batchInsertWithId(List<RemiseNotice> remiseNotices);

    public long findMinIdWithoutContent(@Param("lastDataCount") Long lastDataCount);
}