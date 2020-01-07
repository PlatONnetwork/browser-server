package com.platon.browser.util.decode;

import com.platon.browser.param.StakeModifyParam;
import com.platon.browser.param.TxParam;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import static com.platon.browser.util.decode.Decoder.bigIntegerResolver;
import static com.platon.browser.util.decode.Decoder.stringResolver;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:13:04
 *
 *  return Arrays.<Type>asList(new Uint16(stakingAmountType.getValue()) 1
 *          , new BytesType(Numeric.hexStringToByteArray(benifitAddress)) 2
 *          , new BytesType(Numeric.hexStringToByteArray(nodeId)) 3
 *          , new Utf8String(externalId) 4
 *          , new Utf8String(nodeName) 5
 *          , new Utf8String(webSite) 6
 *          , new Utf8String(details) 7
 *          , new Int256(amount) 8
 *          , new Uint16(rewardPer) 9
 *          , new Uint32(processVersion.getProgramVersion()) 10
 *          , new BytesType(Numeric.hexStringToByteArray(processVersion.getProgramVersionSign())) 11
 *          , new BytesType(Numeric.hexStringToByteArray(blsPubKey)) 12
 *          , new BytesType(Numeric.hexStringToByteArray(blsProof)) 13
 **/
public class StakeModifyDecoder {
    private StakeModifyDecoder(){}
    static TxParam decode(RlpList rootList) {
        // 修改质押信息
        //用于接受出块奖励和质押奖励的收益账户
        String address = stringResolver((RlpString) rootList.getValues().get(1));
        //被质押的节点的NodeId
        String nodeId = stringResolver((RlpString) rootList.getValues().get(2));
        //外部Id
        BigInteger rewardPer = bigIntegerResolver((RlpString) rootList.getValues().get(3));
        //外部Id externalId
        String externalId = stringResolver((RlpString) rootList.getValues().get(4));
        externalId = new String(Numeric.hexStringToByteArray(externalId));
        //被质押节点的名称
        String nodeName = stringResolver((RlpString) rootList.getValues().get(5));
        nodeName = new String(Numeric.hexStringToByteArray(nodeName));
        //节点的第三方主页
        String website = stringResolver((RlpString) rootList.getValues().get(6));
        website = new String(Numeric.hexStringToByteArray(website));
        //节点的描述
        String detail = stringResolver((RlpString) rootList.getValues().get(7));
        detail = new String(Numeric.hexStringToByteArray(detail));

        return StakeModifyParam.builder()
                .nodeId(nodeId)
                .benefitAddress(address)
                .externalId("0x".equals(externalId)?"":externalId)
                .nodeName(nodeName)
                .website(website)
                .details(detail)
                .delegateRewardPer(rewardPer.intValue()) // 非必填
                .build();
    }
}
