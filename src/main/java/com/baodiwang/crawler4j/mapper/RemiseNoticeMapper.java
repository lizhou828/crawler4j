/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.mapper;


import com.baodiwang.crawler4j.model.RemiseNotice;

import java.util.List;

public interface RemiseNoticeMapper extends GenericIBatisMapper <RemiseNotice, Integer>{

    public List<RemiseNotice> findNoticeWithoutDetail();

}