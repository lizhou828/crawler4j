package com.baodiwang.crawler4j.service;

import com.baodiwang.crawler4j.ApplicationTests;
import com.baodiwang.crawler4j.VO.RemiseNoticeVo;
import com.baodiwang.crawler4j.controller.detailPage.RemiseNoticeDetailParser;
import com.baodiwang.crawler4j.mapper.RemiseNoticeMapper;
import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.model.RemiseNoticeDetail;
import com.baodiwang.crawler4j.utils.StringUtils;
import com.github.pagehelper.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhou on 2018年08月22日 21时48分
 */
public class RemiseNoticeServiceTest extends ApplicationTests {

    private static final Logger log = LogManager.getLogger(RemiseNoticeServiceTest.class);
    @Autowired
    private RemiseNoticeService remiseNoticeService;


    @Test
    public void addTest() throws Exception {
        RemiseNotice remiseNotice = new RemiseNotice();
        remiseNotice.setType(1);
        remiseNotice.setTitle("title");
        remiseNotice.setAreaId(1123);
        remiseNotice.setAreaName("地区");
        remiseNotice.setContent("a撒旦法阿斯顿发flkasdfasdfasdflask");
        remiseNotice.setCreator("创建者");
        remiseNotice.setHref("http://www.baidu.com");
        remiseNotice.setNoticeNum("1231231231");
        remiseNotice.setPublishTime(new Timestamp(System.currentTimeMillis()));
        remiseNotice.setCreateTime(new Timestamp(System.currentTimeMillis()));
        int result = remiseNoticeService.save(remiseNotice);
        log.info("result="+result + ",remiseNotice=" + remiseNotice);
    }

    @Test
    public void getByPKTest() throws Exception {
        RemiseNotice remiseNotice  = remiseNoticeService.getByPK(4);
        System.out.println(remiseNotice );
    }

    @Test
    public void listByPropertyTest(){
        RemiseNotice remiseNotice = new RemiseNotice();
        remiseNotice.setHref("http://www.landchina.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=fb91fb93-1e5e-4205-8845-966798e37d3b&sitePath=");
        List<RemiseNotice> listPage = remiseNoticeService.listByProperty(remiseNotice);
        System.out.println(listPage);
    }

    @Test
    public void findNoticeWithoutDetailTest(){
        List<RemiseNotice> remiseNoticeList = remiseNoticeService.findNoticeWithoutDetail();
        System.out.println(remiseNoticeList);
    }

    @Test
    public void findNoticeWithoutContentTest(){
        List<RemiseNotice> remiseNoticeList = remiseNoticeService.findNoticeWithoutContent();
        System.out.println(remiseNoticeList);
    }

    @Test
    public void parseContent(){
        RemiseNotice remiseNotice = remiseNoticeService.getByPK(25138);
        if(null != remiseNotice && StringUtils.isNotEmpty(remiseNotice.getContent())){
            RemiseNoticeVo remiseNoticeVo = RemiseNoticeDetailParser.parseHtml(remiseNotice.getContent());
            System.out.println("remiseNoticeVo=" + remiseNoticeVo);
        }
    }


    /**
     * 从指定的id段中查找content（大文本字段）为空的数据
     */
    @Test
    public void findNoticeWithoutContentByIdTest(){
        List<RemiseNotice> remiseNoticeList = remiseNoticeService.findNoticeWithoutContentById(25138L, null, 1, 100);
        System.out.println(remiseNoticeList);
    }

    @Test
    public void findNoticeMissDetailTest(){
        List<RemiseNotice> remiseNoticeList = remiseNoticeService.findNoticeMissDetail(1, 20);
        System.out.println(remiseNoticeList);
    }


    @Test
    public void findMaxIdTest(){
        long maxId = remiseNoticeService.findMaxId();
        System.out.println("maxId=" + maxId);
    }

    /**
     * 批量插入数据后，返回主键Id
     */
    @Test
    public void batchInsertWithIdTest(){
        List<RemiseNotice> remiseNoticeListInsert = new ArrayList<>();
        RemiseNotice remiseNotice = new RemiseNotice();
        remiseNotice.setHref("http://www.baidu.com");
        remiseNotice.setTitle("测试1");
        remiseNotice.setNoticeNum("test1");
        remiseNotice.setType(0);
        remiseNoticeListInsert.add(remiseNotice);
        remiseNotice = new RemiseNotice();
        remiseNotice.setHref("http://www.qq.com");
        remiseNotice.setTitle("测试2");
        remiseNotice.setNoticeNum("test2");
        remiseNotice.setType(0);
        remiseNoticeListInsert.add(remiseNotice);
        remiseNoticeService.batchInsertWithId(remiseNoticeListInsert);
        System.out.println(remiseNoticeListInsert);
    }


    @Test
    public void findMinIdWithoutContentTest(){
        long minId =remiseNoticeService.findMinIdWithoutContent(1000L);
        System.out.println("minId=" + minId );
    }

    @Test
    public void findContentWithDirtyCodeTest(){
        List<RemiseNotice> remiseNoticeList = remiseNoticeService.findContentWithDirtyCode(1L, 1000L);
        System.out.println("remiseNoticeList=" + remiseNoticeList);
    }





}
