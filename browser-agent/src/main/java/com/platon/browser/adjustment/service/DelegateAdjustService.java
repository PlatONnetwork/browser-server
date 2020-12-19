package com.platon.browser.adjustment.service;

import com.platon.browser.adjustment.bean.AdjustParam;
import com.platon.browser.adjustment.bean.AdjustResult;
import com.platon.browser.adjustment.context.DelegateAdjustContext;
import com.platon.browser.adjustment.dao.AdjustmentMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 质押相关数据调整服务
 */
@Slf4j
@Service
public class DelegateAdjustService {

    @Resource
    private DelegationMapper delegationMapper;
    @Resource
    private StakingMapper stakingMapper;
    @Resource
    private NodeMapper nodeMapper;
    @Resource
    private AdjustmentMapper adjustmentMapper;

    /**
     * @param adjustParams 委托调账明细
     */
    @Transactional
    public AdjustResult adjustDelegateData(List<AdjustParam> adjustParams) {
        AdjustResult adjustResult = new AdjustResult();
        if(adjustParams.isEmpty()) return adjustResult;

        List<DelegateAdjustContext> adjustContextList = new ArrayList<>();
        // 针对每笔委托调账数据获取完整的【调账上下文数据】
        for (AdjustParam adjustParam : adjustParams) {
            // 根据<委托者地址,质押块高,节点ID>找到对应的委托信息
            DelegationKey delegationKey = new DelegationKey();
            delegationKey.setDelegateAddr(adjustParam.getAddr());
            delegationKey.setStakingBlockNum(Long.valueOf(adjustParam.getStakingBlockNum()));
            delegationKey.setNodeId(adjustParam.getNodeId());
            Delegation delegation = delegationMapper.selectByPrimaryKey(delegationKey);
            // 根据<质押块高,节点ID>找到对应的质押信息
            StakingKey stakingKey = new StakingKey();
            stakingKey.setNodeId(adjustParam.getNodeId());
            stakingKey.setStakingBlockNum(Long.valueOf(adjustParam.getStakingBlockNum()));
            Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
            // 根据<节点ID>找到对应的节点信息
            Node node = nodeMapper.selectByPrimaryKey(adjustParam.getNodeId());
            // 拼装调账需要的完整上下文数据
            DelegateAdjustContext dac = new DelegateAdjustContext();
            dac.setAdjustParam(adjustParam);
            dac.setDelegation(delegation);
            dac.setStaking(staking);
            dac.setNode(node);
            adjustContextList.add(dac);
        }

        // 构造调整结果
        adjustContextList.forEach(dac->{
            List<String> errors = dac.validateContext();
            adjustResult.getErrors().put(dac.getAdjustParam(),errors);
        });

        boolean success = adjustResult.validate();
        if(success){
            // 如果调账数据有效，则执行调账操作
            adjustmentMapper.adjustDelegateData(adjustParams);
        }

        return adjustResult;
    }
}
