///*
// * Powered By [Frank-Liz-Lee]
// * Copyright(C) 2012-2017 Liz Company
// * All rights reserved.
// * -----------------------------------------------
// */
//
//package com.baodiwang.crawler4j.controller;
//
//import com.baodiwang.crawler4j.model.RemiseNotice;
//import com.baodiwang.crawler4j.model.RequestModel;
//import com.github.pagehelper.Page;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import com.baodiwang.crawler4j.service.RemiseNoticeService;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.sql.Timestamp;
//import java.util.List;
//
//
//@Controller
//@RequestMapping("/remiseNotice")
//public class RemiseNoticeController {
//
//    private static Log log = LogFactory.getLog(RemiseNoticeController.class);
//
//    @Autowired
//    private RemiseNoticeService remiseNoticeService;
//
//    /**
//     * 通过主键查询实体对象
//     * @return
//     */
//    @RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
//    @ResponseBody
//    public RemiseNotice getByPK(@PathVariable("key") Integer key) throws Exception {
//        return remiseNoticeService.getByPK(key);
//    }
//
//    /**
//     * 分页查询记录
//     * @return
//     */
//    @RequestMapping(value = {"/findByPage"}, method = RequestMethod.POST)
//    @ResponseBody
//    public Page<RemiseNotice> findByPage(@RequestBody RequestModel<RemiseNotice> requestModel) throws Exception {
//        Page<RemiseNotice> page = new Page<RemiseNotice>();
//        page.setPageNum(requestModel.getPageNo());
//        page.setPageSize(requestModel.getPageSize());
//        return remiseNoticeService.findByPage(page, requestModel.getParam());
//    }
//
//    /**
//     * 新增记录
//     * @return
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    public void add(@RequestBody RemiseNotice remiseNotice) throws Exception {
//        remiseNoticeService.save(remiseNotice);
//    }
//
//    /**
//     * 根据多条主键值删除记录
//     * @return
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    public void delete(@RequestBody List<Integer> list) throws Exception {
//        remiseNoticeService.deleteByPKeys(list);
//    }
//
//    /**
//     * 修改记录
//     * @return
//     */
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    public void update(@RequestBody RemiseNotice remiseNotice) throws Exception {
//        remiseNoticeService.update(remiseNotice);
//    }
//
//}
//
//
