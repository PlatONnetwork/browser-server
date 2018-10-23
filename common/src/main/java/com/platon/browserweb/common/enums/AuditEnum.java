package com.platon.browserweb.common.enums;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2018/6/27
 * \* Time: 21:08
 * \
 */
public enum AuditEnum {
    //前端审核类型枚举
    UNAUDITED("1", "待处理"),
    AUDITED("2", "已处理"),

    //---------------------分割线哦兄弟---------------------------
    //后台数据库审核枚举
    PENDING("0","待审核"),
    PASS("1","审核通过"),
    FAIL("2","审核拒绝"),

    //---------------------分割线哦兄弟---------------------------
    REPEAT("REPEAT","重复审核，改用户已经处理")
    ;

    private String code;
    private String desc;

    private AuditEnum(String code, String desc){
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