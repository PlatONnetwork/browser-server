package com.platon.browser.util.decode.innercontract;

import com.platon.browser.param.RestrictingCreateParam;
import com.platon.browser.utils.NetworkParms;
import com.platon.sdk.utlis.Bech32;
import com.platon.sdk.utlis.NetworkParameters;

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
public interface InnerContractDecoder {

    static BigInteger bigIntegerResolver(RlpString rlpString) {
        RlpList integersList = RlpDecoder.decode(rlpString.getBytes());
        if(integersList.getValues().isEmpty()) return null;
        RlpString integersString = (RlpString) integersList.getValues().get(0);
        return new BigInteger(1, integersString.getBytes());
    }

    static String stringResolver(RlpString rlpString) {
        RlpList stringsList = RlpDecoder.decode(rlpString.getBytes());
        if(stringsList.getValues().isEmpty()) return null;
        RlpString stringsListString = (RlpString) stringsList.getValues().get(0);
        return Numeric.toHexString(stringsListString.getBytes());
    }
    
    static String addressResolver(RlpString rlpString) {
        RlpList stringsList = RlpDecoder.decode(rlpString.getBytes());
        if (stringsList.getValues().isEmpty()) return null;
        RlpString stringsListString = (RlpString) stringsList.getValues().get(0);
        /**
         * 判断是否为主网
         */
        String hrp = NetworkParameters.TestNetParams.getHrp();
        if(NetworkParms.getChainId().compareTo(NetworkParameters.MainNetParams.getChainId())==0) {
        	hrp = NetworkParameters.MainNetParams.getHrp();
        }
        return Bech32.addressEncode(hrp,Numeric.toHexString(stringsListString.getBytes()));
    }

    static List<RestrictingCreateParam.RestrictingPlan> resolvePlan(RlpString rlpString) {
        List<RestrictingCreateParam.RestrictingPlan> list = new ArrayList<>();
        RlpList bean = RlpDecoder.decode(rlpString.getBytes());
        List <RlpType> beanList = ((RlpList) bean.getValues().get(0)).getValues();
        for (RlpType beanType : beanList) {
            RlpList beanTypeList = (RlpList) beanType;
            RlpString parama = (RlpString) beanTypeList.getValues().get(0);
            RlpString paramb = (RlpString) beanTypeList.getValues().get(1);
            RestrictingCreateParam.RestrictingPlan planParam = RestrictingCreateParam.RestrictingPlan.builder()
                    .epoch(parama.asPositiveBigInteger())
                    .amount(new BigDecimal(paramb.asPositiveBigInteger()))
                    .build();
            list.add(planParam);
        }
        return list;
    }

    static String normalization(String json){
        if(json.startsWith("\"")){
            json=json.replaceFirst("\"","");
        }
        if(json.endsWith("\"")){
            json=json.substring(0,json.lastIndexOf('"'));
        }
        json=json.replace("\\","");

        return json;
    }
}
