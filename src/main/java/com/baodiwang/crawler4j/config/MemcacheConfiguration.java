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
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(servers);
        pool.setFailover(failover);
        pool.setInitConn(initConn);
        pool.setMinConn(minConn);
        pool.setMaxConn(maxConn);
        pool.setMaintSleep(maintSleep);
        pool.setNagle(nagle);
        pool.setSocketTO(socketTO);
        pool.setAliveCheck(aliveCheck);
        pool.initialize();
        return pool;
    }

    @Bean
    public MemCachedClient memCachedClient(){
        return new MemCachedClient();
    }
}
