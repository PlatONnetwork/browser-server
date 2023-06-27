package com.platon.browser.decoder.ppos;

import com.platon.browser.param.StakeModifyParam;
import com.platon.browser.param.TxParam;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.utils.Numeric;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 */
public class StakeModifyDecoder extends AbstractPPOSDecoder {
    private StakeModifyDecoder(){}
    public static TxParam decode(RlpList rootList) {
        // 修改质押信息
        //用于接受出块奖励和质押奖励的收益账户
        String benefitAddress = addressResolver((RlpString) rootList.getValues().get(1));
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(2));
        //外部Id
        BigInteger rewardPer = bigIntegerResolver((RlpString) rootList.getValues().get(3));
        //外部Id externalId
        String externalId = stringResolver((RlpString) rootList.getValues().get(4));
        externalId = externalId==null?null:new String(Numeric.hexStringToByteArray(externalId));
        //被质押节点的名称
        String nodeName = stringResolver((RlpString) rootList.getValues().get(5));
        nodeName = nodeName==null?null:new String(Numeric.hexStringToByteArray(nodeName));
        //节点的第三方主页
        String website = stringResolver((RlpString) rootList.getValues().get(6));
        website = website==null?null:new String(Numeric.hexStringToByteArray(website));
        //节点的描述
        String detail = stringResolver((RlpString) rootList.getValues().get(7));
        detail = detail==null?null:new String(Numeric.hexStringToByteArray(detail));

        String externalId2 = externalId;
        if(StringUtils.isBlank(externalId)) externalId2 = null;
        if("0x".equals(externalId)) externalId2 = "";

        return StakeModifyParam.builder()
                .nodeId(nodeId)
                .benefitAddress(StringUtils.isBlank(benefitAddress)?null:benefitAddress) // 非必填
                .externalId(externalId2) // 非必填
                .nodeName(StringUtils.isBlank(nodeName)?null:nodeName) // 非必填
                .website(StringUtils.isBlank(website)?null:website) // 非必填
                .details(StringUtils.isBlank(detail)?null:detail) // 非必填
                .delegateRewardPer(rewardPer==null?null:rewardPer.intValue()) // 非必填
                .build();
    }
}
