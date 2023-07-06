package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/4 13:53
 * @Description: 处罚信息Bean
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class SlashInfoTest {
    @Test
    public void test () {
        SlashInfo slash = new SlashInfo();
        slash.setSlashTime(new Date());
        slash.setBlockCount(BigInteger.ONE);
        slash.setBlockNumber(BigInteger.ONE);
        slash.setKickOut(false);
        slash.setSlashAmount(BigDecimal.TEN);
        slash.setSlashBlockCount(BigInteger.ONE);
        assertTrue(true);
    }
}
