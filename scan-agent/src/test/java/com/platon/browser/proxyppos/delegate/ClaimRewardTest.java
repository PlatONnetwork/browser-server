package com.platon.browser.proxyppos.delegate;

import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.utils.EncoderUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

public class ClaimRewardTest extends RewardBase {
    private byte[] encode(){
        Function function = new Function(FunctionType.WITHDRAW_DELEGATE_REWARD_FUNC_TYPE);
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(function));
        return d;
    }

    @Test
    public void claimReward() throws Exception {
        sendRequest(encode(),encode());
    }
}
