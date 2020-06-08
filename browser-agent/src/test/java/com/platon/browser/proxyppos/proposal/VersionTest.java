package com.platon.browser.proxyppos.proposal;

import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.common.FunctionType;
import com.platon.sdk.contracts.ppos.utils.EncoderUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.platon.bean.ProgramVersion;
import org.web3j.utils.Numeric;

import java.util.Arrays;

public class VersionTest extends ProposalBase {


    protected byte[] encode(String nodeId,ProgramVersion pv){
        Function function = new Function(FunctionType.DECLARE_VERSION_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId)),
                        new Uint32(pv.getProgramVersion()),
                        new BytesType(Numeric.hexStringToByteArray(pv.getProgramVersionSign()))));
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(function));
        return d;
    }

    @Test
    public void version() throws Exception {
        ProgramVersion pv = defaultWeb3j.getProgramVersion().send().getAdminProgramVersion();
        sendRequest(
                encode(nodeId1,pv),
                encode(nodeId2,pv)
        );
    }
}
