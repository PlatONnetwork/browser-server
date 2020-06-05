package com.platon.browser.proxyppos.proposal;

import com.platon.sdk.contracts.ppos.dto.resp.Proposal;
import org.junit.Test;

import java.math.BigInteger;

public class CancelTest extends ProposalBase {
    /**
     * TODO: FAIL
     * @throws Exception
     */
    @Test
    public void cancel() throws Exception {
        Proposal p1 = Proposal.createSubmitCancelProposalParam(nodeId, "3", BigInteger.valueOf(6000000),"1");
        Proposal p2 = Proposal.createSubmitCancelProposalParam(nodeId, "4", BigInteger.valueOf(60000000),"2");
        invokeProxyContract(encode(p1),TARGET_CONTRACT_ADDRESS,encode(p2),TARGET_CONTRACT_ADDRESS);
    }
}
