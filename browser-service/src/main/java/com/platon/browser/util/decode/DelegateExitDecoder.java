package com.platon.browser.util.decode;

import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.TxParam;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;

import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:13:04
 **/
public class DelegateExitDecoder extends Decoder {

    static TxParam decode(RlpList rootList) {
        // 减持/撤销委托
        //代表着某个node的某次质押的唯一标示
        String blockNumber = stringResolver((RlpString) rootList.getValues().get(1));
        blockNumber=blockNumber.replace("0x","");
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(2));
        //减持委托的金额(按照最小单位算，1LAT = 10**18 von)
        BigInteger amount =  bigIntegerResolver((RlpString) rootList.getValues().get(3));

        DelegateExitParam param = DelegateExitParam.builder()
                .stakingBlockNum(new BigInteger(blockNumber,16))
                .nodeId(nodeId)
                .amount(amount.toString())
                .build();
        return param;
    }
}
