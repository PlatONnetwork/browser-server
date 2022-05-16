package com.platon.browser.analyzer.ppos;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.bean.NodeItem;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.custommapper.StakeBusinessMapper;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @Auther: dongqile
 * @Date: 2019/11/13
 * @Description: 修改验证人业务参数转换器测试类
 **/
@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeModifyConverterTest extends AgentTestBase {
	
    @Mock
    private StakeBusinessMapper stakeBusinessMapper;
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private NodeCache nodeCache;
    @Mock
    private CollectionEvent collectionEvent;
    @Mock
    private EpochMessage epochMessage;
    @Mock
    private StakingMapper stakingMapper;
    @InjectMocks
    @Spy
    private StakeModifyAnalyzer target;

    @Before
    public void setup()throws Exception{
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7")
                .nodeName("zrj-node1")
                .stakingBlockNum(new BigInteger("20483"))
                .build();
        when(nodeCache.getNode(anyString())).thenReturn(nodeItem);
        //when(nodeItem.getStakingBlockNum()).thenReturn(nodeItem.getStakingBlockNum());
        Staking staking = new Staking();
        staking.setRewardPer(333);
        when(stakingMapper.selectByPrimaryKey(any())).thenReturn(staking);
        when(collectionEvent.getEpochMessage()).thenReturn(epochMessage);
        when(epochMessage.getSettleEpochRound()).thenReturn(BigInteger.TEN);
    }

    @Test
    public void convert()throws Exception{
        Transaction tx = new Transaction();
        for (Transaction transaction : transactionList){
            if(transaction.getTypeEnum().equals(Transaction.TypeEnum.STAKE_MODIFY))
                tx=transaction;
        }
        target.analyze(collectionEvent,tx);
    }

}
