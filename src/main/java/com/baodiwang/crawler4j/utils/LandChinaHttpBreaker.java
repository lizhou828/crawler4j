package com.baodiwang.crawler4j.utils;

import com.baodiwang.crawler4j.VO.RemiseNoticeVo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhou on 2018年08月23日 23时11分
 */
public class LandChinaHttpBreaker {

    private static final Logger log = LogManager.getLogger(LandChinaHttpBreaker.class);
    private static String yunsuo_session_verify = "";
    private static String security_session_mid_verify = "";


    /**
     * 目录：打破 www.landchina.com 反扒屏障
     * 原因：www.landchina.com 对http请求做了反扒处理，每个请求需要跳转3次才能获取正真的数据
     * 机制：递归调用，直到能获取到数据为止（容易造成IP被封，慎用!!!）
     *
     * @param webPageUrl
     * @return
     */
    @Deprecated
    public static String breakBarrier(String webPageUrl, Map<String, String> headMap, Map<String, String> paramsMap){
        return breakBarrier(webPageUrl,  headMap,  paramsMap,HttpUtils.CHAR_SET_GB2312);
    }

    /**
     * 目录：打破 www.landchina.com 反扒屏障
     * 原因：www.landchina.com 对http请求做了反扒处理，每个请求需要跳转3次才能获取正真的数据
     * 机制：递归调用，直到能获取到数据为止（容易造成IP被封，慎用!!!）
     *
     * @param webPageUrl
     * @return
     */
    @Deprecated
    public static String breakBarrier(String webPageUrl, Map<String, String> headMap, Map<String, String> paramsMap,String charSet){
         if(StringUtils.isEmpty(charSet)){
            charSet = HttpUtils.CHAR_SET_GB2312;
        }
        if(StringUtils.isEmpty(webPageUrl)){
            return null;
        }
        HttpResponse response = HttpUtils.postWithResponse(webPageUrl, headMap,paramsMap);
        if(null == response){
            return null;
        }
        //返回获取实体
        HttpEntity entity = response.getEntity();
        String webContent  = "";
        //获取网页内容，指定编码
        try {
            webContent  = HttpUtils.convertStreamToString(entity.getContent(),charSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(webContent )){
            return null;
        }
        if(webContent.length() > 5000){
            System.out.println("webContent.length()=" + webContent.length() + ",请求成功！===========================================================================" +
                    "\n===========================================================================" +
                    "\n===========================================================================");
            yunsuo_session_verify = "";
            security_session_mid_verify = "";
            return webContent;
        }

        Map<String,String> cookieMap = null;

        if(null != response.getHeaders("Set-Cookie")  && response.getHeaders("Set-Cookie").length > 0){
            String cookieValue = response.getHeaders("Set-Cookie")[0].getValue();
            cookieMap = HttpUtils.cookieValueToMap(cookieValue);
            if(cookieMap.containsKey("yunsuo_session_verify")){
                yunsuo_session_verify = cookieMap.get("yunsuo_session_verify");
                security_session_mid_verify = "";
            }
            if(cookieMap.containsKey("security_session_mid_verify")){
                security_session_mid_verify = cookieMap.get("security_session_mid_verify");
            }
        }




        if (StringUtils.isNotEmpty(yunsuo_session_verify) &&  StringUtils.isEmpty(security_session_mid_verify)){ //解析第一次请求
            System.out.println("解析第一次请求！");
            headMap = new HashMap<>();
            headMap.put("Host", "www.landchina.com");
            headMap.put("Connection","keep-alive");
            headMap.put("Upgrade-Insecure-Requests","1");
            headMap.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            headMap.put("Accept-Encoding","gzip, deflate");
            headMap.put("Accept-Language","zh-CN,zh;q=0.9");
            headMap.put("Cookie","yunsuo_session_verify=" + yunsuo_session_verify + "; srcurl=" + StringUtils.stringToHex(webPageUrl));
            headMap.put("Referer",webPageUrl );
            System.out.println("发起第二次请求webPageUrl=" + webPageUrl +",headMap=" + headMap);
            if(!webPageUrl.contains("security_verify_data")){
                webPageUrl = webPageUrl+"&security_verify_data=313336362c373638";
            }
            return breakBarrier(webPageUrl,headMap,paramsMap,charSet);//发起第二次请求


        }else if (StringUtils.isNotEmpty(yunsuo_session_verify) && StringUtils.isNotEmpty(security_session_mid_verify)){ //解析第二次请求
            System.out.println("解析第二次请求！");
            headMap = new HashMap<>();
            headMap.put("Host", "www.landchina.com");
            headMap.put("Connection","keep-alive");
            headMap.put("Upgrade-Insecure-Requests","1");
            headMap.put("Referer",webPageUrl+ "&security_verify_data=313336362c373638");
            headMap.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            headMap.put("Accept-Encoding","gzip, deflate");
            headMap.put("Accept-Language","zh-CN,zh;q=0.9");

            if(headMap.containsKey("Cookie")){
                String cookieValue = headMap.get("Cookie");
                cookieValue += "; security_session_mid_verify="+security_session_mid_verify;
                headMap.put("Cookie",cookieValue);
            }else{
                String cookieValue = "yunsuo_session_verify=" + yunsuo_session_verify;
                cookieValue += "; security_session_mid_verify="+security_session_mid_verify;
                cookieValue +="; srcurl=" + StringUtils.stringToHex(webPageUrl);;
                headMap.put("Cookie",cookieValue);
            }
            if(webPageUrl.contains("&security_verify_data")){
                webPageUrl = webPageUrl.substring(0,webPageUrl.indexOf("&security_verify_data"));
            }
            System.out.println("发起第三次请求webPageUrl=" + webPageUrl +",headMap=" + headMap);
            return breakBarrier(webPageUrl,headMap,paramsMap,charSet);//发起第三次请求
        }else{
            System.out.println("未知异常，无法处理!");
            return null;
        }
    }

}
