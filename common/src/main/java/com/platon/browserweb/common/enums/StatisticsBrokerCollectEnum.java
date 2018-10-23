/*
 * Copyright (c) 2018. juzix.io. All rights reserved.
 */

package com.platon.browserweb.common.enums;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/7/19
 */
public enum StatisticsBrokerCollectEnum {
    REGISTER("今日新增用户数"),
    ORDER_QTY("今日委托量"),
    TRADE_QTY("今日成交量"),
    WITHDRAW_QTY("今日充币量"),
    DEPOSIT_QTY("今日提币量"),
    ORDER_TOTAL("委托总量"),
    TRADE_TOTAL("成交总量"),
    WITHDRAW_TOTAL("提币总量"),
    DEPOSIT_TOTAL("充币总量"),
    ;

    private String desc;

    private StatisticsBrokerCollectEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
