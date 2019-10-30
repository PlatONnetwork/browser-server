package com.platon.browser.old.exception;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/9/17
 * @Description: 缓存异常测试类
 */
public class CacheConstructExceptionTest {
    @Test
    public void exceptionTest() {
        try {
            throw new CacheConstructException("缓存异常");
        } catch (Exception e) {
            assertTrue(e instanceof CacheConstructException);
        }
    }
}