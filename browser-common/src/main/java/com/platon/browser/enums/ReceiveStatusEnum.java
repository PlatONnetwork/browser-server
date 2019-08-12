package com.platon.browser.enums;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/12 19:53
 * @Description:
 */
public enum ReceiveStatusEnum {
    FAILURE(0, "失败"),
    SUCCESS(1, "成功")
    ;

    public int code;
    public String desc;

    ReceiveStatusEnum(int code, String desc) {
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
