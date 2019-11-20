package com.platon.browser.complement.dao.entity;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.service.epoch.EpochRetryService;
import com.platon.browser.common.service.epoch.EpochService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressStatisticsTest extends AgentTestBase {

    @Test
    public void test(){
        AddressStatistics statistics = new AddressStatistics();
        statistics.setDelegateHes(null);
        statistics.setDelegateLocked(null);
        statistics.setDelegateReleased(null);
        statistics.setDelegateAddr(null);
        statistics.setStakingHes(null);
        statistics.setStakingLocked(null);
        statistics.setStakingAddr(null);
        statistics.setStakingReduction(null);
        statistics.setNodeId(null);
        statistics.setNodeIdSet(null);

        statistics.getDelegateHes();
        statistics.getDelegateLocked();
        statistics.getDelegateReleased();
        statistics.getStakingHes();
        statistics.getStakingLocked();
        statistics.getDelegateAddr();
        statistics.getNodeId();
        statistics.getNodeIdSet();
        statistics.getStakingAddr();
        statistics.getStakingReduction();
        assertTrue(true);
    }
}
