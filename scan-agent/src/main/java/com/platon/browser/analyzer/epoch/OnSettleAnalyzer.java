package com.platon.browser.analyzer.epoch;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.*;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.CustomGasEstimateLogMapper;
import com.platon.browser.dao.custommapper.EpochBusinessMapper;
import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.entity.GasEstimateLog;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.mapper.GasEstimateLogMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dao.param.epoch.Settle;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.publisher.GasEstimateEventPublisher;
import com.platon.browser.utils.CalculateUtils;
import com.platon.browser.v0150.service.RestrictingMinimumReleaseParamService;
import com.platon.contracts.ppos.dto.resp.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OnSettleAnalyzer {

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private EpochBusinessMapper epochBusinessMapper;

    @Resource
    private StakingMapper stakingMapper;

    @Resource
    private GasEstimateEventPublisher gasEstimateEventPublisher;

    @Resource
    private CustomGasEstimateLogMapper customGasEstimateLogMapper;

    @Resource
    private GasEstimateLogMapper gasEstimateLogMapper;

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private RestrictingMinimumReleaseParamService restrictingMinimumReleaseParamService;

    public List<NodeOpt> analyze(CollectionEvent event, Block block) {
        long startTime = System.currentTimeMillis();
        // 操作日志列表
        List<NodeOpt> nodeOpts = new ArrayList<>();
        if (block.getNum() == 1) return nodeOpts;

        log.debug("Block Number:{}", block.getNum());

        Map<String, Node> curVerifierMap = new HashMap<>();
        event.getEpochMessage().getCurVerifierList().forEach(v -> curVerifierMap.put(v.getNodeId(), v));
        Map<String, Node> preVerifierMap = new HashMap<>();
        event.getEpochMessage().getPreVerifierList().forEach(v -> preVerifierMap.put(v.getNodeId(), v));

        if (event.getEpochMessage().getPreVerifierList().isEmpty()) {
            throw new BusinessException("当前周期[" + event.getEpochMessage().getSettleEpochRound().intValue() + "]的前一结算周期验证人列表为空！");
        }

        Settle settle = Settle.builder()
                              .preVerifierSet(preVerifierMap.keySet())
                              .curVerifierSet(curVerifierMap.keySet())
                              .stakingReward(event.getEpochMessage().getPreStakeReward())
                              .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                              .stakingLockEpoch(chainConfig.getUnStakeRefundSettlePeriodCount().intValue())
                              .build();

        List<Integer> statusList = new ArrayList<>();
        statusList.add(CustomStaking.StatusEnum.CANDIDATE.getCode());
        statusList.add(CustomStaking.StatusEnum.EXITING.getCode());
        statusList.add(CustomStaking.StatusEnum.LOCKED.getCode());
        StakingExample stakingExample = new StakingExample();
        stakingExample.createCriteria().andStatusIn(statusList);
        List<Staking> stakingList = stakingMapper.selectByExampleWithBLOBs(stakingExample);
        List<String> exitedNodeIds = new ArrayList<>();
        stakingList.forEach(staking -> {
            //犹豫期金额变成锁定金额
            staking.setStakingLocked(staking.getStakingLocked().add(staking.getStakingHes()));
            staking.setStakingHes(BigDecimal.ZERO);

            //退出中记录状态设置（状态为退出中且已经经过指定的结算周期数，则把状态置为已退出）
            if (staking.getStatus() == CustomStaking.StatusEnum.EXITING.getCode() && // 节点状态为退出中
                    //(staking.getStakingReductionEpoch() + staking.getUnStakeFreezeDuration()) < settle.getSettingEpoch()
                    event.getBlock().getNum() >= staking.getUnStakeEndBlock() // 且当前区块号大于等于质押预计的实际退出区块号
            ) {
                staking.setStakingReduction(BigDecimal.ZERO);
                staking.setStatus(CustomStaking.StatusEnum.EXITED.getCode());
                staking.setLowRateSlashCount(0);
                exitedNodeIds.add(staking.getNodeId());
            }

            //锁定中记录状态设置（状态为已锁定中且已经经过指定的结算周期数，则把状态置为候选中）
            if (staking.getStatus() == CustomStaking.StatusEnum.LOCKED.getCode() && // 节点状态为已锁定
                    (staking.getZeroProduceFreezeEpoch() + staking.getZeroProduceFreezeDuration()) < settle.getSettingEpoch()
                // 且当前区块号大于等于质押预计的实际退出区块号
            ) {
                // 低出块处罚次数置0
                staking.setLowRateSlashCount(0);
                // 异常状态
                staking.setExceptionStatus(CustomStaking.ExceptionStatusEnum.NORMAL.getCode());
                // 从已锁定状态恢复到候选中状态
                staking.setStatus(CustomStaking.StatusEnum.CANDIDATE.getCode());
                recoverLog(staking, settle.getSettingEpoch(), block, nodeOpts);
            }

//            // 如果当前节点是因举报而被处罚[exception_status = 5], 则状态直接置为已退出【因为底层实际上已经没有这个节点了】
//            if(staking.getExceptionStatus()== CustomStaking.ExceptionStatusEnum.MULTI_SIGN_SLASHED.getCode()&&
//                    (staking.getStakingReductionEpoch() + staking.getUnStakeFreezeDuration()) < settle.getSettingEpoch()){
//            	staking.setStakingReduction(BigDecimal.ZERO);
//            	staking.setStatus(CustomStaking.StatusEnum.EXITED.getCode());
//            	exitedNodeIds.add(staking.getNodeId());
//            }

            //当前质押是上轮结算周期验证人,发放本结算周期的质押奖励, 奖励金额暂存至stakeReward变量
            BigDecimal curSettleStakeReward = BigDecimal.ZERO;
            if (settle.getPreVerifierSet().contains(staking.getNodeId())) {
                curSettleStakeReward = settle.getStakingReward();
            }

            //当前质押是下轮结算周期验证人
            if (settle.getCurVerifierSet().contains(staking.getNodeId())) {
                staking.setIsSettle(CustomStaking.YesNoEnum.YES.getCode());
            } else {
                staking.setIsSettle(CustomStaking.YesNoEnum.NO.getCode());
            }

            // 设置当前质押的总委托奖励，从节点上取出来的委托总奖励就是当前质押获取的总委托奖励
            Node node = preVerifierMap.get(staking.getNodeId());
            BigDecimal curTotalDelegateCost = BigDecimal.ZERO;
            if (node != null) {
                staking.setTotalDeleReward(new BigDecimal(node.getDelegateRewardTotal()));
                /**
                 * 当底层查询出来的委托数为0时，则成本使用staking中的委托数
                 */
                if (BigInteger.ZERO.compareTo(node.getDelegateTotal()) == 0) {
                    curTotalDelegateCost = staking.getStatDelegateLocked().add(staking.getStatDelegateHes());
                } else {
                    curTotalDelegateCost = new BigDecimal(node.getDelegateTotal());
                }
            } else {
                /**
                 * 当底层查询出来的委托数为0时，则成本使用staking中的委托数
                 */
                curTotalDelegateCost = staking.getStatDelegateLocked().add(staking.getStatDelegateHes());
            }

            // 计算节点质押年化率
            calcStakeAnnualizedRate(staking, curSettleStakeReward, settle);
            // 计算委托年化率
            calcDelegateAnnualizedRate(staking, curTotalDelegateCost, settle);
        });
        settle.setStakingList(stakingList);
        settle.setExitNodeList(exitedNodeIds);

        epochBusinessMapper.settle(settle);
        if (CollUtil.isNotEmpty(settle.getStakingList())) {
            settle.getStakingList().forEach(staking -> {
                log.info("块高[{}]对应的结算周期为[{}]--节点[{}]的质押奖励为{}", block.getNum(), event.getEpochMessage().getSettleEpochRound(), staking.getNodeId(), staking.getStakingRewardValue().toPlainString());
            });
        }

        List<GasEstimate> gasEstimates = new ArrayList<>();
        preVerifierMap.forEach((k, v) -> {
            GasEstimate ge = new GasEstimate();
            ge.setNodeId(v.getNodeId());
            ge.setSbn(v.getStakingBlockNum().longValue());
            gasEstimates.add(ge);
        });

        // 1、把周期数需要自增1的节点质押先入mysql数据库
        Long seq = block.getNum() * 10000;
        List<GasEstimateLog> gasEstimateLogs = new ArrayList<>();
        GasEstimateLog gasEstimateLog = new GasEstimateLog();
        gasEstimateLog.setSeq(seq);
        gasEstimateLog.setJson(JSON.toJSONString(gasEstimates));
        gasEstimateLogs.add(gasEstimateLog);
        customGasEstimateLogMapper.batchInsertOrUpdateSelective(gasEstimateLogs, GasEstimateLog.Column.values());

        if (CollUtil.isNotEmpty(gasEstimates)) {
            epochBusinessMapper.updateGasEstimate(gasEstimates);
        }
        gasEstimateLogMapper.deleteByPrimaryKey(seq);

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);

        try {
            restrictingMinimumReleaseParamService.checkRestrictingMinimumReleaseParam(block);
        } catch (Exception e) {
            log.error("检查链上生效版本出错：", e);
        }

        return nodeOpts;
    }

    /**
     * 计算节点质押年化率
     *
     * @param staking
     * @param curSettleStakeReward
     * @param settle
     */
    private void calcStakeAnnualizedRate(Staking staking, BigDecimal curSettleStakeReward, Settle settle) {
        // 设置发放质押奖励后的金额，用于年化率计算
        staking.setStakingRewardValue(staking.getStakingRewardValue().add(curSettleStakeReward));
        // 解析年化率信息对象
        String ariString = staking.getAnnualizedRateInfo();
        AnnualizedRateInfo ari = StringUtils.isNotBlank(ariString) ? JSON.parseObject(ariString, AnnualizedRateInfo.class) : new AnnualizedRateInfo();
        if (ari.getStakeProfit() == null) ari.setStakeProfit(new ArrayList<>());
        if (ari.getStakeCost() == null) ari.setStakeCost(new ArrayList<>());
        if (ari.getSlash() == null) ari.setSlash(new ArrayList<>());

        // 默认当前节点在下一轮结算周期不是验证人,其在下一轮结算周期的质押成本为0
        BigDecimal curSettleCost = BigDecimal.ZERO;
//        if(settle.getCurVerifierSet().contains(staking.getNodeId())){
        // 如果当前节点在下一轮结算周期还是验证人,则记录下一轮结算周期的质押成本
        // 计算当前质押成本 成本暂时不需要委托
        curSettleCost = staking.getStakingLocked() // 锁定的质押金
                               .add(staking.getStakingHes()); // 犹豫期的质押金
//                    .add(staking.getStatDelegateHes()) // 犹豫期的委托金
//                    .add(staking.getStatDelegateLocked()); // 锁定的委托金

//        }
        // 轮换下一结算周期的成本信息
        CalculateUtils.rotateCost(ari.getStakeCost(), curSettleCost, BigInteger.valueOf(settle.getSettingEpoch()), chainConfig);

        // 计算当前质押的年化率 START ******************************
        // 打地基 START -- 这样收益总和才有减数基础
        layFoundation(ari.getStakeProfit(), settle.getSettingEpoch());

        if (ari.getSlash() == null) ari.setSlash(new ArrayList<>());
        // 打地基 END

        // 默认节点在上一周期的收益为零
        BigDecimal curSettleStakeProfit = BigDecimal.ZERO;
        if (settle.getPreVerifierSet().contains(staking.getNodeId())) {
            // 如果当前节点在前一轮结算周期，则计算真实收益
            curSettleStakeProfit = staking.getStakingRewardValue() // 质押奖励
                                          .add(staking.getBlockRewardValue()) // + 出块奖励
                                          .add(staking.getFeeRewardValue()) // + 手续费奖励
                                          .subtract(staking.getTotalDeleReward()); // - 当前结算周期的委托奖励总和
        }
        // 轮换质押收益信息，把当前节点在上一周期的收益放入轮换信息里
        CalculateUtils.rotateProfit(ari.getStakeProfit(), curSettleStakeProfit, BigInteger.valueOf(settle.getSettingEpoch() - 1L), chainConfig);
        // 计算年化率
        BigDecimal annualizedRate = CalculateUtils.calculateAnnualizedRate(ari.getStakeProfit(), ari.getStakeCost(), chainConfig);
        // 设置年化率
        staking.setAnnualizedRate(annualizedRate.doubleValue());
        // 计算当前质押的年化率 END ******************************

        // 更新年化率计算原始信息
        staking.setAnnualizedRateInfo(ari.toJSONString());

        // 把当前staking的stakingRewardValue的值置为当前结算周期的质押奖励值，累加操作由mapper xmm中的SQL语句完成
        // staking表：【`staking_reward_value` =  `staking_reward_value` + #{staking.stakingRewardValue}】
        // node表：【`stat_staking_reward_value` = `stat_staking_reward_value` + #{staking.stakingRewardValue}】
        staking.setStakingRewardValue(curSettleStakeReward);
    }

    /**
     * 计算委托年化率
     *
     * @param staking              当前质押记录
     * @param curTotalDelegateCost 节点在当前结算周期的总委托数额
     * @param settle               周期切换业务参数
     */
    private void calcDelegateAnnualizedRate(Staking staking, BigDecimal curTotalDelegateCost, Settle settle) {
        //计算委托年化率
        // 解析年化率信息对象
        String ariString = staking.getAnnualizedRateInfo();
        AnnualizedRateInfo ari = StringUtils.isNotBlank(ariString) ? JSON.parseObject(ariString, AnnualizedRateInfo.class) : new AnnualizedRateInfo();
        if (ari.getDelegateProfit() == null) ari.setDelegateProfit(new ArrayList<>());
        if (ari.getDelegateCost() == null) ari.setDelegateCost(new ArrayList<>());

        // 默认当前节点在下一轮结算周期不是验证人,其在下一轮结算周期的委托成本为0
        BigDecimal curDelegateCost = BigDecimal.ZERO;
//        if(settle.getCurVerifierSet().contains(staking.getNodeId())){
        // 如果当前节点在下一轮结算周期还是验证人,则记录下一轮结算周期的委托成本, 委托成本以参数传进来的curTotalDelegateCost为准
        curDelegateCost = curTotalDelegateCost;
//        }
        // 轮换下一结算周期的成本信息
        CalculateUtils.rotateCost(ari.getDelegateCost(), curDelegateCost, BigInteger.valueOf(settle.getSettingEpoch()), chainConfig);

        // 计算当前委托的年化率 START ******************************
        layFoundation(ari.getDelegateProfit(), settle.getSettingEpoch());

        // 默认节点在上一周期的委托收益为零
        BigDecimal curSettleDelegateProfit = BigDecimal.ZERO;
        if (settle.getPreVerifierSet().contains(staking.getNodeId())) {
            curSettleDelegateProfit = staking.getTotalDeleReward();
        }
        // 轮换委托收益信息，把当前节点在上一周期的委托收益放入轮换信息里
        CalculateUtils.rotateProfit(ari.getDelegateProfit(), curSettleDelegateProfit, BigInteger.valueOf(settle.getSettingEpoch() - 1L), chainConfig);
        // 计算年化率
        BigDecimal annualizedRate = CalculateUtils.calculateAnnualizedRate(ari.getDelegateProfit(), ari.getDelegateCost(), chainConfig);
        // 把前一周期的委托奖励年化率设置至preDeleAnnualizedRate字段
        staking.setPreDeleAnnualizedRate(staking.getDeleAnnualizedRate());
        // 设置当前质押记录的委托奖励年化率
        staking.setDeleAnnualizedRate(annualizedRate.doubleValue());
        // 计算当前质押的年化率 END ******************************

        // 更新年化率计算原始信息
        staking.setAnnualizedRateInfo(ari.toJSONString());
    }

    // 打地基
    private void layFoundation(List<PeriodValueElement> pves, int settleEpoch) {
        // 打地基 START -- 这样收益总和才有减数基础
        if (pves.isEmpty()) {
            // 设置0收益作为计算周期间收益的减数
            PeriodValueElement pv = new PeriodValueElement();
            // 例如当前是第6个周期，要得出5和6两个周期的利润和，则需要记录第4个周期末的利润，这样才可以用【第6个周期末的利润】-【第4个周期末的利润】计算出5和6两个周期的利润和
            pv.setPeriod(settleEpoch - 2L);
            pv.setValue(BigDecimal.ZERO);
            pves.add(pv);
        }
        // 打地基 END
    }

    /**
     * 节点恢复记录日志
     *
     * @param staking
     * @param settingEpoch
     * @param block
     * @param nodeOpts
     */
    private void recoverLog(Staking staking, int settingEpoch, Block block, List<NodeOpt> nodeOpts) {
        String desc = NodeOpt.TypeEnum.UNLOCKED.getTpl()
                                               .replace("LOCKED_EPOCH", staking.getZeroProduceFreezeEpoch().toString())
                                               .replace("UNLOCKED_EPOCH", String.valueOf(settingEpoch))
                                               .replace("FREEZE_DURATION", staking.getZeroProduceFreezeDuration().toString());
        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
        nodeOpt.setNodeId(staking.getNodeId());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.UNLOCKED.getCode()));
        nodeOpt.setBNum(block.getNum());
        nodeOpt.setTime(block.getTime());
        nodeOpt.setDesc(desc);
        nodeOpts.add(nodeOpt);
    }

}
