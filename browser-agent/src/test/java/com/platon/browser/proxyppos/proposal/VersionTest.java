package com.platon.browser.proxyppos.proposal;

import com.platon.abi.solidity.datatypes.BytesType;
import com.platon.abi.solidity.datatypes.generated.Uint32;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.protocol.core.methods.response.bean.ProgramVersion;
import com.platon.utils.Numeric;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.util.Arrays;

public class VersionTest extends ProposalBase {


    protected byte[] encode(String nodeId, ProgramVersion pv){
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
