package com.platon.browser.exception;

/**
 * 选举周期切换异常
 * @Auther: Chendongming
 * @Date: 2019/8/17 16:27
 * @Description:
 */
public class ElectionEpochChangeException extends Exception {
    public ElectionEpochChangeException(String msg){
        super(msg);
    }
}
