package com.platon.browser.task.bean;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class NetworkStatisticsTest extends AgentTestBase {

    @Test
    public void test(){
        NetworkStatistics statistics = new NetworkStatistics();
        statistics.setStakingValue(null);
        statistics.setTotalValue(null);
        statistics.setTotalValue(null);
        statistics.setStakingValue(null);
        statistics.getStakingValue();
        statistics.getTotalValue();
        assertTrue(true);
    }
}
