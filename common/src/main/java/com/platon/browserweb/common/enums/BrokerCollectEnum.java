package com.platon.browserweb.common.enums;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2018/6/29
 * \* Time: 10:21
 * \
 */
public enum BrokerCollectEnum {
    REGISTER("REGISTER","今日新增用户数"),
    ORDER_QTY("ORDER_QTY","今日委托量"),
    TRADE_QTY("TRADE_QTY","今日成交量"),
    WITHDRAW_QTY("WITHDRAW_QTY","今日充币量"),
    DEPOSIT_QTY("DEPOSIT_QTY","今日提币量"),
    ;
    private String code;
    private String desc;

    private BrokerCollectEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static BrokerCollectEnum getEnumByCodeValue(String code){
        BrokerCollectEnum[] allEnums = values();
        for(BrokerCollectEnum e : allEnums){
            if(e.getCode().equals(code))
                return e;
        }
        return null;
    }

}