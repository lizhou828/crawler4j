/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.controller.HttpClientController.java <2018年08月22日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.controller.listPage;

import com.baodiwang.crawler4j.constants.Constant;
import com.baodiwang.crawler4j.enums.RemiseNoticeTypeEnum;
import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.service.RemiseNoticeService;
import com.baodiwang.crawler4j.utils.*;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 出让公告列表页面
 * //出让公告（2011年后）
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月22日 11时03分
 */
@RestController
public class RemiseNoticeController {

    private static final Logger log = LogManager.getLogger(RemiseNoticeController.class);

    @Autowired
    private RemiseNoticeService remiseNoticeService;

    @Autowired
    RemiseNoticeParser remiseNoticeParser;

    @Autowired
    LandChinaHttpBreaker3 landChinaHttpBreaker3;

    /**
     * POST方式获取（可以条件查询）
     * @param page
     * @return
     */
    @RequestMapping("/261postHeBei")
    public String listPage261PostHeBei(Integer page){
        String listPageUrl = "http://www.landchina.com/default.aspx?tabid=261";//出让公告（2011年后）

        //发布时间是今年的条件分页
        Map<String,String> headMap = new HashMap<>();
        headMap.put("Cookie", "yunsuo_session_verify=bf058ccefbbc286df1e6c8f6760de4fa; srcurl=687474703a2f2f7777772e6c616e646368696e612e636f6d2f64656661756c742e617370783f74616269643d32363126436f6d4e616d653d64656661756c74; security_session_mid_verify=4d81302ccccb587f75538b575f533210; Hm_lpvt_83853859c7247c5b03b527894622d3fa=1535096355");
        headMap.put("Referer","http://www.landchina.com/default.aspx?tabid=261&ComName=default");
        headMap.put("Origin",Constant.HTTP_HOST);
        headMap.put("Host", Constant.HOST);

        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("__VIEWSTATE","/wEPDwUJNjkzNzgyNTU4D2QWAmYPZBYIZg9kFgICAQ9kFgJmDxYCHgdWaXNpYmxlaGQCAQ9kFgICAQ8WAh4Fc3R5bGUFIEJBQ0tHUk9VTkQtQ09MT1I6I2YzZjVmNztDT0xPUjo7ZAICD2QWAgIBD2QWAmYPZBYCZg9kFgJmD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmDxYEHwEFIENPTE9SOiNEM0QzRDM7QkFDS0dST1VORC1DT0xPUjo7HwBoFgJmD2QWAgIBD2QWAmYPDxYCHgRUZXh0ZWRkAgEPZBYCZg9kFgJmD2QWAmYPZBYEZg9kFgJmDxYEHwEFhwFDT0xPUjojRDNEM0QzO0JBQ0tHUk9VTkQtQ09MT1I6O0JBQ0tHUk9VTkQtSU1BR0U6dXJsKGh0dHA6Ly93d3cubGFuZGNoaW5hLmNvbS9Vc2VyL2RlZmF1bHQvVXBsb2FkL3N5c0ZyYW1lSW1nL3hfdGRzY3dfc3lfamhnZ18wMDAuZ2lmKTseBmhlaWdodAUBMxYCZg9kFgICAQ9kFgJmDw8WAh8CZWRkAgIPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmD2QWAmYPZBYEZg9kFgJmDxYEHwEFIENPTE9SOiNEM0QzRDM7QkFDS0dST1VORC1DT0xPUjo7HwBoFgJmD2QWAgIBD2QWAmYPDxYCHwJlZGQCAg9kFgJmD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmD2QWAmYPFgQfAQUgQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjsfAGgWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAICD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCAgEPZBYCZg8WBB8BBYwBQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjtCQUNLR1JPVU5ELUlNQUdFOnVybChodHRwOi8vd3d3LmxhbmRjaGluYS5jb20vVXNlci9kZWZhdWx0L1VwbG9hZC9zeXNGcmFtZUltZy94X3Rkc2N3X3p5X2NyZ2cyMDExTkhfMDEuZ2lmKTsfAwUCNDYWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAIBD2QWAmYPZBYCZg9kFgJmD2QWAgIBD2QWAmYPFgQfAQUgQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjsfAGgWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAIDD2QWAgIDDxYEHglpbm5lcmh0bWwF+gY8cCBhbGlnbj0iY2VudGVyIj48c3BhbiBzdHlsZT0iZm9udC1zaXplOiB4LXNtYWxsIj4mbmJzcDs8YnIgLz4NCiZuYnNwOzxhIHRhcmdldD0iX3NlbGYiIGhyZWY9Imh0dHA6Ly93d3cubGFuZGNoaW5hLmNvbS8iPjxpbWcgYm9yZGVyPSIwIiBhbHQ9IiIgd2lkdGg9IjI2MCIgaGVpZ2h0PSI2MSIgc3JjPSIvVXNlci9kZWZhdWx0L1VwbG9hZC9mY2svaW1hZ2UvdGRzY3dfbG9nZS5wbmciIC8+PC9hPiZuYnNwOzxiciAvPg0KJm5ic3A7PHNwYW4gc3R5bGU9ImNvbG9yOiAjZmZmZmZmIj5Db3B5cmlnaHQgMjAwOC0yMDE4IERSQ25ldC4gQWxsIFJpZ2h0cyBSZXNlcnZlZCZuYnNwOyZuYnNwOyZuYnNwOyA8c2NyaXB0IHR5cGU9InRleHQvamF2YXNjcmlwdCI+DQp2YXIgX2JkaG1Qcm90b2NvbCA9ICgoImh0dHBzOiIgPT0gZG9jdW1lbnQubG9jYXRpb24ucHJvdG9jb2wpID8gIiBodHRwczovLyIgOiAiIGh0dHA6Ly8iKTsNCmRvY3VtZW50LndyaXRlKHVuZXNjYXBlKCIlM0NzY3JpcHQgc3JjPSciICsgX2JkaG1Qcm90b2NvbCArICJobS5iYWlkdS5jb20vaC5qcyUzRjgzODUzODU5YzcyNDdjNWIwM2I1Mjc4OTQ2MjJkM2ZhJyB0eXBlPSd0ZXh0L2phdmFzY3JpcHQnJTNFJTNDL3NjcmlwdCUzRSIpKTsNCjwvc2NyaXB0PiZuYnNwOzxiciAvPg0K54mI5p2D5omA5pyJJm5ic3A7IOS4reWbveWcn+WcsOW4guWcuue9kSZuYnNwOyZuYnNwO+aKgOacr+aUr+aMgTrmtZnmsZ/oh7vlloTnp5HmioDogqHku73mnInpmZDlhazlj7gmbmJzcDs8YnIgLz4NCuWkh+ahiOWPtzog5LqsSUNQ5aSHMDkwNzQ5OTLlj7cg5Lqs5YWs572R5a6J5aSHMTEwMTAyMDAwNjY2KDIpJm5ic3A7PGJyIC8+DQo8L3NwYW4+Jm5ic3A7Jm5ic3A7Jm5ic3A7PGJyIC8+DQombmJzcDs8L3NwYW4+PC9wPh8BBWRCQUNLR1JPVU5ELUlNQUdFOnVybChodHRwOi8vd3d3LmxhbmRjaGluYS5jb20vVXNlci9kZWZhdWx0L1VwbG9hZC9zeXNGcmFtZUltZy94X3Rkc2N3MjAxM195d18xLmpwZyk7ZGSjTvgFAY2PfftfDHZ1GLafuCLptE2U3EGg6NUts7CwJw==");
        paramsMap.put("__EVENTVALIDATION","/wEWAgLlz6rABQLN3cj/BNgc+onYRJ481S9ZLuF182uDIzBaLGu71si0haYR1c9s");

        paramsMap.put("TAB_QueryConditionItem", "598bdde3-078b-4c9b-b460-2e0b2d944e86");
        paramsMap.put("TAB_QueryConditionItem","87f11024-55ab-4faf-a0af-46371e33ae66");
        paramsMap.put("TAB_QuerySortItemList","c04b6ee6-3975-43ab-a733-28dcc4707112:False");
        paramsMap.put("TAB_QuerySortItemList","c04b6ee6-3975-43ab-a733-28dcc4707112:False");
        //            paramsMap.put("TAB_QuerySubmitConditionData","598bdde3-078b-4c9b-b460-2e0b2d944e86:2018-1-1~2018-12-31");//只根据发布日期来查
        paramsMap.put("TAB_QuerySubmitConditionData","894e12d9-6b0f-46a2-b053-73c49d2f706d:13▓~河北省|598bdde3-078b-4c9b-b460-2e0b2d944e86:2018-1-1~2018-12-31");//根据发布日期、和省份来查
        paramsMap.put("TAB_QuerySubmitOrderData","c04b6ee6-3975-43ab-a733-28dcc4707112:False|c04b6ee6-3975-43ab-a733-28dcc4707112:False");
        paramsMap.put("TAB_RowButtonActionControl","");
        paramsMap.put("TAB_QuerySubmitSortData","");
        if( null == page || page <= 0 || page >= 893){
            page = 1;
        }
        paramsMap.put("TAB_QuerySubmitPagerData",page+"");


        String pageContent = LandChinaHttpBreaker2.breakBarrier0903(listPageUrl, headMap, paramsMap);
        return pageContent;
    }

    /**
     * POST方式获取（可以条件查询）
     * @param page
     * @return
     */
    @RequestMapping("/261post")
    public String listPage261Post(Integer page){
        String listPageUrl = "http://www.landchina.com/default.aspx?tabid=261";//出让公告（2011年后）

        //发布时间是今年的条件分页
        Map<String,String> headMap = new HashMap<>();
        headMap.put("Cookie", "yunsuo_session_verify=a07658c84a6e83c4e917857b8aeaad57; Hm_lvt_83853859c7247c5b03b527894622d3fa=1535094220,1536311959,1536645940; ASP.NET_SessionId=xdhtof12qyziwfde4zmzidcu; Hm_lpvt_83853859c7247c5b03b527894622d3fa=1536646717");
        headMap.put("Referer","http://www.landchina.com/default.aspx?tabid=261&ComName=default");
        headMap.put("Origin",Constant.HTTP_HOST);
        headMap.put("Host", Constant.HOST);


        String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String yesterdayStr = new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDays(new Date(), -1));

        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("__VIEWSTATE","/wEPDwUJNjkzNzgyNTU4D2QWAmYPZBYIZg9kFgICAQ9kFgJmDxYCHgdWaXNpYmxlaGQCAQ9kFgICAQ8WAh4Fc3R5bGUFIEJBQ0tHUk9VTkQtQ09MT1I6I2YzZjVmNztDT0xPUjo7ZAICD2QWAgIBD2QWAmYPZBYCZg9kFgJmD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmDxYEHwEFIENPTE9SOiNEM0QzRDM7QkFDS0dST1VORC1DT0xPUjo7HwBoFgJmD2QWAgIBD2QWAmYPDxYCHgRUZXh0ZWRkAgEPZBYCZg9kFgJmD2QWAmYPZBYEZg9kFgJmDxYEHwEFhwFDT0xPUjojRDNEM0QzO0JBQ0tHUk9VTkQtQ09MT1I6O0JBQ0tHUk9VTkQtSU1BR0U6dXJsKGh0dHA6Ly93d3cubGFuZGNoaW5hLmNvbS9Vc2VyL2RlZmF1bHQvVXBsb2FkL3N5c0ZyYW1lSW1nL3hfdGRzY3dfc3lfamhnZ18wMDAuZ2lmKTseBmhlaWdodAUBMxYCZg9kFgICAQ9kFgJmDw8WAh8CZWRkAgIPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmD2QWAmYPZBYEZg9kFgJmDxYEHwEFIENPTE9SOiNEM0QzRDM7QkFDS0dST1VORC1DT0xPUjo7HwBoFgJmD2QWAgIBD2QWAmYPDxYCHwJlZGQCAg9kFgJmD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCZg9kFgJmD2QWAmYPFgQfAQUgQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjsfAGgWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAICD2QWBGYPZBYCZg9kFgJmD2QWAmYPZBYCAgEPZBYCZg8WBB8BBYwBQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjtCQUNLR1JPVU5ELUlNQUdFOnVybChodHRwOi8vd3d3LmxhbmRjaGluYS5jb20vVXNlci9kZWZhdWx0L1VwbG9hZC9zeXNGcmFtZUltZy94X3Rkc2N3X3p5X2NyZ2cyMDExTkhfMDEuZ2lmKTsfAwUCNDYWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAIBD2QWAmYPZBYCZg9kFgJmD2QWAgIBD2QWAmYPFgQfAQUgQ09MT1I6I0QzRDNEMztCQUNLR1JPVU5ELUNPTE9SOjsfAGgWAmYPZBYCAgEPZBYCZg8PFgIfAmVkZAIDD2QWAgIDDxYEHglpbm5lcmh0bWwF+gY8cCBhbGlnbj0iY2VudGVyIj48c3BhbiBzdHlsZT0iZm9udC1zaXplOiB4LXNtYWxsIj4mbmJzcDs8YnIgLz4NCiZuYnNwOzxhIHRhcmdldD0iX3NlbGYiIGhyZWY9Imh0dHA6Ly93d3cubGFuZGNoaW5hLmNvbS8iPjxpbWcgYm9yZGVyPSIwIiBhbHQ9IiIgd2lkdGg9IjI2MCIgaGVpZ2h0PSI2MSIgc3JjPSIvVXNlci9kZWZhdWx0L1VwbG9hZC9mY2svaW1hZ2UvdGRzY3dfbG9nZS5wbmciIC8+PC9hPiZuYnNwOzxiciAvPg0KJm5ic3A7PHNwYW4gc3R5bGU9ImNvbG9yOiAjZmZmZmZmIj5Db3B5cmlnaHQgMjAwOC0yMDE4IERSQ25ldC4gQWxsIFJpZ2h0cyBSZXNlcnZlZCZuYnNwOyZuYnNwOyZuYnNwOyA8c2NyaXB0IHR5cGU9InRleHQvamF2YXNjcmlwdCI+DQp2YXIgX2JkaG1Qcm90b2NvbCA9ICgoImh0dHBzOiIgPT0gZG9jdW1lbnQubG9jYXRpb24ucHJvdG9jb2wpID8gIiBodHRwczovLyIgOiAiIGh0dHA6Ly8iKTsNCmRvY3VtZW50LndyaXRlKHVuZXNjYXBlKCIlM0NzY3JpcHQgc3JjPSciICsgX2JkaG1Qcm90b2NvbCArICJobS5iYWlkdS5jb20vaC5qcyUzRjgzODUzODU5YzcyNDdjNWIwM2I1Mjc4OTQ2MjJkM2ZhJyB0eXBlPSd0ZXh0L2phdmFzY3JpcHQnJTNFJTNDL3NjcmlwdCUzRSIpKTsNCjwvc2NyaXB0PiZuYnNwOzxiciAvPg0K54mI5p2D5omA5pyJJm5ic3A7IOS4reWbveWcn+WcsOW4guWcuue9kSZuYnNwOyZuYnNwO+aKgOacr+aUr+aMgTrmtZnmsZ/oh7vlloTnp5HmioDogqHku73mnInpmZDlhazlj7gmbmJzcDs8YnIgLz4NCuWkh+ahiOWPtzog5LqsSUNQ5aSHMDkwNzQ5OTLlj7cg5Lqs5YWs572R5a6J5aSHMTEwMTAyMDAwNjY2KDIpJm5ic3A7PGJyIC8+DQo8L3NwYW4+Jm5ic3A7Jm5ic3A7Jm5ic3A7PGJyIC8+DQombmJzcDs8L3NwYW4+PC9wPh8BBWRCQUNLR1JPVU5ELUlNQUdFOnVybChodHRwOi8vd3d3LmxhbmRjaGluYS5jb20vVXNlci9kZWZhdWx0L1VwbG9hZC9zeXNGcmFtZUltZy94X3Rkc2N3MjAxM195d18xLmpwZyk7ZGTTe6VaGlUELBNpAsm+UHur1XMcpdh/0SWc2qQDNsyiRQ==");
        paramsMap.put("__EVENTVALIDATION","/wEWAgLyteT/BwLN3cj/BCgQQ6N/jZP7FNls+m6ym4uOpJEMYQxYJSjWpGAWvrd0");

        paramsMap.put("TAB_QueryConditionItem","894e12d9-6b0f-46a2-b053-73c49d2f706d");
        paramsMap.put("TAB_QueryConditionItem","598bdde3-078b-4c9b-b460-2e0b2d944e86");
        paramsMap.put("TAB_QueryConditionItem","87f11024-55ab-4faf-a0af-46371e33ae66");
        paramsMap.put("TAB_QuerySortItemList","c04b6ee6-3975-43ab-a733-28dcc4707112:False");
        paramsMap.put("TAB_QuerySortItemList","c04b6ee6-3975-43ab-a733-28dcc4707112:False");
        paramsMap.put("TAB_QuerySubmitConditionData","598bdde3-078b-4c9b-b460-2e0b2d944e86:" + yesterdayStr + "~"+todayStr);//只根据发布日期来查
//        paramsMap.put("TAB_QuerySubmitConditionData","894e12d9-6b0f-46a2-b053-73c49d2f706d:13▓~河北省|598bdde3-078b-4c9b-b460-2e0b2d944e86:2018-1-1~2018-12-31");//根据发布日期、和省份来查
        paramsMap.put("TAB_QuerySubmitOrderData","c04b6ee6-3975-43ab-a733-28dcc4707112:False|c04b6ee6-3975-43ab-a733-28dcc4707112:False");
        paramsMap.put("TAB_RowButtonActionControl","");
        paramsMap.put("TAB_QuerySubmitSortData","");
        if( null == page || page <= 0 || page >= 893){
            page = 1;
        }
        paramsMap.put("TAB_QuerySubmitPagerData",page+"");
        String pageContent  = landChinaHttpBreaker3.breakBarrierPost(listPageUrl,headMap,paramsMap);

        if(StringUtils.isEmpty( pageContent ) || pageContent.length()< 10000){
            log.info("【抓取出让公告列表页数据】...............................未能抓到合法的数据 ：pageContent.length()=" + (StringUtils.isEmpty(pageContent) ? 0 : pageContent.length()) );
            return "没抓到数据";
        }

        List<RemiseNotice> remiseNoticeList = null;
        try{
            remiseNoticeList = remiseNoticeParser.parseHtml(pageContent);
        }catch (Exception e){
            log.error("【抓取出让公告列表页数据】...............................解析html网页发生异常:" + e.getMessage(),e);
        }
        if(null == remiseNoticeList || remiseNoticeList.isEmpty()){
            log.info("【抓取出让公告列表页数据】...............................数据已抓取过,无需处理");
            return "没解析到数据";
        }
        int count = 0;

        try{
            count = remiseNoticeService.batchInsert(remiseNoticeList);//批量插入未保存过的数据
        }catch (Exception e){
            log.error("【抓取出让公告列表页数据】...............................批量插入数据发生异常："  + e.getMessage() + " \n remiseNoticeList = " + remiseNoticeList,e);
        }
        int sleepSeconds = IntUtils.getRandomInt(4, 8);
        log.info("【抓取出让公告列表页数据】抓取数据条数：" + remiseNoticeList.size() + ",处理条数：" + count +"，休眠" + sleepSeconds +"秒钟======================================");
        return "抓取到数据后，成功解析并保存";
    }



    @RequestMapping("/261get")
    public String listPage261Get(Integer page) {
        String listPageUrl = "http://www.landchina.com/default.aspx?tabid=261";//出让公告（2011年后）
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Cookie", "yunsuo_session_verify=a07658c84a6e83c4e917857b8aeaad57");
        headMap.put("Referer", "http://www.landchina.com/default.aspx?tabid=261&ComName=default");
        headMap.put("Origin", Constant.HTTP_HOST);
        headMap.put("Host", Constant.HOST);
        headMap.put("Accept-Encoding", "gzip, deflate");
        headMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        String webContent = HttpUtils.get(listPageUrl, headMap);
        return webContent;
    }

    /**
     * 抓取列表的第一页(get方式)
     * @param page
     * @return
     */
    @RequestMapping("/261")
    public String listPage261FirstPage(Integer page) {
        String listPageUrl = "http://www.landchina.com/default.aspx?tabid=261";//出让公告（2011年后）
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Cookie", "yunsuo_session_verify=a07658c84a6e83c4e917857b8aeaad57");
        headMap.put("Referer", "http://www.landchina.com/default.aspx?tabid=261&ComName=default");
        headMap.put("Origin", Constant.HTTP_HOST);
        headMap.put("Host", Constant.HOST);
        headMap.put("Accept-Encoding", "gzip, deflate");
        headMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        String pageContent = HttpUtils.get(listPageUrl, headMap);

        if(StringUtils.isEmpty( pageContent ) || pageContent.length()< 10000){
            log.info("【抓取出让公告列表页数据】...............................未能抓到合法的数据 ：pageContent.length()=" + (StringUtils.isEmpty(pageContent) ? 0 : pageContent.length()) );
            return "没抓到数据";
        }

        List<RemiseNotice> remiseNoticeList = null;
        try{
            remiseNoticeList = remiseNoticeParser.parseHtml(pageContent);
        }catch (Exception e){
            log.error("【抓取出让公告列表页数据】...............................解析html网页发生异常:" + e.getMessage(),e);
        }
        if(null == remiseNoticeList || remiseNoticeList.isEmpty()){
            log.info("【抓取出让公告列表页数据】...............................数据已抓取过,无需处理");
            return "没解析到数据";
        }
        int count = 0;

        try{
            count = remiseNoticeService.batchInsert(remiseNoticeList);//批量插入未保存过的数据
        }catch (Exception e){
            log.error("【抓取出让公告列表页数据】...............................批量插入数据发生异常："  + e.getMessage() + " \n remiseNoticeList = " + remiseNoticeList,e);
        }
        int sleepSeconds = IntUtils.getRandomInt(4, 8);
        log.info("【抓取出让公告列表页数据】抓取数据条数：" + remiseNoticeList.size() + ",处理条数：" + count +"，休眠" + sleepSeconds +"秒钟======================================");
        return "抓取到数据后，成功解析并保存";
    }
}
