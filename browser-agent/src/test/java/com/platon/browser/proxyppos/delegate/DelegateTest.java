package com.platon.browser.proxyppos.delegate;

import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.common.FunctionType;
import com.platon.sdk.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.sdk.contracts.ppos.utils.EncoderUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.util.Arrays;

public class DelegateTest extends DelegateBase {
    private String nodeId = "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c";
    private byte[] encode(String delegateAmount){
        BigDecimal amount = Convert.toVon(delegateAmount, Convert.Unit.LAT);
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
