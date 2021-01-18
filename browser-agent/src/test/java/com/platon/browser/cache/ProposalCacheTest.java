package com.platon.browser.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.exception.NoSuchBeanException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalCacheTest extends AgentTestBase {

    @Spy
    private ProposalCache proposalCache;
    @Test
    public void test() throws NoSuchBeanException {
        proposalCache.init();
        proposalCache.add(4444L,"0xsfsfdsf");
        proposalCache.get(4444L);
        //assertEquals(paramProposalCache.get(444L),"0xsfsfdsf");
    }
}
