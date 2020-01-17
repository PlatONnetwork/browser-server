package com.platon.browser.complement.converter.epoch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.Block;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OnSettleConverterTest extends AgentTestBase {
	
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private EpochBusinessMapper epochBusinessMapper;
    @Mock
    private StakingMapper stakingMapper;

    @Spy
    private OnSettleConverter target;

    @Before
    public void setup()throws Exception{
        ReflectionTestUtils.setField(target,"chainConfig",chainConfig);
        ReflectionTestUtils.setField(target,"epochBusinessMapper",epochBusinessMapper);
        ReflectionTestUtils.setField(target,"stakingMapper",stakingMapper);
        when(chainConfig.getUnStakeRefundSettlePeriodCount()).thenReturn(blockChainConfig.getUnStakeRefundSettlePeriodCount());
        when(chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat()).thenReturn(blockChainConfig.getMaxSettlePeriodCount4AnnualizedRateStat());
        when(chainConfig.getSettlePeriodCountPerIssue()).thenReturn(blockChainConfig.getSettlePeriodCountPerIssue());
        when(stakingMapper.selectByExampleWithBLOBs(any())).thenReturn(new ArrayList <>(stakingList));
    }

    @Test
    public void convert() throws IOException {
        Block block = blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setCurValidatorList(validatorList);
        epochMessage.setCurVerifierList(verifierList);
        epochMessage.setStakeReward(new BigDecimal("10000"));
        epochMessage.setSettleEpochRound(BigInteger.TEN);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        target.convert(collectionEvent,block);
    }


}
