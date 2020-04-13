package com.platon.browser.complement.converter.slash;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.ReportMultiSignParamCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.SlashBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.misc.StakeMiscService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * @Auther: dongqile
 * @Date: 2019/11/13
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ReportConverterTest extends AgentTestBase {

    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private SlashBusinessMapper slashBusinessMapper;
    @Mock
    private ReportMultiSignParamCache reportMultiSignParamCache;
    @Mock
    private NodeCache nodeCache;
    @Mock
    private StakeMiscService stakeMiscService;

    @Spy
    private ReportConverter target;

    @Before
    public void setup()throws Exception{
        ReflectionTestUtils.setField(target,"chainConfig",chainConfig);
        ReflectionTestUtils.setField(target,"slashBusinessMapper",slashBusinessMapper);
        ReflectionTestUtils.setField(target,"reportMultiSignParamCache",reportMultiSignParamCache);
        ReflectionTestUtils.setField(target,"nodeCache",nodeCache);
        ReflectionTestUtils.setField(target,"stakeMiscService",stakeMiscService);
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050")
                .nodeName("integration-node1")
                .stakingBlockNum(new BigInteger("88602"))
                .build();
        when(nodeCache.getNode(anyString())).thenReturn(nodeItem);
        when(chainConfig.getDuplicateSignSlashRate()).thenReturn(blockChainConfig.getDuplicateSignSlashRate());
        when(chainConfig.getDuplicateSignRewardRate()).thenReturn(blockChainConfig.getDuplicateSignRewardRate());
        when(stakeMiscService.getUnStakeEndBlock(anyString(),any(BigInteger.class),anyBoolean())).thenReturn(BigInteger.TEN);
        when(stakeMiscService.getUnStakeFreeDuration()).thenReturn(BigInteger.TEN);
    }

    @Test
    public void convert(){
        Block block = blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setSettleEpochRound(BigInteger.TEN);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        Transaction tx = new Transaction();
        for(CollectionTransaction collectionTransaction : transactionList){
            if(collectionTransaction.getTypeEnum().equals(Transaction.TypeEnum.REPORT)){
                tx = collectionTransaction;
            }
        }
        target.convert(collectionEvent,tx);
    }
}