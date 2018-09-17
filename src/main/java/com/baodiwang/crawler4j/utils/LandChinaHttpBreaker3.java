/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.utils.LandChinaHttpBreaker3.java <2018年09月11日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.utils;

import com.baodiwang.crawler4j.constants.Constant;
import com.whalin.MemCached.MemCachedClient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月11日 15时26分
 */
@Service
public class LandChinaHttpBreaker3 {

    //多个程序同时运行的话，各自的key也就不一样
    public static final String YUNSUO_SESSION_VERIFY_KEY = NetUtils.getLanIpInWindows() + "_yunsuo_session_verify";

    public static final String YUNSUO_SESSION_VERIFY = "yunsuo_session_verify";

    public static final String ASP_NET_SESSIONID = "ASP.NET_SessionId";

    /* 默认过期时间，3天（单位毫秒） */
    public static final Integer YUNSUO_SESSION_VERIFY_EXPIRED = 3 * 24 * 3600 * 1000;

    private static final Logger log = LogManager.getLogger(LandChinaHttpBreaker3.class);

    @Autowired
    MemCachedClient memCachedClient;

    /**
     * 目录：打破 www.landchina.com 反扒屏障
     * 原因：www.landchina.com 对http请求做了反扒处理，
     * 机制：先获取yunsuo_session_verify3天有效期），通过yunsuo_session_verify 抓取真正网页数据
     *
     * @param webPageUrl
     * @return
     */
    public String breakBarrierPost(String webPageUrl, Map<String, String> headMap, Map<String, String> paramsMap) {
        return breakBarrierPost(webPageUrl, headMap, paramsMap, HttpUtils.CHAR_SET_GBK);
    }

    public String breakBarrierPost(String webPageUrl, Map<String, String> headMap, Map<String, String> paramsMap, String charSet) {
        Map<String, String> map = stepOne();
        if(null == map || !map.containsKey(YUNSUO_SESSION_VERIFY_KEY)){
            log.error("获取yunsuo_session_verify============================失败：map=" + map);
            return null;
        }
        String yunsuo_session_verify = map.get(YUNSUO_SESSION_VERIFY_KEY);
        String ASPNET_SessionId = map.containsKey(ASP_NET_SESSIONID) ? map.get(ASP_NET_SESSIONID) : "";
        String cookieValue = "yunsuo_session_verify="+yunsuo_session_verify+"; Hm_lvt_83853859c7247c5b03b527894622d3fa=1535094220,1536311959,1536645940; Hm_lpvt_83853859c7247c5b03b527894622d3fa=1536646717";
        if(StringUtils.isNotEmpty(ASPNET_SessionId)){
            cookieValue += "; ASP.NET_SessionId=" + ASPNET_SessionId;
        }
        headMap.put("Cookie", cookieValue);
        return stepTwoPost(webPageUrl, headMap, paramsMap, charSet);

    }

    private Map<String, String> stepOne() {
        Map<String, String> map = new HashMap<>();
        Object aspSessionObj = memCachedClient.get(ASP_NET_SESSIONID);
        String aspSessionId= "";
        if (null != aspSessionObj) {
            aspSessionId = (String) aspSessionObj;
        }
        if (StringUtils.isNotEmpty(aspSessionId)) {
            map.put(ASP_NET_SESSIONID, aspSessionId);
        }

        Object yunsuo_session_verify_Obj = memCachedClient.get(YUNSUO_SESSION_VERIFY_KEY);
        String yunsuo_session_verify = "";
        if (null != yunsuo_session_verify_Obj) {
            yunsuo_session_verify = (String) yunsuo_session_verify_Obj;
        }
        if (StringUtils.isNotEmpty(yunsuo_session_verify)) {
            map.put(YUNSUO_SESSION_VERIFY_KEY, yunsuo_session_verify);
            log.info("获取yunsuo_session_verify============================从memcache中获取到=" + yunsuo_session_verify);
            return map;
        }

        map = getResponseHeaderMap();
        if(null != map && map.containsKey(ASP_NET_SESSIONID)){
            Date expiresDate = new Date( 3 * 24 * 3600 *1000);//默认有效期为3天
            boolean setResult = memCachedClient.set(ASP_NET_SESSIONID, map.get(ASP_NET_SESSIONID), expiresDate);
            log.info("获取" + ASP_NET_SESSIONID + "============================保存到memcache中setResult=" + setResult+"," + ASP_NET_SESSIONID + "=" + memCachedClient.get(ASP_NET_SESSIONID));
        }
        if (null != map && map.containsKey(YUNSUO_SESSION_VERIFY_KEY)) {
            String expiresTime = map.get(YUNSUO_SESSION_VERIFY+"_expires");
            long expiresTimeLong = Long.parseLong(expiresTime);
            long saveTime = expiresTimeLong - new Date().getTime();//解析出来的有效期
            Date expiresDate = null;
            if(saveTime > 0){
                expiresDate = new Date(saveTime);
            }else{
                expiresDate = new Date( 3 * 24 * 3600 *1000);//默认有效期为3天
            }
            boolean setResult = memCachedClient.set(YUNSUO_SESSION_VERIFY_KEY, map.get(YUNSUO_SESSION_VERIFY_KEY), expiresDate);
            log.info("获取yunsuo_session_verify============================发送get请求获取到=" + map.get(YUNSUO_SESSION_VERIFY_KEY));
            log.info("获取yunsuo_session_verify============================保存到memcache中setResult=" + setResult+",yunsuo_session_verify=" + memCachedClient.get(YUNSUO_SESSION_VERIFY_KEY));
            return map;
        } else {
            return null;
        }
    }

    private static Map<String, String> getResponseHeaderMap() {
        String listPageUrl = "http://www.landchina.com/default.aspx?tabid=261";//出让公告（2011年后）
        Map<String, String> headMap = new HashMap<>();
//        headMap.put("Cookie", "yunsuo_session_verify=a07658c84a6e83c4e917857b8aeaad57");
        headMap.put("Referer", "http://www.landchina.com/default.aspx?tabid=261&ComName=default");
        headMap.put("Origin", Constant.HTTP_HOST_LANDCHINA);
        headMap.put("Host", Constant.HOST_LANDCHINA);
        headMap.put("Accept-Encoding", "gzip, deflate");
        headMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        return LandChinaHttpUtils.getResponseHeader(listPageUrl, headMap);
    }

    private static String stepTwoPost(String webPageUrl, Map<String, String> headMap, Map<String, String> paramsMap, String charSet) {
        if (null == headMap) {
            headMap = new HashMap<>();
        }

        headMap.put("Connection", "keep-alive");
        headMap.put("Upgrade-Insecure-Requests", "1");
        headMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headMap.put("Accept-Encoding", "gzip, deflate");
        headMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        headMap.put("Origin", Constant.HTTP_HOST_LANDCHINA);
        headMap.put("Host", Constant.HOST_LANDCHINA);
        if (StringUtils.isEmpty(charSet)) {
            charSet = HttpUtils.CHAR_SET_GBK;
        }
        if (StringUtils.isEmpty(webPageUrl)) {
            return null;
        }
        HttpResponse response = HttpUtils.postWithResponse(webPageUrl, headMap, paramsMap);
        if (null == response) {
            return null;
        }
        for (Header header : response.getAllHeaders()) {
            if (null == header) {
                continue;
            }
            log.debug("header.getName()=" + header.getName() + ",header.getValue()=" + header.getValue());
        }

        //返回获取实体
        HttpEntity entity = response.getEntity();
        String webContent = "";
        //获取网页内容，指定编码
        try {
            webContent = HttpUtils.convertStreamToString(entity.getContent(), charSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(webContent)) {
            return null;
        }
        if (webContent.length() > 5000) {
            log.info("webContent.length()=" + webContent.length() + ",请求成功！===========================================================================" +
                    "\n===========================================================================" +
                    "\n===========================================================================");
            return webContent;
        } else {
            log.info("未能获取到正确的数据......webContent.length()=" + webContent.length() + ",webContent=" + webContent);
            return webContent;
        }
    }



    /**
     * 目录：打破 www.landchina.com 反扒屏障
     * 原因：www.landchina.com 对http请求做了反扒处理，
     * 机制：先获取yunsuo_session_verify3天有效期），通过yunsuo_session_verify 抓取真正网页数据
     *
     * @param webPageUrl
     * @return
     */
    public String breakBarrierGet(String webPageUrl, Map<String, String> headMap) {
        return breakBarrierGet(webPageUrl, headMap, HttpUtils.CHAR_SET_GBK);
    }

    public String breakBarrierGet(String webPageUrl, Map<String, String> headMap, String charSet) {
        Map<String, String> map = stepOne();
        if(null == map || !map.containsKey(YUNSUO_SESSION_VERIFY_KEY)){
            log.error("获取yunsuo_session_verify============================失败：map=" + map);
            return null;
        }
        String yunsuo_session_verify = map.get(YUNSUO_SESSION_VERIFY_KEY);
        String ASPNET_SessionId = map.containsKey(ASP_NET_SESSIONID) ? map.get(ASP_NET_SESSIONID) : "";
        String cookieValue = "yunsuo_session_verify="+yunsuo_session_verify+"; Hm_lvt_83853859c7247c5b03b527894622d3fa=1535094220,1536311959,1536645940; Hm_lpvt_83853859c7247c5b03b527894622d3fa=1536646717";
        if(StringUtils.isNotEmpty(ASPNET_SessionId)){
            cookieValue += "; ASP.NET_SessionId=" + ASPNET_SessionId;
        }
        if (null == headMap) {
            headMap = new HashMap<>();
            headMap.put("Referer", "http://www.landchina.com/default.aspx?tabid=261&ComName=default");
            headMap.put("Origin", Constant.HTTP_HOST_LANDCHINA);
            headMap.put("Host", Constant.HOST_LANDCHINA);
            headMap.put("Accept-Encoding", "gzip, deflate");
            headMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        }
        headMap.put("Cookie", cookieValue);
        return stepTwoGet(webPageUrl, headMap, charSet);
    }

    private static String stepTwoGet(String webPageUrl, Map<String, String> headMap, String charSet) {
        if (null == headMap) {
            headMap = new HashMap<>();
        }
        headMap.put("Connection", "keep-alive");
        headMap.put("Upgrade-Insecure-Requests", "1");
        headMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headMap.put("Accept-Encoding", "gzip, deflate");
        headMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        headMap.put("Origin", Constant.HTTP_HOST_LANDCHINA);
        headMap.put("Host", Constant.HOST_LANDCHINA);
        if (StringUtils.isEmpty(webPageUrl)) {
            return null;
        }
        return HttpUtils.get(webPageUrl, headMap,charSet);
    }

}
