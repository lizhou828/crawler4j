package com.baodiwang.crawler4j.utils;

import java.util.Map;

/**
 * Created by lizhou on 2018年08月23日 23时11分
 */
public class LandChinaHttpBreaker {

    /**
     * 目录：打破 www.landchina.com 反扒屏障
     * 原因：www.landchina.com 对http请求做了反扒处理，每个请求需要跳转3次才能获取正真的数据
     *
     * @param pageUrl
     * @return
     */
    public static String breakBarrier(String pageUrl){
        Map<String,String> cookieMapOne = stepOne(pageUrl);
        Map<String,String> cookieMapTwo = stepTwo(pageUrl,cookieMapOne);

        return "";
    }

    public static Map<String,String> stepOne(String pageUrl){
        return null;
    }
    public static Map<String,String> stepTwo(String pageUrl,Map<String,String> cookieMapOne){
        return null;
    }

}
