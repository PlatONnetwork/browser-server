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
public enum BlockErrorEnum {

    NOT_EXIST("区块不存在！"),
    DUPLICATE("区块重复！");

    public String desc;

    BlockErrorEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
