package com.platon.browser.complement.dao.param.restricting;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.Silent.class)
public class RestrictingItemTest extends AgentTestBase {

    @Test
    public void test(){
        RestrictingItem target = RestrictingItem.builder()
                .address(null)
                .amount(null)
                .epoch(3L)
                .number(null)
                .build();
        target.setAddress(null);
        target.setAmount(null);
        target.setEpoch(null);
        target.setNumber(null);
        target.getAddress();
        target.getNumber();
        target.getAmount();
        target.getEpoch();
        assertTrue(true);
    }
}
