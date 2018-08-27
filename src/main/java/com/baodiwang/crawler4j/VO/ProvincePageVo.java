/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.VO.ProvincePageVo.java <2018年08月27日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.VO;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月27日 15时26分
 */
public class ProvincePageVo{
    /* 省名 */
    private  String provinceName;

    /* 省代码 */
    private Integer provinceCode;


    /* 本省今年（2018年）的所有分页条数（每页30条数据） */
    private  Integer provinceAllPage;

    public ProvincePageVo() {
    }

    public ProvincePageVo(String provinceName, Integer provinceCode, Integer provinceAllPage) {
        this.provinceName = provinceName;
        this.provinceCode = provinceCode;
        this.provinceAllPage = provinceAllPage;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Integer getProvinceAllPage() {
        return provinceAllPage;
    }

    public void setProvinceAllPage(Integer provinceAllPage) {
        this.provinceAllPage = provinceAllPage;
    }

    @Override
    public String toString() {
        return "ProvincePageVo{" +
                "provinceName='" + provinceName + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", provinceAllPage='" + provinceAllPage + '\'' +
                '}';
    }
}