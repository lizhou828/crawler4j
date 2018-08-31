/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.schedule.RemiseNoticeDetailSchedule2.java <2018年08月28日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.schedule;

import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.service.RemiseNoticeDetailService;
import com.baodiwang.crawler4j.service.RemiseNoticeService;
import com.baodiwang.crawler4j.utils.SpringContextHolder;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程解析数据
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月28日 12时14分
 */

@Component
@EnableScheduling
@EnableAsync
public class RemiseNoticeDetailParserSchedule {

    private static final Logger log = LogManager.getLogger(RemiseNoticeDetailSchedule.class);

    private static CountDownLatch sCountDownLatch = null;

    private static  int THREAD_NUMBER = 0;


    @Autowired
    RemiseNoticeService remiseNoticeService;

    @Autowired
    RemiseNoticeDetailService remiseNoticeDetailService;

    @Scheduled(cron = "0/20 * * * * ? ")//每隔20秒执行一次
    public void parseDataToRemiseNoticeDetail(){

        log.info("多线程解析公告详情页数据============================开始");

        List<RemiseNotice> remiseNoticeList = remiseNoticeService.findNoticeWithoutDetail(1,100); //每次抓取100条未解析的数据（多线程）
        if(null == remiseNoticeList || remiseNoticeList.isEmpty() ){
            log.info("多线程解析公告详情页数据============================没有需要处理的数据");
            return;
        }

        int threadHandleCount = 10;//每个线程处理的任务个数

        long start = System.currentTimeMillis();
        List<BatchParseThread> batchParseThreadList = new ArrayList<BatchParseThread>();
        BatchParseThread batchParseThread = null;
        //整除的情况
        for(int i = 0; i<remiseNoticeList.size() ;i++){
            if( i % threadHandleCount == 0){
                if(null != batchParseThread ){
                    batchParseThreadList.add(batchParseThread);
                }
                batchParseThread = new BatchParseThread();
            }
            batchParseThread.addRemiseNotice(remiseNoticeList.get(i));
        }
        //如果有余数的，也要加进来
        if(null != batchParseThread && !CollectionUtils.isEmpty(batchParseThread.getRemiseNoticeList())){
            batchParseThreadList.add(batchParseThread);
        }


        THREAD_NUMBER = batchParseThreadList.size();
        sCountDownLatch = new CountDownLatch(THREAD_NUMBER);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(THREAD_NUMBER);
        for(BatchParseThread parseThread : batchParseThreadList){
            //放入线程池中，执行线程
            fixedThreadPool.execute(parseThread);
        }
        try {
            sCountDownLatch.await();//导致当前线程（即主线程）等待，直到latch的计数降为0（除非子线程被打断）
        } catch (Exception e) {
            log.error("多线程解析公告详情页数据============================当前线程（即主线程）等待时发生异常：" + e.getMessage(),e);
        }
        long end = System.currentTimeMillis();
        log.info("多线程解析公告详情页数据============================" + THREAD_NUMBER + "个子线程已经执行完毕 耗时：" + (end - start) + "毫秒");
    }

    private static class BatchParseThread implements Runnable{

        protected List<RemiseNotice> remiseNoticeList = new ArrayList<RemiseNotice>();

        public List<RemiseNotice> getRemiseNoticeList() {
            return remiseNoticeList;
        }

        public void setRemiseNoticeList(List<RemiseNotice> remiseNoticeList) {
            this.remiseNoticeList = remiseNoticeList;
        }

        public void addRemiseNotice(RemiseNotice remiseNotice){
            if(null != remiseNotice){
                remiseNoticeList.add(remiseNotice);
            }
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            try{
                RemiseNoticeDetailSchedule  remiseNoticeDetailSchedule = SpringContextHolder.getBean("remiseNoticeDetailSchedule");
                for(RemiseNotice remiseNotice :remiseNoticeList){
                    remiseNoticeDetailSchedule.parseSingleRemiseNotice(remiseNotice);
                }
            }catch (Exception e){
                log.error("多线程解析公告详情页数据============================当前子线程(" + Thread.currentThread().getName() + ")执行发生异常：" + e.getMessage(), e);
            }
            long end = System.currentTimeMillis();
            log.info("多线程解析公告详情页数据============================当前子线程(" + Thread.currentThread().getName() + ")已经执行完毕 耗时：" + (end - start) + "毫秒");
            sCountDownLatch.countDown();
        }
    }
}
