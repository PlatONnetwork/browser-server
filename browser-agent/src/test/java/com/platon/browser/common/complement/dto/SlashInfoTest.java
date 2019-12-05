package com.platon.browser.common.complement.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/4 13:53
 * @Description: 处罚信息Bean
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class SlashInfoTest {
    @Test
    public void test () {
        SlashInfo slash = SlashInfo.builder()
                .slashTime(new Date())
                .blockCount(BigInteger.ONE)
                .blockNumber(BigInteger.ONE)
                .kickOut(false)
                .slashAmount(BigDecimal.TEN)
                .slashBlockCount(BigInteger.ONE)
                .build();
    }
}
