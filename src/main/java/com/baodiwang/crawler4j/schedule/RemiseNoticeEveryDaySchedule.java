/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.schedule.RemiseNoticeSchedule2.java <2018年08月27日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.schedule;

import com.baodiwang.crawler4j.controller.listPage.RemiseNoticeParser;
import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.service.RemiseNoticeService;
import com.baodiwang.crawler4j.utils.IntUtils;
import com.baodiwang.crawler4j.utils.LandChinaHttpBreaker2;
import com.baodiwang.crawler4j.utils.RegexUtil;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 增量抓取出让公告列表页数据
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月27日 14时07分
 */
@Component
@EnableScheduling
@EnableAsync
public class RemiseNoticeEveryDaySchedule {

    private static final Logger log = LogManager.getLogger(RemiseNoticeEveryDaySchedule.class);

    /* 总页数 */
    private static final String All_PAGES = "allPages";

    /* 总记录数 */
    private static final String ALL_RECODES = "allRecords";


    @Autowired
    RemiseNoticeService remiseNoticeService;

    @Autowired
    RemiseNoticeParser remiseNoticeParser;

    @Autowired
    RemiseNoticeSchedule singeProvinceScheduler;


    /**
     * 定时抓取每天的数据
     */
    @Async
//    @Scheduled(cron = "0 0 0/2 * * ? ")//每隔两小时执行一次
//    @Scheduled(cron = "0/20 * * * * ? ")//每隔20秒执行一次

    @Scheduled(cron = "0 08 12 * * ?")//每天固定时间点执行
    public void parseTodayDataToRemiseNotice(){
        String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String yesterdayStr = new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDays(new Date(), -1));
        getRemiseNoticePageByDate(yesterdayStr,todayStr);

    }

    /**
     * 抓取指定时间段的数据
     */
//    @Async
//    @Scheduled(cron = "0 0 0/2 * * ? ")//每隔两小时执行一次
//    @Scheduled(cron = "0/20 * * * * ? ")//每隔20秒执行一次
    public void parseSpecifyDayDataToRemiseNotice(){
        getRemiseNoticePageByDate("2018-08-24", "2018-12-31");

    }

    /**
     * 抓取指定日期的列表页
     * @param startDate 开始日期
     * @param endDate  结束
     */
    public void getRemiseNoticePageByDate(String startDate,String endDate){
        if(StringUtils.isEmpty(startDate)){
            startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        if(StringUtils.isEmpty(endDate)){
            endDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }

        String logMessage =  "【增量抓取出让公告列表页数据】日期从"+startDate+ "到" + endDate + "...............................";
        log.info(logMessage  + "开始啦！！！！！！！！！");

        String listPageUrl = "http://www.landchina.com/default.aspx?tabid=261&ComName=default";//出让公告（2011年后）

        Map<String,String> headMap = new HashMap<>();
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("__VIEWSTATE","/wEPDwUJNjkzNzgyNTU4D2QWAmYPZBYIZg9kFgICAQ9kFgJmDxYCHgdWaXNpYmxlaGQCAQ9kFgICAQ8WAh4Fc3R5bGUFIEJBQ0tHUk9VTkQtQ09MT1I6I2YzZjVmNztDT0xPUjo7ZAICD2QWAgIBD2QWAmYPZBYCZg9kFgJmD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmDxYEHwEFIENPTE9SOiNEM0QzRDM7QkFDS0dST1VORC1DT0xPUjo7HwBoFgJmD2QWAgIBD2QWAmYPDxYCHgRUZXh0ZWRkAgEPZBYCZg9kFgJmD2QWAmYPZBYEZg9kFgJmDxYEHwEFhwFDT0xPUjojRDNEM0QzO0JBQ0tHUk9VTkQtQ09MT1I6O0JBQ0tHUk9VTkQtSU1BR0U6dXJsKGh0dHA6Ly93d3cubGFuZGNoaW5hLmNvbS9Vc2VyL2RlZmF1bHQvVXBsb2FkL3N5c0ZyYW1lSW1nL3hfdGRzY3dfc3lfamhnZ18wMDAuZ2lmKTseBmhlaWdodAUBMxYCZg9kFgICAQ9kFgJmDw8WAh8CZWRkAgIPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmD2QWAmYPZBYEZg9kFgJmDxYEHwEFIENPTE9SOiNEM0QzRDM7QkFDS0dST1VORC1DT0xPUjo7HwBoFgJmD2QWAgIBD2QWAmYPDxYCHwJlZGQCAg9kFgJmD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmD2QWAmYPFgQfAQUgQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjsfAGgWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAICD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCAgEPZBYCZg8WBB8BBYwBQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjtCQUNLR1JPVU5ELUlNQUdFOnVybChodHRwOi8vd3d3LmxhbmRjaGluYS5jb20vVXNlci9kZWZhdWx0L1VwbG9hZC9zeXNGcmFtZUltZy94X3Rkc2N3X3p5X2NyZ2cyMDExTkhfMDEuZ2lmKTsfAwUCNDYWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAIBD2QWAmYPZBYCZg9kFgJmD2QWAgIBD2QWAmYPFgQfAQUgQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjsfAGgWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAIDD2QWAgIDDxYEHglpbm5lcmh0bWwF+gY8cCBhbGlnbj0iY2VudGVyIj48c3BhbiBzdHlsZT0iZm9udC1zaXplOiB4LXNtYWxsIj4mbmJzcDs8YnIgLz4NCiZuYnNwOzxhIHRhcmdldD0iX3NlbGYiIGhyZWY9Imh0dHA6Ly93d3cubGFuZGNoaW5hLmNvbS8iPjxpbWcgYm9yZGVyPSIwIiBhbHQ9IiIgd2lkdGg9IjI2MCIgaGVpZ2h0PSI2MSIgc3JjPSIvVXNlci9kZWZhdWx0L1VwbG9hZC9mY2svaW1hZ2UvdGRzY3dfbG9nZS5wbmciIC8+PC9hPiZuYnNwOzxiciAvPg0KJm5ic3A7PHNwYW4gc3R5bGU9ImNvbG9yOiAjZmZmZmZmIj5Db3B5cmlnaHQgMjAwOC0yMDE4IERSQ25ldC4gQWxsIFJpZ2h0cyBSZXNlcnZlZCZuYnNwOyZuYnNwOyZuYnNwOyA8c2NyaXB0IHR5cGU9InRleHQvamF2YXNjcmlwdCI+DQp2YXIgX2JkaG1Qcm90b2NvbCA9ICgoImh0dHBzOiIgPT0gZG9jdW1lbnQubG9jYXRpb24ucHJvdG9jb2wpID8gIiBodHRwczovLyIgOiAiIGh0dHA6Ly8iKTsNCmRvY3VtZW50LndyaXRlKHVuZXNjYXBlKCIlM0NzY3JpcHQgc3JjPSciICsgX2JkaG1Qcm90b2NvbCArICJobS5iYWlkdS5jb20vaC5qcyUzRjgzODUzODU5YzcyNDdjNWIwM2I1Mjc4OTQ2MjJkM2ZhJyB0eXBlPSd0ZXh0L2phdmFzY3JpcHQnJTNFJTNDL3NjcmlwdCUzRSIpKTsNCjwvc2NyaXB0PiZuYnNwOzxiciAvPg0K54mI5p2D5omA5pyJJm5ic3A7IOS4reWbveWcn+WcsOW4guWcuue9kSZuYnNwOyZuYnNwO+aKgOacr+aUr+aMgTrmtZnmsZ/oh7vlloTnp5HmioDogqHku73mnInpmZDlhazlj7gmbmJzcDs8YnIgLz4NCuWkh+ahiOWPtzog5LqsSUNQ5aSHMDkwNzQ5OTLlj7cg5Lqs5YWs572R5a6J5aSHMTEwMTAyMDAwNjY2KDIpJm5ic3A7PGJyIC8+DQo8L3NwYW4+Jm5ic3A7Jm5ic3A7Jm5ic3A7PGJyIC8+DQombmJzcDs8L3NwYW4+PC9wPh8BBWRCQUNLR1JPVU5ELUlNQUdFOnVybChodHRwOi8vd3d3LmxhbmRjaGluYS5jb20vVXNlci9kZWZhdWx0L1VwbG9hZC9zeXNGcmFtZUltZy94X3Rkc2N3MjAxM195d18xLmpwZyk7ZGSjTvgFAY2PfftfDHZ1GLafuCLptE2U3EGg6NUts7CwJw==");
        paramsMap.put("__EVENTVALIDATION","/wEWAgLlz6rABQLN3cj/BNgc+onYRJ481S9ZLuF182uDIzBaLGu71si0haYR1c9s");

        paramsMap.put("TAB_QueryConditionItem","598bdde3-078b-4c9b-b460-2e0b2d944e86");
        paramsMap.put("TAB_QueryConditionItem","87f11024-55ab-4faf-a0af-46371e33ae66");
        paramsMap.put("TAB_QuerySortItemList","c04b6ee6-3975-43ab-a733-28dcc4707112:False");
        paramsMap.put("TAB_QuerySortItemList","c04b6ee6-3975-43ab-a733-28dcc4707112:False");
        paramsMap.put("TAB_QuerySubmitConditionData","598bdde3-078b-4c9b-b460-2e0b2d944e86:" + startDate + "~" + endDate);
        paramsMap.put("TAB_QuerySubmitOrderData","c04b6ee6-3975-43ab-a733-28dcc4707112:False|c04b6ee6-3975-43ab-a733-28dcc4707112:False");
        paramsMap.put("TAB_RowButtonActionControl","");
        paramsMap.put("TAB_QuerySubmitSortData","");
        paramsMap.put("TAB_QuerySubmitPagerData","1"); //默认先查第一页


        //        doc.select(".pager").select("td").get(0).text()     共11681页 当前只显示200页 共350406条记录

        String pageContent = LandChinaHttpBreaker2.breakBarrier0903(listPageUrl, headMap, paramsMap);
        if(StringUtils.isEmpty(pageContent) || pageContent.length() < 10000){
            log.warn(logMessage + ",列表页中这个时间段内没有合法的数据===================================pageContent=" + pageContent);
            return ;
        }
        Document doc = Jsoup.parse(pageContent);
        if(doc.getElementById("TAB_contentTable").text().contains("没有检索到相关数据!")){
            log.error(logMessage + "没有检索到相关数据!");
            return;
        }
        String pageInfoStr = doc.select(".pager").select("td").get(0).text();//     共11681页 当前只显示200页 共350406条记录
        Map<String ,Integer> pageMap = parsePageInfo(pageInfoStr);
        log.info(logMessage +",解析出来的页数信息：" + pageMap);
        int allPages = pageMap.get(All_PAGES);

        handleRemiseNoticeList(pageContent, logMessage);

        for(int i = 2 ;i<= allPages;i++){
            pageContent = null;
            paramsMap.put("TAB_QuerySubmitPagerData", i + "");
            try{
                pageContent = LandChinaHttpBreaker2.breakBarrier0903(listPageUrl, headMap, paramsMap);
            }catch (Exception e){
                log.error(logMessage +"抓取网页发生异常：" + e.getMessage(),e);
                continue;
            }
            boolean result = handleRemiseNoticeList(pageContent,logMessage);
            if(!result){ //处理失败的则跳过
                continue;
            }
        }
        log.info(logMessage +"结束");
    }


    /**
     * 从列表页中 解析出 RemiseNotice模型数据的集合，并保存到数据库
     * @param pageContent 网页内容
     * @param logMessage  log信息
     * @return
     */
    private boolean handleRemiseNoticeList( String pageContent,String logMessage){
        List<RemiseNotice> remiseNoticeList = null;
        int count = 0;
        int sleepSeconds = 0;
        if(StringUtils.isEmpty(pageContent) || pageContent.length()< 10000){
            log.error(logMessage + "未能抓到合法的数据 ：pageContent.length()=" + (StringUtils.isEmpty(pageContent) ? 0 : pageContent.length()) +",pageContent="+ pageContent);
            return false;
        }
        try{
            remiseNoticeList = remiseNoticeParser.parseHtml(pageContent);
        }catch (Exception e){
            log.error(logMessage+"解析html网页发生异常:" + e.getMessage(),e);
        }
        if(null == remiseNoticeList || remiseNoticeList.isEmpty()){
            log.info(logMessage + "数据已抓取过,无需处理");
            return false;
        }

        try{
            count = remiseNoticeService.batchInsert(remiseNoticeList);//批量插入未保存过的数据
        }catch (Exception e){
            log.error(logMessage + "批量插入数据发生异常："  + e.getMessage() + " \n remiseNoticeList = " + remiseNoticeList,e);
        }

        sleepSeconds = IntUtils.getRandomInt(5, 8);
        log.info(logMessage +"本次抓取数据，抓取数据条数，" + remiseNoticeList.size() + ",处理条数：" + count + ",休眠" + sleepSeconds +"秒钟======================================");
        try {
            Thread.sleep(sleepSeconds  * 1000);
        } catch (InterruptedException e) {
            log.error(logMessage +"休眠" + sleepSeconds + "秒钟发生异常" + e.getMessage(),e);
        }
        return true;
    }

    /**
     * 解析总页数、总记录数的信息
     * @param pageInfoStr
     * @return
     */
    private Map<String, Integer> parsePageInfo(String pageInfoStr) {
//        pageInfoStr = "共11681页 当前只显示200页 共350406条记录";
        Map<String,Integer> map = new HashMap<>();

        String allPages = RegexUtil.findMatchContent("共\\d+页", pageInfoStr);
        allPages = RegexUtil.findMatchContent("\\d+",allPages);
        map.put(All_PAGES,StringUtils.isNotEmpty(allPages) ? Integer.valueOf(allPages) : 0);

        String allRecodes = RegexUtil.findMatchContent("共\\d+条记录", pageInfoStr);
        allRecodes = RegexUtil.findMatchContent("\\d+",allRecodes);
        map.put(ALL_RECODES,StringUtils.isNotEmpty(allRecodes) ? Integer.valueOf(allRecodes) : 0);
        return map;
    }


}


