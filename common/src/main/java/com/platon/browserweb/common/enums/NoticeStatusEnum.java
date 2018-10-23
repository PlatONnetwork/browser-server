/*
 * Copyright (c) 2018. juzhen.io. All rights reserved.
 */

package com.platon.browserweb.common.enums;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/4/13
 */
public enum NoticeStatusEnum {

    ALL("0", "全部"),
    NO_PUBLISH("1", "未发布"),
    PUBLISH("2", "已发布"),
    DELETE("3", "已删除(前端已过期)")
    ;

    private String code;
    private String desc;

    NoticeStatusEnum ( String code, String desc) {
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
