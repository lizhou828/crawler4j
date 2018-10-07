/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.controller.tuliu;

import com.baodiwang.crawler4j.model.SecondLand;
import com.baodiwang.crawler4j.model.SecondLandDetail;

import com.baodiwang.crawler4j.service.SecondLandService;
import com.baodiwang.crawler4j.utils.HttpUtils;
import com.baodiwang.crawler4j.utils.RegexUtil;
import com.baodiwang.crawler4j.utils.SpringContextHolder;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baodiwang.crawler4j.service.SecondLandDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
public class SecondLandDetailController {

    private static Log log = LogFactory.getLog(SecondLandDetailController.class);

    @Autowired
    private SecondLandDetailService secondLandDetailService;

    @Autowired
    private SecondLandService secondLandService;

    @Autowired
    SecondLandDetailParser secondLandDetailParser;

    private static CountDownLatch sCountDownLatch = null;

    private static  int THREAD_NUMBER = 0;

    /**
     * 按照id区间段来抓取网页
     * @return
     */
    @RequestMapping("/fetchDetail")
    public String fetchDetail(){

        Integer maxId = secondLandService.findMaxId();
        Integer pageSize = 1000;
        Integer allPages = maxId % pageSize == 0 ? maxId / pageSize : (maxId/pageSize) +1;
        Integer startId = 0;
        Integer endId = 0;
        for(int i = 0 ;i <= allPages ;i++){
            startId = i == 0 ? 1 : i*pageSize;
            endId = i == 0 ? i*pageSize : i*pageSize + pageSize;
            String logMsg = "抓取id区间段从" + startId + "  到 " + endId + "的数据=====================================";
            List<SecondLand> secondLandList = secondLandService.findWithoutContent(startId, endId);
            if(CollectionUtils.isEmpty(secondLandList)){
                log.info(logMsg + "没有要处理的数据");
                continue;
            }
            getContent(secondLandList,logMsg);

        }
        return "success";

    }
    private void getContent(List<SecondLand> secondLandList,String logMsg){
        SecondLand update = null;
        int successCount = 0;
        long start = System.currentTimeMillis();
        for(SecondLand secondLand : secondLandList){
            if(null == secondLand|| StringUtils.isEmpty(secondLand.getHref())){
                log.error(logMsg + ",数据异常：secondLand" + secondLand);
                continue;
            }
            String result = HttpUtils.get(secondLand.getHref(), null, HttpUtils.CHAR_SET_UTF8);
            if(StringUtils.isEmpty(result) || result.length() < 8000){
                log.error(logMsg + ",抓取的网页数据异常：result" + result);
                continue;
            }
            update = new SecondLand();
            update.setId(secondLand.getId());
            update.setContent(result);
            int resultCount = secondLandService.update(update);
            if(resultCount > 0 ){
                log.info(logMsg + ",id=" + secondLand.getId() + "的数据更新成功!" );
                successCount ++ ;
            }
        }
        long end = System.currentTimeMillis();
        log.info(logMsg + "本次要处理的数据条数为：" + secondLandList.size() + ",更新成功的条数为：" + successCount +",耗时"+ (end - start) + "毫秒");
    }

    /**
     * 此种方式抓取数据易导致ip被封，谨慎使用！
     * @param startId
     * @param endId
     * @return
     */
    @RequestMapping("/multiThreadFetchDetail")
    public String multiThreadFetchDetail(Integer startId ,Integer endId){
        String logMsg = "多线程抓取id区间段从" + startId + "  到 " + endId + "的数据=====================================";
        List<SecondLand> secondLandList = secondLandService.findWithoutContent(startId, endId);
        if(CollectionUtils.isEmpty(secondLandList)){
            return logMsg + "没有要处理的数据";
        }

        int threadHandleCount = 10;//每个线程处理的任务个数

        long start = System.currentTimeMillis();
        List<BatchFetchThread> batchFetchThreadList = new ArrayList<BatchFetchThread>();
        BatchFetchThread batchFetchThread = null;
        //整除的情况
        for(int i = 0; i<secondLandList.size() ;i++){
            if( i % threadHandleCount == 0){
                if(null != batchFetchThread ){
                    batchFetchThreadList.add(batchFetchThread);
                }
                batchFetchThread = new BatchFetchThread();
            }
            batchFetchThread.addSecondLand(secondLandList.get(i));
        }
        //如果有余数的，也要加进来
        if(null != batchFetchThread && !CollectionUtils.isEmpty(batchFetchThread.getSecondLandList())){
            batchFetchThreadList.add(batchFetchThread);
        }


        THREAD_NUMBER = batchFetchThreadList.size();
        sCountDownLatch = new CountDownLatch(THREAD_NUMBER);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(THREAD_NUMBER);
        for(BatchFetchThread fetchThread : batchFetchThreadList){
            //放入线程池中，执行线程
            fixedThreadPool.execute(fetchThread);
        }
        try {
            sCountDownLatch.await();//导致当前线程（即主线程）等待，直到latch的计数降为0（除非子线程被打断）
        } catch (Exception e) {
            log.error(logMsg + "当前线程（即主线程）等待时发生异常：" + e.getMessage(),e);
        }
        long end = System.currentTimeMillis();
        return logMsg + THREAD_NUMBER + "个子线程已经执行完毕 耗时：" + (end - start) + "毫秒";
    }


    private static class BatchFetchThread implements Runnable{

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
            try{

                SecondLandService  secondLandService = SpringContextHolder.getBean("secondLandService");
                String logMsg = "多线程抓取id区间段的数据=====================================";
                SecondLand update = null;
                int successCount = 0;
                for(SecondLand secondLand : secondLandList){
                    if(null == secondLand|| StringUtils.isEmpty(secondLand.getHref())){
                        log.error(logMsg + ",数据异常：secondLand" + secondLand);
                        continue;
                    }
                    String result = HttpUtils.get(secondLand.getHref(), null, HttpUtils.CHAR_SET_UTF8);
                    if(StringUtils.isEmpty(result) || result.length() < 10000){
                        log.error(logMsg + ",抓取的网页数据异常：result" + result);
                        continue;
                    }
                    update = new SecondLand();
                    update.setId(secondLand.getId());
                    update.setContent(result);
                    int resultCount = secondLandService.update(update);
                    if(resultCount > 0 ){
                        log.info(logMsg + ",id=" + secondLand.getId() + "的数据更新成功!" );
                        successCount ++ ;
                    }
                }
                log.info(logMsg + "本次要处理的数据条数为：" + secondLandList.size() + ",更新成功的条数为：" + successCount);
            }catch (Exception e){
                log.error("当前子线程(" + Thread.currentThread().getName() + ")执行发生异常：" + e.getMessage(), e);
            }
            long end = System.currentTimeMillis();
            log.info("当前子线程(" + Thread.currentThread().getName() + ")已经执行完毕 耗时：" + (end - start) + "毫秒");
            sCountDownLatch.countDown();
        }
    }


    /**
     * 解析详情的网页为 SecondLandDetail对象
     * @param secondLandId
     * @return
     */
    @RequestMapping("/parserZhaijidiDetail")
    public String parserZhaijidiDetail(Integer secondLandId){
        String logMsg = "解析id=" + secondLandId+ "  的数据=====================================";
        SecondLandDetail detail = secondLandDetailParser.parserHtml(secondLandId);
        return detail.toString();
    }

    /**
     * todo 测试抓取登陆后的页面（因为部分信息只有登陆后才可见）
     */
    @RequestMapping("/fetchAfterLogin")
    public String fetchAfterLogin(){
        String url = "https://wanning.tuliu.com/view-541593.html";
        url = "https://wanning.tuliu.com/landext/view/541593/2";
        Map <String,String> headMap = new HashMap<>();
        headMap.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headMap.put("Accept-Encoding","gzip, deflate, br");
        headMap.put("Accept-Language","zh-CN,zh;q=0.9");
        headMap.put("Connection","keep-alive");
        headMap.put("Cookie","gr_user_id=3bfc5c67-dc75-4231-b417-12f66c5a6842; gr_session_id_8d0f4cbc7395183a=e66cc24c-4d0d-40c9-8f03-731e89ccb961; gr_session_id_8d0f4cbc7395183a_e66cc24c-4d0d-40c9-8f03-731e89ccb961=true; PHPSESSID=ST-1276-agr9czuvbxgdm2GgZ4YS-castuliucom; tluid=860188; tlusername=17520464602; tlauth=0c1etY0RTfof%2FCDYZK%2FjSaZdO9dh%2Ftp3LjT9N6krbx2MypXaj8Liu3x6%2FC%2FRvGk1WVqmgpcB; tluser_agent=Mozilla%2F5.0+%28Windows+NT+6.3%3B+Win64%3B+x64%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F69.0.3497.81+Safari%2F537.36; Hm_lvt_621cacc45e5c3b2243ac6211222ee1e5=1538043259,1538143014,1538188314,1538191311; Hm_lpvt_621cacc45e5c3b2243ac6211222ee1e5=1538191315; XSRF-TOKEN=eyJpdiI6Im1pWGI4WWVLWVh3bGFLOFdsU016TXc9PSIsInZhbHVlIjoiNzhxRVFudkpON3lZUTg3WGZkNXJvVUlxZ0V5SE5SbmtwU1ZMdkhrcGd3cFp5d2FtZkEwdnpTN0F4cmpIM1RkTHRtb251VDFyY0w3XC9FVFM2dTRRTkJBPT0iLCJtYWMiOiI2NTViNzFhNWQwZDY3ZDVkZjVmYTIzMmJkZTg2ZGM4MDVhYzdkODllOTM5YTY5ZTlkYmYxZDRhZmYxZGYyMTgwIn0%3D; tuliu_session=eyJpdiI6IkVQTzhMcDRxQmIrZ2JpTGhDZlRNeVE9PSIsInZhbHVlIjoiQXhKYTk5eFQ3Y0R3eUwzUUxMa2ljUktCbHpuXC85SGZ3QVFnZ21XTFVaNHg4aWdFcVwveGNYMGFWUmZJbzRWNFN2RkZoMU02NjFZOFBQNVQwNVg3ZjlNZz09IiwibWFjIjoiMGJkZmRhYThjOGFkZTI3ZjcxZjdlODk4ZDBiMTE4NTRjMThlNThkMDAyOTgxMGJjNTE3MjM1YzAwMTE5MjU5MiJ9");
        headMap.put("Host","wanning.tuliu.com");
        headMap.put("Pragma","no-cache");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
        String result = HttpUtils.get(url,headMap,HttpUtils.CHAR_SET_UTF8);

        return result;
//        landext/viewphonetatistics/
//        return secondLandDetailService.getContactsPhone(url);
    }


}


