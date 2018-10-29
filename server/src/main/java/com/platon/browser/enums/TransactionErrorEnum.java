/*
 * Copyright (c) 2018. juzhen.io. All rights reserved.
 */

package com.platon.browser.enums;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/4/13
 */
public enum TransactionErrorEnum {

    NOT_EXIST("交易不存在！"),
    DUPLICATE("交易重复！");

    public String desc;

    TransactionErrorEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
