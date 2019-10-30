package com.platon.browser.old.exception;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/9/17
 * @Description: 共识周期切换异常测试类
 */
public class ConsensusEpochChangeExceptionTest {
    @Test
    public void exceptionTest() {
        try {
            throw new ConsensusEpochChangeException("共识周期切换异常");
        } catch (Exception e) {
            assertTrue(e instanceof ConsensusEpochChangeException);
        }
    }
}