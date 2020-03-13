package com.platon.browser.complement.converter.epoch;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.complement.dao.param.epoch.Election;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.utils.EpochUtil;
import com.platon.sdk.contracts.ppos.dto.resp.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
	private ParameterService parameterService;
	
	public Optional<List<NodeOpt>> convert(CollectionEvent event, Block block) throws BlockNumberException {

		long startTime = System.currentTimeMillis();

		//当前周期共识验证人
		List<String> curValidatorList = event.getEpochMessage().getCurValidatorList().stream()
				.map(Node::getNodeId).collect(Collectors.toList());

		//前一周期共识验证人
		List<String> preValidatorList = event.getEpochMessage().getPreValidatorList().stream()
				.map(Node::getNodeId).collect(Collectors.toList());
		if(preValidatorList.isEmpty()) {
			return Optional.ofNullable(null);
		}

		// 操作日志列表
		List<NodeOpt> nodeOpts = new ArrayList<>();

		/**1、先检查前一共识周期是否有异常节点，有则在此时进行处罚*/
		//查询在上一轮共识周期被设置为异常的节点列表
		List<Staking> preExceptionNodeList = epochBusinessMapper.getException(preValidatorList);
		if(!preExceptionNodeList.isEmpty()){
			//处罚上一共识周期被设置为异常的节点：即上上轮低出块的节点;
			BigInteger curConsEpochLastBlockNumber = EpochUtil.getCurEpochLastBlockNumber(BigInteger.valueOf(block.getNum()),blockChainConfig.getConsensusPeriodBlockCount());
			BigInteger prePreConsEpochLastBlockNumber = curConsEpochLastBlockNumber.subtract(blockChainConfig.getConsensusPeriodBlockCount().multiply(BigInteger.valueOf(2)));
			BigInteger prePreSettleEpoch = EpochUtil.getEpoch(prePreConsEpochLastBlockNumber,blockChainConfig.getSettlePeriodBlockCount());
			List<NodeOpt> exceptionNodeOpts = slash(block,prePreSettleEpoch.intValue(),preExceptionNodeList, BusinessParam.YesNoEnum.YES,event.getEpochMessage().getBlockReward());
			nodeOpts.addAll(exceptionNodeOpts);
		}

		/**2、再检查前一共识周期是否有低出块节点，把这些节点分成两类:
		 *   a、参与当前共识周期，则标识为异常；
		 *   b、不参与当前共识周期，则直接处罚；
		 * */
		//查询在上一轮共识周期出块率低的节点列表
		List<Staking> preLowRateNodeList = epochBusinessMapper.querySlashNode(preValidatorList);
		// 前一周期出块率低且在当前周期当选的异常节点列表, 这些列表的节点需要被设置为异常状态
		List<String> curExceptionNodeList = new ArrayList<>();
		List<Staking> curSlashNodeList = new ArrayList<>();
		// 前一周期出块率低且在当前周期未当选的低出块节点列表, 这些列表的节点需要被处罚
		List<Staking> preSlashNodeList = new ArrayList<>();
		preLowRateNodeList.forEach(n->{
			if(curValidatorList.contains(n.getNodeId())){
				curExceptionNodeList.add(n.getNodeId());
				curSlashNodeList.add(n);
			}else{
				preSlashNodeList.add(n);
			}
		});

		if (!curExceptionNodeList.isEmpty()){
			//a、参与当前共识周期，则标识为异常；且登记节点行为
			epochBusinessMapper.setException(curExceptionNodeList);
			List<NodeOpt> preLowRateNodeOpts = slashOnlyOpt(block, curSlashNodeList, event.getEpochMessage().getBlockReward());
			nodeOpts.addAll(preLowRateNodeOpts);
		}

		if(!preSlashNodeList.isEmpty()){
			//b、不参与当前共识周期，则直接处罚；
			List<NodeOpt> preLowRateNodeOpts = slash(block,event.getEpochMessage().getSettleEpochRound().intValue(),preSlashNodeList, BusinessParam.YesNoEnum.NO,event.getEpochMessage().getBlockReward());
			nodeOpts.addAll(preLowRateNodeOpts);
		} 

		log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

       return Optional.ofNullable(nodeOpts);
	}

	/**
	 * 处罚节点
	 * @param block 区块
	 * @param settleEpoch 所在结算周期
	 * @param slashNodeList 被处罚的节点列表
	 * @param isPrePreRound slashNodeList是否是上上个共识周期的低出块节点
	 * @return
	 */
	private List<NodeOpt> slash(Block block, int settleEpoch, List<Staking> slashNodeList, BusinessParam.YesNoEnum isPrePreRound,BigDecimal blockReward){
		//惩罚节点
		Election election = Election.builder()
				.time(block.getTime())
				.settingEpoch(settleEpoch)
				.slashNodeList(slashNodeList)
				.isPrePreRound(isPrePreRound.getCode())
				.build();
		epochBusinessMapper.slashNode(election);

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
					return nodeOpt;
				}).collect(Collectors.toList());
		return nodeOpts;
	}
	
	/**
	 * 不处罚节点只登记操作日志
	 * @param block 区块
	 * @param slashNodeList 被处罚的节点列表
	 * @return
	 */
	private List<NodeOpt> slashOnlyOpt(Block block, List<Staking> slashNodeList,BigDecimal blockReward){

		// 更新解质押到账需要经过的结算周期数
		String configVal = parameterService.getValueInBlockChainConfig(ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
		if(StringUtils.isBlank(configVal)){
			throw new BusinessException("参数表参数缺失："+ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
		}
		Integer unStakeFreezeDuration = Integer.parseInt(configVal);
		Election election = Election.builder()
				.slashNodeList(slashNodeList)
				.unStakeFreezeDuration(unStakeFreezeDuration)
				.build();
		epochBusinessMapper.updateUnStakeFreezeDuration(election);

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
					return nodeOpt;
				}).collect(Collectors.toList());
		return nodeOpts;
	}
}
