/*
 * Copyright (c) 2018. juzhen.io. All rights reserved.
 */

package com.platon.browser.enums;

/**
 * 错误枚举统计
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/4/13
 */
public enum ErrorCodeEnum {

    DEFAULT(-1, "系统错误"),
    PARAM_VALID_ERROR(1, "请求参数错误"),
    SYSTEM_CONFIG_ERROR(2, "系统配置错误"),
    REPEAT_SUBMIT (3,"重复提交"),
    RECORD_NOT_EXIST(4,"记录不存在"),
    RECORD_DELETED(5,"该id记录已删除"),
    BLOCKCHAIN_ERROR(6,"同步链信息异常"),
    TX_ERROR(7,"同步交易信息异常"),
    PENDINGTX_ERROR(8,"同步pending交易异常"),
    PENDINGTX_REPEAT(9,"pengding交易更新异常"),
    NODE_ERROR(10,"同步节点信息异常"),
    STOMP_ERROR(11,"推送统计信息异常");


	
    private int code;
    private String desc;

    ErrorCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
