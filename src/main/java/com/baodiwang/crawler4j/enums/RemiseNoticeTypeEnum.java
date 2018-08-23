package com.baodiwang.crawler4j.enums;

import com.baodiwang.crawler4j.utils.StringUtils;

/**
 * 土地出让公告类型枚举类
 * Created by lizhou on 2018年08月22日 22时12分
 */
public enum  RemiseNoticeTypeEnum {
    BID(1,"招标"),
    AUCTION(2,"拍卖"),
    LIST_EXCHANGE(3,"挂牌"),
    PUBLIC_NOTICE(4,"公开公告"),
    NOTICE_MODIFY(5,"公告调整"),
    OTHER_NOTICE(6,"其他公告");

    private int code;
    private String name;

    RemiseNoticeTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(int code) {
        for (RemiseNoticeTypeEnum enumObj : RemiseNoticeTypeEnum.values()) {
            if (enumObj.code == code )
                return enumObj.name;
        }
        return null;
    }
    public static int getCodeByName(String name) {
        for (RemiseNoticeTypeEnum enumObj : RemiseNoticeTypeEnum.values()) {
            if (StringUtils.isNotEmpty(name) && name.contains(enumObj.getName())){
                return enumObj.code;
            }
        }
        return 0;
    }
    public static RemiseNoticeTypeEnum getByCode(int code) {
        for (RemiseNoticeTypeEnum enumObj : RemiseNoticeTypeEnum.values()) {
            if (enumObj.code == code )
                return enumObj;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getCodeByName("挂牌"));
    }

}