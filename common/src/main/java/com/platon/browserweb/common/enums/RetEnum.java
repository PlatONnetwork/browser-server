package com.platon.browserweb.common.enums;

/**
 * Created by yanze on 2016/12/15.
 */
public enum RetEnum {

    // 业务错误码定义
    
    RET_SUCCESS(0,"成功"),
    RET_PARAM_VALLID(-1,"请求参数无效"),
    RET_SYS_EXCEPTION(-100,"系统异常"),
    RET_FAIL(1,"失败");

    private String name;
    private int code;

    private RetEnum ( int code, String name){
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static RetEnum getEnumByCodeValue(int code){
        RetEnum[] allEnums = values();
        for(RetEnum enableStatus : allEnums){
            if(enableStatus.getCode()==code)
                return enableStatus;
        }
        return null;
    }
}
