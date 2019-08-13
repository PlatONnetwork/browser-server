package com.platon.browser.util;

import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/13
 * Time: 16:48
 */
public  class Resolver {

    public static BigInteger bigIntegerResolver( RlpString rlpString){
        RlpString integers = rlpString;
        RlpList integersList = RlpDecoder.decode(integers.getBytes());
        RlpString integersString = (RlpString) integersList.getValues().get(0);
        BigInteger integer = new BigInteger(1, integersString.getBytes());
        return integer;
    }

    public static String StringResolver( RlpString rlpString){
        RlpString Strings = rlpString;
        RlpList StringsList = RlpDecoder.decode(Strings.getBytes());
        RlpString StringsListString = (RlpString) StringsList.getValues().get(0);
        String stringValue = Numeric.toHexString(StringsListString.getBytes());
        return stringValue;
    }

}