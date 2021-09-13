package com.platon.browser.analyzer.epoch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.cache.ProposalCache;
import com.platon.browser.bean.NodeItem;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.service.proposal.ProposalService;
import com.platon.browser.dao.custommapper.NewBlockMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.service.statistic.StatisticService;
import com.platon.contracts.ppos.dto.resp.GovernParam;
import com.platon.contracts.ppos.dto.resp.ParamItem;
import com.platon.contracts.ppos.dto.resp.ParamValue;
import com.platon.contracts.ppos.dto.resp.TallyResult;
import com.platon.browser.v0150.V0150Config;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OnNewBlockAnalyzerTest extends AgentTestBase {

    @Mock
    private NodeCache nodeCache;

    @Mock
    private NewBlockMapper newBlockMapper;

    @Mock
    private NetworkStatCache networkStatCache;

    @Mock
    private ProposalCache proposalCache;

    @Mock
    private ProposalService proposalService;

    @Mock
    private ProposalMapper proposalMapper;

    @Mock
    private ParameterService parameterService;

    @Mock
    private PlatOnClient platOnClient;

    @Mock
    private V0150Config v0150Config;

    @Mock
    private StatisticService statisticService;

    @InjectMocks
    @Spy
    private OnNewBlockAnalyzer target;

    @Before
    public void setup() throws Exception {
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050")
                .nodeName("integration-node1")
                .stakingBlockNum(new BigInteger("88602"))
                .build();

        when(v0150Config.getAdjustmentActiveVersion()).thenReturn(BigInteger.TEN);
        when(v0150Config.getAdjustmentPipId()).thenReturn("10");
        when(nodeCache.getNode(any())).thenReturn(nodeItem);
        Set<String> proposalSet = new HashSet<>();
        Proposal proposal = proposalList.get(0);
        proposalSet.add(proposal.getHash());
        when(proposalCache.get(any())).thenReturn(proposalSet);
        TallyResult tr = new TallyResult();
        tr.setStatus(2);
        tr.setProposalID(proposal.getPipId());
        when(proposalService.getTallyResult(any())).thenReturn(tr);
        when(proposalMapper.selectByExample(any())).thenReturn(new ArrayList<>(proposalList));
        when(networkStatCache.getNetworkStat()).thenReturn(new NetworkStat());
        List<GovernParam> governParamList = new ArrayList<>();
        GovernParam gp = new GovernParam();
        ParamItem pi = new ParamItem();
        pi.setModule("staking");
        pi.setName("maxValidators");
        gp.setParamItem(pi);
        ParamValue pv = new ParamValue();
        pv.setActiveBlock("3333");
        pv.setStaleValue("3");
        pv.setValue("34");
        gp.setParamValue(pv);
        governParamList.add(gp);
        when(platOnClient.getGovernParamValue(any())).thenReturn(governParamList);
    }


    @Test
    public void convert() throws Exception {
        Block block = blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setBlockReward(new BigDecimal("200000"));
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        target.analyze(collectionEvent, block);
    }

}
