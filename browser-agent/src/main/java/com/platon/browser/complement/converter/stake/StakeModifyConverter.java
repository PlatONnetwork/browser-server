package com.platon.browser.complement.converter.stake;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.complement.dao.param.stake.StakeModify;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.StakeModifyParam;
import com.platon.browser.utils.HexTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

/**
 * @description: 修改验证人业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
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

        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return Optional.ofNullable(null);

        long startTime = System.currentTimeMillis();


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
		nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.MODIFY.getCode()));
		nodeOpt.setTxHash(tx.getHash());
		nodeOpt.setBNum(tx.getNum());
		nodeOpt.setTime(tx.getTime());

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return Optional.ofNullable(nodeOpt);
    }
}
