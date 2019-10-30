package com.platon.browser.old.exception;

import com.platon.browser.exception.BusinessException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/9/17
 * @Description: 业务异常测试类
 */
public class BusinessExceptionTest {
    @Test
    public void exceptionTest(){
        try {
            throw new com.platon.browser.exception.BusinessException("业务异常");
        } catch (Exception e){
            assertTrue(e instanceof BusinessException);
        }
    }
}