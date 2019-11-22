package com.platon.browser.util.decode;

import com.platon.browser.param.StakeCreateParam;
import com.platon.browser.param.TxParam;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.platon.browser.util.decode.Decoder.bigIntegerResolver;
import static com.platon.browser.util.decode.Decoder.stringResolver;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:13:04
 **/
class StakeCreateDecoder {
    private StakeCreateDecoder(){}
    static TxParam decode(RlpList rootList) {
        // 发起质押
        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
        BigInteger type =  bigIntegerResolver((RlpString) rootList.getValues().get(1));
        //用于接受出块奖励和质押奖励的收益账户benefitAddress
        String address = stringResolver((RlpString) rootList.getValues().get(2));
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
        BigInteger version =  bigIntegerResolver((RlpString) rootList.getValues().get(9));

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
                .build();
    }
}
