package com.platon.browser.complement.converter.stake;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.complement.dao.param.stake.StakeCreate;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.param.StakeCreateParam;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.utils.HexTool;
import com.platon.browser.utils.VerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;


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
    @Autowired
	private BlockChainConfig chainConfig;

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
        Integer  unStakeFreezeDuration = Integer.parseInt(configVal);
        BigInteger unStakeEndBlock = event.getEpochMessage()
				.getSettleEpochRound() // 当前块所处的结算周期轮数
				.add(BigInteger.valueOf(unStakeFreezeDuration)) //+ 解质押需要经过的结算周期轮数
				.multiply(chainConfig.getSettlePeriodBlockCount()); // x 每个结算周期的区块数
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
        		.joinTime(tx.getTime())
        		.txHash(tx.getHash())
				.delegateRewardPer(txParam.getDelegateRewardPer())
				.unStakeFreezeDuration(unStakeFreezeDuration)
				.unStakeEndBlock(unStakeEndBlock)
                .build();

        stakeBusinessMapper.create(businessParam);
        
        updateNodeCache(HexTool.prefix(txParam.getNodeId()),txParam.getNodeName(),stakingBlockNum);
        
        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
		nodeOpt.setNodeId(txParam.getNodeId());
		nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.CREATE.getCode()));
		nodeOpt.setTxHash(tx.getHash());
		nodeOpt.setBNum(tx.getNum());
		nodeOpt.setTime(tx.getTime());

		log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return nodeOpt;
    }
}
