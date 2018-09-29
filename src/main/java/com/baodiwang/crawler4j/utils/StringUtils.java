package com.baodiwang.crawler4j.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lizhou on 2018年08月22日 08时50分
 */
public class StringUtils {

    private static final Logger log = LogManager.getLogger(StringUtils.class);

    public static boolean isValidPhoneNO(String phoneNum){
        if(isNotEmpty(phoneNum)) return false;
        String regexp="^1[3|4|5|6|7|8|9][0-9]\\d{8}$";
        return phoneNum.matches(regexp);
    }

    /**
     * 判断是否包含内网IPv4
     * @param ip 合法的ip地址
     * @return boolean
     */
    public static  boolean isContainInnerIPv4(String ip) {
        String reg = "(10|172|192)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(ip);
        return matcher.find();
    }

    /**
     * 获取 字符串是否空
     *
     * @param string
     *            字符串
     * @return 字符串是否空
     */
    public static boolean isEmpty(String string) {
        return string == null || string.equals("") || string.trim().equals("") || "null".equalsIgnoreCase(string);
    }

    /**
     * 获取 字符串是否空
     *
     * @param string
     *            字符串
     * @return 字符串是否空
     */
    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }
    /**
     * 字符串转16进制
     * @param str
     * @return
     */
    public static String stringToHex(String str){
        String value="";
        if(null == str || "".equals(str) || str.length() == 0){
            return value;
        }
        try{
            for(int i = 0 ;i<str.length();i++){
                if("".equals(value) ){
                    value =  String.format("%x", new BigInteger(1, String.valueOf(str.charAt(i)).getBytes("UTF-8")));;
                }else {
                    value +=  String.format("%x", new BigInteger(1, String.valueOf(str.charAt(i)).getBytes("UTF-8")));;
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return value;
    }

    /**
     * 通过公告标题获取公告号
     * @param title
     * @return
     */
    public static String getNoticeNumFromTitle(String title){
        try{
            if(isEmpty(title)){
                return null;
            }
            if(!title.contains("(") || !title.contains(")")){
                return title;
            }
            String noticeNum = "";
            if(title.contains("出让公告")){
                noticeNum = title.substring(title.indexOf("出让公告")+4);
                if(noticeNum.startsWith("(")){
                    noticeNum = noticeNum.substring(1);
                }
                if(noticeNum.endsWith(")")){
                    noticeNum = noticeNum.substring(0,noticeNum.length()-1);
                }
                if(StringUtils.isNotEmpty(noticeNum)){
                    return noticeNum;
                }
            }
            noticeNum = title.substring(title.indexOf("(")+1 ,title.indexOf(")")- 1);
            if(StringUtils.isEmpty(noticeNum)){
                return title;
            }else{
                return noticeNum;
            }
        }catch (Exception e){
            log.error("通过公告标题获取公告号,发生异常:" +e.getMessage(),e);
        }
        return title;

    }

    /**
     * unicode的字符串转中文
     * @param unicode
     * @return
     */
    private static String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }

    /**
     * 中文字符串转成unicode的字符串
     * @param cn
     * @return
     */
    private static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
            returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }

    public static void main(String[] args) {
        String str ="1366,768";
        System.out.println(stringToHex(str));
        System.out.println("313336362c373638".equals(stringToHex(str)));


        System.out.println(getNoticeNumFromTitle("常熟市(320581)国有建设用地使用权挂牌出让公告"));
        System.out.println(getNoticeNumFromTitle("郁南县国土资源局国有土地使用权挂牌出让公告(郁土交易（公）告字[2018]14号)"));
        System.out.println(getNoticeNumFromTitle("台州市(本级)国土资源局国有建设用地使用权挂牌出让公告(台土告字[2018] 077号)"));


        String cn = "你";
        System.out.println(cnToUnicode(cn));
        // 字符串 : \u5f00\u59cb\u4efb\u52a1 ，由于 \ 在java里是转义字符，要写出下面这种形式
        String unicode = "\\u4f60";
        System.out.println(unicodeToCn(unicode));
    }
}
