package com.platon.browser.util.decode;

import com.platon.browser.param.ReportParam;
import com.platon.browser.param.TxParam;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:13:04
 **/
public class ReportDecoder extends Decoder {

    static TxParam decode(RlpList rootList) {
        // 举报双签
        //type
        BigInteger type = bigIntegerResolver((RlpString) rootList.getValues().get(1));
        //data
        String evidence = stringResolver((RlpString) rootList.getValues().get(2));
        evidence = new String(Numeric.hexStringToByteArray(evidence));

        evidence=normalization(evidence);

        ReportParam param = ReportParam.builder()
                .type(type)
                .data(evidence)
                .build();
        param.init();
        return param;
    }
}
