/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.controller.tuliu;


import com.baodiwang.crawler4j.model.SecondLand;
import com.baodiwang.crawler4j.utils.HttpUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baodiwang.crawler4j.service.SecondLandService;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class SecondLandController {

    private static Log log = LogFactory.getLog(SecondLandController.class);

    @Autowired
    private SecondLandService secondLandService;

    @Autowired
    SecondLandParser secondLandParser;

    @RequestMapping("/getZhaijidi")
    public String getZhaijidi(){
        for(int i = 1 ;i<150 ;i++){
            try{
                String url = "https://www.tuliu.com/gongying/nongcunzhaijidi/list-pg"+i+".html";
                String result = HttpUtils.get(url,null,HttpUtils.CHAR_SET_UTF8);
                List<SecondLand> secondLandList = secondLandParser.parserHtml(result);
                if(CollectionUtils.isNotEmpty(secondLandList)){
                    secondLandService.batchInsert(secondLandList);
                    log.info("抓取宅基地数据===================================================第" + i + "页数据保存成功!");
                }else{
                    log.error("抓取宅基地数据===================================================第" + i + "页数据解析异常!");
                }
            }catch (Exception e){
                log.error("抓取宅基地数据===================================================第" + i + "页数据时发生异常:" + e.getMessage(),e);
            }

        }
        return "success";
    }


}


