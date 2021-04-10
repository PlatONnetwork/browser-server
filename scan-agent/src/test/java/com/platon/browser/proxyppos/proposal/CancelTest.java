package com.platon.browser.proxyppos.proposal;

import com.platon.contracts.ppos.ProposalContract;
import com.platon.contracts.ppos.dto.resp.Proposal;
import org.junit.Test;

import java.math.BigInteger;

public class CancelTest extends ProposalBase {
    /**
     * TODO: Transaction has failed with status: 0x0. Gas used: 999999. (not-enough gas?)
     * @throws Exception
     */
    @Test
    public void cancel() throws Exception {
        ProposalContract.load(defaultWeb3j).getProposalList();

        Proposal p1 = Proposal.createSubmitCancelProposalParam(nodeId1, "110", BigInteger.valueOf(5),"0x121719200b6863d8c5201efbcceaae19938a5202f6ba6abf2c84044f48531af5");
        Proposal p2 = Proposal.createSubmitCancelProposalParam(nodeId2, "210", BigInteger.valueOf(5),"0x121719200b6863d8c5201efbcceaae19938a5202f6ba6abf2c84044f48531af5");
        sendRequest(
                encode(p1),
                encode(p2)
        );
    }
}
