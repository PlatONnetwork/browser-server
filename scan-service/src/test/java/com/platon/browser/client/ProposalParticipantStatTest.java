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

        pps.setAbstainCount(8L);
        pps.setOpposeCount(4L);
        pps.setSupportCount(998L);
        pps.setVoterCount(8798L);

        pps.getAbstainCount();
        pps.getOpposeCount();
        pps.getSupportCount();
        pps.getVoterCount();

        assertTrue(true);
    }
}
