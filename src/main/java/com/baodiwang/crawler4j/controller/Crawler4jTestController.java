package com.baodiwang.crawler4j.controller;

import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


import java.io.File;
import java.util.HashSet;

/**
 * Created by lizhou on 2018年08月21日 16时44分
 */

@RestController
public class Crawler4jTestController {

    private static final Logger log = LogManager.getLogger(IndexController.class);

    @RequestMapping("/test")
    public  void test() throws Exception {
        String crawlStorageFolder = "E:/crawler";// 定义爬虫数据存储位置
        File file = new File(crawlStorageFolder);
        if(!file.exists() ){
            log.error("logger.error：定义爬虫数据存储位置  不存在！");
            System.out.println("System.out.println：定义爬虫数据存储位置  不存在！");
            return;
        }else{
            log.info("logger.info：定义爬虫数据存储位置:" + file.getPath());
            System.out.println("System.out.println:定义爬虫数据存储位置:" + file.getPath());
        }

        int numberOfCrawlers = 1;// 定义了1个爬虫，也就是1个线程

        CrawlConfig config = new CrawlConfig();// 定义爬虫配置
        HashSet<BasicHeader> collections = new HashSet<BasicHeader>();
        collections.add(new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3278.0 Safari/537.36"));
        collections.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        collections.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
        collections.add(new BasicHeader("Pragma","no-cache"));
        collections.add(new BasicHeader("Cache-Control","no-cache"));
        collections.add(new BasicHeader("Referer","http://www.landchina.com/default.aspx?tabid=261&ComName=default"));
        collections.add(new BasicHeader("Connection", "keep-alive"));
        collections.add(new BasicHeader("Host", "www.landchina.com"));
        collections.add(new BasicHeader("Cookie", "yunsuo_session_verify=108ac5d03c448e34f9faad26f25927a2; srcurl=687474703a2f2f7777772e6c616e646368696e612e636f6d2f64656661756c742e617370783f74616269643d32363126436f6d4e616d653d64656661756c74; security_session_mid_verify=d70d231ed4e7b195938aac569dccf384; ASP.NET_SessionId=ueudiu3jtu553hjpapfahhem; Hm_lvt_83853859c7247c5b03b527894622d3fa=1533886565,1534835749; Hm_lpvt_83853859c7247c5b03b527894622d3fa=1534909710"));
        config.setDefaultHeaders(collections);
        config.setCrawlStorageFolder(crawlStorageFolder);// 设置爬虫文件存储位置

        /*
         * 实例化爬虫控制器。
         */
        PageFetcher pageFetcher = new PageFetcher(config);// 实例化页面获取器
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();// 实例化爬虫机器人配置
        // 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件
        // 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        // 实例化爬虫控制器
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * 对于每次抓取，您需要添加一些种子网址。 这些是抓取的第一个URL，然后抓取工具开始跟随这些页面中的链接
         */
//        controller.addSeed("http://www.landchina.com/default.aspx?tabid=261");//出让公告（2011）
//        controller.addSeed("http://www.landchina.com/default.aspx?tabid=262");//地块公示
//        controller.addSeed("http://www.landchina.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=JYXT_ZJGG_4465&sitePath=");
//        controller.addSeed("http://www.landchina.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=8abdd753-6dc2-40b2-9331-8a6bcc4dff3d");
        String str = StringUtils.stringToHex("1366,768");
        String detailPageUrl = "http://www.landchina.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=JYXT_ZJGG_4465&sitePath&security_verify_data=" + str;
        controller.addSeed(detailPageUrl);
        /**
         * 启动爬虫，爬虫从此刻开始执行爬虫任务，根据以上配置
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
    }
}
