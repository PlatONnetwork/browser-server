package com.platon.browser.decoder.ppos;

import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.TxParam;
import com.platon.contracts.ppos.dto.common.ErrorCode;
import com.platon.rlp.solidity.RlpDecoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;
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

    /**
     * // addLog let the result add to event.
     * // 参数datas可为空,里面的值不能为空
     * // Log.data字段编码规则:
     * // 如果datas为空,  rlp([errCodeString]),
     * // 如果datas不为空,rlp([errCodeString,rlp(data1),rlp(data2)...]),
     * 对撤销委托，返回的业务数据的顺序是：
     * issueIncome, released, restrictingPlan, lockReleased, lockRestrictingPlan
     *
     * @param rootList
     * @param errCode
     * @param rlpDataList
     * @return
     */
    public static TxParam decode(RlpList rootList, int errCode, List<RlpType> rlpDataList) {
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
        delegateExitParam.setDecodedStatus(String.valueOf(errCode));

        if (errCode == ErrorCode.SUCCESS) {
            BigInteger delegateIncome = ((RlpString) RlpDecoder.decode(((RlpString) rlpDataList.get(0)).getBytes()).getValues().get(0)).asPositiveBigInteger();
            delegateExitParam.setDelegateIncome(new BigDecimal(delegateIncome));
            if (rlpDataList.size() ==5) {
                BigInteger released = ((RlpString) RlpDecoder.decode(((RlpString) rlpDataList.get(1)).getBytes()).getValues().get(0)).asPositiveBigInteger();
                BigInteger restrictingPlan = ((RlpString) RlpDecoder.decode(((RlpString) rlpDataList.get(2)).getBytes()).getValues().get(0)).asPositiveBigInteger();
                BigInteger lockReleased = ((RlpString) RlpDecoder.decode(((RlpString) rlpDataList.get(3)).getBytes()).getValues().get(0)).asPositiveBigInteger();
                BigInteger lockRestrictingPlan = ((RlpString) RlpDecoder.decode(((RlpString) rlpDataList.get(4)).getBytes()).getValues().get(0)).asPositiveBigInteger();
                delegateExitParam.setReleased(new BigDecimal(released))
                        .setRestrictingPlan(new BigDecimal(restrictingPlan))
                        .setLockReleased(new BigDecimal(lockReleased))
                        .setLockRestrictingPlan(new BigDecimal(lockRestrictingPlan));
            }
        }

        return delegateExitParam;
    }
}
