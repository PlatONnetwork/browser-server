package com.platon.browser.decoder.ppos;

import com.platon.browser.param.ProposalParameterParam;
import com.platon.browser.param.TxParam;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.utils.Numeric;

/**
 * @description: 参数提案交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-25 10:13:04
 **/
public class ProposalParameterDecoder extends AbstractPPOSDecoder {
    private ProposalParameterDecoder(){}
    public static TxParam decode(RlpList rootList) {
        // 提交升级提案
        //提交提案的验证人
        String nodeId = stringResolver((RlpString) rootList.getValues().get(1));
        //pIDID
        String pIdID = stringResolver((RlpString) rootList.getValues().get(2));
        pIdID =  new String(Numeric.hexStringToByteArray(pIdID));
        //参数模块
        String module = stringResolver((RlpString) rootList.getValues().get(3));
        module =  new String(Numeric.hexStringToByteArray(module));
        //参数名称
        String name = stringResolver((RlpString) rootList.getValues().get(4));
        name =  new String(Numeric.hexStringToByteArray(name));
        //参数值
        String newValue = stringResolver((RlpString) rootList.getValues().get(5));
        newValue =  new String(Numeric.hexStringToByteArray(newValue));
        return ProposalParameterParam.builder()
                .verifier(nodeId)
                .pIDID(pIdID)
                .module(module)
                .name(name)
                .newValue(newValue)
                .build();
    }
}
