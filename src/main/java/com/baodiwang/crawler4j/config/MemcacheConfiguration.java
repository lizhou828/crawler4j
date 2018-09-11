/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.config.MemcacheConfiguration.java <2018年09月11日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.config;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring-boot整合memcache
 * https://blog.csdn.net/weixin_38336658/article/details/80368606
 * https://blog.csdn.net/saytime/article/details/80585370
 *
 * 新增依赖
 *  https://mvnrepository.com/artifact/com.whalin/Memcached-Java-Client
 <dependency>
 <groupId>com.whalin</groupId>
 <artifactId>Memcached-Java-Client</artifactId>
 <version>3.0.2</version>
 </dependency>
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月11日 15时03分
 */
@Configuration
public class MemcacheConfiguration {
    @Value("${memcache.servers}")
    private String[] servers;

    @Value("${memcache.failover}")
    private boolean failover;

    @Value("${memcache.weights}")
    private int weights;

    @Value("${memcache.initConn}")
    private int initConn;

    @Value("${memcache.minConn}")
    private int minConn;

    @Value("${memcache.maxConn}")
    private int maxConn;

    @Value("${memcache.maintSleep}")
    private int maintSleep;

    @Value("${memcache.nagle}")
    private boolean nagle;

    @Value("${memcache.socketTO}")
    private int socketTO;

    @Value("${memcache.aliveCheck}")
    private boolean aliveCheck;

    @Bean
    public SockIOPool sockIOPool () {
        SockIOPool pool = SockIOPool.getInstance(); //获取连接池的实例

        //服务器列表及其权重
        pool.setServers(servers);
        pool.setFailover(failover);

        //设置初始连接数、最小连接数、最大连接数
        pool.setInitConn(initConn);
        pool.setMinConn(minConn);
        pool.setMaxConn(maxConn);

        //设置连接池守护线程的睡眠时间
        pool.setMaintSleep(maintSleep);

        //设置TCP参数，连接超时
        pool.setNagle(nagle);
        pool.setSocketTO(socketTO);
        pool.setAliveCheck(aliveCheck);

        //初始化并启动连接池
        pool.initialize();
        return pool;
    }

    @Bean
    public MemCachedClient memCachedClient(){
        return new MemCachedClient();
    }
}
