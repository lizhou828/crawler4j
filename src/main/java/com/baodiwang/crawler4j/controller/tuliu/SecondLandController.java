/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.controller.tuliu;


import com.baodiwang.crawler4j.model.SecondLand;
import com.baodiwang.crawler4j.model.SecondLandDetail;
import com.baodiwang.crawler4j.service.SecondLandDetailService;
import com.baodiwang.crawler4j.utils.HttpUtils;
import com.baodiwang.crawler4j.utils.SpringContextHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baodiwang.crawler4j.service.SecondLandService;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
public class SecondLandController {

    private static Log log = LogFactory.getLog(SecondLandController.class);

    @Autowired
    private SecondLandService secondLandService;

    @Autowired
    SecondLandParser secondLandParser;

    public static final String LAND_SOURCE_TULIU = "土流网";

    private static CountDownLatch sCountDownLatch = null;

    private static  int THREAD_NUMBER = 0;

    /**
     * 抓取宅基地数据
     * @return
     */
    @RequestMapping("/getZhaijidi")
    public String getZhaijidi(){
        Integer landType = 1;
        int count = 0;
        int page = 30;
        for(int i = 1 ;i< page ;i++){
            try{
                String url = "https://www.tuliu.com/gongying/nongcunzhaijidi/list-pg"+i+".html";
                String result = HttpUtils.get(url,null,HttpUtils.CHAR_SET_UTF8);
                List<SecondLand> secondLandList = secondLandParser.parserHtml(result,landType,LAND_SOURCE_TULIU);
                if(CollectionUtils.isNotEmpty(secondLandList)){
                    secondLandService.batchInsert(secondLandList);
                    count += secondLandList.size();
                    log.info("抓取宅基地数据===================================================第" + i + "页数据保存成功的数据条数：" + secondLandList.size());
                }else{
                    log.error("抓取宅基地数据===================================================第" + i + "页数据解析异常!");
                }
            }catch (Exception e){
                log.error("抓取宅基地数据===================================================第" + i + "页数据时发生异常:" + e.getMessage(),e);
            }
        }
        return  "抓取宅基地数据===================================================本次处理了" + page + "页数据，保存成功了"+count+"条数数据";
    }


    /**
     * 抓取工商用地数据
     * @return
     */
    @RequestMapping("/getGongShang")
    public String getGongShang(){
        Integer landType = 2;
        int count = 0;
        int page = 30;
        for(int i = 1 ;i<page ;i++){
            try{
                String url = "https://www.tuliu.com/gongying/chengshi/list-pg"+i+".html";
                String result = HttpUtils.get(url,null,HttpUtils.CHAR_SET_UTF8);
                List<SecondLand> secondLandList = secondLandParser.parserHtml(result,landType,LAND_SOURCE_TULIU);
                if(CollectionUtils.isNotEmpty(secondLandList)){
                    secondLandService.batchInsert(secondLandList);
                    count += secondLandList.size();
                    log.info("抓取工商用地数据===================================================第" + i + "页数据保存成功的数据条数：" + secondLandList.size());
                }else{
                    log.error("抓取工商用地数据===================================================第" + i + "页数据解析异常!");
                }
            }catch (Exception e){
                log.error("抓取工商用地数据===================================================第" + i + "页数据时发生异常:" + e.getMessage(),e);
            }
        }
        return  "抓取工商用地数据===================================================本次处理了" + page + "页数据，保存成功了"+count+"条数数据";
    }

    /**
     * 抓取农村用地数据（电话核实后的）
     * @return
     */
    @RequestMapping("/getNongcun")
    public String getNongcun(){
        Integer landType = 3;
        int count = 0;
        int page = 30;
        for(int i = 1 ;i<30 ;i++){
            try{
                String url = "https://www.tuliu.com/gongying/nongcun/list-vt2-pg"+i+".html";
                String result = HttpUtils.get(url,null,HttpUtils.CHAR_SET_UTF8);
                List<SecondLand> secondLandList = secondLandParser.parserHtml(result,landType,LAND_SOURCE_TULIU);
                if(CollectionUtils.isNotEmpty(secondLandList)){
                    secondLandService.batchInsert(secondLandList);
                    count += secondLandList.size();
                    log.info("抓取农村用地数据（电话核实后的）===================================================第"+ i + "页数据保存成功的数据条数：" + secondLandList.size());
                }else{
                    log.error("抓取农村用地数据（电话核实后的）===================================================第" + i + "页数据解析异常!");
                }
            }catch (Exception e){
                log.error("抓取农村用地数据（电话核实后的）===================================================第" + i + "页数据时发生异常:" + e.getMessage(),e);
            }

        }
        return  "抓取农村用地数据（电话核实后的）===================================================本次处理了" + page + "页数据，保存成功了"+count+"条数数据";
    }


    @RequestMapping("/multiThreadParser")
    public String multiThreadParser(Integer startId ,Integer endId){
        String logMsg = "多线程解析id区间段从" + startId + "  到 " + endId + "的数据=====================================";
        List<SecondLand> secondLandList = secondLandService.findWithId(startId, endId);
        if(CollectionUtils.isEmpty(secondLandList)){
            return logMsg + "需要处理的数据条数=" + (secondLandList.size());
        }
        int threadHandleCount = 10;//每个线程处理的任务个数

        long start = System.currentTimeMillis();
        List<BatchParseThread> batchParseThreadList = new ArrayList<BatchParseThread>();
        BatchParseThread batchParseThread = null;
        //整除的情况
        for(int i = 0; i<secondLandList.size() ;i++){
            if( i % threadHandleCount == 0){
                if(null != batchParseThread ){
                    batchParseThreadList.add(batchParseThread);
                }
                batchParseThread = new BatchParseThread();
            }
            batchParseThread.addSecondLand(secondLandList.get(i));
        }
        //如果有余数的，也要加进来
        if(null != batchParseThread && !CollectionUtils.isEmpty(batchParseThread.getSecondLandList())){
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
            log.error(logMsg + "当前线程（即主线程）等待时发生异常：" + e.getMessage(),e);
        }
        long end = System.currentTimeMillis();
        return logMsg + THREAD_NUMBER + "个子线程已经执行完毕 耗时：" + (end - start) + "毫秒";
    }

    private static class BatchParseThread implements Runnable{

        protected List<SecondLand> secondLandList = new ArrayList<SecondLand>();

        public List<SecondLand> getSecondLandList() {
            return secondLandList;
        }

        public void setSecondLandList(List<SecondLand> secondLandList) {
            this.secondLandList = secondLandList;
        }

        public void addSecondLand(SecondLand secondLand){
            if(null != secondLand){
                secondLandList.add(secondLand);
            }
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            int count = 0;
            SecondLandDetail query = null;
            try{

                SecondLandDetailParser  secondLandDetailParser = SpringContextHolder.getBean("secondLandDetailParser");
                SecondLandDetailService secondLandDetailService= SpringContextHolder.getBean("secondLandDetailService");

                SecondLandDetail secondLandDetail = null;
                List<SecondLandDetail> secondLandDetailList = new ArrayList<>();
                for(SecondLand secondLand :secondLandList){
                    if(null == secondLand || secondLand.getId() <= 0){
                        continue;
                    }
//                    判断是否已经解析过了
                    query = new SecondLandDetail();
                    query.setSecondLandId(secondLand.getId());
                    count = secondLandDetailService.findByCount(query);
                    if(count > 0){
                        continue;
                    }

                    secondLandDetail = secondLandDetailParser.parserHtml(secondLand.getId(),secondLand.getContent(),secondLand.getHref());
                    if(null != secondLandDetail){
                        secondLandDetailList.add(secondLandDetail);
                    }
                }
                if(CollectionUtils.isNotEmpty(secondLandDetailList)){
                    int insertCount = secondLandDetailService.batchInsert(secondLandDetailList);

                    log.info("批量插入数据，返回结果insertCount=" + insertCount);

                }
            }catch (Exception e){
                log.error("当前子线程(" + Thread.currentThread().getName() + ")执行发生异常：" + e.getMessage(), e);
            }
            long end = System.currentTimeMillis();
            log.info("当前子线程(" + Thread.currentThread().getName() + ")已经执行完毕 耗时：" + (end - start) + "毫秒");
            sCountDownLatch.countDown();
        }
    }



}


