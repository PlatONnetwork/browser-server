package com.platon.browser.proxyppos.proposal;

import com.platon.sdk.contracts.ppos.dto.resp.Proposal;
import org.junit.Test;

public class ParameterTest extends ProposalBase {
    @Test
    public void parameter() throws Exception {
        Proposal p1 = Proposal.createSubmitParamProposalParam(nodeId, "5", "staking","unStakeFreezeDuration","5");
        Proposal p2 = Proposal.createSubmitParamProposalParam(nodeId, "6", "slashing","maxEvidenceAge","5");
        sendRequest(
                encode(p1),
                encode(p2)
        );
    }
}
