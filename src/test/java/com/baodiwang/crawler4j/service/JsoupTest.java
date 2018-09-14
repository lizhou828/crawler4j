/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.service.JsoupTest.java <2018年09月13日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.service;

import com.baodiwang.crawler4j.ApplicationTests;
import com.baodiwang.crawler4j.model.RemiseNotice;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月13日 14时03分
 */
public class JsoupTest extends ApplicationTests{

    @Autowired
    RemiseNoticeService remiseNoticeService;

    @Autowired
    RemiseNoticeDetailService remiseNoticeDetailService;

    @Test
    public void parseWebContentTest(){
        RemiseNotice remiseNotice = remiseNoticeService.getByPK(647);
        Document doc = Jsoup.parse(remiseNotice.getContent());
        System.out.println(doc.text());
    }
}
