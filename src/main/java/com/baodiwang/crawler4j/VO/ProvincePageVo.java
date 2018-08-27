/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.VO.ProvincePageVo.java <2018年08月27日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.VO;

import java.util.ArrayList;
import java.util.List;

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

    public ProvincePageVo(String provinceName, Integer provinceCode) {
        this.provinceName = provinceName;
        this.provinceCode = provinceCode;
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
    
    public static List<ProvincePageVo> getAllProvince(){
        List<ProvincePageVo> provincePageVoList = new ArrayList<>();
        provincePageVoList.add(new ProvincePageVo("北京市",11));
        provincePageVoList.add(new ProvincePageVo("天津市", 12));
        provincePageVoList.add(new ProvincePageVo("河北省", 13));
        provincePageVoList.add(new ProvincePageVo("山西省", 14));
        provincePageVoList.add(new ProvincePageVo("内蒙古", 15));
        provincePageVoList.add(new ProvincePageVo("辽宁省", 21));
        provincePageVoList.add(new ProvincePageVo("吉林省",22));
        provincePageVoList.add(new ProvincePageVo("黑龙江省", 23));
        provincePageVoList.add(new ProvincePageVo("上海市", 31));
        provincePageVoList.add(new ProvincePageVo("江苏省", 32));
        provincePageVoList.add(new ProvincePageVo("浙江省", 33));
        provincePageVoList.add(new ProvincePageVo("安徽省", 34));
        provincePageVoList.add(new ProvincePageVo("福建省", 35));
        provincePageVoList.add(new ProvincePageVo("江西省", 36));
        provincePageVoList.add(new ProvincePageVo("山东省", 37));
        provincePageVoList.add(new ProvincePageVo("河南省", 41));
        provincePageVoList.add(new ProvincePageVo("湖北省", 42));
        provincePageVoList.add(new ProvincePageVo("湖南省", 43));
        provincePageVoList.add(new ProvincePageVo("广东省", 44));
        provincePageVoList.add(new ProvincePageVo("广西壮族", 45));
        provincePageVoList.add(new ProvincePageVo("海南省", 46));
        provincePageVoList.add(new ProvincePageVo("重庆市", 50));
        provincePageVoList.add(new ProvincePageVo("四川省", 51));
        provincePageVoList.add(new ProvincePageVo("贵州省", 52));
        provincePageVoList.add(new ProvincePageVo("云南省", 53));
        provincePageVoList.add(new ProvincePageVo("西藏", 54));
        provincePageVoList.add(new ProvincePageVo("陕西省", 61));
        provincePageVoList.add(new ProvincePageVo("甘肃省", 62));
        provincePageVoList.add(new ProvincePageVo("青海省", 63));
        provincePageVoList.add(new ProvincePageVo("宁夏回族", 64));
        provincePageVoList.add(new ProvincePageVo("新疆维吾尔", 65));
        provincePageVoList.add(new ProvincePageVo("新疆建设兵团", 66));
        return provincePageVoList;
    }
}