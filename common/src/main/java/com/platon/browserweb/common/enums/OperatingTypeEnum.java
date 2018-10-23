package com.platon.browserweb.common.enums;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2018/7/3
 * \* Time: 11:10
 * \
 */
public enum OperatingTypeEnum {
    ADD("ADD","增加"),
    EDIT("EDIT","编辑"),
    DETELE("DETELE","删除"),
    ACCOUNTLOCKED("ACCOUNTLOCKED","账户冻结"),
    ACCOUNTUNLOCK("ACCOUNTUNLOCK","账户解冻"),
    USERLOCKED("USERLOCKED","用户冻结"),
    USERUNLOCK("USERUNLOCK","用户解冻"),
    AUTHZ("AUTHZ","授权"),
    PUBLISH("PUBLISH","发布"),
    SET_QUOTE("SET_QUOTE","设置为计价货币"),
    UNSET_QUOTE("UNSET_QUOTE","设置为非计价货币"),
    SET_BASE("SET_BASE","设置为基准货币"),
    UNSET_BASE("UNSET_BASE","设置为非基准货币"),

    ;
    private String code;
    private String desc;

    private OperatingTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static OperatingTypeEnum getEnumByCodeValue(String code){
        OperatingTypeEnum[] allEnums = values();
        for(OperatingTypeEnum e : allEnums){
            if(e.getCode().equals(code))
                return e;
        }
        return null;
    }
}