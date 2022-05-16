package com.platon.browser.analyzer.ppos;

import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.CustomStaking;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.SlashBusinessMapper;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Slash;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.SlashMapper;
import com.platon.browser.dao.param.ppos.Report;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.ReportParam;
import com.platon.browser.service.ppos.StakeEpochService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 举报验证人业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class ReportAnalyzer extends PPOSAnalyzer<NodeOpt> {

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private SlashBusinessMapper slashBusinessMapper;

    @Resource
    private NodeMapper nodeMapper;

    @Resource
    private StakeEpochService stakeEpochService;

    @Resource
    private SlashMapper slashMapper;

    /**
     * 举报多签(举报验证人)
     *
     * @param event
     * @param tx
     * @return com.platon.browser.elasticsearch.dto.NodeOpt
     * @date 2021/6/15
     */
    @Override
    public NodeOpt analyze(CollectionEvent event, Transaction tx) {
        // 举报信息
        ReportParam txParam = tx.getTxParam(ReportParam.class);
        if (null == txParam) return null;
        Node staking = nodeMapper.selectByPrimaryKey(txParam.getVerify());
        if (staking != null) {
            // 回填设置参数中的惩罚奖励信息
            //惩罚的金额  假设锁定的金额为0，则获取待赎回的金额
            BigDecimal stakingAmount = staking.getStakingLocked();
            if (stakingAmount.compareTo(BigDecimal.ZERO) == 0) {
                stakingAmount = staking.getStakingReduction();
            }
            //奖励的金额
            BigDecimal codeRewardValue = stakingAmount.multiply(chainConfig.getDuplicateSignSlashRate()).multiply(chainConfig.getDuplicateSignRewardRate());
            txParam.setReward(codeRewardValue);
        }

        updateTxInfo(txParam, tx);
        // 失败的交易不分析业务数据
        if (Transaction.StatusEnum.FAILURE.getCode() == tx.getStatus()) return null;

        long startTime = System.currentTimeMillis();

        // 举报成功，先把节点设置为异常，后续处罚操作在共识周期切换时执行
        List<String> nodeIdList = new ArrayList<>();
        nodeIdList.add(txParam.getVerify());
        slashBusinessMapper.setException(txParam.getVerify(), txParam.getStakingBlockNum().longValue());

        // 更新解质押到账需要经过的结算周期数
        BigInteger unStakeFreezeDuration = stakeEpochService.getUnStakeFreeDuration();

        Long blockNum = event.getBlock().getNum() - (event.getBlock().getNum() % chainConfig.getConsensusPeriodBlockCount().longValue()) + chainConfig.getConsensusPeriodBlockCount().longValue();
        // 理论上的退出区块号, 实际的退出块号还要跟状态为进行中的提案的投票截至区块进行对比，取最大者
        BigInteger unStakeEndBlock = stakeEpochService.getUnStakeEndBlock(txParam.getVerify(), event.getEpochMessage().getSettleEpochRound(), true);
        Report businessParam = Report.builder()
                                     .slashData(txParam.getData())
                                     .nodeId(txParam.getVerify())
                                     .txHash(tx.getHash())
                                     .time(tx.getTime())
                                     .stakingBlockNum(txParam.getStakingBlockNum())
                                     .slashRate(chainConfig.getDuplicateSignSlashRate())
                                     .benefitAddr(tx.getFrom())
                                     .slash2ReportRate(chainConfig.getDuplicateSignRewardRate())
                                     .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                                     .unStakeFreezeDuration(unStakeFreezeDuration.intValue())
                                     .unStakeEndBlock(unStakeEndBlock)
                                     .blockNum(blockNum)
                                     .build();

        /**
         * 只有第一次候选中惩罚的时候才需要更新质押锁定周期数
         */
        if (staking != null && staking.getStatus().intValue() == CustomStaking.StatusEnum.CANDIDATE.getCode()) {
            //更新节点提取质押需要经过的周期数
            slashBusinessMapper.updateUnStakeFreezeDuration(businessParam);
        }

        // 把举报参数暂时缓存，待共识周期切换时处理
        Slash slash = new Slash();
        slash.setNodeId(businessParam.getNodeId());
        slash.setTxHash(businessParam.getTxHash());
        slash.setTime(businessParam.getTime());
        slash.setSettingEpoch(businessParam.getSettingEpoch());
        slash.setStakingBlockNum(businessParam.getStakingBlockNum().longValue());
        slash.setSlashRate(businessParam.getSlashRate());
        slash.setSlashReportRate(businessParam.getSlash2ReportRate());
        slash.setBenefitAddress(businessParam.getBenefitAddr());
        slash.setUnStakeFreezeDuration(businessParam.getUnStakeFreezeDuration());
        slash.setUnStakeEndBlock(businessParam.getUnStakeEndBlock().longValue());
        slash.setBlockNum(businessParam.getBlockNum());
        slash.setIsHandle(false);
        slash.setSlashData(businessParam.getSlashData());
        slashMapper.insert(slash);
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);

        return null;
    }

}
