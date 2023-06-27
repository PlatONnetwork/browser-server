package com.platon.browser.decoder.ppos;

import com.platon.browser.param.ProposalVoteParam;
import com.platon.browser.param.TxParam;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;

import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 **/
public class ProposalVoteDecoder extends AbstractPPOSDecoder {
    private ProposalVoteDecoder(){}
    public static TxParam decode(RlpList rootList) {
        // 给提案投票
        //投票验证人
        String nodeId = stringResolver((RlpString) rootList.getValues().get(1));
        //提案ID
        String proposalID = stringResolver((RlpString) rootList.getValues().get(2));
        //投票选项
        BigInteger option =  bigIntegerResolver((RlpString) rootList.getValues().get(3));
        //节点代码版本，有rpc的getProgramVersion接口获取
        BigInteger programVersion =  bigIntegerResolver((RlpString) rootList.getValues().get(4));
        //代码版本签名，有rpc的getProgramVersion接口获取
        String versionSign = stringResolver((RlpString) rootList.getValues().get(5));

        return ProposalVoteParam.builder()
                .verifier(nodeId)
                .proposalId(proposalID)
                .option(option.toString())
                .programVersion(programVersion.toString())
                .versionSign(versionSign)
                .build();
    }
}
