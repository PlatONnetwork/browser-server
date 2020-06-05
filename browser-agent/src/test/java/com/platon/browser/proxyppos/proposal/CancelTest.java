package com.platon.browser.proxyppos.proposal;

import com.platon.sdk.contracts.ppos.dto.resp.Proposal;
import org.junit.Test;

import java.math.BigInteger;

public class CancelTest extends ProposalBase {
    /**
     * TODO: Transaction has failed with status: 0x0. Gas used: 999999. (not-enough gas?)
     * @throws Exception
     */
    @Test
    public void cancel() throws Exception {
        Proposal p1 = Proposal.createSubmitCancelProposalParam(nodeId1, "55", BigInteger.valueOf(6000000),"1");
        Proposal p2 = Proposal.createSubmitCancelProposalParam(nodeId2, "66", BigInteger.valueOf(60000000),"2");
        sendRequest(
                encode(p1),
                encode(p2)
        );
    }
}
