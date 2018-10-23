package com.platon.browserweb.common.enums;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2018/7/3
 * \* Time: 11:08
 * \
 */
public enum OperatingRecordEnum {
    INVESTOR("INVESTOR","投资者"),
    BROKER("BROKER","代理商"),
    NOTICE("NOTICE","公告"),
    CURRENCY("CURRENCY","数字货币"),
    TRADE_ZONE("TRADE_ZONE","交易区设置"),

    ;
    private String code;
    private String desc;

    private OperatingRecordEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static OperatingRecordEnum getEnumByCodeValue(String code){
        OperatingRecordEnum[] allEnums = values();
        for(OperatingRecordEnum e : allEnums){
            if(e.getCode().equals(code))
                return e;
        }
        return null;
    }
}