package com.platon.browser.analyzer.epoch;

import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.bean.CustomStaking.StatusEnum;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.EpochBusinessMapper;
import com.platon.browser.dao.custommapper.SlashBusinessMapper;
import com.platon.browser.dao.entity.Slash;
import com.platon.browser.dao.entity.SlashExample;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.SlashMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dao.param.epoch.Consensus;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.service.proposal.ProposalParameterService;
import com.platon.browser.service.statistic.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 共识
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/2/3
 */
@Slf4j
@Service
public class OnConsensusAnalyzer {

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private EpochBusinessMapper epochBusinessMapper;

    @Resource
    private StakingMapper stakingMapper;

    @Resource
    private SlashBusinessMapper slashBusinessMapper;

    @Resource
    private ProposalParameterService proposalParameterService;

    @Resource
    private StatisticService statisticService;

    @Resource
    private SlashMapper slashMapper;

    public Optional<List<NodeOpt>> analyze(CollectionEvent event, Block block) {
        long startTime = System.currentTimeMillis();

        log.debug("Block Number:{}", block.getNum());

        // 取下一共识周期的节点ID列表
        List<String> nextConsNodeIdList = new ArrayList<>();
        event.getEpochMessage().getCurValidatorList().forEach(v -> nextConsNodeIdList.add(v.getNodeId()));
        // 每个共识周期的期望出块数
        BigInteger expectBlockNum = chainConfig.getConsensusPeriodBlockCount().divide(BigInteger.valueOf(nextConsNodeIdList.size()));
        Consensus consensus = Consensus.builder().expectBlockNum(expectBlockNum).validatorList(nextConsNodeIdList) // 在sql中，如果节点在下一共识周期列表中，则共识状态设置为1，否则为2
                                       .build();
        epochBusinessMapper.consensus(consensus);

        // 取出双签参数缓存中的所有被举报节点ID列表
        SlashExample slashExample = new SlashExample();
        slashExample.createCriteria().andIsHandleEqualTo(false);
        List<Slash> reportedNodeIdList = slashMapper.selectByExampleWithBLOBs(slashExample);
        // 如果被举报节点不在下一轮共识周期中，则可对其执行安全的处罚操作
        List<String> notInNextConsNodeIdList = new ArrayList<>();
        reportedNodeIdList.forEach(slash -> {
            if (!nextConsNodeIdList.contains(slash.getNodeId())) notInNextConsNodeIdList.add(slash.getNodeId());
        });

        List<NodeOpt> nodeOpts = new ArrayList<>();
        if (!notInNextConsNodeIdList.isEmpty()) {
            // 以数据库中的数据为准，进行处罚
            List<Staking> slashList = slashBusinessMapper.getException(notInNextConsNodeIdList);
            if (!slashList.isEmpty()) {
                slashList.forEach(slashNode -> {
                    SlashExample slashNodeIdExample = new SlashExample();
                    slashNodeIdExample.createCriteria().andNodeIdEqualTo(slashNode.getNodeId()).andIsHandleEqualTo(false);
                    List<Slash> reportList = slashMapper.selectByExampleWithBLOBs(slashNodeIdExample);
                    reportList.forEach(report -> {
                        NodeOpt nodeOpt = slashNode(report, block);
                        nodeOpts.add(nodeOpt);
                    });
                    //对提案数据进行处罚
                    proposalParameterService.setSlashParameters(slashNode.getNodeId());
                });
            }
        }

        statisticService.nodeSettleStatisElected(event);

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);

        return Optional.ofNullable(nodeOpts);
    }

    /**
     * 惩罚节点
     *
     * @param businessParam:
     * @param block:
     * @return: com.platon.browser.elasticsearch.dto.NodeOpt
     * @date: 2021/12/2
     */
    private NodeOpt slashNode(Slash businessParam, Block block) {
        /**
         * 处理双签处罚
         * 重要！！！！！！： 一旦节点被双签处罚，节点所有金额都会变成待赎回状态
         * 锁定状态的金额会被置0
         * */
        // 根据节点ID和质押区块号查询符合条件得质押记录
        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(businessParam.getNodeId());
        stakingKey.setStakingBlockNum(businessParam.getStakingBlockNum());
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        // 锁定金额和待赎回只有一个会有值，所以取锁定或赎回的金额作为惩罚金的计算基数
        BigDecimal baseAmount = staking.getStakingLocked();
        if (baseAmount.compareTo(BigDecimal.ZERO) == 0) {
            baseAmount = staking.getStakingReduction();
        }
        //惩罚的金额=基数x惩罚比例
        BigDecimal codeSlashValue = baseAmount.multiply(businessParam.getSlashRate());
        //奖励的金额=惩罚金x奖励比例
        BigDecimal codeRewardValue = codeSlashValue.multiply(businessParam.getSlashReportRate());
        // 计算扣除惩罚金额后剩下的待赎回的金额
        BigDecimal codeRemainRedeemAmount = BigDecimal.ZERO;
        if (staking.getStakingLocked().compareTo(BigDecimal.ZERO) > 0) {
            codeRemainRedeemAmount = staking.getStakingLocked().subtract(codeSlashValue);
        }
        /**
         * 如果节点状态为退出中则需要reduction进行扣减
         * 因为处于退出中状态的节点所有钱都在赎回中状态
         */
        if (staking.getStatus().intValue() == StatusEnum.EXITING.getCode()) {
            codeRemainRedeemAmount = staking.getStakingReduction().subtract(codeSlashValue);
        }
        if (codeRemainRedeemAmount.compareTo(BigDecimal.ZERO) >= 0) {
            // 节点状态置为退出中
            businessParam.setCodeStatus(2);
            // 设置退出操作所在周期
            businessParam.setCodeStakingReductionEpoch(businessParam.getSettingEpoch());
        } else {
            // 节点状态置为已退出
            businessParam.setCodeStatus(3);
            businessParam.setCodeStakingReductionEpoch(0);
            //如果扣减的结果小于0则设置为0
            codeRemainRedeemAmount = BigDecimal.ZERO;
        }
        businessParam.setCodeRewardValue(codeRewardValue);
        businessParam.setCodeRemainRedeemAmount(codeRemainRedeemAmount);
        businessParam.setCodeSlashValue(codeSlashValue);
        slashBusinessMapper.slashNode(businessParam);
        //操作描述:6【PERCENT|AMOUNT】
        String desc = NodeOpt.TypeEnum.MULTI_SIGN.getTpl().replace("PERCENT", chainConfig.getDuplicateSignSlashRate().toString()).replace("AMOUNT", codeSlashValue.toString());
        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setNodeId(businessParam.getNodeId());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.MULTI_SIGN.getCode()));
        nodeOpt.setDesc(desc);
        nodeOpt.setTxHash(businessParam.getTxHash());
        nodeOpt.setBNum(businessParam.getBlockNum());
        nodeOpt.setTime(block.getTime());
        return nodeOpt;
    }

}
