/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.service.MemcacheTest.java <2018年09月11日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.service;

import com.baodiwang.crawler4j.ApplicationTests;
import com.baodiwang.crawler4j.constants.Constant;
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
//        memCachedClient.set("b", "2", new Date(3000));
//        Object b = memCachedClient.get("b");
//        System.out.println(b);
//
//        Thread.sleep(3000);
//        b = memCachedClient.get("b");
//        System.out.println(b);

//        memcache中的add和set方法区别  https://blog.csdn.net/myweishanli/article/details/40536957
//        0、set方法用于设置一个指定key的缓存内容，set方法是add方法和replace方法的集合体。
//        1）、如果要设置的key不存在时，则set方法与add方法的效果一致；
//        2）、如果要设置的key已经存在时，则set方法与replace方法效果一样。


        memCachedClient.set("yunsuo_session_verify", "9a45a9daf225f18583f9b176867de9ab");
        System.out.println(memCachedClient.get("yunsuo_session_verify"));

        memCachedClient.set("ASP.NET_SessionId", "kyyxsjkbih1anfqdgss415up");
        System.out.println(memCachedClient.get("ASP.NET_SessionId"));

        System.out.println(memCachedClient.keyExists("yunsuo_session_verify"));

//        long minId = 28949L;
//        memCachedClient.set(Constant.LANDCHINA_REMISE_NOTICE_MIN_ID, minId);


    }


}
