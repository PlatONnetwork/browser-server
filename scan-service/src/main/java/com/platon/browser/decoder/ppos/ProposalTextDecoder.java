package com.platon.browser.decoder.ppos;

import com.platon.browser.param.ProposalTextParam;
import com.platon.browser.param.TxParam;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.utils.Numeric;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 **/
public class ProposalTextDecoder extends AbstractPPOSDecoder {
    private ProposalTextDecoder(){}
    public static TxParam decode(RlpList rootList) {
        // 提交文本提案
        //提交提案的验证人
        String nodeId = stringResolver((RlpString) rootList.getValues().get(1));
        //pIDID
        String pIdID = stringResolver((RlpString) rootList.getValues().get(2));
        pIdID =  new String(Numeric.hexStringToByteArray(pIdID));

        return ProposalTextParam.builder()
                .verifier(nodeId)
                .pIDID(pIdID)
                .build();
    }
}
