package com.platon.browser.util.decode;

import com.platon.browser.param.ProposalTextParam;
import com.platon.browser.param.TxParam;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:13:04
 **/
public class ProposalTextDecoder extends Decoder {

    static TxParam decode(RlpList rootList) {
        // 提交文本提案
        //提交提案的验证人
        String nodeId = stringResolver((RlpString) rootList.getValues().get(1));
        //pIDID
        String pIdID = stringResolver((RlpString) rootList.getValues().get(2));
        pIdID =  new String(Numeric.hexStringToByteArray(pIdID));

        ProposalTextParam param = ProposalTextParam.builder()
                .verifier(nodeId)
                .pIDID(pIdID)
                .build();
        return param;
    }
}
