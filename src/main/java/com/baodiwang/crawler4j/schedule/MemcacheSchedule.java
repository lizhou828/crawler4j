/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.schedule.MemcacheSchedule.java <2018年09月12日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.schedule;

import com.baodiwang.crawler4j.constants.Constant;
import com.baodiwang.crawler4j.model.RemiseNoticeDetail;
import com.baodiwang.crawler4j.service.RemiseNoticeDetailService;
import com.baodiwang.crawler4j.service.RemiseNoticeService;
import com.whalin.MemCached.MemCachedClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月12日 16时31分
 */

@Component
@EnableScheduling
@EnableAsync
public class MemcacheSchedule {
    private static final Logger log = LogManager.getLogger(MemcacheSchedule.class);

    @Autowired
    RemiseNoticeService remiseNoticeService;

    @Autowired
    RemiseNoticeDetailService remiseNoticeDetailService;

    @Autowired
    MemCachedClient memCachedClient;

//    @Async
//    @Scheduled(cron = "0 0/2 * * * ? ")//每隔20分钟执行一次
    public void updateRemiseNoticeMinId(){
        Long lastDataCount = 1000L;
        String logMessage = "memcache更新最小id（从RemiseNotice表中，没有content的最近的"+lastDataCount+"条数据中查询）============================================================";
        log.info(logMessage + "开始");
        long start = System.currentTimeMillis();
        long minId =remiseNoticeService.findMinIdWithoutContent(lastDataCount);
        long end = System.currentTimeMillis();
        log.info(logMessage + "结束，耗时" + (end - start) + "毫秒，minId=" + minId);
        if(minId <= 0L){
            RemiseNoticeDetail remiseNoticeDetail = new RemiseNoticeDetail();
            remiseNoticeDetail.setNoticeId(minId);
            int count = remiseNoticeDetailService.findByCount(remiseNoticeDetail);
            if( count > 0){ //已经解析完
                memCachedClient.set(Constant.LANDCHINA_REMISE_NOTICE_MIN_ID,0);
            }

            return ;
        }
        Object obj = memCachedClient.get(Constant.LANDCHINA_REMISE_NOTICE_MIN_ID);
        long minIdInMemcache = 0L;
        try{
            minIdInMemcache = Long.parseLong(null == obj ? "0" :obj.toString());
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        log.info(logMessage + "minIdInMemcache=" +minIdInMemcache);
        if(minIdInMemcache <= 0L || minId >= minIdInMemcache){
            memCachedClient.set(Constant.LANDCHINA_REMISE_NOTICE_MIN_ID,minId);
            log.info(logMessage + "已更新memcache,minIdInMemcache=" + memCachedClient.get(Constant.LANDCHINA_REMISE_NOTICE_MIN_ID));
        }
    }
}
