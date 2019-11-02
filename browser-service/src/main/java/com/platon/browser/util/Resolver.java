package com.platon.browser.util;

import com.platon.browser.param.PlanParam;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * rlp处理数据类
 * User: dongqile
 * Date: 2019/8/13
 * Time: 16:48
 */
public  class Resolver {
    private Resolver(){}

    public static BigInteger bigIntegerResolver ( RlpString rlpString ) {
        RlpString integers = rlpString;
        RlpList integersList = RlpDecoder.decode(integers.getBytes());
        RlpString integersString = (RlpString) integersList.getValues().get(0);
        return new BigInteger(1, integersString.getBytes());
    }

    public static String stringResolver ( RlpString rlpString ) {
        RlpString strings = rlpString;
        RlpList stringsList = RlpDecoder.decode(strings.getBytes());
        RlpString stringsListString = (RlpString) stringsList.getValues().get(0);
        return Numeric.toHexString(stringsListString.getBytes());
    }

    public static List<PlanParam> objectResolver ( RlpString rlpString ) {
        List<PlanParam> list = new ArrayList <>();
        RlpList bean = RlpDecoder.decode(rlpString.getBytes());
        List <RlpType> beanList = ((RlpList) bean.getValues().get(0)).getValues();
        for (RlpType beanType : beanList) {
            RlpList beanTypeList = (RlpList) beanType;
            RlpString parama = (RlpString) beanTypeList.getValues().get(0);
            RlpString paramb = (RlpString) beanTypeList.getValues().get(1);
            PlanParam planParam = PlanParam.builder()
                    .epoch(parama.asPositiveBigInteger().intValue())
                    .amount(paramb.asPositiveBigInteger().toString())
                    .build();
            list.add(planParam);
        }
        return list;
    }
}
