package com.platon.browser.erc;

import java.math.BigInteger;

public interface ErcService {

    BigInteger getBalance(String contractAddress, String account);
}
