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
     * ͨ��������ѯʵ�����
     * @param primaryKey
     * @return
     */
    public SecondLandDetail getByPK(java.lang.Integer primaryKey) {
        return secondLandDetailMapper.getByPK(primaryKey);
    }

    /**
     * ��ѯ���м�¼
     * @return
     */
    public List<SecondLandDetail> list() {
        return secondLandDetailMapper.list();
    }

    /**
     * ���ݲ�ѯ������ѯ���м�¼
     * @return
     */
    public List<SecondLandDetail> listByProperty(SecondLandDetail secondLandDetail){
        return secondLandDetailMapper.listByProperty(secondLandDetail);
    }


    /**
     * ��������ɾ����¼
     * @param primaryKey
     * @return
     */
    public int deleteByPK(java.lang.Integer primaryKey) {
        return secondLandDetailMapper.deleteByPK(primaryKey);
    }

    /**
     * ���ݶ������ɾ����¼
     * @param primaryKeys
     */
    public void deleteByPKeys(List<java.lang.Integer> primaryKeys) {
        secondLandDetailMapper.deleteByPKeys(primaryKeys);
    }

    /**
     * ���ݴ������ɾ����¼
     * @param secondLandDetail
     * @return
     */
    public int deleteByProperty(SecondLandDetail secondLandDetail){
        return secondLandDetailMapper.deleteByProperty(secondLandDetail);
    }

    /**
     * �����¼
     * @param secondLandDetail
     * @return
     */
    public int save(SecondLandDetail secondLandDetail){
        return secondLandDetailMapper.save(secondLandDetail);
    }

    /**
     * ���¼�¼
     * @param secondLandDetail
     * @return
     */
    public int update(SecondLandDetail secondLandDetail){
        return secondLandDetailMapper.update(secondLandDetail);
    }

    /**
     * ����������ѯ��¼����
     * @param secondLandDetail
     * @return
     */
    public int findByCount(SecondLandDetail secondLandDetail){
        return secondLandDetailMapper.findByCount(secondLandDetail);
    }

    /**
     * ���ݲ�ѯ������ѯ��ҳ��¼
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
