package com.platon.browser.proxyppos.proposal;

import com.platon.sdk.contracts.ppos.dto.resp.Proposal;
import org.junit.Test;

public class TextTest extends ProposalBase {
    @Test
    public void text() throws Exception {
        Proposal p1 = Proposal.createSubmitTextProposalParam(nodeId, "1");
        Proposal p2 = Proposal.createSubmitTextProposalParam(nodeId, "2");
        invokeProxyContract(encode(p1),targetContractAddress,encode(p2),targetContractAddress);
    }
}
