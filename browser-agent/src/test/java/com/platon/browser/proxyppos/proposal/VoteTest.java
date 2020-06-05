package com.platon.browser.proxyppos.proposal;

import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.common.FunctionType;
import com.platon.sdk.contracts.ppos.dto.enums.VoteOption;
import com.platon.sdk.contracts.ppos.utils.EncoderUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.platon.bean.ProgramVersion;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

public class VoteTest extends ProposalBase {

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
     * TODO: Transaction has failed with status: 0x0. Gas used: 999999. (not-enough gas?)
     * @throws Exception
     */
    @Test
    public void vote() throws Exception {
        ProgramVersion pv = new ProgramVersion();
        pv.setVersion(BigInteger.valueOf(1792));
        pv.setSign("25a2407f1692febff715655d53912b6284d8672a411d39b250ec40530a7e36f0b7970ed1d413f9b079e104aba80e5cef25eaf299cbd6a01e8015b505cffebc2d");

        sendRequest(
                encode(pv,VoteOption.YEAS,nodeId1,"0x19069d2ef47cbc195976ea0ec03530bdc4ba602e7035108e7ea28bc411517f1a"),
                encode(pv,VoteOption.YEAS,nodeId2,"0x2227cc8b7d6b7e88fb59535ac93bc11e4d845d465d4938ea72b1a9e628623d50")
        );
    }

}
