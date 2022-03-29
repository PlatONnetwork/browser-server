package com.platon.browser.analyzer.ppos;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.bean.NodeItem;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.custommapper.ProposalBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @Auther: dongqile
 * @Date: 2019/11/13
 * @Description: 文本提案参数转换测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalTextConverterTest extends AgentTestBase {
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private ProposalBusinessMapper proposalBusinessMapper;
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private NodeCache nodeCache;
    @InjectMocks
    @Spy
    private ProposalTextAnalyzer target;

    @Before
    public void setup()throws Exception{
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050")
                .nodeName("integration-node1")
                .stakingBlockNum(new BigInteger("88602"))
                .build();
        when(nodeCache.getNode(anyString())).thenReturn(nodeItem);
        when(chainConfig.getProposalUrlTemplate()).thenReturn(blockChainConfig.getProposalUrlTemplate());
        when(chainConfig.getProposalPipNumTemplate()).thenReturn(blockChainConfig.getProposalPipNumTemplate());
    }

    @Test
    public void convert(){
        Block block = blockList.get(0);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        Transaction tx = new Transaction();
        for(CollectionTransaction collectionTransaction : transactionList){
            if(collectionTransaction.getTypeEnum().equals(Transaction.TypeEnum.PROPOSAL_TEXT)){
                tx = collectionTransaction;
            }
        }
        target.analyze(collectionEvent,tx);
    }
}