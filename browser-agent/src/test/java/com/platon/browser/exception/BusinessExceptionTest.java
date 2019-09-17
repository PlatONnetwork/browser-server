package com.platon.browser.exception;

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
            throw new BusinessException("业务异常");
        } catch (Exception e){
            assertTrue(e instanceof BusinessException);
        }
    }
}