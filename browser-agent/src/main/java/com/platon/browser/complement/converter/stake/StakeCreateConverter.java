package com.platon.browser.complement.converter.stake;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.complement.dao.param.stake.StakeCreate;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.param.StakeCreateParam;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.util.DateUtil;
import com.platon.browser.utils.HexTool;
import com.platon.browser.utils.VerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;


/**
 * @description: 创建验证人(质押)业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class StakeCreateConverter extends BusinessParamConverter<NodeOpt> {
	
    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;
    @Autowired
	private ParameterService parameterService;

    @Override
    public NodeOpt convert(CollectionEvent event, Transaction tx) {
		// 失败的交易不分析业务数据
		if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return null;

		long startTime = System.currentTimeMillis();

        StakeCreateParam txParam = tx.getTxParam(StakeCreateParam.class);
        BigInteger bigVersion = VerUtil.transferBigVersion(txParam.getProgramVersion());
        BigInteger stakingBlockNum = BigInteger.valueOf(tx.getNum());

        String configVal = parameterService.getValueInBlockChainConfig(ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
        if(StringUtils.isBlank(configVal)){
        	throw new BusinessException("参数表参数缺失："+ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
		}
        Date txTime = DateUtil.covertTime(tx.getTime());
        Integer  unStakeFreezeDuration = Integer.parseInt(configVal);
        StakeCreate businessParam= StakeCreate.builder()
        		.nodeId(txParam.getNodeId())
        		.stakingHes(txParam.getAmount())
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
        		.joinTime(txTime)
        		.txHash(tx.getHash())
				.delegateRewardPer(txParam.getDelegateRewardPer())
				.unStakeFreezeDuration(unStakeFreezeDuration)
                .build();

        stakeBusinessMapper.create(businessParam);
        
        updateNodeCache(HexTool.prefix(txParam.getNodeId()),txParam.getNodeName(),stakingBlockNum);
        
        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
		nodeOpt.setNodeId(txParam.getNodeId());
		nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.CREATE.getCode()));
		nodeOpt.setTxHash(tx.getHash());
		nodeOpt.setBNum(tx.getNum());
		nodeOpt.setTime(txTime);

		log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return nodeOpt;
    }
}
