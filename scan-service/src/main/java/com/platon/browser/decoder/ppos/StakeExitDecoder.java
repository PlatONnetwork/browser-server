package com.platon.browser.decoder.ppos;

import com.platon.browser.param.StakeExitParam;
import com.platon.browser.param.TxParam;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 **/
public class StakeExitDecoder extends AbstractPPOSDecoder {
    private StakeExitDecoder(){}
    public static TxParam decode(RlpList rootList) {
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(1));
        return StakeExitParam.builder()
                .nodeId(nodeId)
                .nodeName("")
                .stakingBlockNum(null)
                .build();
    }
}
