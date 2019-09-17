package com.platon.browser.task;

import com.platon.browser.TestBase;
import com.platon.browser.client.ProposalParticiantStat;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.dao.mapper.NodeOptMapper;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.ProposalMarkDownDto;
import com.platon.browser.engine.BlockChain;
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
    private ProposalUpdateTask proposalUpdateTask;
    @Mock
    private BlockChain blockChain;
    @Mock
    private SpecialContractApi sca;
    @Mock
    private NodeOptMapper nodeOptMapper;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(proposalUpdateTask, "bc", blockChain);
        ReflectionTestUtils.setField(proposalUpdateTask, "sca", sca);
        ReflectionTestUtils.setField(proposalUpdateTask, "nodeOptMapper", nodeOptMapper);
    }

    @Test
    public void testStart() throws Exception {
        CustomBlock customBlock = blocks.get(0);
        when(blockChain.getCurBlock()).thenReturn(customBlock);
        doReturn(proposals).when(proposalUpdateTask).getAllProposal();
        CustomProposal proposal = proposals.get(0);
        doReturn(proposal).when(proposalUpdateTask).getProposal(anyString());
        ProposalParticiantStat pps = new ProposalParticiantStat();
        pps.setVoterCount(10L);
        pps.setAbstainCount(2L);
        pps.setOpposeCount(3L);
        pps.setSupportCount(6L);
        doReturn(pps).when(proposalUpdateTask).getProposalParticiantStat(anyString(),anyString());
        TallyResult tr = new TallyResult();
        tr.setStatus(1);
        doReturn(tr).when(proposalUpdateTask).getTallyResult(anyString());
        ProposalMarkDownDto pmd = new ProposalMarkDownDto();
        pmd.setTopic(proposal.getTopic());
        pmd.setPIP(proposal.getPipId().toString());
        pmd.setAuthor("aaa");
        pmd.setType(proposal.getType());
        pmd.setCategory("aaa");
        pmd.setStatus(proposal.getStatus().toString());
        pmd.setDescription(proposal.getDescription());
        pmd.setCreated(proposal.getVerifier());
        doReturn(pmd).when(proposalUpdateTask).getMarkdownInfo(anyString());
        proposalUpdateTask.start();
        verify(proposalUpdateTask, times(1)).start();
    }

}
