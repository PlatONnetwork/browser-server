package com.platon.browserweb.common.enums;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2018/6/27
 * \* Time: 14:53
 * \
 */
public enum StatisticsEnum {


    ACCOUNT("ACCOUNT","投资者账户总额"),
    REGISTER("REGISTER","投资者注册数"),
    ORDER_QTY("ORDER_QTY","投资者委托量"),
    TRADE_QTY("TRADE_QTY","成交量"),
    WITHDRAW_QTY("WITHDRAW_QTY","提币量"),
    DEPOSIT_QTY("DEPOSIT_QTY","充币量"),
    USER_ONLINE("USER_ONLINE","在线用户数"),
    USER_ACTIVE("USER_ACTIVE","活跃用户数"),
    ORDER_NUMBER("ORDER_NUMBER","委托数"),
    TRADE_NUMBER("TRADE_NUMBER","成交数"),
    WITHDRAW_NUMBER("WITHDRAW_NUMBER","提币数"),
    DEPOSIT_NUMBER("DEPOSIT_NUMBER","充币数")

    ;
    public String code;
    public String desc;

    StatisticsEnum ( String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}