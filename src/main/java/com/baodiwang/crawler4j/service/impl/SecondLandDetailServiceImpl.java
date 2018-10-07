/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baodiwang.crawler4j.utils.HttpUtils;
import com.baodiwang.crawler4j.utils.RegexUtil;
import com.baodiwang.crawler4j.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.baodiwang.crawler4j.mapper.SecondLandDetailMapper;
import com.baodiwang.crawler4j.model.SecondLandDetail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baodiwang.crawler4j.service.SecondLandDetailService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service("secondLandDetailService")
public class SecondLandDetailServiceImpl extends GenericService<SecondLandDetail, Integer> implements SecondLandDetailService {

    private static Log log = LogFactory.getLog(SecondLandDetailServiceImpl.class);

    private SecondLandDetailMapper secondLandDetailMapper;

    @Autowired
    public void setSecondLandDetailMapper(SecondLandDetailMapper secondLandDetailMapper) {
        this.secondLandDetailMapper = secondLandDetailMapper;
    }

    /**
     * 通过主键查询实体对象
     * @param primaryKey
     * @return
     */
    public SecondLandDetail getByPK(java.lang.Integer primaryKey) {
        return secondLandDetailMapper.getByPK(primaryKey);
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<SecondLandDetail> list() {
        return secondLandDetailMapper.list();
    }

    /**
     * 根据查询条件查询所有记录
     * @return
     */
    public List<SecondLandDetail> listByProperty(SecondLandDetail secondLandDetail){
        return secondLandDetailMapper.listByProperty(secondLandDetail);
    }


    /**
     * 根据主键删除记录
     * @param primaryKey
     * @return
     */
    public int deleteByPK(java.lang.Integer primaryKey) {
        return secondLandDetailMapper.deleteByPK(primaryKey);
    }

    /**
     * 根据多个主键删除记录
     * @param primaryKeys
     */
    public void deleteByPKeys(List<java.lang.Integer> primaryKeys) {
        secondLandDetailMapper.deleteByPKeys(primaryKeys);
    }

    /**
     * 根据传入参数删除记录
     * @param secondLandDetail
     * @return
     */
    public int deleteByProperty(SecondLandDetail secondLandDetail){
        return secondLandDetailMapper.deleteByProperty(secondLandDetail);
    }

    /**
     * 保存记录
     * @param secondLandDetail
     * @return
     */
    public int save(SecondLandDetail secondLandDetail){
        return secondLandDetailMapper.save(secondLandDetail);
    }

    /**
     * 更新记录
     * @param secondLandDetail
     * @return
     */
    public int update(SecondLandDetail secondLandDetail){
        return secondLandDetailMapper.update(secondLandDetail);
    }

    /**
     * 根据条件查询记录条数
     * @param secondLandDetail
     * @return
     */
    public int findByCount(SecondLandDetail secondLandDetail){
        return secondLandDetailMapper.findByCount(secondLandDetail);
    }

    /**
     * 根据查询条件查询分页记录
     * @return
     */
    @Override
    public Page<SecondLandDetail> findByPage(Page<SecondLandDetail> page, SecondLandDetail secondLandDetail) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<SecondLandDetail> secondLandDetailList = secondLandDetailMapper.listByProperty(secondLandDetail);
        if(null == secondLandDetailList || secondLandDetailList.size() == 0){
            return new Page<SecondLandDetail>();
        }
        return (Page<SecondLandDetail>)secondLandDetailList;
    }




    public String getContactsPhone(String content,String href){
        if(StringUtils.isEmpty(content) || StringUtils.isEmpty(href)){
            return null;
        }
        Document doc = Jsoup.parse(content);
        Elements scripts = doc.select("script");
        String src = "";
        String detailId = "";
        for(Element script : scripts) {
            log.info("script.html()=" + script.html());
            if (script.html().contains("var js_data =")) { //注意这里一定是html(), 而不是text()

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
                    continue;
                }
                JSONObject jsonObject  = JSONObject.parseObject(jsonStr);
                src = (String) jsonObject.get("src");
                detailId= (String) jsonObject.get("did");
                if(StringUtils.isNotEmpty(src) && StringUtils.isNotEmpty(detailId)){
                    break;
                }
            }
        }
        if(StringUtils.isEmpty(src) ||  StringUtils.isEmpty(detailId)){
            return null;
        }
        return this.getContactsPhone(detailId,src,href);
    }

    /**
     * 调用其网站的接口，获取详情页的联系人电话号码
     * @param detailId
     * @param landSrc
     * @param href
     * @return
     */
    private String getContactsPhone(String detailId,String landSrc,String href){
        if(StringUtils.isEmpty(href)){
            log.error("获取到的联系电话信息异常，参数landSrc=" + landSrc  + ",href= "+ href);
            return "";
        }
        String httpHost = href.substring(0,href.lastIndexOf("/"));
        if(StringUtils.isEmpty(httpHost)){
            log.error("获取到的联系电话信息异常，解析详情页链接的信息异常！ host=" + httpHost);
            return "";
        }
        String host = httpHost.replace("https://","");

        String url = String.format( httpHost + "/landext/view/%s/%s" , detailId , landSrc)  ;
        Map <String,String> headMap = new HashMap<>();
        headMap.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headMap.put("Accept-Encoding","gzip, deflate, br");
        headMap.put("Accept-Language","zh-CN,zh;q=0.9");
        headMap.put("Connection","keep-alive");
        headMap.put("Cookie","gr_user_id=3bfc5c67-dc75-4231-b417-12f66c5a6842; gr_session_id_8d0f4cbc7395183a=b08d29fa-5a9b-4368-89a4-976893ca70e9; gr_session_id_8d0f4cbc7395183a_b08d29fa-5a9b-4368-89a4-976893ca70e9=true; PHPSESSID=ST-1989-iVcf6gHfY5Dog6VIeUq6-castuliucom; tluid=860188; tlusername=17520464602; tlauth=c1b3uc7IWa%2FsSwAQBi5T9YPMqfOoAZ1MBz28KLdJBW0tvkO%2By8x2eaqAQg4dnDx%2Fkxr1gHO%2F; tluser_agent=Mozilla%2F5.0+%28Windows+NT+6.3%3B+Win64%3B+x64%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F69.0.3497.81+Safari%2F537.36; Hm_lvt_621cacc45e5c3b2243ac6211222ee1e5=1538191311,1538535033,1538535215,1538535551; Hm_lpvt_621cacc45e5c3b2243ac6211222ee1e5=1538901723; XSRF-TOKEN=eyJpdiI6Ik5Tb1ZxXC9xMXUzS3BKcjY0YXZzMENRPT0iLCJ2YWx1ZSI6IllnTzNJNTRkZHJMdW1KVFBKTHI0WFVJZmxhTlNURVJoQU95M1JWQVl6b2dTRGdKeXZLdHJYdkk0NXA3SGZMXC9sMGt4R3pYUG8wXC9wVFkzTGd6M3NVN2c9PSIsIm1hYyI6IjJjMWQyNzkyOTNhOWU1YWFlMzQ4ZDJjMzEyZmI4NmRhODliYjRiOGI4YjZjMzQ2MjgwN2RjYmJmNDhlZjM4NDEifQ%3D%3D; tuliu_session=eyJpdiI6Im5wUkhPSzhKU3hnSzljT0ZJSGJiR2c9PSIsInZhbHVlIjoiSWxKR01HMzBjZ2JYeUVjaEFQOEFSSTcyOXBENjNrNW9leVIrWW5iT1Y4ZzRoZUtPZGlURWVSUVJTdXJqK1pZQjdzMW5DaEFSZFlMNTlLSHVhWEdJbVE9PSIsIm1hYyI6IjhhOTgzNmFlN2RkYjllZTc0ZjI5MWFlYTQwMzNlOWZkNjI1NThiNThkYjYwMzMwYjk1M2IzNzg1ZjczZDdhMTQifQ%3D%3D");
        headMap.put("Host",host);
        headMap.put("Pragma","no-cache");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
        String result = HttpUtils.get(url, headMap, HttpUtils.CHAR_SET_UTF8);
        log.info("获取到的联系电话信息的原始数据：result =" + result );
        if(StringUtils.isEmpty(result)){
            return null;
        }
        Map map = JSONObject.parseObject(result, Map.class);
        if(null == map || map.get("data") == null ){
            log.error("map=" + map);
            return  null;
        }
        Integer code = (Integer) map.get("code");
        if(null == code || 200 != code){
            log.error("map=" + map);
            return null;
        }

        String phone = "";
        try{
            phone = ((JSONObject) map.get("data")).get("display_phone").toString();
            if(StringUtils.isNotEmpty(phone)){
                phone = phone.trim();
            }
        }catch (Exception e){
             log.error("解析联系电话发生异常:"+e.getMessage(),e);
        }
        if(phone.contains("\\u")){
            phone = StringUtils.unicodeToCn(phone);
        }
        return phone;
    }
}
