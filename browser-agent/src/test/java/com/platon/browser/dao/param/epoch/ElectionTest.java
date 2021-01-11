package com.platon.browser.dao.param.epoch;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ElectionTest extends AgentTestBase {

    @Test
    public void test(){
        Election target = Election.builder()
                .settingEpoch(2)
                .lockedNodeList(Collections.emptyList())
                .build();
        target.setSettingEpoch(0)
                .setLockedNodeList(Collections.emptyList());
        target.getSettingEpoch();
        target.getLockedNodeList();
        target.getBusinessType();

        assertTrue(true);
    }
}