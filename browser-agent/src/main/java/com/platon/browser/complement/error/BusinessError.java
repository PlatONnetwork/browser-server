package com.platon.browser.complement.error;

/**
 * @description: 会导致应用崩溃的关键业务错误
 * @author: chendongming@juzix.net
 * @create: 2019-11-09 10:21:58
 **/
public class BusinessError extends Error {
    public BusinessError(String msg){
        super(msg);
    }
}
