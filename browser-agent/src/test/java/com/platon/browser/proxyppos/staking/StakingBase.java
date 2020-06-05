package com.platon.browser.proxyppos.staking;

import com.platon.browser.proxyppos.TestBase;
import com.platon.sdk.utlis.NetworkParameters;

public class StakingBase extends TestBase {
    protected final String TARGET_CONTRACT_ADDRESS = NetworkParameters.getPposContractAddressOfStaking(chainId);
}
