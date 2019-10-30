package com.platon.browser.exception;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/9/17
 * @Description: 区块链内部异常测试类
 */
public class BlockChainExceptionTest {

    @Test
    public void exceptionTest() {
        try {
            throw new BlockChainException("区块链内部异常");
        } catch (Exception e) {
            assertTrue(e instanceof BlockChainException);
        }
    }
}