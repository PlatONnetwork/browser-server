package com.platon.browser.util.decode;

import com.platon.browser.param.StakeIncreaseParam;
import com.platon.browser.param.TxParam;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;

import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:13:04
 **/
public class StakeIncreaseDecoder extends Decoder {

    static TxParam decode(RlpList rootList) {
        // 增持质押
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(1));
        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
        BigInteger type =  bigIntegerResolver((RlpString) rootList.getValues().get(2));
        //质押的von
        BigInteger amount =  bigIntegerResolver((RlpString) rootList.getValues().get(3));

        StakeIncreaseParam param = StakeIncreaseParam.builder()
                .nodeId(nodeId)
                .type(type.intValue())
                .amount(amount.toString())
                .stakingBlockNum(null)
                .build();
        return param;
    }
}
