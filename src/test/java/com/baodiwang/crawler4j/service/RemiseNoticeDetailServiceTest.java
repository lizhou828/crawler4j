/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.service.RemiseNoticeDetailServiceTest.java <2018年08月23日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.service;

import com.baodiwang.crawler4j.ApplicationTests;
import com.baodiwang.crawler4j.mapper.RemiseNoticeDetailMapper;
import com.baodiwang.crawler4j.mapper.RemiseNoticeMapper;
import com.baodiwang.crawler4j.model.RemiseNoticeDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月23日 11时23分
 */
public class RemiseNoticeDetailServiceTest extends ApplicationTests {

    private static final Logger log = LogManager.getLogger(RemiseNoticeServiceTest.class);

    @Autowired
    private RemiseNoticeDetailMapper remiseNoticeDetailMapper;
    @Test
    public void batchInsertTest(){
        List<RemiseNoticeDetail> remiseNoticeDetailList = new ArrayList<>();
        RemiseNoticeDetail remiseNoticeDetail = new RemiseNoticeDetail();
        remiseNoticeDetail.setNoticeId(1L);
        remiseNoticeDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        remiseNoticeDetail.setCreator("test1");
        remiseNoticeDetailList.add(remiseNoticeDetail);

        remiseNoticeDetail = new RemiseNoticeDetail();
        remiseNoticeDetail.setNoticeId(2L);
        remiseNoticeDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        remiseNoticeDetail.setCreator("test2");
        remiseNoticeDetailList.add(remiseNoticeDetail);
        int result = remiseNoticeDetailMapper.batchInsert(remiseNoticeDetailList);
        log.info("result=" + result);
    }
}
