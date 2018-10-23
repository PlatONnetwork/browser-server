/*
 * Copyright (c) 2018. juzhen.io. All rights reserved.
 */

package com.platon.browserweb.common.enums;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/6/7
 */
public enum BizCode {
    DEPOSIT("充币"),
    WITHDRAW("提币"),
    BID("买"),
    ASK("卖"),
    TRADE("交易");

    private String desc;

    BizCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
