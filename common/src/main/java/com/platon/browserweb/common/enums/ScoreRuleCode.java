/*
 * Copyright (c) 2018. juzix.io. All rights reserved.
 */

package com.platon.browserweb.common.enums;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/8/15
 */
public enum ScoreRuleCode {
    TRADE_AMOUNT("交易金额");

    private String desc;

    ScoreRuleCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
