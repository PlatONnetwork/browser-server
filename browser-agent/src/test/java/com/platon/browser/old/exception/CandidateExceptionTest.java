package com.platon.browser.old.exception;

import com.platon.browser.old.exception.CandidateException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/9/17
 * @Description: 验证人异常测试类
 */
public class CandidateExceptionTest {
    @Test
    public void exceptionTest() {
        try {
            throw new CandidateException("验证人异常");
        } catch (Exception e) {
            assertTrue(e instanceof CandidateException);
        }
    }
}