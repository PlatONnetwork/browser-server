package com.platon.browser.proxyppos.delegate;

import com.platon.browser.proxyppos.TestBase;
import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.common.FunctionType;
import com.platon.sdk.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.sdk.contracts.ppos.utils.EncoderUtils;
import com.platon.sdk.utlis.NetworkParameters;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.util.Arrays;

public class DelegateTest extends TestBase {
    private String nodeId = "15245d4dceeb7552b52d70e56c53fc86aa030eab6b7b325e430179902884fca3d684b0e896ea421864a160e9c18418e4561e9a72f911e2511c29204a857de71a";
    private String targetContractAddress = NetworkParameters.getPposContractAddressOfStaking(chainId);

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
        invokeProxyContract(encode("65000"),targetContractAddress,encode("35000"),targetContractAddress);
    }
}
