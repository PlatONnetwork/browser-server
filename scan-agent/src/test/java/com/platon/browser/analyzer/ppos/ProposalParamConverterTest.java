package com.platon.browser.analyzer.ppos;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.cache.ProposalCache;
import com.platon.browser.bean.NodeItem;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.custommapper.ProposalBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.govern.ParameterService;
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
 * @Description: 文本提案参数转换测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalParamConverterTest extends AgentTestBase {
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private ProposalBusinessMapper proposalBusinessMapper;
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private ProposalCache proposalCache;
    @Mock
    private ParameterService parameterService;
    @Mock
    private NodeCache nodeCache;
    @InjectMocks
    @Spy
    private ProposalParameterAnalyzer target;

    @Before
    public void setup()throws Exception{
        when(parameterService.getValueInBlockChainConfig(any())).thenReturn("600");
        when(chainConfig.getProposalUrlTemplate()).thenReturn(blockChainConfig.getProposalUrlTemplate());
        when(chainConfig.getProposalPipNumTemplate()).thenReturn(blockChainConfig.getProposalPipNumTemplate());
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7")
                .nodeName("integration-node1")
                .stakingBlockNum(new BigInteger("88602"))
                .build();
        when(nodeCache.getNode(anyString())).thenReturn(nodeItem);
    }

    @Test
    public void convert(){
        Block block = blockList.get(0);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        Transaction tx = new Transaction();
        for(CollectionTransaction collectionTransaction : transactionList){
            if(collectionTransaction.getTypeEnum().equals(Transaction.TypeEnum.PROPOSAL_PARAMETER)){
                tx = collectionTransaction;
            }
        }
        target.analyze(collectionEvent,tx);
    }
}