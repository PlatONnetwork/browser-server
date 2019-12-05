package com.platon.browser.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BlockChainConfigTest {
    @Test
    public void test() throws InvocationTargetException, IllegalAccessException {
        BlockChainConfig blockChainConfig = new BlockChainConfig();
        for(Method method:BlockChainConfig.class.getDeclaredMethods()){
            if(method.getName().contains("get")){
                method.invoke(blockChainConfig);
            }
        }
    }
}
