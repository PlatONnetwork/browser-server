package com.platon.browser.service.misc;

import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.service.govern.ParameterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * 质押相关杂项业务分析服务
 * 1、真实的解质押退出块号
 */
@Service
public class StakeMiscService {
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private ProposalMapper proposalMapper;

    /**
     * 获取解质押的实际退出块号
     * @param nodeId
     * @param curSettleEpoch
     * @param compareProposalVotingEndBlock 是否需要对比关联的提案
     * @return
     */
    public BigInteger getUnStakeEndBlock(String nodeId,BigInteger curSettleEpoch,Boolean compareProposalVotingEndBlock){
        // 理论上的退出区块号, 实际的退出块号还要跟状态为进行中的提案的投票截至区块进行对比，取最大者
        BigInteger unStakeEndBlock = curSettleEpoch // 当前块所处的结算周期轮数
                .add(getUnStakeFreeDuration()) //+ 解质押需要经过的结算周期轮数
                .multiply(chainConfig.getSettlePeriodBlockCount()); // x 每个结算周期的区块数
        if(compareProposalVotingEndBlock){
            // 如果需要跟当前节点的投票中的提案进行对比，则执行如下逻辑
            ProposalExample condition = new ProposalExample();
            condition.createCriteria()
                    .andNodeIdEqualTo(nodeId) // 提案节点ID等于当前退出节点
                    .andStatusEqualTo(CustomProposal.StatusEnum.VOTING.getCode()); // 提案状态为投票中
            List<Proposal> proposalList = proposalMapper.selectByExample(condition);
            for (Proposal proposal : proposalList) {
                BigInteger endVotingBlock = BigInteger.valueOf(proposal.getEndVotingBlock());
                if(endVotingBlock.compareTo(unStakeEndBlock)>0){
                    // 如果提案中的截至块号大于当前质押理论上的退出块号，则使用提案的截至块号
                    unStakeEndBlock = endVotingBlock;
                }
            }
        }
        return unStakeEndBlock;
    }

    /**
     * 获取解质押的需要经过的理论结算周期数
     * @return
     */
    public BigInteger getUnStakeFreeDuration(){
        // 更新解质押到账需要经过的结算周期数
        String configVal = parameterService.getValueInBlockChainConfig(ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
        if(StringUtils.isBlank(configVal)){
            throw new BusinessException("参数表参数缺失："+ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
        }
        return new BigInteger(configVal);
    }
}
