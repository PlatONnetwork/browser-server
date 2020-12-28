package com.platon.browser.decoder.ppos;

import com.platon.browser.param.ReportParam;
import com.platon.browser.param.TxParam;
import com.alaya.rlp.solidity.RlpList;
import com.alaya.rlp.solidity.RlpString;
import com.alaya.utils.Numeric;

import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 **/
class ReportDecoder {
    private ReportDecoder(){}
    static TxParam decode(RlpList rootList) {
        // 举报双签
        //type
        BigInteger type = InnerContractDecoder.bigIntegerResolver((RlpString) rootList.getValues().get(1));
        //data
        String evidence = InnerContractDecoder.stringResolver((RlpString) rootList.getValues().get(2));
        evidence = new String(Numeric.hexStringToByteArray(evidence));

        evidence= InnerContractDecoder.normalization(evidence);

        return ReportParam.builder()
                .type(type)
                .data(evidence)
                .build().init();
    }
}
