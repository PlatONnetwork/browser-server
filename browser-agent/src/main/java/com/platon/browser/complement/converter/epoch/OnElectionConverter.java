package com.platon.browser.complement.converter.epoch;

import com.platon.browser.client.HistoryLowRateSlash;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.complement.dao.param.epoch.Election;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomStaking.StatusEnum;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.utils.HexTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OnElectionConverter {
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;
    @Autowired
    private BlockChainConfig blockChainConfig;
	@Autowired
	private SpecialApi specialApi;
	@Autowired
	private PlatOnClient platOnClient;
	@Autowired
	private StakingMapper stakingMapper;
	@Autowired
	private ParameterService parameterService;
	
	public List<NodeOpt> convert(CollectionEvent event, Block block) {
		long startTime = System.currentTimeMillis();
		// 操作日志列表
		List<NodeOpt> nodeOpts = new ArrayList<>();
		try {
			Web3j web3j = platOnClient.getWeb3jWrapper().getWeb3j();
			List<HistoryLowRateSlash> slashList = specialApi.getHistoryLowRateSlashList(web3j,BigInteger.valueOf(block.getNum()));
			if(!slashList.isEmpty()){
				List<String> slashNodeIdList = new ArrayList<>();
				// 统一节点ID格式： 0x开头
				slashList.forEach(n->slashNodeIdList.add(HexTool.prefix(n.getNodeId())));
				log.info("低出块节点：{}",slashNodeIdList);
				// 查询节点
				StakingExample stakingExample = new StakingExample();
				stakingExample.createCriteria()
						.andStatusEqualTo(CustomStaking.StatusEnum.CANDIDATE.getCode())
						.andNodeIdIn(slashNodeIdList);
				List<Staking> slashStakingList = stakingMapper.selectByExample(stakingExample);
				if(slashStakingList.isEmpty()){
					log.info("特殊节点查询到的低出块率节点["+slashNodeIdList+"]在staking表中查询不到对应的候选中节点数据!");
				}else {
					//处罚低出块率的节点;
					BigInteger curSettleEpoch = EpochUtil.getEpoch(BigInteger.valueOf(block.getNum()),blockChainConfig.getSettlePeriodBlockCount());
					List<NodeOpt> exceptionNodeOpts = slash(block,curSettleEpoch.intValue(),slashStakingList, event.getEpochMessage().getBlockReward());
					nodeOpts.addAll(exceptionNodeOpts);
					log.debug("被处罚节点列表["+slashStakingList+"]");
				}
			}
		} catch (Exception e) {
			log.error("OnElectionConverter error", e);
			throw new BusinessException(e.getMessage());
		}
		log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
       return nodeOpts;
	}

	/**
	 * 处罚节点
	 * @param block 区块
	 * @param settleEpoch 所在结算周期
	 * @param slashNodeList 被处罚的节点列表
	 * @return
	 */
	private List<NodeOpt> slash(Block block, int settleEpoch, List<Staking> slashNodeList,BigDecimal blockReward){
		// 更新解质押到账需要经过的结算周期数
		String configVal = parameterService.getValueInBlockChainConfig(ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
		if(StringUtils.isBlank(configVal)){
			throw new BusinessException("参数表参数缺失："+ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
		}
		Integer  unStakeFreezeDuration = Integer.parseInt(configVal);
		//惩罚节点
		Election election = Election.builder()
				.time(block.getTime())
				.settingEpoch(settleEpoch)
				.slashNodeList(slashNodeList)
				.unStakeFreezeDuration(unStakeFreezeDuration)
				.build();

		//节点操作日志
		BigInteger bNum = BigInteger.valueOf(block.getNum());
		List<NodeOpt> nodeOpts = slashNodeList.stream()
				.map(node -> {
					StringBuffer desc = new StringBuffer("0|");
					/**
					 * 如果低出块惩罚不等于0的时候，需要配置惩罚金额
					 */
					String amount =  blockReward
							.multiply(blockChainConfig.getSlashBlockRewardCount()).toString();
					desc.append(blockChainConfig.getSlashBlockRewardCount().toString()).append("|").append(amount).append( "|1");
					NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
					nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
					nodeOpt.setNodeId(node.getNodeId());
					nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.LOW_BLOCK_RATE.getCode()));
					nodeOpt.setBNum(bNum.longValue());
					nodeOpt.setTime(block.getTime());
					nodeOpt.setDesc(desc.toString());
					
					 //当前锁定的大于零则扣除金额，不大于则保留
			        BigDecimal codeCurStakingLocked = BigDecimal.ZERO;
			        if(node.getStakingLocked().compareTo(BigDecimal.ZERO) > 0) {
			        	codeCurStakingLocked = node.getStakingLocked().subtract(new BigDecimal(amount));
			        	/**
				         * 如果扣减的结果小于0则设置为0
				         */
				        if(codeCurStakingLocked.compareTo(BigDecimal.ZERO) < 0) {
				        	codeCurStakingLocked = BigDecimal.ZERO;
				        }
			        	node.setStakingLocked(codeCurStakingLocked);
			        }
			        /**
			         * 如果节点状态为退出中则需要reduction进行扣减
			         */
			        if(node.getStatus().intValue() ==  StatusEnum.EXITING.getCode()) {
			        	codeCurStakingLocked = node.getStakingReduction().subtract(new BigDecimal(amount));
			        	/**
				         * 如果扣减的结果小于0则设置为0
				         */
				        if(codeCurStakingLocked.compareTo(BigDecimal.ZERO) < 0) {
				        	codeCurStakingLocked = BigDecimal.ZERO;
				        }
			        	node.setStakingReduction(codeCurStakingLocked);
			        }
			        
					return nodeOpt;
				}).collect(Collectors.toList());
		epochBusinessMapper.slashNode(election);
		return nodeOpts;
	}
}
