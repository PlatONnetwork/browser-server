package com.platon.browser.analyzer.epoch;

import cn.hutool.json.JSONUtil;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.bean.HistoryLowRateSlash;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.EpochBusinessMapper;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dao.param.epoch.Election;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.service.ppos.StakeEpochService;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.utils.HexUtil;
import com.platon.protocol.Web3j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 选举
 *
 * @date 2021/2/3
 */
@Slf4j
@Service
public class OnElectionAnalyzer {

    @Resource
    private EpochBusinessMapper epochBusinessMapper;

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private SpecialApi specialApi;

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private StakingMapper stakingMapper;

    @Resource
    private StakeEpochService stakeEpochService;

    /**
     * 2023/04/07 lvixaoyi
     * 注意：此方法过程，没有影响输入参数的值
     *
     * @param event
     * @param block
     * @return
     */
    public List<NodeOpt> analyze(CollectionEvent event, Block block) {
        long startTime = System.currentTimeMillis();
        // 操作日志列表
        List<NodeOpt> nodeOpts = new ArrayList<>();
        try {
            Web3j web3j = platOnClient.getWeb3jWrapper().getWeb3j();
            List<HistoryLowRateSlash> slashList = specialApi.getHistoryLowRateSlashList(web3j, BigInteger.valueOf(block.getNum()));
            if (!slashList.isEmpty()) {
                List<String> slashNodeIdList = new ArrayList<>();
                // 统一节点ID格式： 0x开头
                slashList.forEach(n -> slashNodeIdList.add(HexUtil.prefix(n.getNodeId())));
                log.debug("特殊节点查询出来的低出块节点：{}", slashNodeIdList);
                // 查询节点
                StakingExample stakingExample = new StakingExample();
                List<Integer> status = new ArrayList<>();
                status.add(Staking.StatusEnum.CANDIDATE.getCode()); //候选中
                status.add(Staking.StatusEnum.EXITING.getCode()); //退出中
                stakingExample.createCriteria().andStatusIn(status).andNodeIdIn(slashNodeIdList);
                List<Staking> slashStakingList = stakingMapper.selectByExample(stakingExample);
                if (slashStakingList.isEmpty() || slashStakingList.size() < slashNodeIdList.size()) {
                    log.warn("特殊节点查询到的低出块率节点[" + JSONUtil.toJsonStr(slashNodeIdList) + "]在staking表中查询不到对应的候选中节点数据!");
                } else {
                    //处罚低出块率的节点;
                    BigInteger curSettleEpoch = EpochUtil.getEpoch(BigInteger.valueOf(block.getNum()), chainConfig.getSettlePeriodBlockCount());
                    // slashStakingList中对象的值被修改，但是不影响上层的数据
                    // 2023/04/07 lvixaoyi
                    // 注意：slashStakingList中对象的值被修改，但是不影响上层的数据:(CollectionEvent event, Block block)
                    List<NodeOpt> exceptionNodeOpts = slashNodeForZeroProduce(event, block, curSettleEpoch.intValue(), slashStakingList);
                    nodeOpts.addAll(exceptionNodeOpts);
                    log.debug("块高[{}]结算周期[{}]共识周期[{}]被处罚节点列表为：{}",
                             block.getNum(),
                             event.getEpochMessage().getSettleEpochRound(),
                             event.getEpochMessage().getConsensusEpochRound(),
                             JSONUtil.toJsonStr(slashStakingList));
                }
            }
        } catch (Exception e) {
            log.error("OnElectionConverter error", e);
            throw new BusinessException(e.getMessage());
        }
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        return nodeOpts;
    }

    /**
     * 0出块处罚节点
     *
     * @param block         区块
     * @param settleEpoch   所在结算周期
     * @param slashNodeList 被处罚的节点列表
     * @return
     *
     * 2023/04/07 lvixaoyi
     * 注意：
     * 修改了输入参数：slashNodeList中staking对象的属性值
     *
     */
    private List<NodeOpt> slashNodeForZeroProduce(CollectionEvent event, Block block, int settleEpoch, List<Staking> slashNodeList) {
        // 更新锁定结算周期数
        BigInteger zeroProduceFreezeDuration = stakeEpochService.getZeroProduceFreeDuration();
        slashNodeList.forEach(staking -> {
            staking.setZeroProduceFreezeDuration(zeroProduceFreezeDuration.intValue());
        });
        //惩罚节点
        Election election = Election.builder().settingEpoch(settleEpoch).zeroProduceFreezeEpoch(settleEpoch) // 记录零出块被惩罚时所在结算周期
                                    .zeroProduceFreezeDuration(zeroProduceFreezeDuration.intValue()) //记录此刻的零出块锁定周期数
                                    .build();

        //节点操作日志
        BigInteger bNum = BigInteger.valueOf(block.getNum());

        List<NodeOpt> nodeOpts = new ArrayList<>();
        List<Staking> lockedNodes = new ArrayList<>();
        List<Staking> exitingNodes = new ArrayList<>();
        for (Staking staking : slashNodeList) {
            if (staking.getLowRateSlashCount() > 0) {
                // 已经被低出块处罚过一次，则不再处罚
                continue;
            }
            // 废弃CustomStaking，直接在Staking.class增加属性
            /*CustomStaking customStaking = new CustomStaking();
            BeanUtils.copyProperties(staking, customStaking);*/

            StringBuffer desc = new StringBuffer("0|");
            /**
             * 如果低出块惩罚不等于0的时候，需要配置惩罚金额
             */
            BigDecimal slashAmount = event.getEpochMessage().getBlockReward().multiply(chainConfig.getSlashBlockRewardCount());
            staking.setSlashAmount(slashAmount);
            desc.append(chainConfig.getSlashBlockRewardCount().toString()).append("|").append(slashAmount.toString()).append("|1");
            NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
            nodeOpt.setNodeId(staking.getNodeId());
            nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.LOW_BLOCK_RATE.getCode()));
            nodeOpt.setBNum(bNum.longValue());
            nodeOpt.setTime(block.getTime());
            nodeOpt.setDesc(desc.toString());

            // 根据节点不同状态，更新节点实例的各字段
            if (Staking.StatusEnum.EXITING == Staking.StatusEnum.getEnum(staking.getStatus())) {
                // 节点之前处于退出中状态，则其所有钱已经变为赎回中了，所以从赎回中扣掉处罚金额
                // 总质押+委托统计字段也要更新
                election.setUnStakeFreezeDuration(staking.getUnStakeFreezeDuration());
                election.setUnStakeEndBlock(BigInteger.valueOf(staking.getUnStakeEndBlock()));
                exitingNodes.add(staking);
            }
            if (Staking.StatusEnum.CANDIDATE == Staking.StatusEnum.getEnum(staking.getStatus())) {
                // 如果节点处于候选中，则从【犹豫+锁定】中的质押中扣掉处罚金额
                BigDecimal remainStakingAmount = staking.getStakingHes().add(staking.getStakingLocked()).subtract(slashAmount);
                // 如果扣除处罚金额后【犹豫+锁定】质押金小于质押门槛，则节点置为退出中
                if (remainStakingAmount.compareTo(chainConfig.getStakeThreshold()) < 0) {
                    // 更新解质押到账需要经过的结算周期数
                    BigInteger unStakeFreezeDuration = stakeEpochService.getUnStakeFreeDuration();
                    // 低出块不需要理会对比提案的生效周期
                    BigInteger unStakeEndBlock = stakeEpochService.getUnStakeEndBlock(staking.getNodeId(), event.getEpochMessage().getSettleEpochRound(), false);
                    election.setUnStakeFreezeDuration(unStakeFreezeDuration.intValue());
                    election.setUnStakeEndBlock(unStakeEndBlock);
                    exitingNodes.add(staking);
                    log.debug("块高[{}]结算周期[{}]共识周期[{}]，节点[{}]扣除处罚金额后【犹豫+锁定】质押金小于质押门槛，节点置为退出中",
                             event.getBlock().getNum(),
                             event.getEpochMessage().getSettleEpochRound(),
                             event.getEpochMessage().getConsensusEpochRound(),
                            staking.getNodeId());
                } else {
                    staking.setStatus(Staking.StatusEnum.LOCKED.getCode());
                    // 锁定节点
                    if (staking.getStakingHes().compareTo(slashAmount) >= 0) {
                        // 犹豫够扣
                        BigDecimal remainStakingHes = staking.getStakingHes().subtract(slashAmount);
                        staking.setStakingHes(remainStakingHes);
                    } else {
                        // 犹豫不够扣, 剩余的从锁定质押扣
                        // 需从锁定期质押减去的金额
                        BigDecimal diffAmount = slashAmount.subtract(staking.getStakingHes());
                        staking.setStakingHes(BigDecimal.ZERO);
                        // 锁定期质押剩余
                        BigDecimal lockedAmount = staking.getStakingLocked().subtract(diffAmount);
                        staking.setStakingLocked(lockedAmount);
                    }
                    lockedNodes.add(staking);
                }
            }
            // 设置离开验证人列表的时间
            staking.setLeaveTime(block.getTime());
            // 低出块处罚次数+1
            staking.setLowRateSlashCount(staking.getLowRateSlashCount() + 1);
            nodeOpts.add(nodeOpt);
        }
        election.setLockedNodeList(lockedNodes);
        election.setExitingNodeList(exitingNodes);
        epochBusinessMapper.slashNodeForZeroProduce(election);
        return nodeOpts;
    }

}
