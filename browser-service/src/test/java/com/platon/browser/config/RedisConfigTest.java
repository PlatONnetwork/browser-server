package com.platon.browser.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RedisConfigTest {
    @Test
    public void test() throws InvocationTargetException, IllegalAccessException {
        RedisConfig config = new RedisConfig();
        for(Method method:RedisConfig.class.getDeclaredMethods()){
            if(method.getName().contains("get")){
                method.invoke(config);
            }
            if(method.getName().contains("set")){
                method.invoke(config,"9999");
            }
        }
    }
}
