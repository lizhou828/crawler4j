/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.service;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.List;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2017年06月08日 15时42分
 */
public interface GenericIService<T, PK extends Serializable> {
    /**
     * 通过主键查询实体对象
     *
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public T getByPK(PK primaryKey);

    /**
     * 查询所有记录
     * @return
     * @throws Exception
     */
    public List<T> list();

    /**
     * 根据查询条件查询所有记录
     * @return
     * @throws Exception
     */
    public List<T> listByProperty(T t);


    /**
     * 根据主键删除记录
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public int deleteByPK(PK primaryKey) ;

    /**
     * 根据多个主键删除记录
     * @param primaryKeys
     * @throws Exception
     */
    public void deleteByPKeys(List<PK> primaryKeys);

    /**
     * 根据传入参数删除记录
     * @param t
     * @return
     * @throws Exception
     */
    public int deleteByProperty(T t);

    /**
     * 保存记录
     * @param t
     * @return
     * @throws Exception
     */
    public int save(T t) ;

    /**
     * 更新记录
     * @param t
     * @return
     * @throws Exception
     */
    public int update(T t);

    /**
     * 根据条件查询记录条数
     * @param t
     * @return
     * @throws Exception
     */
    public int findByCount(T t);

    /**
     * 根据查询条件查询分页记录
     * @return
     * @throws Exception
     */
    public Page<T> findByPage(Page<T> page, T t);

    public int batchInsert(List<T> list);
}
