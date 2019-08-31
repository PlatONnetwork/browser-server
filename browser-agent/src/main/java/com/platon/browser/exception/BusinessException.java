package com.platon.browser.exception;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 业务异常
 */
public class BusinessException extends Exception {
    public BusinessException(String msg){
        super(msg);
    }
}