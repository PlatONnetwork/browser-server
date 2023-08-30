package com.platon.browser.decoder.ppos;

import com.platon.browser.param.TxParam;
import com.platon.browser.param.VersionDeclareParam;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;

import java.math.BigInteger;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:13:04
 **/
public class VersionDeclareDecoder extends AbstractPPOSDecoder {
    private VersionDeclareDecoder(){}

    /**
     * 治理合约，版本声明的参数：
     * declareVersion(activeNode discover.NodeID, programVersion uint32, programVersionSign common.VersionSign)
     * @param rootList
     * @return
     */
    public static TxParam decode(RlpList rootList) {
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
