package com.platon.browser.exception;

/**
 * 结算周期切换异常
 * @Auther: Chendongming
 * @Date: 2019/8/17 16:27
 * @Description:
 */
public class SettleEpochChangeException extends Exception {
    public SettleEpochChangeException(String msg){
        super(msg);
    }
}
