/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.schedule.RemiseNoticeSchedule.java <2018年08月23日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.schedule;

import com.baodiwang.crawler4j.constants.Constant;
import com.baodiwang.crawler4j.controller.listPage.RemiseNoticeParser;
import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.service.RemiseNoticeService;
import com.baodiwang.crawler4j.utils.HttpUtils;
import com.baodiwang.crawler4j.utils.IntUtils;
import com.baodiwang.crawler4j.utils.LandChinaHttpBreaker2;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抓取单个省份数据
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月23日 17时54分
 */
@Component
public class RemiseNoticeSchedule {

    private static final Logger log = LogManager.getLogger(RemiseNoticeDetailSchedule.class);

    @Autowired
    RemiseNoticeService remiseNoticeService;

    @Autowired
    RemiseNoticeParser remiseNoticeParser;

    /**
     * 定时抓取数据、并解析已抓取到的网页，并存到相关表中
     */
    @Scheduled(cron = "0 27 15 1/1 * ? ")//,每小时执行一次 从10点30分开始,
    public void scheduledSingleProvince(){
        parseSingleProvinceDataToRemiseNotice(13, "河北省", 48, 59);
    }

    /**
     * 解析单个省的列表页数据
     * @param provinceCode 省代码
     * @param provinceName  省名称
     * @param startPage    起始页
     * @param provinceAllPages  总页数
     */
    public void parseSingleProvinceDataToRemiseNotice(int provinceCode,String provinceName,int startPage,int provinceAllPages){
        log.info("【抓取" + provinceName + "出让公告列表页数据】...............................开始,从第"+startPage+"页开始，共"+provinceAllPages+"页");
        for(int i = startPage ;i<= provinceAllPages;i++){
            String listPageUrl = "http://www.landchina.com/default.aspx?tabid=261";//出让公告（2011年后）

            Map<String,String> headMap = new HashMap<>();
            headMap.put("Cookie", "yunsuo_session_verify=bf058ccefbbc286df1e6c8f6760de4fa; srcurl=687474703a2f2f7777772e6c616e646368696e612e636f6d2f64656661756c742e617370783f74616269643d32363126436f6d4e616d653d64656661756c74; security_session_mid_verify=4d81302ccccb587f75538b575f533210");
            headMap.put("Referer","http://www.landchina.com/default.aspx?tabid=261&ComName=default");
            headMap.put("Origin",Constant.HTTP_HOST);
            headMap.put("Host", Constant.HOST);

            Map<String,String> paramsMap = new HashMap<>();
            paramsMap.put("__VIEWSTATE","/wEPDwUJNjkzNzgyNTU4D2QWAmYPZBYIZg9kFgICAQ9kFgJmDxYCHgdWaXNpYmxlaGQCAQ9kFgICAQ8WAh4Fc3R5bGUFIEJBQ0tHUk9VTkQtQ09MT1I6I2YzZjVmNztDT0xPUjo7ZAICD2QWAgIBD2QWAmYPZBYCZg9kFgJmD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmDxYEHwEFIENPTE9SOiNEM0QzRDM7QkFDS0dST1VORC1DT0xPUjo7HwBoFgJmD2QWAgIBD2QWAmYPDxYCHgRUZXh0ZWRkAgEPZBYCZg9kFgJmD2QWAmYPZBYEZg9kFgJmDxYEHwEFhwFDT0xPUjojRDNEM0QzO0JBQ0tHUk9VTkQtQ09MT1I6O0JBQ0tHUk9VTkQtSU1BR0U6dXJsKGh0dHA6Ly93d3cubGFuZGNoaW5hLmNvbS9Vc2VyL2RlZmF1bHQvVXBsb2FkL3N5c0ZyYW1lSW1nL3hfdGRzY3dfc3lfamhnZ18wMDAuZ2lmKTseBmhlaWdodAUBMxYCZg9kFgICAQ9kFgJmDw8WAh8CZWRkAgIPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmD2QWAmYPZBYEZg9kFgJmDxYEHwEFIENPTE9SOiNEM0QzRDM7QkFDS0dST1VORC1DT0xPUjo7HwBoFgJmD2QWAgIBD2QWAmYPDxYCHwJlZGQCAg9kFgJmD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmD2QWAmYPFgQfAQUgQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjsfAGgWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAICD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCAgEPZBYCZg8WBB8BBYwBQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjtCQUNLR1JPVU5ELUlNQUdFOnVybChodHRwOi8vd3d3LmxhbmRjaGluYS5jb20vVXNlci9kZWZhdWx0L1VwbG9hZC9zeXNGcmFtZUltZy94X3Rkc2N3X3p5X2NyZ2cyMDExTkhfMDEuZ2lmKTsfAwUCNDYWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAIBD2QWAmYPZBYCZg9kFgJmD2QWAgIBD2QWAmYPFgQfAQUgQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjsfAGgWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAIDD2QWAgIDDxYEHglpbm5lcmh0bWwF+gY8cCBhbGlnbj0iY2VudGVyIj48c3BhbiBzdHlsZT0iZm9udC1zaXplOiB4LXNtYWxsIj4mbmJzcDs8YnIgLz4NCiZuYnNwOzxhIHRhcmdldD0iX3NlbGYiIGhyZWY9Imh0dHA6Ly93d3cubGFuZGNoaW5hLmNvbS8iPjxpbWcgYm9yZGVyPSIwIiBhbHQ9IiIgd2lkdGg9IjI2MCIgaGVpZ2h0PSI2MSIgc3JjPSIvVXNlci9kZWZhdWx0L1VwbG9hZC9mY2svaW1hZ2UvdGRzY3dfbG9nZS5wbmciIC8+PC9hPiZuYnNwOzxiciAvPg0KJm5ic3A7PHNwYW4gc3R5bGU9ImNvbG9yOiAjZmZmZmZmIj5Db3B5cmlnaHQgMjAwOC0yMDE4IERSQ25ldC4gQWxsIFJpZ2h0cyBSZXNlcnZlZCZuYnNwOyZuYnNwOyZuYnNwOyA8c2NyaXB0IHR5cGU9InRleHQvamF2YXNjcmlwdCI+DQp2YXIgX2JkaG1Qcm90b2NvbCA9ICgoImh0dHBzOiIgPT0gZG9jdW1lbnQubG9jYXRpb24ucHJvdG9jb2wpID8gIiBodHRwczovLyIgOiAiIGh0dHA6Ly8iKTsNCmRvY3VtZW50LndyaXRlKHVuZXNjYXBlKCIlM0NzY3JpcHQgc3JjPSciICsgX2JkaG1Qcm90b2NvbCArICJobS5iYWlkdS5jb20vaC5qcyUzRjgzODUzODU5YzcyNDdjNWIwM2I1Mjc4OTQ2MjJkM2ZhJyB0eXBlPSd0ZXh0L2phdmFzY3JpcHQnJTNFJTNDL3NjcmlwdCUzRSIpKTsNCjwvc2NyaXB0PiZuYnNwOzxiciAvPg0K54mI5p2D5omA5pyJJm5ic3A7IOS4reWbveWcn+WcsOW4guWcuue9kSZuYnNwOyZuYnNwO+aKgOacr+aUr+aMgTrmtZnmsZ/oh7vlloTnp5HmioDogqHku73mnInpmZDlhazlj7gmbmJzcDs8YnIgLz4NCuWkh+ahiOWPtzog5LqsSUNQ5aSHMDkwNzQ5OTLlj7cg5Lqs5YWs572R5a6J5aSHMTEwMTAyMDAwNjY2KDIpJm5ic3A7PGJyIC8+DQo8L3NwYW4+Jm5ic3A7Jm5ic3A7Jm5ic3A7PGJyIC8+DQombmJzcDs8L3NwYW4+PC9wPh8BBWRCQUNLR1JPVU5ELUlNQUdFOnVybChodHRwOi8vd3d3LmxhbmRjaGluYS5jb20vVXNlci9kZWZhdWx0L1VwbG9hZC9zeXNGcmFtZUltZy94X3Rkc2N3MjAxM195d18xLmpwZyk7ZGSjTvgFAY2PfftfDHZ1GLafuCLptE2U3EGg6NUts7CwJw==");
            paramsMap.put("__EVENTVALIDATION","/wEWAgLlz6rABQLN3cj/BNgc+onYRJ481S9ZLuF182uDIzBaLGu71si0haYR1c9s");

            paramsMap.put("TAB_QueryConditionItem","894e12d9-6b0f-46a2-b053-73c49d2f706d");
            paramsMap.put("TAB_QueryConditionItem","598bdde3-078b-4c9b-b460-2e0b2d944e86");
            paramsMap.put("TAB_QueryConditionItem","87f11024-55ab-4faf-a0af-46371e33ae66");
            paramsMap.put("TAB_QuerySortItemList","c04b6ee6-3975-43ab-a733-28dcc4707112:False");
            paramsMap.put("TAB_QuerySortItemList","c04b6ee6-3975-43ab-a733-28dcc4707112:False");
//            paramsMap.put("TAB_QuerySubmitConditionData","598bdde3-078b-4c9b-b460-2e0b2d944e86:2018-1-1~2018-12-31");//只根据发布日期来查
            paramsMap.put("TAB_QuerySubmitConditionData","894e12d9-6b0f-46a2-b053-73c49d2f706d:" + provinceCode + "▓~" + provinceName + "|598bdde3-078b-4c9b-b460-2e0b2d944e86:2018-1-1~2018-12-31");//根据发布日期、和省份来查
            paramsMap.put("TAB_QuerySubmitOrderData","c04b6ee6-3975-43ab-a733-28dcc4707112:False|c04b6ee6-3975-43ab-a733-28dcc4707112:False");
            paramsMap.put("TAB_RowButtonActionControl","");
            paramsMap.put("TAB_QuerySubmitSortData","");
            paramsMap.put("TAB_QuerySubmitPagerData",i+"");

            String pageContent = LandChinaHttpBreaker2.breakBarrier(listPageUrl, headMap, paramsMap);
            if(StringUtils.isEmpty( pageContent ) || pageContent.length()< 10000){
                log.info("【抓取" + provinceName +"出让公告列表页数据】...............................未能抓到合法的数据 ：pageContent.length()=" + (StringUtils.isEmpty(pageContent) ? 0 : pageContent.length()) );
                continue;
            }

            List<RemiseNotice> remiseNoticeList = null;
            try{
                remiseNoticeList = remiseNoticeParser.parseHtml(pageContent);
            }catch (Exception e){
                log.error("【抓取" + provinceName + "出让公告列表页数据】...............................解析html网页发生异常:" + e.getMessage(),e);
            }
            if(null == remiseNoticeList || remiseNoticeList.isEmpty()){
                log.info("【抓取"+provinceName+"出让公告列表页数据】...............................数据已抓取过,无需处理");
                continue;
            }
            int count = 0;

            try{
                count = remiseNoticeService.batchInsert(remiseNoticeList);//批量插入未保存过的数据
            }catch (Exception e){
                log.error("【抓取" + provinceName +"出让公告列表页数据】...............................批量插入数据发生异常："  + e.getMessage() + " \n remiseNoticeList = " + remiseNoticeList,e);
                break;
            }
            int sleepSeconds = IntUtils.getRandomInt(4,8);
            log.info("【抓取"+provinceName+"出让公告列表页数据】第" + i + "次抓取数据条数：" + remiseNoticeList.size() + ",处理条数：" + count +"，休眠" + sleepSeconds +"秒钟======================================");
            try {
                Thread.sleep(sleepSeconds  * 1000);
            } catch (InterruptedException e) {
                log.error("【抓取"+provinceName+"出让公告列表页数据】休眠" + sleepSeconds + "秒钟发生异常" + e.getMessage(),e);
            }
        }

        log.info("【抓取"+provinceName+"出让公告列表页数据】...............................结束");
//        int sleepHours = IntUtils.getRandomInt(20,24);
//        log.info("【抓取出让公告列表页数据】...............................完成，休眠" + sleepHours + "小时" );
//        try {
//            Thread.sleep(sleepHours * 3600 * 1000);
//        } catch (InterruptedException e) {
//            log.error("【抓取出让公告列表页数据】休眠" + sleepHours + "小时发生异常" + e.getMessage(),e);
//        }
    }


}
