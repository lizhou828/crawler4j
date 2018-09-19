/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.controller.tuliu.SecondLandDetailParser.java <2018年09月19日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.controller.tuliu;

import com.baodiwang.crawler4j.model.SecondLand;
import com.baodiwang.crawler4j.model.SecondLandDetail;
import com.baodiwang.crawler4j.service.SecondLandDetailService;
import com.baodiwang.crawler4j.service.SecondLandService;
import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年09月19日 09时33分
 */
@Service("secondLandDetailParser")
public class SecondLandDetailParser {


    private static Log log = LogFactory.getLog(SecondLandDetailParser.class);

    /*地块类型 1 宅基地 */
    public static final int LAND_TYPE_ZHAJIDI = 1;

    /*地块类型 2工商用地 */
    public static final int LAND_TYPE_GONGSHANG = 2;

    /*地块类型 3农村用地*/
    public static final int LAND_TYPE_NONGCUN = 3;


    @Autowired
    private SecondLandDetailService secondLandDetailService;

    @Autowired
    private SecondLandService secondLandService;


    public SecondLandDetail parserHtml(Integer secondLandId){
        if(null == secondLandId || secondLandId <= 0){
            return null;
        }
        SecondLand secondLand = secondLandService.getByPK(secondLandId);
        if(null == secondLand || StringUtils.isEmpty(secondLand.getHref()) || StringUtils.isEmpty(secondLand.getContent()) || secondLand.getContent().length() < 10000){
            log.warn("获取网页的数据异常:pageContent=" + secondLand.getContent());
            return null;
        }
        return parserHtml(secondLandId,secondLand.getContent(),secondLand.getHref());
    }

    public SecondLandDetail parserHtml(Integer secondLandId,String content,String href){
        if(StringUtils.isEmpty(content) || StringUtils.isEmpty(href) || content.length() < 10000){
            log.warn("传入的网页数据异常:pageContent=" + content);
            return null;
        }
        Document doc = Jsoup.parse(content);

        Elements titleElement = doc.select(".landD-supply-pshow-tit").select("h1");
        SecondLandDetail detail = new SecondLandDetail();
        if(null != titleElement){
            detail.setTitle(titleElement.text());
        }

        Elements updateTimeEle =  doc.select(".line-h-28").get(0).children();
        Element upEle  =updateTimeEle.get(0);
        for(Element up : upEle.children()){
            if(null != up && up.text().contains("土地编码：")){
                String landCode = up.text().replace("土地编码：","");
                detail.setLandCode(landCode);
            }else if( null != up &&  up.text().contains("土地编码:")){
                String landCode = up.text().replace("土地编码:","");
                detail.setLandCode(landCode);
            }
            if(null != up && up.text().contains("更新时间：")){
                String u = up.text().replace("更新时间：","");
                detail.setUpdateTime(u);
            }else if (null != up && up.text().contains("更新时间:")){
                String u = up.text().replace("更新时间:","");
                detail.setUpdateTime(u);
            }
        }


        Element detailEle = doc.select(".landD-pictxt-l").get(0).children().select(".txts").get(0);

        String landPrice = detailEle.select(".bg-f5").get(0).text();
        if(StringUtils.isNotEmpty(landPrice) && landPrice.contains("土地价格")){
            landPrice = landPrice.replace("土地价格", "");
            if(landPrice.contains("带看费")){
                landPrice = landPrice.substring(0,landPrice.indexOf("带看费"));
            }
            landPrice = landPrice.trim();
            detail.setLandPrice(landPrice);
        }

        for(int j = 0 ;j <detailEle.children().size();j++){
            if(detailEle.child(j).text().contains("土地用途")  && detailEle.child(j).text().contains("流转类型")){
                String [] detail1Str = detailEle.child(j).text().split(" ");
                if(null != detail1Str && detail1Str.length == 4){
                    if(detail1Str[0].contains("土地用途")){
                        detail.setPurpose(detail1Str[1]);
                    }
                    if(detail1Str[2].contains("流转类型")){
                        detail.setTransferType(detail1Str[3]);
                    }
                }
            }


            if(detailEle.child(j).text().contains("流转年限") && detailEle.child(j).text().contains("土地面积")){
                Elements detail2 = detailEle.child(j).children();
                if(null != detail2 ){
                    for(int i = 0 ;i < detail2.size() ;i++){
                        if(null == detail2.get(i)){
                            continue;
                        }
                        if(StringUtils.isNotEmpty(detail2.get(i).text()) && detail2.get(i).text().contains("流转年限")){
                            String transferTime = detail2.get(i+1).text();
                            transferTime = transferTime.trim();
                            detail.setTransferTime(transferTime);
                        }
                        if(StringUtils.isNotEmpty(detail2.get(i).text()) && detail2.get(i).text().contains("土地面积")){
                            String landArea = detail2.get(i+1).text();
                            landArea= landArea.trim();
                            detail.setTotalArea(landArea);
                        }

                    }
                }
            }
            if(StringUtils.isNotEmpty(detailEle.child(j).text()) && detailEle.child(j).text().contains("土地地点")){
                String location =detailEle.child(j).text().replace("土地地点", "");
                location = location.trim();
                detail.setRegionalLocation(location );
            }
        }

        Integer tradeStatus = 0;
        Elements tradedEle = doc.select(".v2-landD-traded-icon");
        Elements waitTradedEle = doc.select(".v2-ld-status-img");
        if(null != tradedEle && tradedEle.size() > 0){
            tradeStatus = 1;
        }else if (null != waitTradedEle && waitTradedEle.html().contains("zt_daijiaoyi.png")){
            tradeStatus = 0;
        }


        detail.setTradeStatus(tradeStatus);

        if(null != doc.getElementById("land_panel_phone") && null != doc.getElementById("land_panel_phone").children() && doc.getElementById("land_panel_phone").children().size() > 0){
            Elements contactEle = doc.getElementById("land_panel_phone").children();

            String contactsStr = StringUtils.isNotEmpty(contactEle .get(0).text()) && contactEle .get(0).text().contains("联系人") ? contactEle .get(0).text() : "" ;
            if(StringUtils.isNotEmpty(contactsStr)){
                contactsStr = contactsStr.replace("联系人", "");
                contactsStr = contactsStr.replace("信息来源 地主","");
                contactsStr  = contactsStr.trim();
                detail.setContacts(contactsStr);
            }
        }

        String remark = doc.select(".landD-intr-txt").text();
        if(StringUtils.isNotEmpty(remark)){
            remark = remark.trim();
            detail.setRemark(remark);
        }

        detail.setSecondLandId(secondLandId);

        String phone = secondLandDetailService.getContactsPhone(href);
        if(StringUtils.isNotEmpty(phone)){
            detail.setContactsPhone(phone);
        }
        detail.setCreateTime(new Timestamp(new Date().getTime()));
        return detail;
    }

}
