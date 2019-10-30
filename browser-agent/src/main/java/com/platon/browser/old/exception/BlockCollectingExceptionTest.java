package com.platon.browser.old.exception;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/9/17
 * @Description: 区块搜集异常测试类
 */
public class BlockCollectingExceptionTest {
    @Test
    public void exceptionTest() {
        try {
            throw new BlockCollectingException("区块搜集异常");
        } catch (Exception e) {
            assertTrue(e instanceof BlockCollectingException);
        }
    }
}