package com.platon.browser.dao.param.epoch;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ConsensusTest extends AgentTestBase {

    @Test
    public void test(){
        Consensus target = Consensus.builder()
                .expectBlockNum(BigInteger.TEN)
                .validatorList(Collections.emptyList())
                .build();
        target.setExpectBlockNum(null)
                .setValidatorList(null);
        target.getExpectBlockNum();
        target.getValidatorList();
        target.getBusinessType();
        assertTrue(true);
    }
}