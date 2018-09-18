/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.controller.tuliu.SecondLandParser.java <2018年09月18日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.controller.tuliu;

import com.baodiwang.crawler4j.model.SecondLand;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月18日 17时54分
 */

@Component
public class SecondLandParser {

    private static final Logger log = LogManager.getLogger(SecondLandParser.class);

    public List<SecondLand> parserHtml(String pageContent){
        if(StringUtils.isEmpty(pageContent) || pageContent.length() < 10000){
            log.warn("获取网页的数据异常:pageContent=" + pageContent);
            return null;
        }

        Document doc = Jsoup.parse(pageContent);
        Element list1Div = doc.getElementById("list1");
        if(null == list1Div){
            log.error("表格数据异常!");
            return null;
        }
        List<SecondLand> secondLandList = new ArrayList<>();
        SecondLand secondLand = null;
        List<Element> itemList = list1Div.select(".col-sm-4").select(".land-square-item");
        for(Element element : itemList){
            if(null == element) continue;
            if(null != element.children() && CollectionUtils.isNotEmpty(element.children()) && element.children().size() == 2 ){
                Element aEle = element.child(0);
                if(null == aEle) continue;
                secondLand = new SecondLand();
                String title = aEle.attr("title");
                if(StringUtils.isNotEmpty(title) && title.contains(" 土地编号:")){
                    title = title.substring(0,title.indexOf(" 土地编号:"));
                }
                secondLand.setTitle(title);
                String href = aEle.attr("href");
                secondLand.setHref(href);
                secondLand.setCreateTime(new Timestamp(new Date().getTime()));
                secondLandList.add(secondLand);
            }
        }
        return secondLandList;
    }
}
