package com.baodiwang.crawler4j.service;

import com.baodiwang.crawler4j.ApplicationTests;
import com.baodiwang.crawler4j.model.SecondLand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lizhou on 2018年10月07日 16时46分
 */
public class SecondLandDetailServiceImplTest extends ApplicationTests {
    @Autowired
    SecondLandDetailService secondLandDetailService;
    @Autowired
    private SecondLandService secondLandService;


    @Test
    public void getContactsPhoneTest(){
        SecondLand secondLand  = secondLandService.getByPK(1);
        String telephone = secondLandDetailService.getContactsPhone(secondLand.getContent(),secondLand.getHref());
        System.out.println("telephone =" + telephone );
    }
}
