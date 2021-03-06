package com.platon.browser.proxyppos.staking;

import com.platon.abi.solidity.datatypes.BytesType;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.utils.Numeric;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.util.Arrays;

public class UnStakingTest extends StakingBase {
    private byte[] encode(String nodeId){
        Function function = new Function(FunctionType.WITHDREW_STAKING_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId))));
        return Hex.decode(EncoderUtils.functionEncoder(function));
    }

    @Test
    public void unStaking() throws Exception {
        String nodeId1 = "77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050";
        String nodeId2 = "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c";
        sendRequest(
                encode(nodeId1),
                encode(nodeId2)
        );
    }
}
