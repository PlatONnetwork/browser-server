package com.platon.browser.analyzer.ppos;

import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.custommapper.StakeBusinessMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dao.param.ppos.StakeExit;
import com.platon.browser.bean.CustomStaking;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.param.StakeExitParam;
import com.platon.browser.service.ppos.StakeEpochService;
import com.platon.browser.utils.EpochUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @description: 退出质押业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class StakeExitAnalyzer extends PPOSAnalyzer<NodeOpt> {

    @Resource
    private StakeBusinessMapper stakeBusinessMapper;

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private StakingMapper stakingMapper;

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private StakeEpochService stakeEpochService;

    /**
     * 撤销质押(退出验证人)
     *
     * @param event
     * @param tx
     * @return com.platon.browser.elasticsearch.dto.NodeOpt
     * @date 2021/6/15
     */
    @Override
    public NodeOpt analyze(CollectionEvent event, Transaction tx) throws BlockNumberException {
        // 撤销质押
        StakeExitParam txParam = tx.getTxParam(StakeExitParam.class);
        // 补充节点名称
        updateTxInfo(txParam, tx);

        BigInteger curEpoch;
        try {
            // 计算当前周期
            curEpoch = EpochUtil.getEpoch(BigInteger.valueOf(tx.getNum()), chainConfig.getSettlePeriodBlockCount());
            // 计算质押金退回的区块号 = 每个结算周期的区块数x(退出质押所在结算周期轮数+需要经过的结算周期轮数)
            BigInteger withdrawBlockNum = chainConfig.getSettlePeriodBlockCount()
                    .multiply(curEpoch.add(chainConfig.getUnStakeRefundSettlePeriodCount()));
            txParam.setWithdrawBlockNum(withdrawBlockNum);
        } catch (BlockNumberException e) {
            log.error("", e);
            throw new BusinessException("周期计算错误!");
        }

        // 失败的交易不分析业务数据，增加info信息
        if (Transaction.StatusEnum.FAILURE.getCode() == tx.getStatus()) {
            tx.setInfo(txParam.toJSONString());
            return null;
        }

        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(txParam.getNodeId());
        stakingKey.setStakingBlockNum(txParam.getStakingBlockNum().longValue());
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        if (staking == null) {
            throw new BusinessException("节点ID为[" + txParam.getNodeId() + "],质押区块号为[" + txParam.getStakingBlockNum() + "]的质押记录不存在！");
        }

        long startTime = System.currentTimeMillis();

        // 更新解质押到账需要经过的结算周期数
        BigInteger unStakeFreezeDuration = stakeEpochService.getUnStakeFreeDuration();
        // 理论上的退出区块号, 实际的退出块号还要跟状态为进行中的提案的投票截至区块进行对比，取最大者
        BigInteger unStakeEndBlock = stakeEpochService.getUnStakeEndBlock(txParam.getNodeId(), event.getEpochMessage().getSettleEpochRound(), true);
        // 撤销质押
        StakeExit businessParam = StakeExit.builder()
                .nodeId(txParam.getNodeId())
                .stakingBlockNum(txParam.getStakingBlockNum())
                .time(tx.getTime())
                .stakingReductionEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                .unStakeFreezeDuration(unStakeFreezeDuration.intValue())
                .unStakeEndBlock(unStakeEndBlock)
                .status(CustomStaking.StatusEnum.EXITING.getCode())
                .build();

        // 判断当前退出质押操作是否与质押创建操作处于同一个结算周期，如果输入同一周期，则节点置为已退出
        BigInteger stakeEpoch = EpochUtil.getEpoch(BigInteger.valueOf(staking.getStakingBlockNum()), chainConfig.getSettlePeriodBlockCount());
        if (stakeEpoch.compareTo(curEpoch) < 0) {
            // 质押被锁定后，撤销质押
            stakeBusinessMapper.lockedExit(businessParam);
        } else {
            // 创建质押和解除质押在同一结算周期，立即退出
            stakeBusinessMapper.unlockExit(businessParam);
        }

        // 查询质押金额
        BigDecimal stakingValue = staking.getStakingHes().add(staking.getStakingLocked());


        // 补充txInfo
        txParam.setAmount(stakingValue);
        tx.setInfo(txParam.toJSONString());

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);

        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setNodeId(txParam.getNodeId());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.QUIT.getCode()));
        nodeOpt.setTxHash(tx.getHash());
        nodeOpt.setBNum(tx.getNum());
        nodeOpt.setTime(tx.getTime());
        return nodeOpt;
    }

}
