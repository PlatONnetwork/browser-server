package com.platon.browser.erc;

import com.platon.browser.dto.ERCData;

import java.math.BigInteger;

public interface ERCInterface {

    String getName();

    String getSymbol();

    BigInteger getDecimals();

    BigInteger getTotalSupply();

    ERCData getErcData();
    
}
