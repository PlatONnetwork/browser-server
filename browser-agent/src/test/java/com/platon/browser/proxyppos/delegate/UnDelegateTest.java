package com.platon.browser.proxyppos.delegate;

import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.common.FunctionType;
import com.platon.sdk.contracts.ppos.utils.EncoderUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class UnDelegateTest extends DelegateBase {
    private String nodeId = "15245d4dceeb7552b52d70e56c53fc86aa030eab6b7b325e430179902884fca3d684b0e896ea421864a160e9c18418e4561e9a72f911e2511c29204a857de71a";

    private byte[] encode(BigInteger stakingBlockNum, String delegateAmount){
        BigDecimal amount = Convert.toVon(delegateAmount, Convert.Unit.LAT);
        Function f = new Function(FunctionType.WITHDREW_DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint64(stakingBlockNum)
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))
                        , new Uint256(amount.toBigInteger())));
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(f));
        return d;
    }

    @Test
    public void unDelegate() throws Exception {
        invokeProxyContract(
            encode(BigInteger.valueOf(4000),"65000"),TARGET_CONTRACT_ADDRESS,
            encode(BigInteger.valueOf(5000),"35000"),TARGET_CONTRACT_ADDRESS
        );
    }
}
