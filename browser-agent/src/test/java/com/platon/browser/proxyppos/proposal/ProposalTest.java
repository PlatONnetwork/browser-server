package com.platon.browser.proxyppos.proposal;

import com.platon.browser.proxyppos.TestBase;
import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.resp.Proposal;
import com.platon.sdk.contracts.ppos.utils.EncoderUtils;
import com.platon.sdk.utlis.NetworkParameters;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.math.BigInteger;

public class ProposalTest extends TestBase {
    private String nodeId = "15245d4dceeb7552b52d70e56c53fc86aa030eab6b7b325e430179902884fca3d684b0e896ea421864a160e9c18418e4561e9a72f911e2511c29204a857de71a";
    private String targetContractAddress = NetworkParameters.getPposContractAddressOfProposal(chainId);

    private byte[] encode(Proposal p){
        Function f = new Function(p.getSubmitFunctionType(),p.getSubmitInputParameters());
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(f));
        return d;
    }

    @Test
    public void text() throws Exception {
        Proposal p1 = Proposal.createSubmitTextProposalParam(nodeId, "1");
        Proposal p2 = Proposal.createSubmitTextProposalParam(nodeId, "2");
        invokeProxyContract(encode(p1),targetContractAddress,encode(p2),targetContractAddress);
    }

    /**
     * TODO: FAIL
     * @throws Exception
     */
    @Test
    public void cancel() throws Exception {
        Proposal p1 = Proposal.createSubmitCancelProposalParam(nodeId, "3", BigInteger.valueOf(6000000),"1");
        Proposal p2 = Proposal.createSubmitCancelProposalParam(nodeId, "4", BigInteger.valueOf(60000000),"2");
        invokeProxyContract(encode(p1),targetContractAddress,encode(p2),targetContractAddress);
    }

    @Test
    public void parameter() throws Exception {
        Proposal p1 = Proposal.createSubmitParamProposalParam(nodeId, "5", "staking","unStakeFreezeDuration","5");
        Proposal p2 = Proposal.createSubmitParamProposalParam(nodeId, "6", "slashing","maxEvidenceAge","5");
        invokeProxyContract(encode(p1),targetContractAddress,encode(p2),targetContractAddress);
    }

    @Test
    public void version() throws Exception {
        Proposal p1 = Proposal.createSubmitVersionProposalParam(nodeId, "7", BigInteger.valueOf(1200),BigInteger.valueOf(20));
        Proposal p2 = Proposal.createSubmitVersionProposalParam(nodeId, "8", BigInteger.valueOf(1200),BigInteger.valueOf(20));
        invokeProxyContract(encode(p1),targetContractAddress,encode(p2),targetContractAddress);
    }
}
