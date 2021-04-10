package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/4 13:53
 * @Description: 处罚信息Bean
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ComplementInfoTest {
    @Test
    public void test() {
        ComplementInfo complementInfo = new ComplementInfo();
        complementInfo.setToType(0);
        complementInfo.setContractType(0);
        complementInfo.setType(0);
        complementInfo.setBinCode("");
        complementInfo.setInfo("");
        complementInfo.setMethod("");
        assertSame(complementInfo.getToType(), 0);
        assertSame(complementInfo.getType(), 0);
        assertSame(complementInfo.getContractType(), 0);
        assertEquals(complementInfo.getBinCode(), "");
        assertEquals(complementInfo.getInfo(), "");
        assertEquals(complementInfo.getMethod(), "");
        complementInfo.toString();
        complementInfo.hashCode();
        assertTrue(complementInfo.equals(complementInfo));
    }
}
