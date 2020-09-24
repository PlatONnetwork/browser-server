package com.platon.browser.proxyppos.delegate;

import com.alaya.abi.solidity.datatypes.BytesType;
import com.alaya.abi.solidity.datatypes.generated.Uint16;
import com.alaya.abi.solidity.datatypes.generated.Uint256;
import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.common.FunctionType;
import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.alaya.contracts.ppos.utils.EncoderUtils;
import com.alaya.utils.Convert;
import com.alaya.utils.Numeric;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

public class DelegateTest extends DelegateBase {
    private String nodeId = "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c";
    private byte[] encode(String delegateAmount){
        BigDecimal amount = Convert.toVon(delegateAmount, Convert.Unit.ATP);
        Function function = new Function(
                FunctionType.DELEGATE_FUNC_TYPE,
                Arrays.asList(
                    new Uint16(StakingAmountType.FREE_AMOUNT_TYPE.getValue()),
                    new BytesType(Numeric.hexStringToByteArray(nodeId)),
                    new Uint256(amount.toBigInteger())
        ));
        byte [] data = Hex.decode(EncoderUtils.functionEncoder(function));
        return data;
    }

    @Test
    public void delegate() throws Exception {
        sendRequest(encode("50000"),encode("20000"));
    }
}
