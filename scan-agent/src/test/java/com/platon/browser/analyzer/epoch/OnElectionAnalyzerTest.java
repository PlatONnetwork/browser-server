package com.platon.browser.analyzer.epoch;

import com.platon.protocol.Web3j;
import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.HistoryLowRateSlash;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.custommapper.EpochBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.bean.CustomStaking.StatusEnum;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.service.ppos.StakeEpochService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OnElectionAnalyzerTest extends AgentTestBase {

    @Mock
    private EpochBusinessMapper epochBusinessMapper;
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private SpecialApi specialApi;
    @Mock
    private PlatOnClient platOnClient;
    @Mock
    private StakingMapper stakingMapper;
    @Mock
    private StakeEpochService stakeEpochService;
    @Mock
    private BlockChainConfig chainConfig;
    @InjectMocks
    @Spy
    private OnElectionAnalyzer target;

    @Before
    public void setup() throws Exception {
        List<Staking> list = new ArrayList<>();
        for (int i = 0; i < this.stakingList.size(); i++) {
            Staking staking = new Staking();
            staking.setNodeId(this.stakingList.get(i).getNodeId());
            staking.setStakingBlockNum(this.stakingList.get(i).getStakingBlockNum());
            staking.setStakingHes(BigDecimal.ONE);
            staking.setStatDelegateHes(BigDecimal.ONE);
            staking.setStatDelegateLocked(BigDecimal.ONE);
            staking.setStatDelegateReleased(BigDecimal.ONE);
            staking.setStakingLocked(BigDecimal.ONE);
            staking.setStakingReduction(BigDecimal.ONE);
            staking.setStatus(StatusEnum.EXITING.getCode());
            staking.setUnStakeFreezeDuration(1);
            staking.setUnStakeEndBlock(1l);
            if (i == 1) {
                staking.setStatus(StatusEnum.CANDIDATE.getCode());
            } else {
                staking.setStakingReduction(new BigDecimal("-1"));
            }
            staking.setLowRateSlashCount(0);
            list.add(staking);
        }

        List<HistoryLowRateSlash> list1 = new ArrayList<>();
        HistoryLowRateSlash historyLowRateSlash = new HistoryLowRateSlash();
        historyLowRateSlash.setNodeId("0x");
        historyLowRateSlash.setAmount(BigInteger.ZERO);
        list1.add(historyLowRateSlash);
        when(this.specialApi.getHistoryLowRateSlashList(any(), any())).thenReturn(list1);
        when(this.stakingMapper.selectByExample(any())).thenReturn(list);
        when(this.chainConfig.getConsensusPeriodBlockCount()).thenReturn(BigInteger.TEN);
        when(this.chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.TEN);
        when(this.chainConfig.getSlashBlockRewardCount()).thenReturn(BigDecimal.TEN);
        when(this.chainConfig.getSlashBlockRewardCount()).thenReturn(BigDecimal.TEN);
        when(this.chainConfig.getStakeThreshold()).thenReturn(BigDecimal.TEN);
        when(this.stakeEpochService.getUnStakeEndBlock(anyString(), any(BigInteger.class), anyBoolean()))
            .thenReturn(BigInteger.TEN);
        when(this.stakeEpochService.getUnStakeFreeDuration()).thenReturn(BigInteger.TEN);
        when(this.stakeEpochService.getZeroProduceFreeDuration()).thenReturn(BigInteger.TEN);
        Web3jWrapper web3jWrapper = mock(Web3jWrapper.class);
        when(this.platOnClient.getWeb3jWrapper()).thenReturn(web3jWrapper);
        Web3j web3j = mock(Web3j.class);
        when(web3jWrapper.getWeb3j()).thenReturn(web3j);
    }

    @Test
    public void convert() throws BlockNumberException {
        Block block = this.blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setPreValidatorList(this.validatorList);
        epochMessage.setSettleEpochRound(BigInteger.TEN);

        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        this.target.analyze(collectionEvent, block);
        when(this.stakingMapper.selectByExample(any())).thenReturn(Collections.EMPTY_LIST);
        this.target.analyze(collectionEvent, block);
        try {
            when(this.stakingMapper.selectByExample(any())).thenReturn(null);
            this.target.analyze(collectionEvent, block);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
