/*
 * Copyright (c) 2018. juzhen.io. All rights reserved.
 */

package com.platon.browserweb.common.enums;

public enum ValidCodeSendModeEnum {

    MOBILE(1, "mobile", "手机验证码"),
    EMAIL(2, "email", "邮箱验证码");

    private int type;
    private String code;
    private String desc;

    private ValidCodeSendModeEnum ( int type, String code, String desc) {
        this.type = type;
        this.code = code;
        this.desc = desc;

    }

    public int getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ValidCodeSendModeEnum getEnumByCodeValue(String code) {
        ValidCodeSendModeEnum[] allEnums = values();
        for (ValidCodeSendModeEnum e : allEnums) {
            if (e.getCode().equals(code))
                return e;
        }
        return null;
    }

}
