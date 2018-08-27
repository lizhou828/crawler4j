/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.schedule.RemiseNoticeDetailSchedule.java <2018年08月23日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.schedule;

import com.baodiwang.crawler4j.VO.RemiseNoticeVo;
import com.baodiwang.crawler4j.controller.detailPage.RemiseNoticeDetailParser;
import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.model.RemiseNoticeDetail;
import com.baodiwang.crawler4j.service.RemiseNoticeDetailService;
import com.baodiwang.crawler4j.service.RemiseNoticeService;
import com.baodiwang.crawler4j.utils.StringUtils;
import com.github.pagehelper.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月23日 10时40分
 */
@Component
public class RemiseNoticeDetailSchedule {

    private static final Logger log = LogManager.getLogger(RemiseNoticeDetailSchedule.class);

    @Autowired
    RemiseNoticeService remiseNoticeService;

    @Autowired
    RemiseNoticeDetailService remiseNoticeDetailService;

    /**
     * 定时的解析已抓取到的网页，并存到相关表中
     */
//    @Scheduled(cron = "0 */5 * * * ?")//,每隔5分钟执行一次
    @Scheduled(cron = "*/20 * * * * ?")//,每隔10秒执行一次（测试）
    public void parseDataToRemiseNoticeDetail(){
        log.info("定时的解析已抓取到的网页，并存到相关表中.............................开始运行");


        List<RemiseNotice> remiseNoticeList = remiseNoticeService.findNoticeWithoutDetail();
        if(null == remiseNoticeList || remiseNoticeList.isEmpty() ){
            log.info("定时的解析已抓取到的网页，并存到相关表中.............................没有需要处理的数据");
            return;
        }

        log.info("定时的解析已抓取到的网页，并存到相关表中.............................本次需要解析的数据条数："+(CollectionUtils.isEmpty(remiseNoticeList) ? 0 : remiseNoticeList.size()));
        for(RemiseNotice remiseNotice :remiseNoticeList){
            if(null == remiseNotice || null == remiseNotice.getId() || StringUtils.isEmpty(remiseNotice.getContent())){
                continue;
            }
            RemiseNoticeDetail temp = new RemiseNoticeDetail();
            temp.setNoticeId(remiseNotice.getId());
            int detailCount = remiseNoticeDetailService.findByCount(temp);
            if( detailCount > 0){
                continue;//已经解析过则不再处理
            }

            if(StringUtils.isEmpty(remiseNotice.getContent()) || remiseNotice.getContent().length() < 1000){
                log.warn("定时的解析已抓取到的网页，并存到相关表中.............................有获取不到内容的数据,先不处理remiseNotice= " + remiseNotice);
                continue;
            }

            RemiseNoticeVo remiseNoticeVo = RemiseNoticeDetailParser.parseHtml(remiseNotice.getContent());
            List<RemiseNoticeDetail> remiseNoticeDetailList = null == remiseNoticeVo ? null : remiseNoticeVo.getRemiseNoticeDetailList();
            if(null != remiseNoticeDetailList && !remiseNoticeDetailList.isEmpty()){
                for(RemiseNoticeDetail remiseNoticeDetail :remiseNoticeDetailList){
                    if(null == remiseNoticeDetail){
                        continue;
                    }
                    remiseNoticeDetail.setCreateTime(new Timestamp(new Date().getTime()));
                    remiseNoticeDetail.setNoticeId(remiseNotice.getId());

                }
                log.info("定时的解析已抓取到的网页，并存到相关表中.............................本次成功解析的数据条数："+(CollectionUtils.isEmpty(remiseNoticeDetailList) ? 0 : remiseNoticeDetailList.size()));
                remiseNoticeDetailService.batchInsert(remiseNoticeDetailList);
            }

            RemiseNotice update  = null == remiseNoticeVo ? null : remiseNoticeVo.getRemiseNotice();
            if(null != update ){
                update.setId(remiseNotice.getId());
                if(StringUtils.isNotEmpty(remiseNotice.getNoticeNum()) && !remiseNotice.getNoticeNum().equals(update.getNoticeNum())){
                    remiseNoticeService.update(update);
                }
            }
        }
    }
}
