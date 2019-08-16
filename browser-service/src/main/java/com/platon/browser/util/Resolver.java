package com.platon.browser.util;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.platon.contracts.DelegateContract;
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

    public static BigInteger bigIntegerResolver ( RlpString rlpString ) {
        RlpString integers = rlpString;
        RlpList integersList = RlpDecoder.decode(integers.getBytes());
        RlpString integersString = (RlpString) integersList.getValues().get(0);
        BigInteger integer = new BigInteger(1, integersString.getBytes());
        return integer;
    }

    public static String StringResolver ( RlpString rlpString ) throws Exception{
        RlpString Strings = rlpString;
        RlpList StringsList = RlpDecoder.decode(Strings.getBytes());
        RlpString StringsListString = (RlpString) StringsList.getValues().get(0);
        String stringValue = Numeric.toHexString(StringsListString.getBytes());
        return stringValue;
    }

    public static BigInteger[] ObjectResolver ( RlpString rlpString ) {
        BigInteger[] list = new BigInteger[]{};
        RlpList bean = RlpDecoder.decode(rlpString.getBytes());
        List <RlpType> beanList = ((RlpList) bean.getValues().get(0)).getValues();
        byte[] paramaBytes = new byte[0];
        byte[] parambBytes = new byte[0];
        for (RlpType beanType : beanList) {
            RlpList beanTypeList = (RlpList) beanType;
            RlpString parama = (RlpString) beanTypeList.getValues().get(0);
            RlpString paramb = (RlpString) beanTypeList.getValues().get(1);
            paramaBytes = RlpEncoder.encode(parama);
            parambBytes = RlpEncoder.encode(paramb);
            list[0] = new BigInteger(1, paramaBytes);
            list[1] = new BigInteger(1, parambBytes);
        }
        return list;
    }




}