package com.platon.browserweb.common.enums;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2018/6/29
 * \* Time: 17:25
 * \
 */
public enum UserTypeEnum {
    INVESTOR("INVESTOR","投资者"),
    BROKER("BROKER","代理商"),
    EXCHANGE("EXCHANGE","交易所"),

    ;
    private String code;
    private String desc;

    private UserTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static UserTypeEnum getEnumByCodeValue(String code){
        UserTypeEnum[] allEnums = values();
        for(UserTypeEnum e : allEnums){
            if(e.getCode().equals(code))
                return e;
        }
        return null;
    }
}