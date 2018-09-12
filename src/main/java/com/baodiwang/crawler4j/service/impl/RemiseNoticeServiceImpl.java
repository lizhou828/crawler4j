/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.baodiwang.crawler4j.mapper.RemiseNoticeMapper;
import com.baodiwang.crawler4j.model.RemiseNotice;
import java.util.List;

import com.baodiwang.crawler4j.service.RemiseNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemiseNoticeServiceImpl extends GenericService<RemiseNotice, Integer> implements RemiseNoticeService {

    private RemiseNoticeMapper remiseNoticeMapper;

    @Autowired
    public void setRemiseNoticeMapper(RemiseNoticeMapper remiseNoticeMapper) {
        this.remiseNoticeMapper = remiseNoticeMapper;
    }

    /**
     * 通过主键查询实体对象
     * @param primaryKey
     * @return
     */
    public RemiseNotice getByPK(Integer primaryKey) {
        return remiseNoticeMapper.getByPK(primaryKey);
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<RemiseNotice> list() {
        return remiseNoticeMapper.list();
    }

    /**
     * 根据查询条件查询所有记录
     * @return
     */
    public List<RemiseNotice> listByProperty(RemiseNotice remiseNotice){
        return remiseNoticeMapper.listByProperty(remiseNotice);
    }


    /**
     * 根据主键删除记录
     * @param primaryKey
     * @return
     */
    public int deleteByPK(Integer primaryKey) {
        return remiseNoticeMapper.deleteByPK(primaryKey);
    }

    /**
     * 根据多个主键删除记录
     * @param primaryKeys
     */
    public void deleteByPKeys(List<Integer> primaryKeys) {
        remiseNoticeMapper.deleteByPKeys(primaryKeys);
    }

    /**
     * 根据传入参数删除记录
     * @param remiseNotice
     * @return
     */
    public int deleteByProperty(RemiseNotice remiseNotice){
        return remiseNoticeMapper.deleteByProperty(remiseNotice);
    }

    /**
     * 保存记录
     * @param remiseNotice
     * @return
     */
    public int save(RemiseNotice remiseNotice){
        return remiseNoticeMapper.save(remiseNotice);
    }

    /**
     * 更新记录
     * @param remiseNotice
     * @return
     */
    public int update(RemiseNotice remiseNotice){
        return remiseNoticeMapper.update(remiseNotice);
    }

    /**
     * 根据条件查询记录条数
     * @param remiseNotice
     * @return
     */
    public int findByCount(RemiseNotice remiseNotice){
        return remiseNoticeMapper.findByCount(remiseNotice);
    }

     /**
     * 根据查询条件查询分页记录
     * @return
     */
    @Override
    public Page<RemiseNotice> findByPage(Page<RemiseNotice> page, RemiseNotice remiseNotice) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<RemiseNotice> remiseNoticeList = remiseNoticeMapper.listByProperty(remiseNotice);
        if(null == remiseNoticeList || remiseNoticeList.size() == 0){
            return new Page<RemiseNotice>();
        }
        return (Page<RemiseNotice>)remiseNoticeList;
    }

    public List<RemiseNotice> findNoticeWithoutDetail(){
        return remiseNoticeMapper.findNoticeWithoutDetail(1, 20);
    }
    public List<RemiseNotice> findNoticeWithoutDetail( int pageNum , int pageSize){
        if(pageNum <= 0 || pageSize <= 0){
            return null;
        }
        return remiseNoticeMapper.findNoticeWithoutDetail(pageNum, pageSize);
    }


    @Override
    public List<RemiseNotice> findNoticeMissDetail(int pageNum, int pageSize) {
        return remiseNoticeMapper.findNoticeMissDetail(pageNum, pageSize);
    }

    /**
     * 默认查询最后一页
     */
    @Override
    public List<RemiseNotice> findNoticeWithoutContent() {
        long maxId =  remiseNoticeMapper.findMaxId();
        int pageSize = 20;
        long allPages = maxId % pageSize == 0 ? maxId % pageSize : maxId % pageSize + 1;
        long startId = ( allPages - 1)  * pageSize ;
        return remiseNoticeMapper.findNoticeWithoutContent(startId, pageSize);
    }

    @Override
    public List<RemiseNotice> findNoticeWithoutContent(long startId, int pageSize) {
        if(startId <= 0L ){
            long maxId = remiseNoticeMapper.findMaxId();
            startId = maxId - 1000L;
        }
        if( pageSize <= 0){
            pageSize = 1000;
        }
        return remiseNoticeMapper.findNoticeWithoutContent( startId , pageSize);
    }


    public List<RemiseNotice> findNoticeWithoutContentById(Long startId,Long endId, int pageNum ,int pageSize){
        if(pageNum <= 0 || pageSize <= 0){
            return null;
        }
        if(null == startId || startId <= 0L){
            return null;
        }
        return remiseNoticeMapper.findNoticeWithoutContentById(startId, endId, pageNum, pageSize);
    }

    @Override
    public long findMaxId() {
        return remiseNoticeMapper.findMaxId();
    }

    @Override
    public long findMinId() {
        return remiseNoticeMapper.findMinId();
    }

    public void batchInsertWithId(List<RemiseNotice> remiseNotices){
        remiseNoticeMapper.batchInsertWithId(remiseNotices);
    }

    public long findMinIdWithoutContent(Long lastDataCount){
        if(null == lastDataCount || lastDataCount <= 0L){
            lastDataCount = 1000L;//默认在最新的1000条数据中查询 没有content的记录的最小的id
        }
        return remiseNoticeMapper.findMinIdWithoutContent(lastDataCount);
    }
}
