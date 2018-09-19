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
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baodiwang.crawler4j.service.SecondLandDetailService;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class SecondLandDetailController {

    private static Log log = LogFactory.getLog(SecondLandDetailController.class);

    @Autowired
    private SecondLandDetailService secondLandDetailService;

    @Autowired
    private SecondLandService secondLandService;

    @Autowired
    SecondLandDetailParser secondLandDetailParser;


    @RequestMapping("/fetchDetail")
    public String fetchDetail(Integer startId ,Integer endId){
        String logMsg = "抓取id区间段从" + startId + "  到 " + endId + "的数据=====================================";
        List<SecondLand> secondLandList = secondLandService.findWithoutContent(startId, endId);
        if(CollectionUtils.isEmpty(secondLandList)){
            return logMsg + "没有要处理的数据";
        }
        SecondLand update = null;
        int successCount = 0;
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
        return (logMsg + "本次要处理的数据条数为：" + secondLandList.size() + ",更新成功的条数为：" + successCount);
    }


    @RequestMapping("/parserZhaijidiDetail")
    public String parserZhaijidiDetail(Integer secondLandId){
        String logMsg = "解析id=" + secondLandId+ "  的数据=====================================";
        SecondLandDetail detail = secondLandDetailParser.parserHtml(secondLandId);
        return detail.toString();
    }



}


