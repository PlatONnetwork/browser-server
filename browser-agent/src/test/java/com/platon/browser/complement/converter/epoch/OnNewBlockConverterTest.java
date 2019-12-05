package com.platon.browser.complement.converter.epoch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.ParamProposalCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.service.proposal.ProposalService;
import com.platon.browser.complement.dao.mapper.NewBlockMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.service.govern.ParameterService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.platon.bean.TallyResult;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OnNewBlockConverterTest  extends AgentTestBase {
	
    @Mock
    private NodeCache nodeCache;
    @Mock
    private NewBlockMapper newBlockMapper;
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private ParamProposalCache paramProposalCache;
    @Mock
    private ProposalService proposalService;
    @Mock
    private ProposalMapper proposalMapper;
    @Mock
    private ParameterService parameterService;

    @Spy
    private OnNewBlockConverter target;

    @Before
    public void setup()throws Exception{
        ReflectionTestUtils.setField(target,"nodeCache",nodeCache);
        ReflectionTestUtils.setField(target,"newBlockMapper",newBlockMapper);
        ReflectionTestUtils.setField(target,"networkStatCache",networkStatCache);
        ReflectionTestUtils.setField(target,"paramProposalCache",paramProposalCache);
        ReflectionTestUtils.setField(target,"proposalService",proposalService);
        ReflectionTestUtils.setField(target,"proposalMapper",proposalMapper);
        ReflectionTestUtils.setField(target,"parameterService",parameterService);
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050")
                .nodeName("integration-node1")
                .stakingBlockNum(new BigInteger("88602"))
                .build();
        when(nodeCache.getNode(any())).thenReturn(nodeItem);
        Set<String> proposalSet = new HashSet<>();
        Proposal proposal = proposalList.get(0);
        proposalSet.add(proposal.getHash());
        when(paramProposalCache.get(any())).thenReturn(proposalSet);
        TallyResult tr = new TallyResult();
        tr.setStatus(2);
        tr.setProposalID(proposal.getPipId());
        when(proposalService.getTallyResult(any())).thenReturn(tr);
        when(proposalMapper.selectByExample(any())).thenReturn(new ArrayList<>(proposalList));
        when(networkStatCache.getNetworkStat()).thenReturn(new NetworkStat());
    }


    @Test
    public void convert()throws Exception{
        Block block = blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setBlockReward(new BigDecimal("200000"));
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        target.convert(collectionEvent,block);
    }
}
