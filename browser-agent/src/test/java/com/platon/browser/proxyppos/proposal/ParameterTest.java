package com.platon.browser.proxyppos.proposal;

import com.platon.sdk.contracts.ppos.dto.resp.Proposal;
import org.junit.Test;

public class ParameterTest extends ProposalBase {
    /**
     * TODO: Transaction has failed with status: 0x0. Gas used: 999999. (not-enough gas?)
     * @throws Exception
     */
    @Test
    public void parameter() throws Exception {
        Proposal p1 = Proposal.createSubmitParamProposalParam(nodeId1, "5", "staking","unStakeFreezeDuration","5");
        Proposal p2 = Proposal.createSubmitParamProposalParam(nodeId2, "6", "slashing","maxEvidenceAge","5");
        sendRequest(
                encode(p1),
                encode(p2)
        );
    }
}
