package com.platon.browser.complement.converter.epoch;

import com.platon.browser.client.HistoryLowRateSlash;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.complement.dao.param.epoch.Election;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomStaking.StatusEnum;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.service.misc.StakeMiscService;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.utils.HexTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OnElectionConverter {
	@Autowired
	private EpochBusinessMapper epochBusinessMapper;
	@Autowired
	private StakeBusinessMapper stakeBusinessMapper;
	@Autowired
	private NetworkStatCache networkStatCache;
	@Autowired
	private BlockChainConfig chainConfig;
	@Autowired
	private SpecialApi specialApi;
	@Autowired
	private PlatOnClient platOnClient;
	@Autowired
	private StakingMapper stakingMapper;
	@Autowired
	private StakeMiscService stakeMiscService;

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
				List<Integer> status = new ArrayList<>();
				status.add(StatusEnum.CANDIDATE.getCode()); //候选中
				status.add(StatusEnum.EXITING.getCode()); //退出中
				stakingExample.createCriteria()
						.andStatusIn(status)
						.andNodeIdIn(slashNodeIdList);
				List<Staking> slashStakingList = stakingMapper.selectByExample(stakingExample);
				if(slashStakingList.isEmpty()){
					log.info("特殊节点查询到的低出块率节点["+slashNodeIdList+"]在staking表中查询不到对应的候选中节点数据!");
				}else {
					//处罚低出块率的节点;
					BigInteger curSettleEpoch = EpochUtil.getEpoch(BigInteger.valueOf(block.getNum()),chainConfig.getSettlePeriodBlockCount());
					List<NodeOpt> exceptionNodeOpts = slash(event,block,curSettleEpoch.intValue(),slashStakingList);
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
	private List<NodeOpt> slash(CollectionEvent event,Block block, int settleEpoch, List<Staking> slashNodeList){
		// 更新锁定结算周期数
		BigInteger  zeroProduceFreezeDuration = stakeMiscService.getZeroProduceFreeDuration();
		slashNodeList.forEach(staking -> {
			staking.setZeroProduceFreezeDuration(zeroProduceFreezeDuration.intValue());
		});
		//惩罚节点
		Election election = Election.builder()
				.settingEpoch(settleEpoch)
				.zeroProduceFreezeEpoch(settleEpoch) // 记录零出块被惩罚时所在结算周期
				.zeroProduceFreezeDuration(zeroProduceFreezeDuration.intValue()) //记录此刻的零出块锁定周期数
				.build();

		//节点操作日志
		BigInteger bNum = BigInteger.valueOf(block.getNum());

		List<NodeOpt> nodeOpts = new ArrayList<>();
		List<CustomStaking> lockedNodes = new ArrayList<>();
		List<CustomStaking> exitedNodes = new ArrayList<>();
		for(Staking staking:slashNodeList){
			if(staking.getLowRateSlashCount()>0){
				// 如果节点之前因低出块被罚过，且还没被解锁（节点在结算周期切换被解锁时会把低出块处罚次数置零）,则不做任何操作
				// 进入此处的节点只可能是退出中状态的节点
				continue;
			}

			CustomStaking customStaking = new CustomStaking();
			BeanUtils.copyProperties(staking,customStaking);

			StringBuffer desc = new StringBuffer("0|");
			/**
			 * 如果低出块惩罚不等于0的时候，需要配置惩罚金额
			 */
			BigDecimal slashAmount =  event.getEpochMessage().getBlockReward()
					.multiply(chainConfig.getSlashBlockRewardCount());
			customStaking.setSlashAmount(slashAmount);

			desc.append(chainConfig.getSlashBlockRewardCount().toString()).append("|").append(slashAmount.toString()).append( "|1");
			NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
			nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
			nodeOpt.setNodeId(staking.getNodeId());
			nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.LOW_BLOCK_RATE.getCode()));
			nodeOpt.setBNum(bNum.longValue());
			nodeOpt.setTime(block.getTime());
			nodeOpt.setDesc(desc.toString());

			// 根据节点不同状态，更新节点实例的各字段
			if(StatusEnum.EXITING==StatusEnum.getEnum(staking.getStatus())){
				// 节点之前处于退出中状态，则其所有钱已经变为赎回中了，所以从赎回中扣掉处罚金额
				BigDecimal remainRedeemAmount = staking.getStakingReduction().subtract(slashAmount);
				if(remainRedeemAmount.compareTo(BigDecimal.ZERO)<0) remainRedeemAmount=BigDecimal.ZERO;
				staking.setStakingReduction(remainRedeemAmount);
				lockedNodes.add(customStaking);
			}
			if(StatusEnum.CANDIDATE==StatusEnum.getEnum(staking.getStatus())){
				// 如果节点处于候选中，则从锁定中的质押中扣掉处罚金额
				BigDecimal remainLockedAmount=staking.getStakingLocked().subtract(slashAmount);
				// 状态置为已锁定
				staking.setStatus(StatusEnum.LOCKED.getCode());
				if(remainLockedAmount.compareTo(BigDecimal.ZERO)<0) {
					remainLockedAmount=BigDecimal.ZERO;
				}
				if(
					// 如果扣除处罚金额后小于零，则节点置为退出中
					remainLockedAmount.compareTo(BigDecimal.ZERO)<0||
					// 如果扣除处罚金额后小于质押门槛，则节点置为退出中
					remainLockedAmount.compareTo(chainConfig.getStakeThreshold())<0
				){
					// 更新解质押到账需要经过的结算周期数
					BigInteger  unStakeFreezeDuration = stakeMiscService.getUnStakeFreeDuration();
					// 理论上的退出区块号, 实际的退出块号还要跟状态为进行中的提案的投票截至区块进行对比，取最大者
					BigInteger unStakeEndBlock = stakeMiscService.getUnStakeEndBlock(staking.getNodeId(),event.getEpochMessage().getSettleEpochRound(),true);
					election.setUnStakeFreezeDuration(unStakeFreezeDuration.intValue());
					election.setUnStakeEndBlock(unStakeEndBlock);
					// 退出中状态的节点在结算周期切换时会检查是否已达到指定周期数，如果达到，则进行节点的各种金额的移动处理
					staking.setStatus(StatusEnum.EXITING.getCode());
					exitedNodes.add(customStaking);
				}else{
					lockedNodes.add(customStaking);
				}
				staking.setStakingLocked(remainLockedAmount);
			}
			// 设置离开验证人列表的时间
			staking.setLeaveTime(new Date());
			// 低出块处罚次数+1
			staking.setLowRateSlashCount(staking.getLowRateSlashCount()+1);


			//对提案数据进行处罚
//			proposalParameterService.setSlashParameters(staking.getNodeId());
			nodeOpts.add(nodeOpt);
		}
		election.setLockedNodeList(lockedNodes);
		election.setExitedNodeList(exitedNodes);
		epochBusinessMapper.slashNode(election);
		return nodeOpts;
	}
}
