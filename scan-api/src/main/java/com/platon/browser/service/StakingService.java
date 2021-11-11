package com.platon.browser.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.bean.CustomDelegation.YesNoEnum;
import com.platon.browser.bean.CustomStaking;
import com.platon.browser.bean.CustomStaking.StatusEnum;
import com.platon.browser.bean.DelegationAddress;
import com.platon.browser.bean.DelegationStaking;
import com.platon.browser.bean.NodeSettleStatis;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.constant.Browser;
import com.platon.browser.dao.custommapper.CustomDelegationMapper;
import com.platon.browser.dao.custommapper.CustomNodeMapper;
import com.platon.browser.dao.custommapper.CustomStakingMapper;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.NodeMapper;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    private CustomStakingMapper customStakingMapper;

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
            //实时除以现有的结算周期人数
            Integer count = customStakingMapper.selectCountByActive();
            // 质押奖励 = 当前结算周期总质押奖励转成LAT单位
            stakingStatisticNewResp.setStakingReward(networkStatRedis.getSettleStakingReward().divide(new BigDecimal(count), 18, RoundingMode.FLOOR));
        }
        BigDecimal issueValue = commonService.getIssueValue();
        logger.info("获取总发行量[{}]", issueValue.toPlainString());

        CommonService.check(issueValue);
        issueValue = CommonService.ISSUE_VALUE;

        stakingStatisticNewResp.setIssueValue(issueValue);
        BigDecimal stakingDenominator = commonService.getStakingDenominator();
        stakingStatisticNewResp.setStakingDenominator(stakingDenominator);
        BigDecimal totalStakingValue = commonService.getTotalStakingValue();
        stakingStatisticNewResp.setStakingDelegationValue(totalStakingValue);
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
                aliveStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(staking.getStatus(), staking.getIsConsensus(), staking.getIsSettle()));
            }
            /** 质押总数=有效的质押+委托 */
            aliveStakingListResp.setTotalValue(staking.getTotalValue().toString());
            aliveStakingListResp.setDeleAnnualizedRate(staking.getDeleAnnualizedRate().toString());
            try {
                String nodeSettleStatisInfo = staking.getNodeSettleStatisInfo();
                NodeSettleStatis nodeSettleStatis = NodeSettleStatis.jsonToBean(nodeSettleStatisInfo);
                BigInteger settleEpochRound = EpochUtil.getEpoch(BigInteger.valueOf(networkStatRedis.getCurNumber()), blockChainConfig.getSettlePeriodBlockCount());
                aliveStakingListResp.setGenBlocksRate(nodeSettleStatis.computeGenBlocksRate(settleEpochRound));
            } catch (Exception e) {
                logger.error("获取节点24小时出块率异常", e);
            }
            aliveStakingListResp.setDelegatedRewardRatio(new BigDecimal(staking.getRewardPer()).divide(Browser.PERCENTAGE).toString() + "%");
            if (staking.getProgramVersion() != 0) {
                aliveStakingListResp.setVersion(ChainVersionUtil.toStringVersion(BigInteger.valueOf(staking.getProgramVersion())));
            } else {
                aliveStakingListResp.setVersion(ChainVersionUtil.toStringVersion(BigInteger.valueOf(staking.getBigVersion())));
            }
            lists.add(aliveStakingListResp);
            i++;
        }
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        page.setTotal(stakingPage.getTotal());
        respPage.init(page, lists);
        return respPage;
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
            historyStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(stakingNode.getStatus(), stakingNode.getIsConsensus(), stakingNode.getIsSettle()));
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
                BigInteger settleEpochRound = EpochUtil.getEpoch(BigInteger.valueOf(networkStatRedis.getCurNumber()), blockChainConfig.getSettlePeriodBlockCount());
                resp.setGenBlocksRate(nodeSettleStatis.computeGenBlocksRate(settleEpochRound));
            } catch (Exception e) {
                logger.error("获取节点24小时出块率异常", e);
            }
            resp.setVersion(ChainVersionUtil.toStringVersion(BigInteger.valueOf(stakingNode.getProgramVersion())));
            /**
             * 待领取奖励等于 累积委托奖励加上上轮奖励减去已领取委托奖励
             */
            resp.setDeleRewardRed(stakingNode.getTotalDeleReward().add(stakingNode.getPreTotalDeleReward()).subtract(stakingNode.getHaveDeleReward()));
            /** 只有不是内置节点才计算年化率  */
            if (CustomStaking.YesNoEnum.YES.getCode() != stakingNode.getIsInit()) {
                resp.setExpectedIncome(String.valueOf(stakingNode.getAnnualizedRate()));
                resp.setRewardValue(stakingNode.getStatFeeRewardValue().add(stakingNode.getStatBlockRewardValue()).add(stakingNode.getStatStakingRewardValue()));
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
                        stakingOptRecordListResp.setId(Browser.PIP_NAME + desces[0]);
                        stakingOptRecordListResp.setTitle(Browser.INQUIRY.equals(desces[1]) ? "" : desces[1]);
                        stakingOptRecordListResp.setProposalType(desces[2]);
                        if (desces.length > 3) {
                            stakingOptRecordListResp.setVersion(desces[3]);
                        }
                        break;
                    /** 投票类型 */
                    case VOTE:
                        stakingOptRecordListResp.setId(Browser.PIP_NAME + desces[0]);
                        stakingOptRecordListResp.setTitle(Browser.INQUIRY.equals(desces[1]) ? "" : desces[1]);
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
