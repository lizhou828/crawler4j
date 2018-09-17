/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.utils.LandChinaHttpUtils.java <2018年09月11日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.utils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月11日 18时00分
 */
public class LandChinaHttpUtils {
    private static final Logger log = LogManager.getLogger(LandChinaHttpUtils.class);

    public static Map<String,String> getResponseHeader(String webPageUrl, Map<String, String> headMap) {
        return getResponseHeader(webPageUrl, headMap, HttpUtils.CHAR_SET_GBK);
    }
    public static Map<String,String> getResponseHeader(String webPageUrl, Map<String, String> headMap,String charSet) {
        if (StringUtils.isEmpty(webPageUrl)) {
            return null;
        }
        //创建client实例
        CloseableHttpClient client = null;
        String webContent = "";
        Map<String,String> map = new HashMap<>();
        try {
            long start = System.currentTimeMillis();
            client = HttpClients.createDefault();
            //创建httpget实例
            HttpGet httpGet = new HttpGet(webPageUrl);
            httpGet.addHeader("Pragma", "no-cache");
            httpGet.addHeader("Cache-Control", "no-cache");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
            if (null != headMap && !headMap.isEmpty()) {
                for (Map.Entry entry : headMap.entrySet()) {
                    if (StringUtils.isNotEmpty(entry.getKey() + "") && StringUtils.isNotEmpty(entry.getValue() + "")) {
                        httpGet.addHeader(entry.getKey() + "", entry.getValue() + "");
                    }
                }
            }

            //执行 get请求
            HttpResponse response = client.execute(httpGet);
            if(null != response && null != response.getStatusLine()){
                int statusCode = response.getStatusLine().getStatusCode();
                if(200 != statusCode && 302 != statusCode && 304 != statusCode){
                    log.error("执行get请求完成,返回的http响应码=" + statusCode);
                    return null;
                }
            }
            Header[] headers = response.getAllHeaders();
            if(null == headers  || headers.length == 0){
                return null;
            }
            for(Header header : headers){
                if(null == header ){
                    continue;
                }
                log.debug("header.getName()=" + header.getName() + ",header.getValue()=" + header.getValue());
                if("Set-Cookie".equals(header.getName())){
                    if(StringUtils.isNotEmpty(header.getValue()) && header.getValue().contains("yunsuo_session_verify")){
                        //yunsuo_session_verify=a07658c84a6e83c4e917857b8aeaad57; expires=Fri, 14-Sep-18 15:56:44 GMT; path=/; HttpOnly
                        //yunsuo_session_verify=a07658c84a6e83c4e917857b8aeaad57; expires=Fri, 14-Sep-18 15:56:44 GMT; path=/; HttpOnly; path=/
                        String [] arrayStr = header.getValue().split("; ");
                        for(String str :arrayStr){
                            if(StringUtils.isEmpty(str) ){
                                continue;
                            }
                            if(str.contains("yunsuo_session_verify")){
                                String[] s  = str.split("=");
                                if(s.length == 2 && !map.containsKey("yunsuo_session_verify")){
                                    map.put(LandChinaHttpBreaker3.YUNSUO_SESSION_VERIFY_KEY,s[1]);
                                }
                            }else if (str.contains("expires")){
                                String[] s  = str.split("=");
                                if(s.length == 2){
                                    Date expiredDate = DateUtils.parseGmtDateToChineseTime(s[1],DateUtils.COOKIE_EXPIRED_DATE_PATTERN_LAND_CHINA);
                                    map.put("yunsuo_session_verify_" + s[0],null == expiredDate ? "0" : expiredDate.getTime()+"");
                                }
                            }
                        }
                    }else if (StringUtils.isNotEmpty(header.getValue()) && header.getValue().contains("ASP.NET_SessionId")){
                        //ASP.NET_SessionId=dljuigpea3f4nkx0anwtqcvu; path=/; HttpOnly
                        String [] arrayStr = header.getValue().split("; ");
                        for(String str :arrayStr) {
                            if (StringUtils.isEmpty(str)) {
                                continue;
                            }
                            if(str.contains("ASP.NET_SessionId")){
                                String[] s  = str.split("=");
                                if(s.length == 2){
                                    map.put(s[0],s[1]);
                                }
                            }
                        }
                    }
                }else {
                    map.put(header.getName(),header.getValue());
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("执行get请求完成,耗时：" + (end - start) + "毫秒");
            return map;
        } catch (Exception e) {
            log.error("执行get请求发生异常："+e.getMessage(), e);
        } finally {
            if (null != client) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return map;
    }
}
