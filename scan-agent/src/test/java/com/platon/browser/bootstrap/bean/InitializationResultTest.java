package com.platon.browser.bootstrap.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @description: 初始化结果
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-06 10:28:48
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class InitializationResultTest {

    @Test
    public void test(){
        InitializationResult initializationResult = new InitializationResult();
        initializationResult.setCollectedBlockNumber(100L);
        initializationResult.getCollectedBlockNumber();
    }
}
