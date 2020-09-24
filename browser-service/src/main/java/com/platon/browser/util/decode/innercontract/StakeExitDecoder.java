package com.platon.browser.util.decode.innercontract;

import com.platon.browser.param.StakeExitParam;
import com.platon.browser.param.TxParam;
import com.alaya.rlp.solidity.RlpList;
import com.alaya.rlp.solidity.RlpString;

import static com.platon.browser.util.decode.innercontract.InnerContractDecoder.stringResolver;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:13:04
 **/
class StakeExitDecoder {
    private StakeExitDecoder(){}
    static TxParam decode(RlpList rootList) {
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(1));
        return StakeExitParam.builder()
                .nodeId(nodeId)
                .nodeName("")
                .stakingBlockNum(null)
                .build();
    }
}
