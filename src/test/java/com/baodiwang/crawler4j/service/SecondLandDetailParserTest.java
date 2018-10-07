package com.baodiwang.crawler4j.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baodiwang.crawler4j.ApplicationTests;
import com.baodiwang.crawler4j.controller.tuliu.SecondLandDetailParser;
import com.baodiwang.crawler4j.model.SecondLand;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lizhou on 2018年09月29日 17时22分
 */
public class SecondLandDetailParserTest extends ApplicationTests {

    @Autowired
    SecondLandDetailParser secondLandDetailParser;


    @Autowired
    private SecondLandService secondLandService;

    @Test
    public void parseTest(){
        SecondLand secondLand = secondLandService.getByPK(1);
        Document doc = Jsoup.parse(secondLand.getContent());
        Elements scripts = doc.select("script");
        for(Element script : scripts) {
            if (script.html().contains("var js_data =")) { //注意这里一定是html(), 而不是text()
                System.out.println(script.html());
                String jsonStr  = script.html();
                if(StringUtils.isNotEmpty(jsonStr)){
                    jsonStr = jsonStr.replace("var js_data =","");
                }
                if(jsonStr.contains(";")){
                    jsonStr = jsonStr.replace(";","");
                }
                if(jsonStr.contains("'")){
                    jsonStr = jsonStr.replace("'","\"");
                }
                if(!jsonStr.startsWith("{") && !jsonStr.endsWith("}")){

                }
                JSONObject jsonObject  = JSONObject.parseObject(jsonStr);
                String src = (String) jsonObject.get("src");
                String detailId= (String) jsonObject.get("did");
            }
        }

    }
}
