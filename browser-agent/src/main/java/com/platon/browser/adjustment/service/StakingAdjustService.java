package com.platon.browser.adjustment.service;

import com.platon.browser.adjustment.bean.AdjustParam;
import com.platon.browser.adjustment.bean.AdjustResult;
import com.platon.browser.adjustment.context.StakingAdjustContext;
import com.platon.browser.adjustment.dao.AdjustmentMapper;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 质押相关数据调整服务
 */
@Service
public class StakingAdjustService {

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
    public AdjustResult adjustStakingData(List<AdjustParam> adjustParams) {
        AdjustResult adjustResult = new AdjustResult();
        if(adjustParams.isEmpty()) return adjustResult;

        List<StakingAdjustContext> adjustContextList = new ArrayList<>();
        // 针对每笔质押调账数据获取完整的【调账上下文数据】
        for (AdjustParam adjustParam : adjustParams) {
            // 根据<质押块高,节点ID>找到对应的质押信息
            StakingKey stakingKey = new StakingKey();
            stakingKey.setNodeId(adjustParam.getNodeId());
            stakingKey.setStakingBlockNum(Long.valueOf(adjustParam.getStakingBlockNum()));
            Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
            // 根据<节点ID>找到对应的节点信息
            Node node = nodeMapper.selectByPrimaryKey(adjustParam.getNodeId());
            // 拼装调账需要的完整上下文数据
            StakingAdjustContext sac = new StakingAdjustContext();
            sac.setAdjustParam(adjustParam);
            sac.setStaking(staking);
            sac.setNode(node);
            adjustContextList.add(sac);
        }

        // 构造调整结果
        adjustContextList.forEach(dac->{
            List<String> errors = dac.validateContext();
            adjustResult.getErrors().put(dac.getAdjustParam(),errors);
        });

        boolean success = adjustResult.validate();
        if(success){
            // 如果调账数据有效，则执行调账操作
            adjustmentMapper.adjustStakingData(adjustParams);
        }

        return adjustResult;
    }
}
