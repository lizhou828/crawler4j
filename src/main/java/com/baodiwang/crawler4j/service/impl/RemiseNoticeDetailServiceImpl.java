/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.baodiwang.crawler4j.mapper.RemiseNoticeDetailMapper;
import com.baodiwang.crawler4j.model.RemiseNoticeDetail;
import java.util.List;

import com.baodiwang.crawler4j.service.RemiseNoticeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service
public class RemiseNoticeDetailServiceImpl extends GenericService<RemiseNoticeDetail, Integer> implements RemiseNoticeDetailService {

    private RemiseNoticeDetailMapper remiseNoticeDetailMapper;

    @Autowired
    public void setRemiseNoticeDetailMapper(RemiseNoticeDetailMapper remiseNoticeDetailMapper) {
        this.remiseNoticeDetailMapper = remiseNoticeDetailMapper;
    }

    /**
     * 通过主键查询实体对象
     * @param primaryKey
     * @return
     */
    public RemiseNoticeDetail getByPK(Integer primaryKey) {
        return remiseNoticeDetailMapper.getByPK(primaryKey);
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<RemiseNoticeDetail> list() {
        return remiseNoticeDetailMapper.list();
    }

    /**
     * 根据查询条件查询所有记录
     * @return
     */
    public List<RemiseNoticeDetail> listByProperty(RemiseNoticeDetail remiseNoticeDetail){
        return remiseNoticeDetailMapper.listByProperty(remiseNoticeDetail);
    }


    /**
     * 根据主键删除记录
     * @param primaryKey
     * @return
     */
    public int deleteByPK(Integer primaryKey) {
        return remiseNoticeDetailMapper.deleteByPK(primaryKey);
    }

    /**
     * 根据多个主键删除记录
     * @param primaryKeys
     */
    public void deleteByPKeys(List<Integer> primaryKeys) {
        remiseNoticeDetailMapper.deleteByPKeys(primaryKeys);
    }

    /**
     * 根据传入参数删除记录
     * @param remiseNoticeDetail
     * @return
     */
    public int deleteByProperty(RemiseNoticeDetail remiseNoticeDetail){
        return remiseNoticeDetailMapper.deleteByProperty(remiseNoticeDetail);
    }

    /**
     * 保存记录
     * @param remiseNoticeDetail
     * @return
     */
    public int save(RemiseNoticeDetail remiseNoticeDetail){
        return remiseNoticeDetailMapper.save(remiseNoticeDetail);
    }

    /**
     * 更新记录
     * @param remiseNoticeDetail
     * @return
     */
    public int update(RemiseNoticeDetail remiseNoticeDetail){
        return remiseNoticeDetailMapper.update(remiseNoticeDetail);
    }

    /**
     * 根据条件查询记录条数
     * @param remiseNoticeDetail
     * @return
     */
    public int findByCount(RemiseNoticeDetail remiseNoticeDetail){
        return remiseNoticeDetailMapper.findByCount(remiseNoticeDetail);
    }

     /**
     * 根据查询条件查询分页记录
     * @return
     */
    @Override
    public Page<RemiseNoticeDetail> findByPage(Page<RemiseNoticeDetail> page, RemiseNoticeDetail remiseNoticeDetail) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<RemiseNoticeDetail> remiseNoticeDetailList = remiseNoticeDetailMapper.listByProperty(remiseNoticeDetail);
        if(null == remiseNoticeDetailList || remiseNoticeDetailList.size() == 0){
            return new Page<RemiseNoticeDetail>();
        }
        return (Page<RemiseNoticeDetail>)remiseNoticeDetailList;
    }

}
