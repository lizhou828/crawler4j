/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.utils.NetUtils.java <2018年09月17日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月17日 16时25分
 */
public class NetUtils {

    /**
     * @Description: 获取本机在局域网中的IP地址
     * @Date 2018年09月17日 16时25分
     */
    public static String getLanIpInWindows(){
        try{
            String lanIp = InetAddress.getLocalHost().toString();
            if(StringUtils.isNotEmpty(lanIp) &&lanIp.contains("/")){
                return lanIp.substring(0,lanIp.indexOf("/"));
            }else {
                return lanIp;
            }
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取本机的主机名
     * @return
     */
    public static String getHostNameInWindows(){
        try{
            String lanIp = InetAddress.getLocalHost().toString();
            if(StringUtils.isNotEmpty(lanIp) && lanIp.contains("/")){
                return lanIp.substring(lanIp.indexOf("/")+1,lanIp.length());
            }else {
                return lanIp;
            }
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getLanIpInWindows());
        System.out.println(getHostNameInWindows());


    }



}
