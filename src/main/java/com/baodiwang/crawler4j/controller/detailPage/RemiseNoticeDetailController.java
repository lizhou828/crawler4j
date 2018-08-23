/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.controller.HttpClientController.java <2018年08月22日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.controller.detailPage;

import com.baodiwang.crawler4j.VO.RemiseNoticeVo;
import com.baodiwang.crawler4j.model.RemiseNoticeDetail;
import com.baodiwang.crawler4j.utils.HttpUtils;
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

    @RequestMapping("/remiseNoticeDetail")
    public String detailPage(){
        //出让公告（2011年后） ->  详情页
        String listPageUrl = "http://www.landchina.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=b2297a6e-2368-4743-ac06-8adf1988fd60&sitePath=";

        Map<String,String> headMap = new HashMap<>();
        headMap.put("Cookie", "security_session_mid_verify=d70d231ed4e7b195938aac569dccf384;");
        String pageContent  = HttpUtils.post(listPageUrl, headMap,null);

//        String pageContent = getListPageContent(listPageUrl);
        if(StringUtils.isEmpty(pageContent) || pageContent.length() < 10000){
            log.warn("获取网页的数据异常:pageContent=" + pageContent);
            return pageContent;
        }
        RemiseNoticeVo remiseNoticeVo = RemiseNoticeDetailParser.parseHtml(pageContent);
        log.info(remiseNoticeVo);
        return remiseNoticeVo.toString();
    }




}
