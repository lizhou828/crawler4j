/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.service.SecondLandServiceTest.java <2018年09月18日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.service;

import com.baodiwang.crawler4j.ApplicationTests;
import com.baodiwang.crawler4j.model.SecondLand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月18日 16时53分
 */
public class SecondLandServiceTest extends ApplicationTests {

    @Autowired
    private SecondLandService secondLandService;

    @Test
    public void getByPKTest() throws Exception {
        SecondLand secondLand  = secondLandService.getByPK(1);
        System.out.println(secondLand );
    }
}
