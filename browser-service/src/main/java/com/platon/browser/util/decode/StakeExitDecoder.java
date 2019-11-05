package com.platon.browser.util.decode;

import com.platon.browser.param.StakeExitParam;
import com.platon.browser.param.TxParam;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:13:04
 **/
public class StakeExitDecoder extends Decoder {

    static TxParam decode(RlpList rootList) {
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(1));
        StakeExitParam param = StakeExitParam.builder()
                .nodeId(nodeId)
                .nodeName("")
                .stakingBlockNum(null)
                .build();
        return param;
    }
}
