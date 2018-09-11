/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.controller.HttpClientController.java <2018年08月22日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.controller.detailPage;

import com.baodiwang.crawler4j.VO.RemiseNoticeVo;
import com.baodiwang.crawler4j.constants.Constant;
import com.baodiwang.crawler4j.model.RemiseNoticeDetail;
import com.baodiwang.crawler4j.utils.HttpUtils;
import com.baodiwang.crawler4j.utils.LandChinaHttpBreaker2;
import com.baodiwang.crawler4j.utils.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出让公告详情页面
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月22日 11时03分
 */
@RestController
public class RemiseNoticeDetailController {

    private static final Logger log = LogManager.getLogger(RemiseNoticeDetailController.class);

    @RequestMapping("/postDetail")
    public String postDetailPage(){
        //出让公告（2011年后） ->  详情页
        String detailPageUrl = "http://www.landchina.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=c64ce206-9367-40e0-92a1-5938c978d560&sitePath=";//特殊字符  ㎡
//        detailPageUrl =  "http://www.landchina.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=b2297a6e-2368-4743-ac06-8adf1988fd60&sitePath="  //部分中文乱码 ： 博罗县石湾镇滘源路南侧地段

        Map<String,String> headMap = new HashMap<>();
        String pageContent  = LandChinaHttpBreaker2.breakBarrier(detailPageUrl, headMap, null);

//        String pageContent = getListPageContent(listPageUrl);
        if(StringUtils.isEmpty(pageContent) || pageContent.length() < 10000){
            log.warn("获取网页的数据异常:pageContent=" + pageContent);
            return pageContent;
        }
        RemiseNoticeVo remiseNoticeVo = RemiseNoticeDetailParser.parseHtml(pageContent);
        log.info(remiseNoticeVo);
        return remiseNoticeVo.toString();
    }


    @RequestMapping("/getDetail")
    public String getDetailPage(){
        //出让公告（2011年后） ->  详情页
        String detailPageUrl = "http://www.landchina.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=c64ce206-9367-40e0-92a1-5938c978d560&sitePath=";//特殊字符  ㎡
//        detailPageUrl =  "http://www.landchina.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=b2297a6e-2368-4743-ac06-8adf1988fd60&sitePath="  //部分中文乱码 ： 博罗县石湾镇滘源路南侧地段
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Cookie", "yunsuo_session_verify=a07658c84a6e83c4e917857b8aeaad57");
        headMap.put("Referer", "http://www.landchina.com/default.aspx?tabid=261&ComName=default");
        headMap.put("Origin", Constant.HTTP_HOST);
        headMap.put("Host", Constant.HOST);
        headMap.put("Accept-Encoding", "gzip, deflate");
        headMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        String webContent = HttpUtils.get(detailPageUrl, headMap);
        return webContent;
    }



}
