package com.platon.browser.decoder.ppos;

import com.platon.browser.param.StakeCreateParam;
import com.platon.browser.param.TxParam;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 *
 * return Arrays.<Type>asList(new Uint16(stakingAmountType.getValue()) 1
 *         , new BytesType(Numeric.hexStringToByteArray(benifitAddress)) 2
 *         , new BytesType(Numeric.hexStringToByteArray(nodeId)) 3
 *         , new Utf8String(externalId) 4
 *         , new Utf8String(nodeName) 5
 *         , new Utf8String(webSite) 6
 *         , new Utf8String(details) 7
 *         , new Int256(amount) 8
 *         , new Uint16(rewardPer) 9
 *         , new Uint32(processVersion.getProgramVersion()) 10
 *         , new BytesType(Numeric.hexStringToByteArray(processVersion.getProgramVersionSign())) 11
 *         , new BytesType(Numeric.hexStringToByteArray(blsPubKey)) 12
 *         , new BytesType(Numeric.hexStringToByteArray(blsProof)) 13
 **/
public class StakeCreateDecoder extends AbstractPPOSDecoder {
    private StakeCreateDecoder(){}
    public static TxParam decode(RlpList rootList) {
        // 发起质押
        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
        BigInteger type =  bigIntegerResolver((RlpString) rootList.getValues().get(1));
        //用于接受出块奖励和质押奖励的收益账户benefitAddress
        String address = addressResolver((RlpString) rootList.getValues().get(2));
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(3));
        //外部Id externalId
        String externalId = stringResolver((RlpString) rootList.getValues().get(4));
        externalId = new String(Numeric.hexStringToByteArray(externalId));
        //被质押节点的名称 nodeName
        String nodeName = stringResolver((RlpString) rootList.getValues().get(5));
        nodeName = new String(Numeric.hexStringToByteArray(nodeName));
        //节点的第三方主页 website
        String website = stringResolver((RlpString) rootList.getValues().get(6));
        website = new String(Numeric.hexStringToByteArray(website));
        //节点的描述 details
        String details = stringResolver((RlpString) rootList.getValues().get(7));
        details = new String(Numeric.hexStringToByteArray(details));
        //质押的von amount programVersion
        BigInteger amount =  bigIntegerResolver((RlpString) rootList.getValues().get(8));
        //程序的真实版本，治理rpc获取
        BigInteger rewardPer =  bigIntegerResolver((RlpString) rootList.getValues().get(9));
        //程序的真实版本，治理rpc获取
        BigInteger version =  bigIntegerResolver((RlpString) rootList.getValues().get(10));

        return StakeCreateParam.builder()
                .type(type.intValue())
                .benefitAddress(address)
                .nodeId(nodeId)
                .externalId("0x".equals(externalId)?"":externalId)
                .nodeName(nodeName)
                .website(website)
                .details(details)
                .amount(new BigDecimal(amount))
                .programVersion(version)
                .delegateRewardPer(rewardPer.intValue()) // 必填
                .build();
    }
}
