package com.platon.browser.decoder.ppos;

import com.platon.browser.param.ProposalUpgradeParam;
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
public class ProposalUpgradeDecoder extends AbstractPPOSDecoder {
    private ProposalUpgradeDecoder(){}
    public static TxParam decode(RlpList rootList) {
        // 提交升级提案
        //提交提案的验证人
        String nodeId = stringResolver((RlpString) rootList.getValues().get(1));
        //pIDID
        String pIdID = stringResolver((RlpString) rootList.getValues().get(2));
        pIdID =  new String(Numeric.hexStringToByteArray(pIdID));
        //升级版本
        BigInteger version =  bigIntegerResolver((RlpString) rootList.getValues().get(3));
        //投票截止区块高度
        BigInteger round =  bigIntegerResolver((RlpString) rootList.getValues().get(4));
        //结束轮转换结束区块高度

        return ProposalUpgradeParam.builder()
                .verifier(nodeId)
                .endVotingRound(new BigDecimal(round))
                .newVersion(version.intValue())
                .pIDID(pIdID)
                .build();
    }
}
