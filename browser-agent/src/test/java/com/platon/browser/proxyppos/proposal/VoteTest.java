package com.platon.browser.proxyppos.proposal;

import com.platon.browser.proxyppos.TestBase;
import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.common.FunctionType;
import com.platon.sdk.contracts.ppos.dto.enums.VoteOption;
import com.platon.sdk.contracts.ppos.utils.EncoderUtils;
import com.platon.sdk.utlis.NetworkParameters;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.platon.bean.ProgramVersion;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

public class VoteTest extends TestBase {

    private String nodeId = "77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050";
    private String targetContractAddress = NetworkParameters.getPposContractAddressOfProposal(chainId);

    private byte[] encode(ProgramVersion pv,VoteOption voteOption,String verifier,String proposalID){
        Function function = new Function(FunctionType.VOTE_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(verifier)),
                        new BytesType(Numeric.hexStringToByteArray(proposalID)), new Uint8(voteOption.getValue()),
                        new Uint32(pv.getProgramVersion()),
                        new BytesType(Numeric.hexStringToByteArray(pv.getProgramVersionSign()))));
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(function));
        return d;
    }

    /**
     * TODO: FAIL
     * @throws Exception
     */
    @Test
    public void vote() throws Exception {
        ProgramVersion pv = new ProgramVersion();
        pv.setVersion(BigInteger.valueOf(1792));
        pv.setSign("25a2407f1692febff715655d53912b6284d8672a411d39b250ec40530a7e36f0b7970ed1d413f9b079e104aba80e5cef25eaf299cbd6a01e8015b505cffebc2d");

        invokeProxyContract(
                encode(pv,VoteOption.YEAS,nodeId,"0x1178f6dcecd1731e2556d4a014d30ebe04cf5522c07776135e60f613e51af0c9"),
                targetContractAddress,
                encode(pv,VoteOption.YEAS,nodeId,"0x1178f6dcecd1731e2556d4a014d30ebe04cf5522c07776135e60f613e51af0c9"),
                targetContractAddress);
    }

}
