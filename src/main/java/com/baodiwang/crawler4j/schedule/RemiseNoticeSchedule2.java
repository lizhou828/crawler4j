/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.schedule.RemiseNoticeSchedule2.java <2018年08月27日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.schedule;

import com.baodiwang.crawler4j.VO.ProvincePageVo;
import com.baodiwang.crawler4j.controller.listPage.RemiseNoticeParser;
import com.baodiwang.crawler4j.service.RemiseNoticeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/**
 * 抓取所有省份数据
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月27日 14时07分
 */
public class RemiseNoticeSchedule2 {

    private static final Logger log = LogManager.getLogger(RemiseNoticeDetailSchedule.class);


    @Autowired
    RemiseNoticeService remiseNoticeService;

    @Autowired
    RemiseNoticeParser remiseNoticeParser;

    @Autowired
    RemiseNoticeSchedule singeProvinceScheduler;


    /**
     * 定时抓取数据、并解析已抓取到的网页，并存到相关表中
     * 多线程执行
     */
    @Scheduled(cron = "00 00 00 1 1/1 ? ")//每个月执行一次 从1号的0点00分开始,
    public void parseEveryProvinceDataToRemiseNotice(){
        List<ProvincePageVo> provincePageVoList = new ArrayList<>();
        provincePageVoList.add(new ProvincePageVo("北京市",11,1));
        provincePageVoList.add(new ProvincePageVo("河北省", 13, 59));
        //todo 其他省份 singeProvinceScheduler.parseSingleProvinceDataToRemiseNotice();


    }


}


