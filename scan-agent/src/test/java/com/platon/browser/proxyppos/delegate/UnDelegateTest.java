package com.platon.browser.proxyppos.delegate;

import com.platon.abi.solidity.datatypes.BytesType;
import com.platon.abi.solidity.datatypes.generated.Uint256;
import com.platon.abi.solidity.datatypes.generated.Uint64;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.utils.Convert;
import com.platon.utils.Numeric;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class UnDelegateTest extends DelegateBase {
    private String nodeId1 = "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c";
    private String nodeId2 = "77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050";

    private byte[] encode(String nodeId,BigInteger stakingBlockNum, String delegateAmount){
        BigDecimal amount = Convert.toVon(delegateAmount, Convert.Unit.KPVON);
        Function f = new Function(FunctionType.WITHDREW_DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint64(stakingBlockNum)
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))
                        , new Uint256(amount.toBigInteger())));
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(f));
        return d;
    }

    @Test
    public void unDelegate() throws Exception {
        //20000000000000000000000
        sendRequest(
            encode(nodeId1,BigInteger.valueOf(2792),"20000"),
            encode(nodeId2,BigInteger.valueOf(2792),"11000")
        );
    }
}
