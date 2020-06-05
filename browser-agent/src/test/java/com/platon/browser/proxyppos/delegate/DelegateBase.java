package com.platon.browser.proxyppos.delegate;

import com.platon.browser.proxyppos.TestBase;
import com.platon.sdk.utlis.NetworkParameters;

public class DelegateBase extends TestBase {
    protected final String TARGET_CONTRACT_ADDRESS = NetworkParameters.getPposContractAddressOfStaking(chainId);
}
