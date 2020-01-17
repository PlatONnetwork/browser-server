package com.platon.browser.util.decode.innercontract;

import com.platon.browser.param.TxParam;
import com.platon.browser.param.VersionDeclareParam;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;

import java.math.BigInteger;

import static com.platon.browser.util.decode.innercontract.InnerContractDecoder.bigIntegerResolver;
import static com.platon.browser.util.decode.innercontract.InnerContractDecoder.stringResolver;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:13:04
 **/
public class VersionDeclareDecoder {
    private VersionDeclareDecoder(){}
    static TxParam decode(RlpList rootList) {
        // 版本声明
        //声明的节点，只能是验证人/候选人
        String nodeId = stringResolver((RlpString) rootList.getValues().get(1));
        //声明的版本，有rpc的getProgramVersion接口获取
        BigInteger version =  bigIntegerResolver((RlpString) rootList.getValues().get(2));
        //声明的版本签名，有rpc的getProgramVersion接口获取
        String versionSign = stringResolver((RlpString) rootList.getValues().get(3));
        return VersionDeclareParam.builder()
                .activeNode(nodeId)
                .version(version.intValue())
                .versionSigns(versionSign)
                .build();
    }
}
