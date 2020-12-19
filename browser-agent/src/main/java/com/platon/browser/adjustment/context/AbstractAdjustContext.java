package com.platon.browser.adjustment.context;

import com.platon.browser.adjustment.bean.AdjustParam;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 调账上下文
 */
@Data
public abstract class AbstractAdjustContext {
    private AdjustParam adjustParam;
    private Staking staking;
    private Node node;
    /**
     * 检查上下文是否有错：需要的数据是否都有
     * @return
     */
    public List<String> validateContext(){
        List<String> errors = new ArrayList<>();
        if(adjustParam ==null) errors.add("调账数据缺失！");
        // 调账目标记录是否都存在
        if(node==null) errors.add("节点记录缺失:[节点ID="+ adjustParam.getNodeId()+"]");
        if(staking==null) errors.add("质押记录缺失:[节点ID="+ adjustParam.getNodeId()+",节点质押块号="+ adjustParam.getStakingBlockNum()+"]");
        // 调账目标记录各项金额是否都足够扣减
        validateAmount(errors);
        return errors;
    }

    /**
     * 校验调账金额数据
     * @param errors
     */
    abstract void validateAmount(List<String> errors);
}
