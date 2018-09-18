/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.baodiwang.crawler4j.mapper.SecondLandDetailMapper;
import com.baodiwang.crawler4j.model.SecondLandDetail;
import java.util.List;

import com.baodiwang.crawler4j.service.SecondLandDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service
public class SecondLandDetailServiceImpl extends GenericService<SecondLandDetail, Integer> implements SecondLandDetailService {

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
}
