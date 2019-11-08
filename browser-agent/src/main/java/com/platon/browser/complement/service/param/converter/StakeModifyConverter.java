package com.platon.browser.complement.service.param.converter;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.complement.param.stake.StakeModify;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.mapper.StakeBusinessMapper;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.param.StakeModifyParam;
import com.platon.browser.utils.HexTool;

/**
 * @description: 修改验证人业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class StakeModifyConverter extends BusinessParamConverter<Optional<ComplementNodeOpt>> {
	
    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;

    @Override
    public Optional<ComplementNodeOpt> convert(CollectionEvent event, CollectionTransaction tx) {
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
                .build();
        
        stakeBusinessMapper.modify(businessParam);
        // 更新节点缓存
        updateNodeCache(HexTool.prefix(txParam.getNodeId()),txParam.getNodeName());
        
        ComplementNodeOpt complementNodeOpt = ComplementNodeOpt.newInstance();
        complementNodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
		complementNodeOpt.setNodeId(txParam.getNodeId());
		complementNodeOpt.setType(Integer.valueOf(CustomNodeOpt.TypeEnum.MODIFY.getCode()));
		complementNodeOpt.setTxHash(tx.getHash());
		complementNodeOpt.setBNum(tx.getNum());
		complementNodeOpt.setTime(tx.getTime());   
        return Optional.ofNullable(complementNodeOpt);
    }
}
