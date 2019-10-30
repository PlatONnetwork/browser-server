package com.platon.browser.old.exception;

import com.platon.browser.old.exception.ElectionEpochChangeException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/9/17
 * @Description: 选举周期切换异常测试类
 */
public class ElectionEpochChangeExceptionTest {
    @Test
    public void exceptionTest() {
        try {
            throw new ElectionEpochChangeException("选举周期切换异常");
        } catch (Exception e) {
            assertTrue(e instanceof ElectionEpochChangeException);
        }
    }
}