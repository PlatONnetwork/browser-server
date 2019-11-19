package com.platon.browser.common.complement.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.dao.entity.NetworkStat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.when;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class NetworkStatCacheTest extends AgentTestBase {
    @Mock
    private TpsCalcCache tpsCalcCache;
    @Spy
    private NetworkStatCache target;
    NetworkStat networkStat = new NetworkStat();
    @Before
    public void setup() {

        networkStat.setCurNumber(33L);
        networkStat.setAddressQty(33);
        networkStat.setId(33);
        networkStat.setAddIssueBegin(33L);
        networkStat.setAddIssueEnd(33L);
        networkStat.setBlockReward(BigDecimal.ZERO);
        networkStat.setCreateTime(new Date());
        networkStat.setCurBlockHash("");
        networkStat.setTxQty(55);
        networkStat.setProposalQty(55);
        networkStat.setCurTps(55);
        networkStat.setMaxTps(55);
        networkStat.setNodeOptSeq(55L);
        ReflectionTestUtils.setField(target,"networkStat",networkStat);
        ReflectionTestUtils.setField(target,"tpsCalcCache",tpsCalcCache);
        when(tpsCalcCache.getTps()).thenReturn(90);
    }

    @Test
    public void test(){
        target.updateByBlock(33,33,new Date(),"0x2323");
        target.getAndIncrementNodeOptSeq();
        target.getNetworkStat();
        target.setNetworkStat(networkStat);
        target.setTpsCalcCache(tpsCalcCache);
        target.init(networkStat);
        target.updateByTask(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,33,33);
    }
}
