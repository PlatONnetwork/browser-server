package com.platon.browser.old.exception;

import com.platon.browser.old.exception.IssueEpochChangeException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/9/17
 * @Description: 增发周期切换异常测试类
 */
public class IssueEpochChangeExceptionTest {
    @Test
    public void exceptionTest() {
        try {
            throw new IssueEpochChangeException("增发周期切换异常");
        } catch (Exception e) {
            assertTrue(e instanceof IssueEpochChangeException);
        }
    }

}