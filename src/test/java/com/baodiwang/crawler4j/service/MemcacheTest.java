/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.service.MemcacheTest.java <2018年09月11日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.service;

import com.baodiwang.crawler4j.ApplicationTests;
import com.whalin.MemCached.MemCachedClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月11日 15时08分
 */
public class MemcacheTest extends ApplicationTests {

    @Autowired
    MemCachedClient memCachedClient;
    @Test
    public void memcachedTest() throws InterruptedException {
        // 放入缓存
        boolean flag = memCachedClient.set("a", 1);

        // 取出缓存
        Object a = memCachedClient.get("a");
        System.out.println(a);


        // 3s后过期
        memCachedClient.set("b", "2", new Date(3000));
        Object b = memCachedClient.get("b");
        System.out.println(b);

        Thread.sleep(3000);
        b = memCachedClient.get("b");
        System.out.println(b);

    }

}
