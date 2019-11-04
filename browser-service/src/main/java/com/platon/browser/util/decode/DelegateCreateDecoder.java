package com.platon.browser.util.decode;

import com.platon.browser.param.DelegateCreateParam;
import com.platon.browser.param.TxParam;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;

import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:13:04
 **/
public class DelegateCreateDecoder extends Decoder {

    static TxParam decode(RlpList rootList) {
        // 发起委托
        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
        BigInteger type =  bigIntegerResolver((RlpString) rootList.getValues().get(1));
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(2));
        //委托的金额
        BigInteger amount =  bigIntegerResolver((RlpString) rootList.getValues().get(3));

        DelegateCreateParam param = DelegateCreateParam.builder()
                .type(type.intValue())
                .nodeId(nodeId)
                .amount(amount.toString())
                .nodeName("")
                .stakingBlockNum("")
                .build();
        return param;
    }
}
