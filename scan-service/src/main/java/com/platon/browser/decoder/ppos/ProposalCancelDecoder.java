package com.platon.browser.decoder.ppos;

import com.platon.browser.param.ProposalCancelParam;
import com.platon.browser.param.TxParam;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 **/
public class ProposalCancelDecoder extends AbstractPPOSDecoder {
    private ProposalCancelDecoder(){}
    public static TxParam decode(RlpList rootList) {
        // 提交取消提案
        //提交提案的验证人
        String nodeId = stringResolver((RlpString) rootList.getValues().get(1));
        //本提案的pIDID
        String pIdID = stringResolver((RlpString) rootList.getValues().get(2));
        pIdID =  new String(Numeric.hexStringToByteArray(pIdID));
        //投票截止区块高度
        BigInteger round = bigIntegerResolver((RlpString) rootList.getValues().get(3));
        //被取消的pIDID
        String cancelPidID = stringResolver((RlpString) rootList.getValues().get(4));

        return ProposalCancelParam.builder()
                .verifier(nodeId)
                .pIDID(pIdID)
                .endVotingRound(new BigDecimal(round))
                .canceledProposalID(cancelPidID)
                .build();
    }
}
