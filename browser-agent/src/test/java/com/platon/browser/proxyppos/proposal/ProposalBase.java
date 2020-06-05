package com.platon.browser.proxyppos.proposal;

import com.platon.browser.proxyppos.TestBase;
import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.resp.Proposal;
import com.platon.sdk.contracts.ppos.utils.EncoderUtils;
import com.platon.sdk.utlis.NetworkParameters;
import org.bouncycastle.util.encoders.Hex;

public class ProposalBase extends TestBase {
    protected String nodeId = "15245d4dceeb7552b52d70e56c53fc86aa030eab6b7b325e430179902884fca3d684b0e896ea421864a160e9c18418e4561e9a72f911e2511c29204a857de71a";
    protected final String TARGET_CONTRACT_ADDRESS = NetworkParameters.getPposContractAddressOfProposal(chainId);

    protected byte[] encode(Proposal p){
        Function f = new Function(p.getSubmitFunctionType(),p.getSubmitInputParameters());
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(f));
        return d;
    }
}
