/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.schedule.RemiseNoticeDetailCrawlerSchedule.java <2018年08月31日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.schedule;

import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.service.RemiseNoticeService;
import com.baodiwang.crawler4j.utils.HttpUtils;
import com.baodiwang.crawler4j.utils.IntUtils;
import com.baodiwang.crawler4j.utils.LandChinaHttpBreaker2;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 目的：多线程抓取详情页的定时器
 * 原因：在批量抓取数据时，因ip限频的原因，导致部分详情页的数据没有被抓取，故单独做个定时任务去抓
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月31日 11时07分
 */
@Component
@EnableScheduling
@EnableAsync
public class RemiseNoticeDetailCrawlerSchedule {

    private static final Logger log = LogManager.getLogger(RemiseNoticeDetailCrawlerSchedule.class);

    @Autowired
    RemiseNoticeService remiseNoticeService;

    @Async
    @Scheduled(cron = "0 0/2 * * * ?")//每隔2分钟执行一次
    public void schedule(){
        log.info("抓取详情页的定时器=======================开始执行");

        List<RemiseNotice> remiseNoticeList = remiseNoticeService.findNoticeWithoutContent();
        log.info("抓取详情页的定时器============================需要处理的数据条数：" + (CollectionUtils.isEmpty(remiseNoticeList) ? 0 : remiseNoticeList.size()));
        if(null == remiseNoticeList || remiseNoticeList.isEmpty() ){
            return;
        }
        int count = 0;
        Map<String,String> headMap = new HashMap<>();

        for(RemiseNotice remiseNotice: remiseNoticeList ){
//            String content = LandChinaHttpBreaker2.breakBarrier(remiseNotice.getHref(), headMap, null); //post过多、过于频繁，易导致IP被封
            String content = HttpUtils.get(remiseNotice.getHref(), headMap);//get请求目前不会导致IP被封
            if(StringUtils.isNotEmpty(content) && content.length() > 8000){
                remiseNotice.setContent(content);
                int resultCount = remiseNoticeService.update(remiseNotice);
                if(resultCount > 0 ){
                    count ++;
                }
            }

            int sleepSeconds = IntUtils.getRandomInt(5,8);
            try {
                log.info("抓取详情页的定时器============================成功抓取到详情页数据后，休眠" + sleepSeconds + "秒");
                Thread.sleep( sleepSeconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("抓取详情页的定时器============================成功抓取到详情页并保存数据条数：" + count);
    }
}
