package com.platon.browser.common.complement.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.exception.NoSuchBeanException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ParamProposalCacheTest extends AgentTestBase {

    @Spy
    private ParamProposalCache paramProposalCache;
    @Test
    public void test() throws NoSuchBeanException {
        paramProposalCache.init(new ArrayList<>(proposalList));
        paramProposalCache.add(4444L,"0xsfsfdsf");
        paramProposalCache.get(4444L);
    }
}
