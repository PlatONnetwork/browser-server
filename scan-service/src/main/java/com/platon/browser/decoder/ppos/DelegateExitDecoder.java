package com.platon.browser.decoder.ppos;

import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.TxParam;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 **/
public class DelegateExitDecoder extends AbstractPPOSDecoder{
    private DelegateExitDecoder(){}
    public static TxParam decode(RlpList rootList) {
        // 减持/撤销委托
        //代表着某个node的某次质押的唯一标示
        String blockNumber = stringResolver((RlpString) rootList.getValues().get(1));
        blockNumber=blockNumber.replace("0x","");
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(2));
        //减持委托的金额(按照最小单位算，1LAT = 10**18 von)
        BigInteger amount =  bigIntegerResolver((RlpString) rootList.getValues().get(3));

        BigInteger bl = BigInteger.ZERO;
        if(StringUtils.isNotBlank(blockNumber)) {
        	bl = new BigInteger(blockNumber,16);
        }
        return DelegateExitParam.builder()
                .stakingBlockNum(bl)
                .nodeId(nodeId)
                .amount(new BigDecimal(amount))
                .build();
    }
}
