package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.complement.dao.param.stake.StakeModify;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.StakeModifyParam;
import com.platon.browser.utils.HexTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

/**
 * @description: 修改验证人业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class StakeModifyConverter extends BusinessParamConverter<Optional<NodeOpt>> {
	
    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;

    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
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
        
        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
		nodeOpt.setNodeId(txParam.getNodeId());
		nodeOpt.setType(Integer.valueOf(CustomNodeOpt.TypeEnum.MODIFY.getCode()));
		nodeOpt.setTxHash(tx.getHash());
		nodeOpt.setBNum(tx.getNum());
		nodeOpt.setTime(tx.getTime());
        return Optional.ofNullable(nodeOpt);
    }
}
