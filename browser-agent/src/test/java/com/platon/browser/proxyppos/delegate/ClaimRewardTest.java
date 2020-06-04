package com.platon.browser.proxyppos.delegate;

import com.platon.browser.proxyppos.TestBase;
import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.common.FunctionType;
import com.platon.sdk.contracts.ppos.utils.EncoderUtils;
import com.platon.sdk.utlis.NetworkParameters;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

public class ClaimRewardTest extends TestBase {
    private String targetContractAddress = NetworkParameters.getPposContractAddressOfStaking(chainId);

    private byte[] encode(){
        Function function = new Function(FunctionType.WITHDRAW_DELEGATE_REWARD_FUNC_TYPE);
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(function));
        return d;
    }

    @Test
    public void claimReward() throws Exception {
        invokeProxyContract(encode(),targetContractAddress,encode(),targetContractAddress);
    }
}
