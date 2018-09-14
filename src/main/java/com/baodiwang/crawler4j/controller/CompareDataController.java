/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.controller.CompareDataController.java <2018年09月13日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.controller;

import com.baodiwang.crawler4j.service.impl.CompareService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 比对数据的准确性
 *
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月13日 09时34分
 */
@RestController
public class CompareDataController {

    private static final Logger log = LogManager.getLogger(CompareDataController.class);

    @Autowired
    CompareService compareService;
    /**
     *
     * @param id remiseNoticeId
     * @param autoUpdate 数据不一致时，是否需要自动更新数据
     * @return
     */
    @RequestMapping("/compare")
    public String compare(Integer id,Boolean autoUpdate){
        if(null == autoUpdate){
            autoUpdate = true;
        }
        Boolean result =  compareService.compare(id,autoUpdate);
        return result == null ? "无需更新" : ( result ? "更新成功!" : "更新失败!");
    }

}
