package com.platon.browser.bootstrap.service;

import com.github.pagehelper.Page;
import com.platon.browser.AgentTestBase;
import com.platon.browser.bootstrap.bean.InitializationResult;
import com.platon.browser.bean.CollectionNetworkStat;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.cache.ProposalCache;
import com.platon.browser.publisher.GasEstimateEventPublisher;
import com.platon.browser.service.elasticsearch.*;
import com.platon.browser.service.epoch.EpochRetryService;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.GasEstimateLog;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.bean.CustomStaking;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.service.ppos.StakeEpochService;
import com.platon.browser.utils.CommonUtil;
import com.platon.browser.v0152.analyzer.ErcCache;
import com.platon.contracts.ppos.dto.resp.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@matrixelements.com
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
    private StakeEpochService stakeEpochService;

    @InjectMocks
    @Spy
    private InitializationService target;

    @Mock
    private ErcCache ercCache;

    @Mock
    private TokenMapper tokenMapper;

    @Mock
    private EsBlockRepository ESBlockRepository;

    @Mock
    private EsTransactionRepository ESTransactionRepository;

    @Mock
    private EsDelegationRewardRepository ESDelegationRewardRepository;

    @Mock
    private EsNodeOptRepository ESNodeOptRepository;

    @Mock
    private EsErc20TxRepository esErc20TxRepository;

    @Mock
    private EsErc721TxRepository esErc721TxRepository;

    @Mock
    private EsTransferTxRepository esTransferTxRepository;

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

        when(epochRetryService.getPreValidators()).thenReturn(candidateList);
        when(epochRetryService.getPreVerifiers()).thenReturn(candidateList);
        when(epochRetryService.getCandidates()).thenReturn(candidateList);
        when(epochRetryService.getExpectBlockCount()).thenReturn(10L);
        when(chainConfig.getDefaultStakingList()).thenReturn(stakingList);
        when(chainConfig.getDefaultStakingLockedAmount()).thenReturn(BigDecimal.valueOf(100000000));
        when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.TEN);
        when(chainConfig.getAddIssueRate()).thenReturn(BigDecimal.TEN);
        when(proposalMapper.selectByExample(any())).thenReturn(new ArrayList<>(proposalList));
        when(parameterService.getValueInBlockChainConfig(any())).thenReturn("5");
        List<GasEstimateLog> gasEstimateLogs = new ArrayList<>();
        GasEstimateLog gel = new GasEstimateLog();
        gel.setSeq(1l);
        gel.setJson("[]");
        gasEstimateLogs.add(gel);
        when(gasEstimateLogMapper.selectByExample(any())).thenReturn(gasEstimateLogs);
        when(stakeEpochService.getUnStakeFreeDuration()).thenReturn(BigInteger.TEN);
        when(stakeEpochService.getUnStakeEndBlock(anyString(), any(BigInteger.class), anyBoolean())).thenReturn(BigInteger.TEN);
    }

    @Test
    public void post() throws Exception {
        NetworkStat networkStat = null;
        when(networkStatMapper.selectByPrimaryKey(anyInt())).thenReturn(networkStat);
        InitializationResult result = target.init(CommonUtil.createTraceId());
        assertEquals(-1L, result.getCollectedBlockNumber().longValue());

        networkStat = CollectionNetworkStat.newInstance();
        networkStat.setCurNumber(7000L);
        when(networkStatMapper.selectByPrimaryKey(anyInt())).thenReturn(networkStat);
        Page<com.platon.browser.dao.entity.Node> page = new Page<>();
        when(nodeMapper.selectByExample(any())).thenReturn(page);
        result = target.init(CommonUtil.createTraceId());
        assertEquals(7000L, result.getCollectedBlockNumber().longValue());
    }

}
