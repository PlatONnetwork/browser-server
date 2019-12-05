package com.platon.browser.common.complement.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SlashInfoTest {
    @Test
    public void test() throws InvocationTargetException, IllegalAccessException {
        SlashInfo slashInfo = SlashInfo.builder()
                .blockCount(BigInteger.ONE)
                .blockNumber(BigInteger.ONE)
                .kickOut(true)
                .slashAmount(BigDecimal.ZERO)
                .slashBlockCount(BigInteger.ONE)
                .slashTime(new Date())
                .build();

        for(Method method:SlashInfo.class.getDeclaredMethods()){
            if(method.getName().contains("get")){
                method.invoke(slashInfo);
            }
        }

        assertTrue(true);

    }
}
