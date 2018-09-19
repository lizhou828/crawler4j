/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.service;

import com.baodiwang.crawler4j.model.SecondLand;

import java.util.List;

public interface SecondLandService extends GenericIService<SecondLand,Integer>{

    List<SecondLand> findWithoutContent(Integer startId, Integer endId);

    List<SecondLand> findWithId(Integer startId, Integer endId);
}
