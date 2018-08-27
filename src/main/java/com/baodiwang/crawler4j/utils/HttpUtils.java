/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.utils.HttpUtils.java <2018年08月22日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月22日 15时06分
 */
public class HttpUtils {

    public static final String CHAR_SET_UTF8 = "UTF-8";

    public static final String CHAR_SET_GBK = "GBK";

    public static final String CHAR_SET_GB2312 = "gb2312";

    private static final Logger log = LogManager.getLogger(HttpUtils.class);


    public static String get(String webPageUrl, Map<String, String> headMap) {
        return get(webPageUrl, headMap, CHAR_SET_GB2312);
    }

    public static String get(String webPageUrl, Map<String, String> headMap, String charSet) {
        if (StringUtils.isEmpty(webPageUrl)) {
            return null;
        }
        //创建client实例
        CloseableHttpClient client = null;
        String webContent = "";
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
            if (null != response.getHeaders("Set-Cookie") && response.getHeaders("Set-Cookie").length > 0) {
                log.info(response.getHeaders("Set-Cookie")[0].getValue());
            }

            //返回获取实体
            HttpEntity entity = response.getEntity();

            //获取网页内容，指定编码
            webContent  = convertStreamToString(entity.getContent(),charSet);

            long end = System.currentTimeMillis();
            log.info(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,sss").format(new Date()) + ":网页字符长度：" + webContent.length() + ",耗时：" + (end - start) + "毫秒");

            return webContent;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (null != client) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return webContent;
    }



    /**
     * 默认字符集编码为GBK
     *
     * @param webPageUrl
     * @param headMap
     * @param paramsMap
     * @return
     */
    public static String post(String webPageUrl, Map<String, String> headMap, Map<String, String> paramsMap) {
        return post(webPageUrl, headMap, paramsMap, CHAR_SET_GB2312);
    }
    public static String post(String webPageUrl, Map<String, String> headMap, Map<String, String> paramsMap, String charSet) {
        if (StringUtils.isEmpty(webPageUrl)) {
            return null;
        }
        //创建client实例
        CloseableHttpClient client = null;
        String webContent = "";
        try {
            long start = System.currentTimeMillis();
            client = HttpClients.createDefault();
            //创建httpget实例
            HttpPost httpPost = new HttpPost(webPageUrl);

            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3278.0 Safari/537.36");
            if (null != headMap && !headMap.isEmpty()) {
                for (Map.Entry entry : headMap.entrySet()) {
                    if (StringUtils.isNotEmpty(entry.getKey() + "") && StringUtils.isNotEmpty(entry.getValue() + "")) {
//                        httpGet.addHeader("Cookie", "security_session_mid_verify=d70d231ed4e7b195938aac569dccf384;");
                        httpPost.addHeader(entry.getKey() + "", entry.getValue() + "");
                    }
                }
            }

            //设置参数
            if (null != paramsMap && !paramsMap.isEmpty()) {
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                for (Iterator iter = paramsMap.keySet().iterator(); iter.hasNext(); ) {
                    String name = (String) iter.next();
                    String value = String.valueOf(paramsMap.get(name));
                    nameValuePairList.add(new BasicNameValuePair(name, value));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, CHAR_SET_UTF8));
            }


            //执行 post请求
            HttpResponse response = client.execute(httpPost);
            if (null != response.getHeaders("Set-Cookie") && response.getHeaders("Set-Cookie").length > 0) {
                log.info(response.getHeaders("Set-Cookie")[0].getValue());
            }

            //返回获取实体
            HttpEntity entity = response.getEntity();
            //获取网页内容，指定编码
            webContent  = convertStreamToString(entity.getContent(),charSet);
            long end = System.currentTimeMillis();
            log.info(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,sss").format(new Date()) + ":网页字符长度：" + webContent.length() + ",耗时：" + (end - start) + "毫秒");

            return webContent;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (null != client) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return webContent;
    }

    public static HttpResponse postWithResponse(String webPageUrl, Map<String, String> headMap, Map<String, String> paramsMap) {
        return postWithResponse(webPageUrl,headMap,paramsMap,CHAR_SET_GB2312);
    }
    public static HttpResponse postWithResponse(String webPageUrl, Map<String, String> headMap, Map<String, String> paramsMap, String charSet) {
        if (StringUtils.isEmpty(webPageUrl)) {
            return null;
        }
        //创建client实例
        CloseableHttpClient client = HttpClients.createDefault();

        //创建httpPost实例
        HttpPost httpPost = new HttpPost(webPageUrl);

        try {
            long start = System.currentTimeMillis();
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3278.0 Safari/537.36");
            if (null != headMap && !headMap.isEmpty()) {
                for (Map.Entry entry : headMap.entrySet()) {
                    if (StringUtils.isNotEmpty(entry.getKey() + "") && StringUtils.isNotEmpty(entry.getValue() + "")) {
//                        httpGet.addHeader("Cookie", "security_session_mid_verify=d70d231ed4e7b195938aac569dccf384;");
                        httpPost.addHeader(entry.getKey() + "", entry.getValue() + "");
                    }
                }
            }

            //设置参数
            if (null != paramsMap && !paramsMap.isEmpty()) {
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                for (Iterator iter = paramsMap.keySet().iterator(); iter.hasNext(); ) {
                    String name = (String) iter.next();
                    String value = String.valueOf(paramsMap.get(name));
                    nameValuePairList.add(new BasicNameValuePair(name, value));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, CHAR_SET_UTF8));
            }

            //执行post请求
            HttpResponse  response  = client.execute(httpPost);
            long end = System.currentTimeMillis();
            log.info("执行post请求完成，耗时：" + (end - start) + "毫秒");
            return response;
        }catch (Exception e){
            log.error("执行post请求发生异常:" + e.getMessage(),e);
        }
        return null;
    }


    /**
     * 把输入流中的数据 按照指定字符集转成字符串 ，防止部分中文乱码的问题
     * @param is  输入流
     * @param charSet  字符集 （默认GBK）
     * @return
     */
    public static String convertStreamToString(InputStream is, String charSet) {
        if (StringUtils.isEmpty(charSet)) {
            charSet = CHAR_SET_GB2312;
        }
        StringBuilder sb1 = new StringBuilder();
        byte[] bytes = new byte[4096];
        int size = 0;

        try {
            while ((size = is.read(bytes)) > 0) {
                String str = new String(bytes, 0, size, charSet);
                sb1.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return sb1.toString();
    }

    public static Map<String,String> cookieValueToMap(String cookieValue){
        if(StringUtils.isEmpty(cookieValue)){
            return null;
        }
        if(!cookieValue.contains("=") || !cookieValue.contains(";")){
            return null;
        }
        String [] keyValueArray = cookieValue.split("; ");
        if(keyValueArray.length == 0){
            return null;
        }
        Map<String ,String> map = new HashMap<>();
        for(String keyValue :keyValueArray){
            if(StringUtils.isEmpty(keyValue)){
                continue;
            }
            String[] kv = keyValue.split("=");
            if(kv.length == 2){
                map.put(kv[0],kv[1]);
            }
        }
        return map;
    }

    public static void main(String[] args) {
        cookieValueToMap("");
    }
}
