package com.platon.browser.util;

import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

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
/*

    public static String ObjectResolver(RlpString rlpString ){
        List <RlpType> rlpTypeList = ((RlpList) rlps2.getValues().get(0)).getValues();

        for (RlpType rlpType1 : rlpTypeList) {
            RlpList rlpList1 = (RlpList) rlpType1;
            RlpString rlpString = (RlpString) rlpList1.getValues().get(0);
            RlpString rlpString1 = (RlpString) rlpList1.getValues().get(1);
            byte[] bytes = RlpEncoder.encode(rlpString);
            byte[] bytes1 = RlpEncoder.encode(rlpString1);
            System.out.println(new BigInteger(1, bytes) + ":" + new BigInteger(1, bytes1));
        }
    }
*/

}