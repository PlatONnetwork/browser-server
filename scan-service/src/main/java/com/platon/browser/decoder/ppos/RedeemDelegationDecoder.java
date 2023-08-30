package com.platon.browser.decoder.ppos;

import com.platon.browser.param.RedeemDelegationParm;
import com.platon.browser.param.TxParam;
import com.platon.contracts.ppos.dto.common.ErrorCode;
import com.platon.rlp.solidity.RlpDecoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 领取解锁的委托解码
 *
 * @date: 2022/8/30
 */
public class RedeemDelegationDecoder extends AbstractPPOSDecoder {

    public RedeemDelegationDecoder() {
    }

    /**
     * // addLog let the result add to event.
     * // 参数datas可为空,里面的值不能为空
     * // Log.data字段编码规则:
     * // 如果datas为空,  rlp([errCodeString]),
     * // 如果datas不为空,rlp([errCodeString,rlp(data1),rlp(data2)...]),
     * 对领取解锁的委托，返回的业务数据的顺序是：
     * released, restrictingPlan
     *
     * @param rootList
     * @param errCode
     * @param rlpDataList
     * @return
     */
    public static TxParam decode(RlpList rootList, int errCode, List<RlpType> rlpDataList) {
        RedeemDelegationParm redeemDelegationParm = new RedeemDelegationParm();
        redeemDelegationParm.setStatus(String.valueOf(errCode));

        if (errCode == ErrorCode.SUCCESS) {
            BigInteger released = ((RlpString) ((RlpList) RlpDecoder.decode(((RlpString) rlpDataList.get(0)).getBytes())).getValues().get(0)).asPositiveBigInteger();
            BigInteger restrictingPlan = ((RlpString) ((RlpList) RlpDecoder.decode(((RlpString) rlpDataList.get(1)).getBytes())).getValues().get(0)).asPositiveBigInteger();
            redeemDelegationParm
                    .setReleased(new BigDecimal(released))
                    .setRestrictingPlan(new BigDecimal(restrictingPlan));
            redeemDelegationParm
                    .setValue(redeemDelegationParm.getReleased().add(redeemDelegationParm.getRestrictingPlan()));
        }
        return redeemDelegationParm;
    }


}
