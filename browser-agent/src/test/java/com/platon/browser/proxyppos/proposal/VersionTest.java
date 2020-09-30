package com.platon.browser.proxyppos.proposal;

import com.alaya.abi.solidity.datatypes.BytesType;
import com.alaya.abi.solidity.datatypes.generated.Uint32;
import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.common.FunctionType;
import com.alaya.contracts.ppos.utils.EncoderUtils;
import com.alaya.protocol.core.methods.response.bean.ProgramVersion;
import com.alaya.utils.Numeric;
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
