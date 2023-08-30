package com.platon.browser.decoder.ppos;

import com.platon.browser.param.ReportParam;
import com.platon.browser.param.TxParam;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.utils.Numeric;

import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 **/
public class ReportDecoder extends AbstractPPOSDecoder {
    private ReportDecoder(){}

    /**
     * 惩罚合约，举报双签的参数：
     *  reportDuplicateSign(dupType uint8, data string)
     * @param rootList
     * @return
     */
    public static TxParam decode(RlpList rootList) {
        // 举报双签
        //type
        BigInteger type = bigIntegerResolver((RlpString) rootList.getValues().get(1));
        //data
        String evidence = stringResolver((RlpString) rootList.getValues().get(2));
        evidence = new String(Numeric.hexStringToByteArray(evidence));

        evidence= normalization(evidence);

        return ReportParam.builder()
                .type(type)
                .data(evidence)
                .build().init();
    }
}
