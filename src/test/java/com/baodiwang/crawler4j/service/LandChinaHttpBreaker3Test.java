/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.service.LandChinaHttpBreaker3Test.java <2018年09月11日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.service;

import com.baodiwang.crawler4j.ApplicationTests;
import com.baodiwang.crawler4j.constants.Constant;
import com.baodiwang.crawler4j.utils.HttpUtils;
import com.baodiwang.crawler4j.utils.LandChinaHttpBreaker3;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月11日 17时38分
 */
public class LandChinaHttpBreaker3Test extends ApplicationTests {

    @Autowired
    LandChinaHttpBreaker3 landChinaHttpBreaker3;
    @Test
    public void breakBarrierTest(){
        String listPageUrl = "http://www.landchina.com/default.aspx?tabid=261";//出让公告（2011年后）

        //发布时间是今年的条件分页
        Map<String,String> headMap = new HashMap<>();
        headMap.put("Cookie", "yunsuo_session_verify=a07658c84a6e83c4e917857b8aeaad57; Hm_lvt_83853859c7247c5b03b527894622d3fa=1535094220,1536311959,1536645940; ASP.NET_SessionId=xdhtof12qyziwfde4zmzidcu; Hm_lpvt_83853859c7247c5b03b527894622d3fa=1536646717");
        headMap.put("Referer","http://www.landchina.com/default.aspx?tabid=261&ComName=default");
        headMap.put("Origin", Constant.HTTP_HOST_LANDCHINA);
        headMap.put("Host", Constant.HOST_LANDCHINA);


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
        paramsMap.put("TAB_QuerySubmitPagerData", "1");
        String pageContent  = landChinaHttpBreaker3.breakBarrierPost(listPageUrl, headMap, paramsMap);
        System.out.println("pageContent.length=" +(StringUtils.isEmpty(pageContent) ? 0 : pageContent.length()) + "  ================================================================ ");
        System.out.println("pageContent=" +pageContent);
    }
}
