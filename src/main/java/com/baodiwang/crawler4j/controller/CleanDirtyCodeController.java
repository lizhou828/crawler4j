/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.controller.CleanDirtyCodeController.java <2018年09月14日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.controller;

import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.service.RemiseNoticeService;
import com.baodiwang.crawler4j.service.impl.CompareService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月14日 16时30分
 */
@RestController
public class CleanDirtyCodeController {

    private static final Logger log = LogManager.getLogger(CleanDirtyCodeController.class);

    @Autowired
    CompareService compareService;

    @Autowired
    RemiseNoticeService remiseNoticeService;

    @RequestMapping("/clean")
    public String clean (){
        Long startId = 4000L;
        Long endId = 5000L;
        long start = System.currentTimeMillis();
        List<RemiseNotice> remiseNoticeList = remiseNoticeService.findContentWithDirtyCode(startId, endId);
        long end = System.currentTimeMillis();
        log.info("查询需要清除乱码数据的条数：" + (CollectionUtils.isEmpty(remiseNoticeList) ? 0 : remiseNoticeList.size()) + ",耗时" + (end - start) + "毫秒");

        start = System.currentTimeMillis();
        try{
            for(int i =0 ;i < remiseNoticeList.size();i++){
                RemiseNotice remiseNotice = remiseNoticeList.get(i);
                Boolean result = compareService.compareContent(Integer.parseInt(remiseNotice.getId() + ""));
                if( null == result ){
                    log.info("需要清除乱码数据的条数共"+remiseNoticeList.size()+"条，当前第"+i+"条无需更新");
                }else if (result ){
                    log.info("需要清除乱码数据的条数共"+remiseNoticeList.size()+"条，当前第"+i+"条更新成功!");
                }else {
                    log.error("需要清除乱码数据的条数共"+remiseNoticeList.size()+"条，当前第"+i+"条更新失败.....");
                }
//            try {
//                long sleepSeconds = 2L;
//                Thread.sleep(sleepSeconds * 1000);
//                log.info("已休眠" + sleepSeconds +"秒");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        end = System.currentTimeMillis();
        return "startId=" + startId + ",endId=" + endId  + ",此区间段的数据更新完毕，耗时" + ((end-start)/1000) + "秒==================================================";
    }
}
