/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.baodiwang.crawler4j.mapper.SecondLandMapper;
import com.baodiwang.crawler4j.model.SecondLand;
import java.util.List;

import com.baodiwang.crawler4j.service.SecondLandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service("secondLandService")
public class SecondLandServiceImpl extends GenericService<SecondLand, Integer> implements SecondLandService {

    private SecondLandMapper secondLandMapper;

    @Autowired
    public void setSecondLandMapper(SecondLandMapper secondLandMapper) {
        this.secondLandMapper = secondLandMapper;
    }

    /**
     * ͨ��������ѯʵ�����
     * @param primaryKey
     * @return
     */
    public SecondLand getByPK(java.lang.Integer primaryKey) {
        return secondLandMapper.getByPK(primaryKey);
    }

    /**
     * ��ѯ���м�¼
     * @return
     */
    public List<SecondLand> list() {
        return secondLandMapper.list();
    }

    /**
     * ���ݲ�ѯ������ѯ���м�¼
     * @return
     */
    public List<SecondLand> listByProperty(SecondLand secondLand){
        return secondLandMapper.listByProperty(secondLand);
    }


    /**
     * ��������ɾ����¼
     * @param primaryKey
     * @return
     */
    public int deleteByPK(java.lang.Integer primaryKey) {
        return secondLandMapper.deleteByPK(primaryKey);
    }

    /**
     * ���ݶ������ɾ����¼
     * @param primaryKeys
     */
    public void deleteByPKeys(List<java.lang.Integer> primaryKeys) {
        secondLandMapper.deleteByPKeys(primaryKeys);
    }

    /**
     * ���ݴ������ɾ����¼
     * @param secondLand
     * @return
     */
    public int deleteByProperty(SecondLand secondLand){
        return secondLandMapper.deleteByProperty(secondLand);
    }

    /**
     * �����¼
     * @param secondLand
     * @return
     */
    public int save(SecondLand secondLand){
        return secondLandMapper.save(secondLand);
    }

    /**
     * ���¼�¼
     * @param secondLand
     * @return
     */
    public int update(SecondLand secondLand){
        return secondLandMapper.update(secondLand);
    }

    /**
     * ����������ѯ��¼����
     * @param secondLand
     * @return
     */
    public int findByCount(SecondLand secondLand){
        return secondLandMapper.findByCount(secondLand);
    }

    /**
     * ���ݲ�ѯ������ѯ��ҳ��¼
     * @return
     */
    @Override
    public Page<SecondLand> findByPage(Page<SecondLand> page, SecondLand secondLand) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<SecondLand> secondLandList = secondLandMapper.listByProperty(secondLand);
        if(null == secondLandList || secondLandList.size() == 0){
            return new Page<SecondLand>();
        }
        return (Page<SecondLand>)secondLandList;
    }

    @Override
    public List<SecondLand> findWithoutContent(Integer startId, Integer endId) {
        return secondLandMapper.findWithoutContent(startId, endId);
    }

    @Override
    public List<SecondLand> findWithId(Integer startId, Integer endId) {
        return secondLandMapper.findWithId(startId, endId);
    }

    @Override
    public Integer findMaxId() {
        return secondLandMapper.findMaxId();
    }

    @Override
    public Integer findMinId() {
        return secondLandMapper.findMinId();
    }
}
