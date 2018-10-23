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
public enum ErrorCodeEnum {

    Default(-1, "系统错误"),
    PARAM_VALIT_ERROR(1, "请求参数错误"),
    SYSTEM_CONFIG_ERROR(2, "系统配置错误"),
    REPEAT_SUBMIT (3,"重复提交"),
    RECORD_NOT_EXIST(4,"记录不存在"),
    RECORD_DELETED(5,"该id记录已删除"),
    RECORD_EXIST(6,"用户已存在"),
    
    VERIFICATION_CODE_ERROR(100, "验证码错误"),
    VERIFICATION_CODE_LAPSE(101, "验证码已过期"),
    USER_LOCKED(102, "账号已被锁定"),
    USER_ACCOUNT_LOCKED(103, "账户已被锁定"),
    USER_NO_EXIST(104, "用户不存在"),
    USER_AUDITED(105, "用户已审核"),
	EXIST_CION_RISK(106, "已存在该币种风控记录！"),
	EFFECT_TIME_ERROR(107,"生效时间要大于当前时间"),
	EXIST_CION_FEE(108, "已存在该币种费率记录！"),
	NOT_MODIFIED_EXECUTING_TIME(109, "不能修改执行中费率记录！"),
	UNSET_TRADE_ZONE_ATTRIBUTE(110, "请先设置交易区属性！"),
	EXIST_LEVEL_RISK_DISCOUNT(111, "已存在该等级的风控折扣！"),
	EXIST_LEVEL_FEE_DISCOUNT(112, "已存在该等级的费率折扣！"),
	THIRD_PARTY_INTERFACE_ERROR(113, "第三方接口调用异常！"),
    QUOTE_CURRENCY_IS_ENABLE(121, "计价币启用中，无法禁用"),
    TRADE_ZONE_IS_ENABLE(115, "基准币启用中，无法禁用"),
    WITHDRAW_IS_ENABLE(116, "提币操作开启中，无法禁用"),
    CURRENCY_IS_ENABLE(117, "币种启用中，不可移除"),
    BROKER_TRADE_ZONE_IS_ENABLE(118, "交易操作开启中，无法禁用"),
    TRADE_FEE_NOT_SETTING(119, "费率未设置，无法开启"),
    FEE_DISCOUNT_OVER(120, "费率折扣超过限定值");
	
    public int code;
    public String desc;

    ErrorCodeEnum ( int code, String desc) {
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
