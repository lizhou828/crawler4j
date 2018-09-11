/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.utils.DateUtils.java <2018年09月11日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月11日 16时05分
 */
public class DateUtils {

    /* landchina 网站上的cookie过期时间的格式 */
    public static final String COOKIE_EXPIRED_DATE_PATTERN_LAND_CHINA = "EEE, dd-MMM-yy HH:mm:ss z";

    public static Date parseGmtDateToChineseTime(String gmtString,String pattern){
        if (StringUtils.isEmpty(gmtString) || StringUtils.isEmpty(pattern)){
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        Date date = null;
        try {
            date = format.parse(gmtString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(null != date){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            String dateStr = simpleDateFormat.format(date);
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static Date parseGmtDateToChineseTime(String gmtString){
        if (StringUtils.isEmpty(gmtString)){
            return null;
        }
        String pattern = "EEE, dd MMM yyyy HH:mm:ss z";
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        Date date = null;
        try {
            date = format.parse(gmtString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(null != date){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            String dateStr = simpleDateFormat.format(date);
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static void main(String[] args) {
        Date d =parseGmtDateToChineseTime("Tue, 26 Feb 2013 09:26:57 GMT");
        System.out.println(d.getTime() + "," + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));


        Date date  = parseGmtDateToChineseTime("Fri, 14-Sep-18 15:56:44 GMT",COOKIE_EXPIRED_DATE_PATTERN_LAND_CHINA);
        System.out.println(date.getTime() + "," + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

    }
}
