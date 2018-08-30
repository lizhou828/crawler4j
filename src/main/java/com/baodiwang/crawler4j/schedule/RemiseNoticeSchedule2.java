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
        provincePageVoList.add(new ProvincePageVo("天津市", 12, 6));
        provincePageVoList.add(new ProvincePageVo("河北省", 13, 59));
        provincePageVoList.add(new ProvincePageVo("山西省", 14, 16));
        provincePageVoList.add(new ProvincePageVo("内蒙古", 15, 26));
        provincePageVoList.add(new ProvincePageVo("辽宁省", 21, 16));
        provincePageVoList.add(new ProvincePageVo("吉林省",22, 27));
        provincePageVoList.add(new ProvincePageVo("黑龙江省", 23, 19));
        provincePageVoList.add(new ProvincePageVo("上海市", 31, 4));
        provincePageVoList.add(new ProvincePageVo("江苏省", 32, 65));
        provincePageVoList.add(new ProvincePageVo("浙江省", 33, 79));
        provincePageVoList.add(new ProvincePageVo("安徽省", 34, 37));
        provincePageVoList.add(new ProvincePageVo("福建省", 35, 25));
        provincePageVoList.add(new ProvincePageVo("江西省", 36, 56));
        provincePageVoList.add(new ProvincePageVo("山东省", 37, 61));

        provincePageVoList.add(new ProvincePageVo("河南省", 41, 65));
        provincePageVoList.add(new ProvincePageVo("湖北省", 42, 48));
        provincePageVoList.add(new ProvincePageVo("湖南省", 43, 46));
        provincePageVoList.add(new ProvincePageVo("广东省", 44, 39));
        provincePageVoList.add(new ProvincePageVo("广西壮族", 45, 28));
        provincePageVoList.add(new ProvincePageVo("海南省", 46, 1));
        provincePageVoList.add(new ProvincePageVo("重庆市", 50, 15));
        provincePageVoList.add(new ProvincePageVo("四川省", 51, 38));
        provincePageVoList.add(new ProvincePageVo("贵州省", 52, 26));
        provincePageVoList.add(new ProvincePageVo("云南省", 53, 27));
        provincePageVoList.add(new ProvincePageVo("西藏", 54, 0));
        provincePageVoList.add(new ProvincePageVo("陕西省", 61, 21));
        provincePageVoList.add(new ProvincePageVo("甘肃省", 62, 19));
        provincePageVoList.add(new ProvincePageVo("青海省", 63, 4));
        provincePageVoList.add(new ProvincePageVo("宁夏回族", 64, 7));
        provincePageVoList.add(new ProvincePageVo("新疆维吾尔", 65, 22));
        provincePageVoList.add(new ProvincePageVo("新疆建设兵团", 66, 6));


        //todo 其他省份 singeProvinceScheduler.parseSingleProvinceDataToRemiseNotice();


    }


}


