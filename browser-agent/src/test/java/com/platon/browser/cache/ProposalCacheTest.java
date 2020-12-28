package com.platon.browser.cache;

import com.platon.browser.TestBase;
import com.platon.browser.exception.NoSuchBeanException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalCacheTest extends TestBase {

    @Spy
    private ProposalCache proposalCache;
    @Test
    public void test() throws NoSuchBeanException {
        proposalCache.init(new ArrayList<>(proposalList));
        proposalCache.add(4444L,"0xsfsfdsf");
        proposalCache.get(4444L);
        //assertEquals(paramProposalCache.get(444L),"0xsfsfdsf");
    }
}
