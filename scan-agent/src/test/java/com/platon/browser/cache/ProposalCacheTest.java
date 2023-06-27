package com.platon.browser.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.exception.NoSuchBeanException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalCacheTest extends AgentTestBase {

    @Mock
    private ProposalMapper proposalMapper;

    @InjectMocks
    @Spy
    private ProposalCache proposalCache;

    @Before
    public void setup() {
        List<Proposal> proposalList = new ArrayList<>();
        Proposal proposal = new Proposal();
        proposalList.add(proposal);
        proposal.setCanceledPipId("123");
        when(proposalMapper.selectByExample(any())).thenReturn(proposalList);
    }

    @Test
    public void test() throws NoSuchBeanException {
        proposalCache.init();
        proposalCache.add(4444L, "0xsfsfdsf");
        proposalCache.get(4444L);
        //assertEquals(paramProposalCache.get(444L),"0xsfsfdsf");
    }

}
