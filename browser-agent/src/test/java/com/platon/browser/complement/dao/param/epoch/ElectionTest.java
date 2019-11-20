package com.platon.browser.complement.dao.param.epoch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.enums.BusinessType;
import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.dao.entity.Staking;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ElectionTest extends AgentTestBase {

    @Test
    public void test(){
        Election target = Election.builder()
                .settingEpoch(2)
                .slashNodeList(Collections.emptyList())
                .time(new Date())
                .build();
        target.setSettingEpoch(0)
                .setSlashNodeList(Collections.emptyList())
                .setTime(new Date());
        target.getSettingEpoch();
        target.getSlashNodeList();
        target.getTime();
        target.getBusinessType();

        assertTrue(true);
    }
}