/*
 * Powered By [Frank-Liz-Lee]
 * Copyright(C) 2012-2017 Liz Company
 * All rights reserved.
 * -----------------------------------------------
 */

package com.baodiwang.crawler4j.service;

import com.baodiwang.crawler4j.model.SecondLandDetail;

public interface SecondLandDetailService extends GenericIService<SecondLandDetail,Integer>{

    public String getContactsPhone(String content,String href);
}
