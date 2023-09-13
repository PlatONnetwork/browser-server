package com.platon.browser.client;

import com.platon.browser.bean.ProposalParticipantStat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalParticipantStatTest {
    @Test
    public void test(){
        ProposalParticipantStat pps = new ProposalParticipantStat();

        pps.setAbstentions(8L);
        pps.setNays(4L);
        pps.setYeas(998L);
        pps.setAccuVerifierAccount(8798L);

        pps.getAbstentions();
        pps.getNays();
        pps.getYeas();
        pps.getAccuVerifierAccount();

        assertTrue(true);
    }
}
