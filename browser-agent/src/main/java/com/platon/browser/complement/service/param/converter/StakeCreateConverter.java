package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.complement.dao.param.stake.StakeCreate;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.StakeCreateParam;
import com.platon.browser.utils.HexTool;
import com.platon.browser.utils.VerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;


/**
 * @description: 创建验证人业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class StakeCreateConverter extends BusinessParamConverter<Optional<NodeOpt>> {
	
    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;

    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
        StakeCreateParam txParam = tx.getTxParam(StakeCreateParam.class);
        BigInteger bigVersion = VerUtil.transferBigVersion(txParam.getProgramVersion());
        BigInteger stakingBlockNum = BigInteger.valueOf(tx.getNum());
        StakeCreate businessParam= StakeCreate.builder()
        		.nodeId(txParam.getNodeId())
        		.stakingHes(new BigDecimal(txParam.getAmount()))
        		.nodeName(txParam.getNodeName())
        		.externalId(txParam.getExternalId())
        		.benefitAddr(txParam.getBenefitAddress())
        		.programVersion(txParam.getProgramVersion().toString())
        		.bigVersion(bigVersion.toString())
        		.webSite(txParam.getWebsite())
        		.details(txParam.getDetails())
        		.isInit(isInit(txParam.getBenefitAddress())) 
        		.stakingBlockNum(stakingBlockNum)
        		.stakingTxIndex(tx.getIndex())
        		.stakingAddr(tx.getFrom())
        		.joinTime(tx.getTime())
        		.txHash(tx.getHash())               
                .build();
        
        stakeBusinessMapper.create(businessParam);
        
        updateNodeCache(HexTool.prefix(txParam.getNodeId()),txParam.getNodeName(),stakingBlockNum);
        
        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
		nodeOpt.setNodeId(txParam.getNodeId());
		nodeOpt.setType(Integer.valueOf(CustomNodeOpt.TypeEnum.CREATE.getCode()));
		nodeOpt.setTxHash(tx.getHash());
		nodeOpt.setBNum(tx.getNum());
		nodeOpt.setTime(tx.getTime());
        return Optional.ofNullable(nodeOpt);
    }
}
