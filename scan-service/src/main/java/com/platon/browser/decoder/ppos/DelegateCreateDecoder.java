package com.platon.browser.decoder.ppos;

import com.platon.browser.param.DelegateCreateParam;
import com.platon.browser.param.TxParam;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 **/
public class DelegateCreateDecoder extends AbstractPPOSDecoder{
    private DelegateCreateDecoder(){}
    public static TxParam decode(RlpList rootList) {
        // 发起委托
        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
        BigInteger type =  bigIntegerResolver((RlpString) rootList.getValues().get(1));
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(2));
        //委托的金额
        BigInteger amount = bigIntegerResolver((RlpString) rootList.getValues().get(3));

        return DelegateCreateParam.builder()
                .type(type.intValue())
                .nodeId(nodeId)
                .amount(new BigDecimal(amount))
                .nodeName("")
                .stakingBlockNum(null)
                .build();
    }
}
