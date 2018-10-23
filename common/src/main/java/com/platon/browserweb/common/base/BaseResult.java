package com.platon.browserweb.common.base;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browserweb.common.enums.RetEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResult<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseResult.class);

    private int ret ;
    private T data;
    private String message;

    public BaseResult () {
    }

    public BaseResult ( int errorCode, T data) {
        this.data = data;
        this.ret = errorCode;
        this.printResponse();
    }

    public BaseResult ( int errorCode, String errorMsg) {
        this.ret = errorCode;
        this.message = errorMsg;
        this.printResponse();
    }

    public BaseResult ( RetEnum retEnum, String errorMmsg){
        this.ret = retEnum.getCode();
        this.message = errorMmsg;
        this.printResponse();
    }

    public BaseResult ( RetEnum retEnum, T data){
        this.ret = retEnum.getCode();
        this.data = data;
        this.printResponse();
    }

    public BaseResult ( RetEnum retEnum){
        this.ret = retEnum.getCode();
        this.message = retEnum.getName();
        this.printResponse();
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void printResponse(){
        logger.info("【响应报文】：" + JSON.toJSONString(this));
    }

}
