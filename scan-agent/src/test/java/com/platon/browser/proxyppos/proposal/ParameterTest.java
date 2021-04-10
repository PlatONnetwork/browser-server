package com.platon.browser.proxyppos.proposal;

import com.platon.contracts.ppos.dto.resp.Proposal;
import org.junit.Test;

public class ParameterTest extends ProposalBase {
    /**
     *
     * @throws Exception
     */
    @Test
    public void parameter() throws Exception {
        Proposal p1 = Proposal.createSubmitParamProposalParam(nodeId1, "15", "staking","unStakeFreezeDuration","10");
        Proposal p2 = Proposal.createSubmitParamProposalParam(nodeId2, "16", "slashing","maxEvidenceAge","12");
        sendRequest(
                encode(p1),
                encode(p2)
        );
    }
}
