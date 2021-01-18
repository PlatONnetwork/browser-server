package com.platon.browser.v0151.contract;

import com.alaya.protocol.core.RemoteCall;

import java.math.BigInteger;

public interface ErcContract {
    RemoteCall<String> name();
    RemoteCall<String> symbol();
    RemoteCall<BigInteger> decimals();
    RemoteCall<BigInteger> totalSupply();
}
