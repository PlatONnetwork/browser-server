package com.platon.browser.exception;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/9/17
 * @Description: 结算周期切换异常测试类
 */
public class SettleEpochChangeExceptionTest {
    @Test
    public void exceptionTest() {
        try {
            throw new SettleEpochChangeException("结算周期切换异常");
        } catch (Exception e) {
            assertTrue(e instanceof SettleEpochChangeException);
        }
    }
}