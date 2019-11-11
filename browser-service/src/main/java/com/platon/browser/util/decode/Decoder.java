package com.platon.browser.util.decode;

import com.platon.browser.param.RestrictingCreateParam;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 交易输入解码器基类
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:14:38
 **/
public abstract class Decoder {

    public static BigInteger bigIntegerResolver(RlpString rlpString) {
        RlpString integers = rlpString;
        RlpList integersList = RlpDecoder.decode(integers.getBytes());
        RlpString integersString = (RlpString) integersList.getValues().get(0);
        return new BigInteger(1, integersString.getBytes());
    }

    public static String stringResolver(RlpString rlpString) {
        RlpString strings = rlpString;
        RlpList stringsList = RlpDecoder.decode(strings.getBytes());
        RlpString stringsListString = (RlpString) stringsList.getValues().get(0);
        return Numeric.toHexString(stringsListString.getBytes());
    }

    public static List<RestrictingCreateParam.RestrictingPlan> resolvePlan(RlpString rlpString) {
        List<RestrictingCreateParam.RestrictingPlan> list = new ArrayList<>();
        RlpList bean = RlpDecoder.decode(rlpString.getBytes());
        List <RlpType> beanList = ((RlpList) bean.getValues().get(0)).getValues();
        for (RlpType beanType : beanList) {
            RlpList beanTypeList = (RlpList) beanType;
            RlpString parama = (RlpString) beanTypeList.getValues().get(0);
            RlpString paramb = (RlpString) beanTypeList.getValues().get(1);
            RestrictingCreateParam.RestrictingPlan planParam = RestrictingCreateParam.RestrictingPlan.builder()
                    .epoch(parama.asPositiveBigInteger().intValue())
                    .amount(new BigDecimal(paramb.asPositiveBigInteger()))
                    .build();
            list.add(planParam);
        }
        return list;
    }
}
