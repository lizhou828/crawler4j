/*
 *Project: credentials
 *File: com.glorypty.credentials.client.utils.RegexUtil.java <2018年05月10日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年05月10日 14时47分
 */
public class RegexUtil {
    public static String findMatchContent(String regx, String source) {
        if(StringUtils.isEmpty(regx) ||  StringUtils.isEmpty(source)) {
            return null;
        }

        Matcher m = Pattern.compile(regx).matcher(source);
        if(m.find()) {
            return m.group();
        }
        return null;
    }

    public static List<String> findMatchContents(String regx, String source) {
        if(StringUtils.isEmpty(regx) || StringUtils.isEmpty(source)) {
            return null;
        }
        ArrayList lst = new ArrayList();
        Matcher m = Pattern.compile(regx).matcher(source);
        while(m.find()) {
            lst.add(m.group());
        }
        return lst;
    }

    public static void main(String[] args) {
        String src = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_3 like Mac OS X) AppleWebKit/603.3.8 (KHTML, like Gecko) Mobile/14G60 MicroMessenger/6.5.18 NetType/WIFI Language/zh_CN";

        String otherRegex = "\\([A-Za-z0-9_\\s,]+\\)";
        String otherStr = findMatchContent(otherRegex ,src);
        System.out.println("otherStr=" + otherStr+ ",src= " + src);

        String OS_AND_DEVICE_REGEX = "\\([A-Za-z0-9_\\s;]+\\)";
        String osAndDeviceStr = findMatchContent(OS_AND_DEVICE_REGEX ,src);
        System.out.println("osAndDeviceStr=" + osAndDeviceStr + ",src= " + src);

        String pageInfoStr = "共11681页 当前只显示200页 共350406条记录";
        String pageInfoStrMatch = RegexUtil.findMatchContent("共\\d+页", pageInfoStr);
        String allRecodes = RegexUtil.findMatchContent("共\\d+条记录", pageInfoStr);
        System.out.println("pageInfoStrMatch=" + pageInfoStrMatch);


    }
}
