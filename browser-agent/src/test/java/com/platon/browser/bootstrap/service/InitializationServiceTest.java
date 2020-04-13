package com.platon.browser.bootstrap.service;

import com.github.pagehelper.Page;
import com.platon.browser.AgentTestBase;
import com.platon.browser.bootstrap.bean.InitializationResult;
import com.platon.browser.common.collection.dto.CollectionNetworkStat;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.ProposalCache;
import com.platon.browser.common.queue.gasestimate.publisher.GasEstimateEventPublisher;
import com.platon.browser.common.service.epoch.EpochRetryService;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.GasEstimateLog;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.service.misc.StakeMiscService;
import com.platon.sdk.contracts.ppos.dto.resp.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class InitializationServiceTest extends AgentTestBase {
    @Mock
    private EpochRetryService epochRetryService;
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private NodeMapper nodeMapper;
    @Mock
    private StakingMapper stakingMapper;
    @Mock
    private NetworkStatMapper networkStatMapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private NodeCache nodeCache;
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private AddressCache addressCache;
    @Mock
    private ProposalMapper proposalMapper;
    @Mock
    private ProposalCache proposalCache;
    @Mock
    private ParameterService parameterService;
    @Mock
    private GasEstimateLogMapper gasEstimateLogMapper;
    @Mock
    private GasEstimateEventPublisher gasEstimateEventPublisher;
    @Mock
    private StakeMiscService stakeMiscService;
    @Spy
    private InitializationService target;
    @Before
    public void setup() throws Exception {
        // 修改测试数据
        Node node = candidateList.get(0);
        node.setNodeName("node");
        node.setProgramVersion(BigInteger.valueOf(7988));
        node.setStakingBlockNum(BigInteger.valueOf(7988));
        Node node2 = candidateList.get(1);
        node2.setNodeName(null);
        CustomStaking staking = stakingList.get(0);
        staking.setNodeId(node.getNodeId());
        staking.setNodeName("node-name");

        ReflectionTestUtils.setField(target, "epochRetryService", epochRetryService);
        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(target, "nodeMapper", nodeMapper);
        ReflectionTestUtils.setField(target, "stakingMapper", stakingMapper);
        ReflectionTestUtils.setField(target, "networkStatMapper", networkStatMapper);
        ReflectionTestUtils.setField(target, "addressMapper", addressMapper);
        ReflectionTestUtils.setField(target, "nodeCache", nodeCache);
        ReflectionTestUtils.setField(target, "networkStatCache", networkStatCache);
        ReflectionTestUtils.setField(target, "addressCache", addressCache);
        ReflectionTestUtils.setField(target, "proposalMapper", proposalMapper);
        ReflectionTestUtils.setField(target, "proposalCache", proposalCache);
        ReflectionTestUtils.setField(target, "parameterService", parameterService);
        ReflectionTestUtils.setField(target, "gasEstimateLogMapper", gasEstimateLogMapper);
        ReflectionTestUtils.setField(target, "gasEstimateEventPublisher", gasEstimateEventPublisher);
        ReflectionTestUtils.setField(target,"stakeMiscService",stakeMiscService);
        when(epochRetryService.getPreValidators()).thenReturn(candidateList);
        when(epochRetryService.getPreVerifiers()).thenReturn(candidateList);
        when(epochRetryService.getCandidates()).thenReturn(candidateList);
        when(epochRetryService.getExpectBlockCount()).thenReturn(10L);
        when(chainConfig.getDefaultStakingList()).thenReturn(stakingList);
        when(chainConfig.getDefaultStakingLockedAmount()).thenReturn(BigDecimal.valueOf(100000000));
        when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.TEN);
        when(proposalMapper.selectByExample(any())).thenReturn(new ArrayList<>(proposalList));
        when(parameterService.getValueInBlockChainConfig(any())).thenReturn("5");
        List<GasEstimateLog> gasEstimateLogs = new ArrayList<>();
        GasEstimateLog gel = new GasEstimateLog();
        gel.setSeq(1l);
        gel.setJson("[]");
        gasEstimateLogs.add(gel);
        when(gasEstimateLogMapper.selectByExample(any())).thenReturn(gasEstimateLogs);
        when(stakeMiscService.getUnStakeFreeDuration()).thenReturn(BigInteger.TEN);
        when(stakeMiscService.getUnStakeEndBlock(anyString(),any(BigInteger.class),anyBoolean())).thenReturn(BigInteger.TEN);
    }

    @Test
    public void post() throws Exception {
        NetworkStat networkStat = null;
        when(networkStatMapper.selectByPrimaryKey(anyInt())).thenReturn(networkStat);
        InitializationResult result = target.init();
        assertEquals(-1L,result.getCollectedBlockNumber().longValue());

        networkStat = CollectionNetworkStat.newInstance();
        networkStat.setCurNumber(7000L);
        when(networkStatMapper.selectByPrimaryKey(anyInt())).thenReturn(networkStat);
        Page<com.platon.browser.dao.entity.Node> page = new Page<>();
        when(nodeMapper.selectByExample(any())).thenReturn(page);
        result = target.init();
        assertEquals(7000L,result.getCollectedBlockNumber().longValue());
    }

}
