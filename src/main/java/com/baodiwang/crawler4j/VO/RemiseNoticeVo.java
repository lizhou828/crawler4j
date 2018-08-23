/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.VO.RemiseNoticeVo.java <2018年08月23日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.VO;

import com.baodiwang.crawler4j.model.RemiseNotice;
import com.baodiwang.crawler4j.model.RemiseNoticeDetail;

import java.util.List;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月23日 11时31分
 */
public class RemiseNoticeVo {
    private RemiseNotice remiseNotice;
    private List<RemiseNoticeDetail> remiseNoticeDetailList;

    public RemiseNotice getRemiseNotice() {
        return remiseNotice;
    }

    public void setRemiseNotice(RemiseNotice remiseNotice) {
        this.remiseNotice = remiseNotice;
    }

    public List<RemiseNoticeDetail> getRemiseNoticeDetailList() {
        return remiseNoticeDetailList;
    }

    public void setRemiseNoticeDetailList(List<RemiseNoticeDetail> remiseNoticeDetailList) {
        this.remiseNoticeDetailList = remiseNoticeDetailList;
    }

    @Override
    public String toString() {
        return "RemiseNoticeVo{" +
                "remiseNotice=" + remiseNotice +
                ", remiseNoticeDetailList=" + remiseNoticeDetailList +
                '}';
    }
}
