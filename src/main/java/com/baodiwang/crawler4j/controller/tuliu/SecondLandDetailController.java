/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.controller.tuliu;

import com.baodiwang.crawler4j.model.SecondLandDetail;

import com.baodiwang.crawler4j.utils.HttpUtils;
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


    @RequestMapping("/getZhaijidiDetail")
    public String getZhaijidi(){
        String url = "https://zunyixian.tuliu.com/s-view-101235.html";
        String result = HttpUtils.get(url, null, HttpUtils.CHAR_SET_UTF8);
        return result;
    }

}


