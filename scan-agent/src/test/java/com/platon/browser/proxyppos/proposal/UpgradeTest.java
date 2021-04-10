package com.platon.browser.proxyppos.proposal;

import com.platon.contracts.ppos.dto.resp.Proposal;
import org.junit.Test;

import java.math.BigInteger;

public class UpgradeTest extends ProposalBase {
    @Test
    public void upgrade() throws Exception {
        Proposal p1 = Proposal.createSubmitVersionProposalParam(nodeId1, "100", BigInteger.valueOf(7800),BigInteger.valueOf(20));
        Proposal p2 = Proposal.createSubmitVersionProposalParam(nodeId2, "200", BigInteger.valueOf(7800),BigInteger.valueOf(20));
        sendRequest(
                encode(p1),
                encode(p2)
        );
    }
}
