package com.platon.browser.complement.service.param.converter;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.stake.StakeModify;
import com.platon.browser.param.StakeModifyParam;
import com.platon.browser.utils.HexTool;

/**
 * @description: 修改验证人业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class StakeModifyConverter extends BusinessParamConverter<StakeModify> {

    @Override
    public StakeModify convert(CollectionTransaction tx) {
        // 修改质押信息
        StakeModifyParam txParam = tx.getTxParam(StakeModifyParam.class);
        StakeModify businessParam= StakeModify.builder()
        		.nodeId(txParam.getNodeId())
        		.nodeName(txParam.getNodeName())
        		.externalId(txParam.getExternalId())
        		.benefitAddr(txParam.getBenefitAddress())
        		.webSite(txParam.getWebsite())
        		.details(txParam.getDetails())
        		.isInit(isInit(txParam.getBenefitAddress())) 
        		.stakingBlockNum(BigInteger.valueOf(tx.getNum()))
        		.time(tx.getTime())
        		.txHash(tx.getHash())               
                .build();
        // 更新节点缓存
        updateNodeCache(HexTool.prefix(txParam.getNodeId()),txParam.getNodeName());
        return businessParam;
    }
}
