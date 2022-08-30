package com.platon.browser.decoder.ppos;

import cn.hutool.core.collection.CollUtil;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.TxParam;
import com.platon.protocol.core.methods.response.Log;
import com.platon.rlp.solidity.RlpDecoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;
import com.platon.utils.Numeric;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 **/
public class DelegateExitDecoder extends AbstractPPOSDecoder {

    private DelegateExitDecoder() {
    }

    public static TxParam decode(RlpList rootList, List<Log> logs) {
        DelegateExitParam delegateExitParam = new DelegateExitParam();
        // 减持/撤销委托
        //代表着某个node的某次质押的唯一标示
        String blockNumber = stringResolver((RlpString) rootList.getValues().get(1));
        blockNumber = blockNumber.replace("0x", "");
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(2));
        //减持委托的金额(按照最小单位算，1LAT = 10**18 von)
        BigInteger amount = bigIntegerResolver((RlpString) rootList.getValues().get(3));
        BigInteger bl = BigInteger.ZERO;
        if (StringUtils.isNotBlank(blockNumber)) {
            bl = new BigInteger(blockNumber, 16);
        }
        delegateExitParam.setNodeId(nodeId).setStakingBlockNum(bl).setAmount(new BigDecimal(amount));
        if (CollUtil.isNotEmpty(logs)) {
            String logData = logs.get(0).getData();
            RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
            List<RlpType> rlpList = ((RlpList) (rlp.getValues().get(0))).getValues();
            String decodedStatus = new String(((RlpString) rlpList.get(0)).getBytes());
            BigInteger delegateIncome = ((RlpString) ((RlpList) RlpDecoder.decode(((RlpString) rlpList.get(1)).getBytes())).getValues().get(0)).asPositiveBigInteger();
            BigInteger released = ((RlpString) ((RlpList) RlpDecoder.decode(((RlpString) rlpList.get(2)).getBytes())).getValues().get(0)).asPositiveBigInteger();
            BigInteger restrictingPlan = ((RlpString) ((RlpList) RlpDecoder.decode(((RlpString) rlpList.get(3)).getBytes())).getValues().get(0)).asPositiveBigInteger();
            BigInteger lockReleased = ((RlpString) ((RlpList) RlpDecoder.decode(((RlpString) rlpList.get(4)).getBytes())).getValues().get(0)).asPositiveBigInteger();
            BigInteger lockRestrictingPlan = ((RlpString) ((RlpList) RlpDecoder.decode(((RlpString) rlpList.get(5)).getBytes())).getValues().get(0)).asPositiveBigInteger();
            delegateExitParam.setDecodedStatus(decodedStatus)
                             .setDelegateIncome(new BigDecimal(delegateIncome))
                             .setReleased(new BigDecimal(released))
                             .setRestrictingPlan(new BigDecimal(restrictingPlan))
                             .setLockReleased(new BigDecimal(lockReleased))
                             .setLockRestrictingPlan(new BigDecimal(lockRestrictingPlan));
        }
        return delegateExitParam;
    }

}
