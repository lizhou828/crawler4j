/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.mapper;


import com.baodiwang.crawler4j.mapper.GenericIBatisMapper;
import com.baodiwang.crawler4j.model.SecondLand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SecondLandMapper extends GenericIBatisMapper<SecondLand, Integer> {

    List<SecondLand> findWithoutContent(@Param("startId")Integer startId, @Param("endId") Integer endId);

    List<SecondLand> findWithId(@Param("startId") Integer startId, @Param("endId") Integer endId);
}