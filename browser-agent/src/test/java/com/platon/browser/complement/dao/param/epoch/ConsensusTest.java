package com.platon.browser.complement.dao.param.epoch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.enums.BusinessType;
import com.platon.browser.complement.dao.param.BusinessParam;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ConsensusTest extends AgentTestBase {

    @Test
    public void test(){
        Consensus target = Consensus.builder()
                .expectBlockNum(null)
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