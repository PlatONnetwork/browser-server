package com.platon.browser.task;

import com.platon.browser.TestBase;
import com.platon.browser.client.ProposalParticiantStat;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.dao.mapper.CustomProposalMapper;
import com.platon.browser.dao.mapper.NodeOptMapper;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.ProposalMarkDownDto;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.stage.BlockChainStage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.platon.bean.TallyResult;

import static org.mockito.Mockito.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 20:29
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalUpdateTaskTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(ProposalUpdateTaskTest.class);
    @Spy
    private ProposalUpdateTask task;
    @Mock
    private BlockChain blockChain;
    @Mock
    private SpecialContractApi sca;
    @Mock
    private NodeOptMapper nodeOptMapper;
    @Mock
    private CacheHolder cacheHolder;
    @Mock
    private CustomProposalMapper customProposalMapper;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(task, "bc", blockChain);
        ReflectionTestUtils.setField(task, "sca", sca);
        ReflectionTestUtils.setField(task, "nodeOptMapper", nodeOptMapper);
        ReflectionTestUtils.setField(task, "cacheHolder", cacheHolder);
        ReflectionTestUtils.setField(task, "customProposalMapper", customProposalMapper);
    }

    @Test
    public void testStart() throws Exception {
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        ProposalCache proposalCache = new ProposalCache();
        when(cacheHolder.getProposalCache()).thenReturn(proposalCache);

        CustomBlock customBlock = blocks.get(0);
        when(blockChain.getCurBlock()).thenReturn(customBlock);
        doReturn(proposals).when(task).getAllProposal();
        CustomProposal proposal = proposals.get(0);
        doReturn(proposal).when(task).getProposal(anyString());
        ProposalParticiantStat pps = new ProposalParticiantStat();
        pps.setVoterCount(10L);
        pps.setAbstainCount(2L);
        pps.setOpposeCount(3L);
        pps.setSupportCount(6L);
        doReturn(pps).when(task).getProposalParticipantStat(anyString(),anyString());
        TallyResult tr = new TallyResult();
        tr.setStatus(1);
        doReturn(tr).when(task).getTallyResult(anyString());
        ProposalMarkDownDto pmd = new ProposalMarkDownDto();
        pmd.setTopic(proposal.getTopic());
        pmd.setPIP(proposal.getPipId().toString());
        pmd.setAuthor("aaa");
        pmd.setType(proposal.getType());
        pmd.setCategory("aaa");
        pmd.setStatus(proposal.getStatus().toString());
        pmd.setDescription(proposal.getDescription());
        pmd.setCreated(proposal.getVerifier());
        doReturn(pmd).when(task).getMarkdownInfo(anyString());
        task.start();
        verify(task, times(1)).start();
    }

}
