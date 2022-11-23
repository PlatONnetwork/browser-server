package com.platon.browser.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.bean.*;
import com.platon.browser.bean.CustomDelegation.YesNoEnum;
import com.platon.browser.bean.CustomStaking.StatusEnum;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.constant.Browser;
import com.platon.browser.dao.custommapper.CustomDelegationMapper;
import com.platon.browser.dao.custommapper.CustomNodeMapper;
import com.platon.browser.dao.custommapper.CustomVoteMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.enums.AddressTypeEnum;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.enums.StakingStatusEnum;
import com.platon.browser.request.staking.*;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.staking.*;
import com.platon.browser.service.elasticsearch.EsNodeOptRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import com.platon.browser.utils.*;
import com.platon.contracts.ppos.dto.resp.Reward;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 验证人模块方法
 *
 * @author zhangrj
 * @file StakingServiceImpl.java
 * @description
 * @data 2019年8月31日
 */
@Service
public class StakingService {

    private final Logger logger = LoggerFactory.getLogger(StakingService.class);

    @Resource
    private StatisticCacheService statisticCacheService;

    @Resource
    private CustomVoteMapper customVoteMapper;

    @Resource
    private CustomDelegationMapper customDelegationMapper;

    @Resource
    private CustomNodeMapper customNodeMapper;

    @Resource
    private NodeMapper nodeMapper;

    @Resource
    private EsNodeOptRepository ESNodeOptRepository;

    @Resource
    private I18nUtil i18n;

    @Resource
    private BlockChainConfig blockChainConfig;

    @Resource
    private PlatOnClient platonClient;

    @Resource
    private CommonService commonService;

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private ProposalMapper proposalMapper;

    public StakingStatisticNewResp stakingStatisticNew() {
        /** 获取统计信息 */
        NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
        StakingStatisticNewResp stakingStatisticNewResp = new StakingStatisticNewResp();
        if (networkStatRedis != null) {
            BeanUtils.copyProperties(networkStatRedis, stakingStatisticNewResp);
            stakingStatisticNewResp.setCurrentNumber(networkStatRedis.getCurNumber());
            stakingStatisticNewResp.setNextSetting(networkStatRedis.getNextSettle());
            // 接受委托 = 实时质押委托总数 - 实时质押总数
            stakingStatisticNewResp.setDelegationValue(networkStatRedis.getStakingDelegationValue().subtract(networkStatRedis.getStakingValue()));
            stakingStatisticNewResp.setStakingReward(networkStatRedis.getStakingReward());
            stakingStatisticNewResp.setIssueValue(networkStatRedis.getIssueValue());
            StakingBO bo = commonService.getTotalStakingValueAndStakingDenominator(networkStatRedis);
            stakingStatisticNewResp.setStakingDenominator(bo.getStakingDenominator());
            stakingStatisticNewResp.setStakingDelegationValue(bo.getTotalStakingValue());
        }
        return stakingStatisticNewResp;
    }

    public RespPage<AliveStakingListResp> aliveStakingList(AliveStakingListReq req) {
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        Integer status = null;
        Integer isSettle = null;

        // 是否需要把退出中状态的节点当做活跃中处理，默认不需要，只有在查询全部或活跃中时需要把退出中状态当作活跃中
        boolean exitingAsActive = false;
        /**
         *  对前端传的参数进行转换查询条件
         */
        switch (StakingStatusEnum.valueOf(req.getQueryStatus().toUpperCase())) {
            case ALL:
                /** 查询候选人 */
                status = StakingStatusEnum.CANDIDATE.getCode();
                exitingAsActive = true;
                break;
            case ACTIVE:
                /** 活跃中代表即使后续同时也是结算周期验证人 */
                status = StakingStatusEnum.CANDIDATE.getCode();
                isSettle = CustomStaking.YesNoEnum.YES.getCode();
                exitingAsActive = true;
                break;
            case CANDIDATE:
                /** 查询候选人 */
                status = StakingStatusEnum.CANDIDATE.getCode();
                isSettle = CustomStaking.YesNoEnum.NO.getCode();
                break;
            default:
                break;
        }
        RespPage<AliveStakingListResp> respPage = new RespPage<>();
        List<AliveStakingListResp> lists = new LinkedList<>();
        /** 根据条件和状态进行查询列表 */
        NodeExample nodeExample = new NodeExample();
        nodeExample.setOrderByClause(" big_version desc, total_value desc,staking_block_num asc, staking_tx_index asc");
        NodeExample.Criteria criteria1 = nodeExample.createCriteria();
        criteria1.andStatusEqualTo(status);
        if (StringUtils.isNotBlank(req.getKey())) {
            criteria1.andNodeNameLike("%" + req.getKey() + "%");
        }
        if (isSettle != null) {
            criteria1.andIsSettleEqualTo(isSettle);
        }

        if (exitingAsActive) {
            /**
             * 如果节点状态为退出中且为结算周期则认为在活跃中
             */
            NodeExample.Criteria criteria2 = nodeExample.createCriteria();
            criteria2.andStatusEqualTo(CustomStaking.StatusEnum.EXITING.getCode());
            if (StringUtils.isNotBlank(req.getKey())) {
                criteria2.andNodeNameLike("%" + req.getKey() + "%");
            }
            criteria2.andIsSettleEqualTo(CustomStaking.YesNoEnum.YES.getCode());
            nodeExample.or(criteria2);
        }

        Page<Node> stakingPage = customNodeMapper.selectListByExample(nodeExample);
        List<Node> stakings = stakingPage.getResult();
        /** 查询出块节点 */
        NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
        int i = (req.getPageNo() - 1) * req.getPageSize();
        for (Node staking : stakings) {
            AliveStakingListResp aliveStakingListResp = new AliveStakingListResp();
            BeanUtils.copyProperties(staking, aliveStakingListResp);
            aliveStakingListResp.setBlockQty(staking.getStatBlockQty());
            aliveStakingListResp.setDelegateQty(staking.getStatValidAddrs());
            aliveStakingListResp.setExpectedIncome(staking.getAnnualizedRate().toString());
            /** 委托总金额数=委托交易总金额(犹豫期金额)+委托交易总金额(锁定期金额) */
            String sumAmount = staking.getStatDelegateValue().toString();
            aliveStakingListResp.setDelegateValue(sumAmount);
            aliveStakingListResp.setIsInit(staking.getIsInit() == 1);
            aliveStakingListResp.setStakingIcon(staking.getNodeIcon());
            if (staking.getIsRecommend() != null) {
                aliveStakingListResp.setIsRecommend(CustomStaking.YesNoEnum.YES.getCode() == staking.getIsRecommend());
            }
            /** 设置排行 */
            aliveStakingListResp.setRanking(i + 1);
            aliveStakingListResp.setSlashLowQty(staking.getStatSlashLowQty());
            aliveStakingListResp.setSlashMultiQty(staking.getStatSlashMultiQty());
            /** 如果是对应的出块节点则置为出块中，否则为活跃中或者退出 */
            if (staking.getNodeId().equals(networkStatRedis.getNodeId())) {
                aliveStakingListResp.setStatus(StakingStatusEnum.BLOCK.getCode());
            } else {
                aliveStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(staking.getStatus(),
                                                                                 staking.getIsConsensus(),
                                                                                 staking.getIsSettle()));
            }
            /** 质押总数=有效的质押+委托 */
            aliveStakingListResp.setTotalValue(staking.getTotalValue().toString());
            aliveStakingListResp.setDeleAnnualizedRate(staking.getDeleAnnualizedRate().toString());

            try {
                String nodeSettleStatisInfo = staking.getNodeSettleStatisInfo();
                NodeSettleStatis nodeSettleStatis = NodeSettleStatis.jsonToBean(nodeSettleStatisInfo);
                BigInteger settleEpochRound = EpochUtil.getEpoch(BigInteger.valueOf(networkStatRedis.getCurNumber()),
                                                                 blockChainConfig.getSettlePeriodBlockCount());
                aliveStakingListResp.setGenBlocksRate(nodeSettleStatis.computeGenBlocksRate(settleEpochRound));
                aliveStakingListResp.setUpTime(computeUpTime(staking, nodeSettleStatis, settleEpochRound));
            } catch (Exception e) {
                logger.error("获取节点24小时出块率异常", e);
            }
            aliveStakingListResp.setDelegatedRewardRatio(new BigDecimal(staking.getRewardPer()).divide(Browser.PERCENTAGE).toString() + "%");
            if (staking.getProgramVersion() != 0) {
                aliveStakingListResp.setVersion(ChainVersionUtil.toStringVersion(BigInteger.valueOf(staking.getProgramVersion())));
            } else {
                aliveStakingListResp.setVersion(ChainVersionUtil.toStringVersion(BigInteger.valueOf(staking.getBigVersion())));
            }
            aliveStakingListResp.setPreDeleAnnualizedRate(NodeApr.getPreDeleAnnualizedRate(staking.getNodeApr()));
            lists.add(aliveStakingListResp);
            i++;
        }
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        page.setTotal(stakingPage.getTotal());
        respPage.init(page, lists);
        return respPage;
    }

    // 节点最近24小时(8个结算周期)的正常运行时间比例
    private String computeUpTime(Node node, NodeSettleStatis nodeSettleStatis, BigInteger settleEpochRound) {
        try {
            BigInteger settleNum = BigInteger.valueOf(10750L);
            // 统计周期总区块
            BigInteger totalBlockNum = settleNum.multiply(BigInteger.valueOf(CommonConstant.BLOCK_APR_EPOCH_NUM));
            BigInteger max = settleNum.multiply(settleEpochRound.subtract(BigInteger.ONE));
            // 统计周期最大区块
            max = max.compareTo(settleNum) < 0 ? settleNum : max;
            BigInteger min = settleNum.multiply(settleEpochRound.subtract(BigInteger.valueOf(CommonConstant.BLOCK_APR_EPOCH_NUM))
                                                                .subtract(BigInteger.ONE));
            // 统计周期最小区块
            min = min.compareTo(BigInteger.ZERO) <= 0 ? BigInteger.ZERO : min;
            BigInteger subNum = computeSubNum(nodeSettleStatis, settleEpochRound);
            BigInteger stakingSubNum = computeStakingSubNum(max, min, BigInteger.valueOf(node.getStakingBlockNum()));
            BigInteger leaveSubNum = leaveSubNum(node, max, min, settleNum);
            BigInteger subBlockNUm = totalBlockNum.subtract(subNum).subtract(stakingSubNum).subtract(leaveSubNum);
            BigDecimal percent = new BigDecimal(subBlockNUm)
                    .divide(new BigDecimal(totalBlockNum), 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(6, RoundingMode.HALF_UP)
                    .stripTrailingZeros();
            logger.info("节点[{}]运行时间比例[{}]=(总区块数[{}]-应出未出区块[{}]-质押未出区块[{}]-退出区块[{}])/总区块数[{}]*100%",
                        node.getNodeId(),
                        percent.toPlainString() + "%",
                        totalBlockNum.longValue(),
                        subNum.longValue(),
                        stakingSubNum.longValue(),
                        leaveSubNum.longValue(),
                        totalBlockNum.longValue());
            return percent.toPlainString() + "%";
        } catch (Exception e) {
            logger.error("计算节点24小时正常运行时间异常", e);
            return "0%";
        }
    }

    /**
     * 统计节点在8个结算周期时应出而未出的区块数
     *
     * @param nodeSettleStatis:
     * @param settleEpochRound:
     * @return:
     * @date: 2022/11/22
     */
    private BigInteger computeSubNum(NodeSettleStatis nodeSettleStatis, BigInteger settleEpochRound) {
        BigInteger subNum = BigInteger.ZERO;
        if (nodeSettleStatis.getNodeSettleStatisQueue().size() > 0) {
            // 已按结算周期轮数排序，index[0]是最大的
            List<NodeSettleStatisBase> list = nodeSettleStatis.getNodeSettleStatisQueue().toList();
            // 过滤数据，与当前结算周期轮数的差值大于0且小于等于8的，即是最近8个结算周期。不包括当前结算周期
            List<NodeSettleStatisBase> last = list.stream().filter(v -> {
                BigInteger difference = settleEpochRound.subtract(v.getSettleEpochRound());
                return difference.compareTo(BigInteger.ZERO) > 0 && difference.compareTo(BigInteger.valueOf(CommonConstant.BLOCK_APR_EPOCH_NUM)) <= 0;
            }).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(last)) {
                LongSummaryStatistics blockNumGrandTotal = last.stream()
                                                               .collect(Collectors.summarizingLong(v -> v.getBlockNumGrandTotal().longValue()));
                LongSummaryStatistics blockNumElected = last.stream()
                                                            .collect(Collectors.summarizingLong(v -> v.getBlockNumElected().longValue()));
                // 在当前结算周期少出块
                if (BigInteger.valueOf(blockNumElected.getSum())
                              .multiply(BigInteger.TEN)
                              .compareTo(BigInteger.valueOf(blockNumGrandTotal.getSum())) > 0) {
                    BigInteger sub = BigInteger.valueOf(blockNumElected.getSum())
                                               .multiply(BigInteger.TEN)
                                               .subtract(BigInteger.valueOf(blockNumGrandTotal.getSum()));
                    subNum = subNum.add(sub);
                }
            }
        }
        return subNum;
    }

    /**
     * 节点质押块高减去开始区块
     *
     * @param max:
     * @param min:
     * @param stakingBlockNum:
     * @return:
     * @date: 2022/11/22
     */
    private BigInteger computeStakingSubNum(BigInteger max,
                                            BigInteger min,
                                            BigInteger stakingBlockNum) {
        BigInteger stakingSubNum = BigInteger.ZERO;
        if (stakingBlockNum.compareTo(min) >= 0 && stakingBlockNum.compareTo(max) <= 0) {
            stakingSubNum = stakingBlockNum.subtract(min);
        }
        return stakingSubNum;
    }

    private BigInteger leaveSubNum(Node node,
                                   BigInteger max,
                                   BigInteger min,
                                   BigInteger settleNum) {
        BigInteger leaveSubNum = BigInteger.ZERO;
        BigInteger startNum = min;
        BigInteger endNum = max;
        if (ObjectUtil.isNull(node.getLeaveNum())
                && node.getZeroProduceFreezeEpoch() <= 0
                && node.getZeroProduceFreezeDuration() <= 0) {
            return leaveSubNum;
        }
        // 有leaveNum，就以leaveNum为准
        if (ObjectUtil.isNotNull(node.getLeaveNum())
                && BigInteger.valueOf(node.getLeaveNum()).compareTo(min) >= 0
                && BigInteger.valueOf(node.getLeaveNum()).compareTo(max) <= 0) {
            startNum = BigInteger.valueOf(node.getLeaveNum());
        } else if (ObjectUtil.isNull(node.getLeaveNum())
                && node.getZeroProduceFreezeEpoch() > 0
                && node.getZeroProduceFreezeDuration() > 0) {
            startNum = BigInteger.valueOf(node.getZeroProduceFreezeEpoch()).multiply(settleNum);
        }
        if (startNum.compareTo(min) < 0) {
            startNum = min;
        }
        if (node.getZeroProduceFreezeEpoch() > 0 && node.getZeroProduceFreezeDuration() > 0) {
            BigInteger zeroProduceFreezeEpoch = BigInteger.valueOf(node.getZeroProduceFreezeEpoch() + node.getZeroProduceFreezeDuration())
                                                          .multiply(settleNum);
            endNum = (zeroProduceFreezeEpoch.compareTo(min) >= 0 && zeroProduceFreezeEpoch.compareTo(max) <= 0) ? zeroProduceFreezeEpoch : max;
        }

        leaveSubNum = endNum.subtract(startNum);
        return leaveSubNum;
    }

    public RespPage<HistoryStakingListResp> historyStakingList(HistoryStakingListReq req) {
        /** 设置只查询退出中和已退出 */
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<Integer> status = new ArrayList<>();
        status.add(CustomStaking.StatusEnum.EXITING.getCode());
        status.add(CustomStaking.StatusEnum.EXITED.getCode());
        RespPage<HistoryStakingListResp> respPage = new RespPage<>();
        List<HistoryStakingListResp> lists = new LinkedList<>();
        /** 根据条件和状态进行查询列表 */
        NodeExample nodeExample = new NodeExample();
        nodeExample.setOrderByClause(" leave_time desc");
        NodeExample.Criteria criteria = nodeExample.createCriteria();
        criteria.andStatusIn(status);
        /**
         * 防止直接退出的节点出现在历史表中
         */
        criteria.andIsSettleEqualTo(CustomStaking.YesNoEnum.NO.getCode());

        if (StringUtils.isNotBlank(req.getKey())) {
            criteria.andNodeNameLike("%" + req.getKey() + "%");
        }
        Page<Node> stakings = customNodeMapper.selectListByExample(nodeExample);

        for (Node stakingNode : stakings.getResult()) {
            HistoryStakingListResp historyStakingListResp = new HistoryStakingListResp();
            BeanUtils.copyProperties(stakingNode, historyStakingListResp);
            if (stakingNode.getLeaveTime() != null) {
                historyStakingListResp.setLeaveTime(stakingNode.getLeaveTime().getTime());
            }
            historyStakingListResp.setNodeName(stakingNode.getNodeName());
            historyStakingListResp.setStakingIcon(stakingNode.getNodeIcon());
            historyStakingListResp.setSlashLowQty(stakingNode.getStatSlashLowQty());
            historyStakingListResp.setSlashMultiQty(stakingNode.getStatSlashMultiQty());
            /**
             * 带提取的委托等于hes+lock
             */
            historyStakingListResp.setStatDelegateReduction(stakingNode.getStatDelegateValue().add(stakingNode.getStatDelegateReleased()));
            historyStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(stakingNode.getStatus(),
                                                                               stakingNode.getIsConsensus(),
                                                                               stakingNode.getIsSettle()));
            historyStakingListResp.setBlockQty(stakingNode.getStatBlockQty());

            // 退出中节点预估解锁块高
            Long unlockBlockNum = stakingNode.getUnStakeEndBlock();
            historyStakingListResp.setUnlockBlockNum(unlockBlockNum);

            lists.add(historyStakingListResp);
        }
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        page.setTotal(stakings.getTotal());
        respPage.init(page, lists);
        return respPage;
    }

    public BaseResp<StakingDetailsResp> stakingDetails(StakingDetailsReq req) {
        /**
         * 先查询是否活跃节点，查不到再查询是否历史汇总
         */
        Node stakingNode = nodeMapper.selectByPrimaryKey(req.getNodeId());
        StakingDetailsResp resp = new StakingDetailsResp();
        // 只有一条数据
        if (stakingNode != null) {
            BeanUtils.copyProperties(stakingNode, resp);
            resp.setIsInit(stakingNode.getIsInit() == 1);
            resp.setStatus(StakingStatusEnum.getCodeByStatus(stakingNode.getStatus(), stakingNode.getIsConsensus(), stakingNode.getIsSettle()));
            resp.setSlashLowQty(stakingNode.getStatSlashLowQty());
            resp.setSlashMultiQty(stakingNode.getStatSlashMultiQty());
            resp.setBlockQty(stakingNode.getStatBlockQty());
            resp.setExpectBlockQty(stakingNode.getStatExpectBlockQty());
            resp.setVerifierTime(stakingNode.getStatVerifierTime());
            resp.setJoinTime(stakingNode.getJoinTime().getTime());
            resp.setDenefitAddr(stakingNode.getBenefitAddr());
            Address denefitAddr = addressMapper.selectByPrimaryKey(stakingNode.getBenefitAddr());
            resp.setDenefitAddrType(CommonUtil.ofNullable(() -> denefitAddr.getType()).orElse(AddressTypeEnum.ACCOUNT.getCode()));
            Address stakingAddr = addressMapper.selectByPrimaryKey(stakingNode.getStakingAddr());
            resp.setStakingAddrType(CommonUtil.ofNullable(() -> stakingAddr.getType()).orElse(AddressTypeEnum.ACCOUNT.getCode()));
            resp.setStakingIcon(stakingNode.getNodeIcon());
            resp.setDeleAnnualizedRate(stakingNode.getDeleAnnualizedRate().toString());
            resp.setRewardPer(new BigDecimal(stakingNode.getRewardPer()).divide(Browser.PERCENTAGE).toString());
            resp.setNextRewardPer(new BigDecimal(stakingNode.getNextRewardPer()).divide(Browser.PERCENTAGE).toString());
            resp.setTotalDeleReward(stakingNode.getTotalDeleReward().add(stakingNode.getPreTotalDeleReward()));
            try {
                String nodeSettleStatisInfo = stakingNode.getNodeSettleStatisInfo();
                NodeSettleStatis nodeSettleStatis = NodeSettleStatis.jsonToBean(nodeSettleStatisInfo);
                NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
                BigInteger settleEpochRound = EpochUtil.getEpoch(BigInteger.valueOf(networkStatRedis.getCurNumber()),
                                                                 blockChainConfig.getSettlePeriodBlockCount());
                resp.setGenBlocksRate(nodeSettleStatis.computeGenBlocksRate(settleEpochRound));
            } catch (Exception e) {
                logger.error("获取节点24小时出块率异常", e);
            }
            resp.setVersion(ChainVersionUtil.toStringVersion(BigInteger.valueOf(stakingNode.getProgramVersion())));
            /**
             * 待领取奖励等于 累积委托奖励加上上轮奖励减去已领取委托奖励
             */
            resp.setDeleRewardRed(stakingNode.getTotalDeleReward()
                                             .add(stakingNode.getPreTotalDeleReward())
                                             .subtract(stakingNode.getHaveDeleReward()));
            /** 只有不是内置节点才计算年化率  */
            if (CustomStaking.YesNoEnum.YES.getCode() != stakingNode.getIsInit()) {
                resp.setExpectedIncome(String.valueOf(stakingNode.getAnnualizedRate()));
                resp.setRewardValue(stakingNode.getStatFeeRewardValue()
                                               .add(stakingNode.getStatBlockRewardValue())
                                               .add(stakingNode.getStatStakingRewardValue()));
                logger.info("累计系统奖励[{}]=出块奖励统计(手续费)[{}]+出块奖励统计(激励池)[{}]+质押奖励统计(激励池)[{}]",
                            resp.getRewardValue(),
                            stakingNode.getStatFeeRewardValue(),
                            stakingNode.getStatBlockRewardValue(),
                            stakingNode.getStatStakingRewardValue());
            } else {
                resp.setRewardValue(stakingNode.getStatFeeRewardValue());
                logger.info("累计系统奖励[{}]=出块奖励统计(手续费)[{}]", resp.getRewardValue(), stakingNode.getStatFeeRewardValue());
                resp.setExpectedIncome("");
            }
            String webSite = "";
            if (StringUtils.isNotBlank(stakingNode.getWebSite())) {
                /**
                 * 如果地址不是http开头就补齐
                 */
                if (stakingNode.getWebSite().startsWith(Browser.HTTP) || stakingNode.getWebSite().startsWith(Browser.HTTPS)) {
                    webSite = stakingNode.getWebSite();
                } else {
                    webSite = Browser.HTTP + stakingNode.getWebSite();
                }
            }
            resp.setWebsite(webSite);
            /** 实际跳转地址是url拼接上名称 */
            if (StringUtils.isNotBlank(stakingNode.getExternalName())) {
                resp.setExternalUrl(blockChainConfig.getKeyBase() + stakingNode.getExternalName());
            } else {
                resp.setExternalUrl(blockChainConfig.getKeyBase());
            }
            if (stakingNode.getLeaveTime() != null) {
                resp.setLeaveTime(stakingNode.getLeaveTime().getTime());
            }
            // 有效的委托金额
            resp.setDelegateValue(stakingNode.getStatDelegateValue());
            // 有效的质押委托总数
            resp.setTotalValue(stakingNode.getTotalValue());

            /**
             * 如果判断为true则表示为查历史数据
             * 没有值则标识查询活跃账户
             */
            if (stakingNode.getStatus().intValue() == StatusEnum.CANDIDATE.getCode()) {
                // 候选中的节点设置有效委托地址数
                resp.setDelegateQty(stakingNode.getStatValidAddrs());
                /** 质押金额=质押（犹豫期）+ 质押（锁定期）  */
                BigDecimal stakingValue = stakingNode.getStakingHes().add(stakingNode.getStakingLocked());
                resp.setStakingValue(stakingValue);
            } else {
                // 其它状态节点设置待提取委托地址数
                resp.setDelegateQty(stakingNode.getStatInvalidAddrs());
                /**
                 * 如果在结算中则直接设置为0
                 */
                if (stakingNode.getIsSettle().intValue() == YesNoEnum.YES.getCode()) {
                    resp.setTotalValue(BigDecimal.ZERO);
                    resp.setStakingValue(BigDecimal.ZERO);
                } else {
                    if (stakingNode.getStatus().intValue() == StatusEnum.LOCKED.getCode()) {
                        resp.setStakingValue(stakingNode.getStakingLocked());
                    } else {
                        resp.setStakingValue(stakingNode.getStakingReduction());
                    }
                }
                // 待提取的委托金额(von)
                resp.setStatDelegateReduction(resp.getDelegateValue().add(stakingNode.getStatDelegateReleased()));
            }
        }
        return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp);
    }

    public RespPage<StakingOptRecordListResp> stakingOptRecordList(StakingOptRecordListReq req) {
        RespPage<StakingOptRecordListResp> respPage = new RespPage<>();
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.must(new ESQueryBuilders().term("nodeId", req.getNodeId()));
        ESResult<NodeOpt> items = new ESResult<>();
        constructor.setDesc("id");
        try {
            items = ESNodeOptRepository.search(constructor, NodeOpt.class, req.getPageNo(), req.getPageSize());
        } catch (Exception e) {
            logger.error("获取节点操作错误。", e);
            return respPage;
        }
        List<NodeOpt> nodeOpts = items.getRsData();
        List<StakingOptRecordListResp> lists = new LinkedList<>();
        for (NodeOpt nodeOpt : nodeOpts) {
            StakingOptRecordListResp stakingOptRecordListResp = new StakingOptRecordListResp();
            BeanUtils.copyProperties(nodeOpt, stakingOptRecordListResp);
            stakingOptRecordListResp.setType(String.valueOf(nodeOpt.getType()));
            stakingOptRecordListResp.setTimestamp(nodeOpt.getTime().getTime());
            stakingOptRecordListResp.setBlockNumber(nodeOpt.getBNum());
            if (StringUtils.isNotBlank(nodeOpt.getDesc())) {
                String[] desces = nodeOpt.getDesc().split(Browser.OPT_SPILT);
                /** 根据不同类型组合返回 */
                switch (NodeOpt.TypeEnum.getEnum(String.valueOf(nodeOpt.getType()))) {
                    /**
                     *修改验证人
                     */
                    case MODIFY:
                        if (desces.length > 1) {
                            stakingOptRecordListResp.setBeforeRate(new BigDecimal(desces[0]).divide(Browser.PERCENTAGE).toString());
                            stakingOptRecordListResp.setAfterRate(new BigDecimal(desces[1]).divide(Browser.PERCENTAGE).toString());
                        }
                        break;
                    /** 提案类型 */
                    case PROPOSALS:
                        Proposal proposal = proposalMapper.selectByPrimaryKey(nodeOpt.getTxHash());
                        if (ObjectUtil.isNotNull(proposal) && StrUtil.isNotBlank(proposal.getTopic())) {
                            String desc = StrUtil.replace(stakingOptRecordListResp.getDesc(), Browser.INQUIRY, proposal.getTopic());
                            nodeOpt.setDesc(desc);
                            stakingOptRecordListResp.setDesc(desc);
                            desces = nodeOpt.getDesc().split(Browser.OPT_SPILT);
                        }
                        stakingOptRecordListResp.setId(Browser.PIP_NAME + desces[0]);
                        stakingOptRecordListResp.setTitle(Browser.INQUIRY.equals(desces[1]) ? "" : desces[1]);
                        stakingOptRecordListResp.setProposalType(desces[2]);
                        if (desces.length > 3) {
                            stakingOptRecordListResp.setVersion(desces[3]);
                        }
                        break;
                    /** 投票类型 */
                    case VOTE:
                        // 描述是由定时任务更新的，所以每次查询都要重新查询取最新的值
                        CustomVoteProposal customVoteProposal = customVoteMapper.selectVotePropal(nodeOpt.getTxHash());
                        if (ObjectUtil.isNotNull(customVoteProposal) && StrUtil.isNotBlank(customVoteProposal.getTopic())) {
                            String desc = StrUtil.replace(stakingOptRecordListResp.getDesc(), Browser.INQUIRY, customVoteProposal.getTopic());
                            nodeOpt.setDesc(desc);
                            stakingOptRecordListResp.setDesc(desc);
                            desces = nodeOpt.getDesc().split(Browser.OPT_SPILT);
                        }
                        stakingOptRecordListResp.setTitle(Browser.INQUIRY.equals(desces[1]) ? "" : desces[1]);
                        stakingOptRecordListResp.setId(Browser.PIP_NAME + desces[0]);
                        stakingOptRecordListResp.setOption(desces[2]);
                        stakingOptRecordListResp.setProposalType(desces[3]);
                        if (desces.length > 4) {
                            stakingOptRecordListResp.setVersion(desces[4]);
                        }
                        break;
                    /** 双签 */
                    case MULTI_SIGN:
                        stakingOptRecordListResp.setPercent(desces[0]);
                        stakingOptRecordListResp.setAmount(new BigDecimal(desces[1]));
                        break;
                    /** 出块率低 */
                    case LOW_BLOCK_RATE:
                        stakingOptRecordListResp.setPercent(desces[1]);
                        stakingOptRecordListResp.setAmount(new BigDecimal(desces[2]));
                        stakingOptRecordListResp.setIsFire(Integer.parseInt(desces[3]));
                        break;
                    /**
                     * 参数提案
                     */
                    case PARAMETER:
                        stakingOptRecordListResp.setId(Browser.PIP_NAME + desces[0]);
                        stakingOptRecordListResp.setTitle(Browser.INQUIRY.equals(desces[1]) ? "" : desces[1]);
                        stakingOptRecordListResp.setProposalType(desces[2]);
                        stakingOptRecordListResp.setType("4");
                        break;
                    /**
                     * 版本声明
                     */
                    case VERSION:
                        String v = desces[2];
                        if (StringUtils.isNotBlank(v)) {
                            v = ChainVersionUtil.toStringVersion(new BigInteger(v));
                        } else {
                            v = "0";
                        }
                        stakingOptRecordListResp.setVersion(v);
                        stakingOptRecordListResp.setType("12");
                        break;
                    default:
                        break;
                }
            }

            lists.add(stakingOptRecordListResp);
        }
        /** 查询分页总数 */
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        page.setTotal(items.getTotal());
        respPage.init(page, lists);
        return respPage;
    }

    public RespPage<DelegationListByStakingResp> delegationListByStaking(DelegationListByStakingReq req) {
        Node node = nodeMapper.selectByPrimaryKey(req.getNodeId());
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<DelegationListByStakingResp> lists = new LinkedList<>();
        /** 根据节点id和区块查询验证委托信息 */
        Page<DelegationStaking> delegationStakings = customDelegationMapper.selectStakingByNodeId(req.getNodeId());
        for (DelegationStaking delegationStaking : delegationStakings.getResult()) {
            DelegationListByStakingResp byStakingResp = new DelegationListByStakingResp();
            BeanUtils.copyProperties(delegationStaking, byStakingResp);
            byStakingResp.setDelegateAddr(delegationStaking.getDelegateAddr());
            /**已锁定委托（LAT）如果关联的验证人状态正常则正常显示，如果其他情况则为零（delegation）  */
            byStakingResp.setDelegateTotalValue(node.getStatDelegateValue());
            /**
             * 委托金额等于has加上实际lock金额
             */
            BigDecimal delValue = delegationStaking.getDelegateHes().add(delegationStaking.getDelegateLocked());
            byStakingResp.setDelegateValue(delValue);
            lists.add(byStakingResp);
        }
        /** 分页统计总数 */
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        page.setTotal(delegationStakings.getTotal());
        RespPage<DelegationListByStakingResp> respPage = new RespPage<>();
        respPage.init(page, lists);
        return respPage;
    }

    public RespPage<DelegationListByAddressResp> delegationListByAddress(DelegationListByAddressReq req) {
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<DelegationListByAddressResp> lists = new LinkedList<>();
        /** 根据地址分页查询委托列表 */
        Page<DelegationAddress> delegationAddresses = customDelegationMapper.selectAddressByAddr(req.getAddress());
        /**
         * 初始化奖励节点id列表，用来后续查询对应的待领取奖励使用
         */
        List<String> nodes = new ArrayList<>();
        for (DelegationAddress delegationAddress : delegationAddresses.getResult()) {
            nodes.add(delegationAddress.getNodeId());
        }
        /**
         * 获取每一个节点的待领取奖励
         */
        List<Reward> rewards = new ArrayList<>();
        try {
            rewards = platonClient.getRewardContract().getDelegateReward(req.getAddress(), nodes).send().getData();
        } catch (Exception e) {
            logger.error("获取奖励数据错误：{}", e.getMessage());
            rewards = new ArrayList<>();
        }
        for (DelegationAddress delegationAddress : delegationAddresses.getResult()) {
            DelegationListByAddressResp byAddressResp = new DelegationListByAddressResp();
            BeanUtils.copyProperties(delegationAddress, byAddressResp);
            byAddressResp.setDelegateHas(delegationAddress.getDelegateHes());
            /** 委托金额=犹豫期金额加上锁定期金额 */
            BigDecimal deleValue = delegationAddress.getDelegateHes().add(byAddressResp.getDelegateLocked());
            byAddressResp.setDelegateValue(deleValue);
            byAddressResp.setDelegateUnlock(delegationAddress.getDelegateHes());
            /**
             * 循环获取奖励
             */
            if (rewards != null) {
                for (Reward reward : rewards) {
                    /**
                     * 匹配成功之后设置金额
                     */
                    if (delegationAddress.getNodeId().equals(HexUtil.prefix(reward.getNodeId()))) {
                        byAddressResp.setDelegateClaim(new BigDecimal(reward.getReward()));
                    }
                }
            }
            lists.add(byAddressResp);
        }
        /** 统计总数 */
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        page.setTotal(delegationAddresses.getTotal());
        RespPage<DelegationListByAddressResp> respPage = new RespPage<>();
        respPage.init(page, lists);
        return respPage;
    }

    public RespPage<LockedStakingListResp> lockedStakingList(LockedStakingListReq req) {
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        RespPage<LockedStakingListResp> respPage = new RespPage<>();
        List<LockedStakingListResp> lists = new LinkedList<>();
        NodeExample nodeExample = new NodeExample();
        nodeExample.setOrderByClause(" leave_time desc");
        NodeExample.Criteria criteria = nodeExample.createCriteria();
        criteria.andStatusEqualTo(StatusEnum.LOCKED.getCode());

        if (StringUtils.isNotBlank(req.getKey())) {
            criteria.andNodeNameLike("%" + req.getKey() + "%");
        }
        Page<Node> stakingPage = customNodeMapper.selectListByExample(nodeExample);

        /** 查询出块节点 */
        //NetworkStat networkStatRedis = this.statisticCacheService.getNetworkStatCache();
        int i = (req.getPageNo() - 1) * req.getPageSize();
        for (Node node : stakingPage) {
            LockedStakingListResp lockedStakingListResp = new LockedStakingListResp();
            BeanUtils.copyProperties(node, lockedStakingListResp);
            lockedStakingListResp.setBlockQty(node.getStatBlockQty());
            lockedStakingListResp.setDelegateQty(node.getStatValidAddrs());
            lockedStakingListResp.setExpectedIncome(node.getAnnualizedRate().toString());
            /** 委托总金额数=委托交易总金额(犹豫期金额)+委托交易总金额(锁定期金额) */
            String sumAmount = node.getStatDelegateValue().toString();
            lockedStakingListResp.setDelegateValue(sumAmount);
            lockedStakingListResp.setIsInit(node.getIsInit() == 1);
            lockedStakingListResp.setStakingIcon(node.getNodeIcon());
            if (node.getIsRecommend() != null) {
                lockedStakingListResp.setIsRecommend(CustomStaking.YesNoEnum.YES.getCode() == node.getIsRecommend());
            }
            /** 设置排行 */
            lockedStakingListResp.setRanking(i + 1);
            lockedStakingListResp.setSlashLowQty(node.getStatSlashLowQty());
            lockedStakingListResp.setSlashMultiQty(node.getStatSlashMultiQty());
            lockedStakingListResp.setStatus(StakingStatusEnum.LOCKED.getCode());
            Date leaveTime = node.getLeaveTime();
            lockedStakingListResp.setLeaveTime(leaveTime == null ? null : leaveTime.getTime());
            /** 质押总数=有效的质押+委托 */
            lockedStakingListResp.setTotalValue(node.getTotalValue().toString());
            lockedStakingListResp.setDeleAnnualizedRate(node.getDeleAnnualizedRate().toString());

            // 锁定节占预估解锁块高 = （节点锁定时所在结算周期+锁定结算周期数）x 每个结算周期区块数量
            int epoches = node.getZeroProduceFreezeEpoch() + node.getZeroProduceFreezeDuration();
            BigInteger unlockBlockNum = blockChainConfig.getSettlePeriodBlockCount().multiply(BigInteger.valueOf(epoches));
            lockedStakingListResp.setUnlockBlockNum(unlockBlockNum.longValue());

            lists.add(lockedStakingListResp);
            i++;
        }
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        page.setTotal(stakingPage.getTotal());
        respPage.init(page, lists);
        return respPage;
    }

}
