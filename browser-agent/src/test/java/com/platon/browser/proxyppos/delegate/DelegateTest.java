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
    private String nodeId = "77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050";
    private byte[] encode(String delegateAmount){
        BigDecimal amount = Convert.toVon(delegateAmount, Convert.Unit.LAT);
        Function f = new Function(FunctionType.DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint16(StakingAmountType.FREE_AMOUNT_TYPE.getValue())
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))
                        , new Uint256(amount.toBigInteger())));
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(f));
        return d;
    }

    @Test
    public void delegate() throws Exception {
        sendRequest(encode("200"),encode("200"));
    }
}
