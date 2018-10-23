package com.platon.browserweb.common.enums;

/**
 * 对账状态
 * @author huangjinfeng
 *
 */
public enum CheckStatusEnum {
    FIT(1,"平"),
    UNFIT(2,"不平"),
    PAYMENT_NO_RECORD(3,"海龟无记录"),
    EXCHANGE_NO_RECORD(4,"海马无记录");
	
	
	
    private Integer code;
    private String desc;

    private CheckStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static CheckStatusEnum getEnumByCodeValue(Integer code){
        CheckStatusEnum[] allEnums = values();
        for(CheckStatusEnum e : allEnums){
            if(e.getCode().equals(code))
                return e;
        }
        return null;
    }
}