package com.platon.browser.proxyppos.proposal;

import com.platon.sdk.contracts.ppos.dto.resp.Proposal;
import org.junit.Test;

import java.math.BigInteger;

public class VersionTest extends ProposalBase {
    @Test
    public void version() throws Exception {
        Proposal p1 = Proposal.createSubmitVersionProposalParam(nodeId, "7", BigInteger.valueOf(1200),BigInteger.valueOf(20));
        Proposal p2 = Proposal.createSubmitVersionProposalParam(nodeId, "8", BigInteger.valueOf(1200),BigInteger.valueOf(20));
        sendRequest(
                encode(p1),
                encode(p2)
        );
    }
}
