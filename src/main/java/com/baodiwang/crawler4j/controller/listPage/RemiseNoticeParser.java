/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.controller.listPage.RemiseNoticeParser.java <2018年08月23日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.controller.listPage;

import com.baodiwang.crawler4j.constants.Constant;
import com.baodiwang.crawler4j.enums.RemiseNoticeTypeEnum;
import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.service.RemiseNoticeService;
import com.baodiwang.crawler4j.utils.HttpUtils;
import com.baodiwang.crawler4j.utils.IntUtils;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月23日 17时07分
 */
@Component
public class RemiseNoticeParser {

    private static final Logger log = LogManager.getLogger(RemiseNoticeParser.class);

    @Autowired
    private RemiseNoticeService remiseNoticeService;

    public  List<RemiseNotice> parseHtml(String pageContent){
        if(StringUtils.isEmpty(pageContent) || pageContent.length() < 10000){
            log.warn("获取网页的数据异常:pageContent=" + pageContent );
            return null;
        }

        Document doc = Jsoup.parse(pageContent);
        Element tableEle = doc.getElementById("TAB_contentTable");
        if(null == tableEle){
            log.error("表格数据异常!");
            return null;
        }

        //根据表格查找表头：
        Elements trsEle = tableEle.select(".gridHeader");
        if(null == trsEle || trsEle.size() != 1){
            log.error("表头数据异常!");
            return null;
        }
        Element headerEle = trsEle.get(0);
        if(null == headerEle || null == headerEle.children() ||  headerEle.children().size() == 0){
            log.error("表头数据异常!");
            return null;
        }


        //根据表格查找表中数据：
        Elements trsItemList = tableEle.select(".gridItem");//奇数行
        List<RemiseNotice> singleList = parseTrData(trsItemList);

        //根据表格查找表中数据：
        Elements trsAlternatingItemList = tableEle.select(".gridAlternatingItem");//偶数行
        List<RemiseNotice> doubleList = parseTrData(trsAlternatingItemList);
        singleList.addAll(doubleList);
        return singleList;

    }

    private  List<RemiseNotice> parseTrData(Elements trsItemList) {
        List<RemiseNotice> remiseNoticeList = new ArrayList<>();
        if(null == trsItemList || trsItemList.isEmpty()){
            log.error("该行数据异常!");
            return remiseNoticeList;
        }
        RemiseNotice remiseNotice = null;
        for(Element trEle  : trsItemList){
            if(null == trEle || null == trEle.children()  || trEle.children().isEmpty()){
                log.error("该行无数据!");
                continue;
            }
            Elements tdEleList = trEle.children();
            if(null == tdEleList || tdEleList.size() == 0){
                log.error("该行无数据!");
                continue;
            }
            remiseNotice = new RemiseNotice();
            for(int i = 0;i<tdEleList.size();i++){
                if(null == tdEleList.get(i)){
                    log.error("该数据不完整!");
                    continue;
                }
                if(i == 1) {//标题
                    remiseNotice.setAreaName(tdEleList.get(i).text());
                }else if(i == 2){//标题
                    Elements aEleList = tdEleList.get(i).getElementsByTag("a");
                    if(null != aEleList && aEleList.size() ==0){
                        continue;
                    }
                    Element aEle = aEleList.get(0);
                    String href = aEle.attr("href");
                    remiseNotice.setHref(Constant.HTTP_HOST + href);
                    String title = "";
                    if(StringUtils.isNotEmpty(aEle.text()) && !aEle.text().contains("...")){
                        remiseNotice.setTitle(aEle.text());
                    }else if (StringUtils.isNotEmpty(aEle.text()) && aEle.text().contains("...")){
                        Elements child = aEle.children();
                        if(null == child || child.size() != 1 ){
                            continue;
                        }
                        Element spanEle = child.get(0);
                        title = spanEle.attr("title");
                        remiseNotice.setTitle(title);
                    }
                    if(StringUtils.isNotEmpty(remiseNotice.getTitle())){
                        String num = StringUtils.getNoticeNumFromTitle(remiseNotice.getTitle());
                        remiseNotice.setNoticeNum(num);
                    }
                }else if  (i == 3 ){
                    int code = RemiseNoticeTypeEnum.getCodeByName(tdEleList.get(i).text());
                    remiseNotice.setType(code);
                }else if  (i == 4 ){
                    String str = tdEleList.get(i).text();
                    if(StringUtils.isNotEmpty(str)){
                        str = str.trim();
                    }
                    if(StringUtils.isNotEmpty(str)){
                        Date publishDate  = null;
                        try {
                            publishDate = new SimpleDateFormat("yyyy/MM/dd").parse(str);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(null != publishDate){
                            remiseNotice.setPublishTime(new Timestamp(publishDate.getTime()));
                        }
                    }
                }
            }

            if(StringUtils.isEmpty(remiseNotice.getNoticeNum())){
                remiseNotice.setNoticeNum(remiseNotice.getTitle());//如果暂时没有解析出正确的公告号，则暂时用title来代替，后续的定时器会定时更正过来
            }

            if( StringUtils.isEmpty(remiseNotice.getHref())){
                log.warn("无效数据:remiseNotice=" + remiseNotice);
                continue;//无效数据
            }

            RemiseNotice temp = new RemiseNotice();
            temp.setHref(remiseNotice.getHref());
            List<RemiseNotice> tempList = remiseNoticeService.listByProperty(temp);
            if(null != tempList && !tempList.isEmpty() && tempList.size() > 0){
                log.error("该土地公告号的数据已存在: title=" + remiseNotice.getTitle());
                temp = tempList.get(0);
                if(temp.getTitle().equals(remiseNotice.getTitle()) && temp.getNoticeNum().equals(remiseNotice.getNoticeNum())){
                    log.info("该土地公告号的数据已存在(且无需更新)，跳过");
                }else{
                    //TODO 异步的其他方式去处理需要更新的数据
                    log.error("该土地公告号的数据已存在，但需要更新（暂不处理）: remiseNotice=" + remiseNotice);
                }
                continue;
            }

            if(StringUtils.isNotEmpty(remiseNotice.getHref())){
                Map<String,String> headMap = new HashMap<>();
                headMap.put("Cookie", "security_session_mid_verify=d70d231ed4e7b195938aac569dccf384;");
                headMap.put("Host", Constant.HOST);
                String content = HttpUtils.get(remiseNotice.getHref(), headMap);
                remiseNotice.setContent(content);
                remiseNotice.setCreateTime(new Timestamp(System.currentTimeMillis()));
                remiseNoticeList.add(remiseNotice);
                int second = IntUtils.getRandomInt(2,6);
                try {
                    log.info("本条数据已抓取完成!休眠" + second + "秒 ,title=" + remiseNotice.getTitle() );
                    long sleep = second  == 0 ? 1000 :  second * 1000;
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return remiseNoticeList;
    }
}
