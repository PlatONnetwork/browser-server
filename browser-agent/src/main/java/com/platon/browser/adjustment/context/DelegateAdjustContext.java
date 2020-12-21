package com.platon.browser.adjustment.context;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.Delegation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 委托调账上下文
 * 必须数据：
 * 1、质押信息
 * 2、节点信息
 * 3、委托信息
 */
@Slf4j
@Data
public class DelegateAdjustContext extends AbstractAdjustContext {
    private Delegation delegation;
    /**
     * 校验委托相关金额是否满足调账要求
     */
    @Override
    public void validateAmount(){
        if(delegation==null) errors.add("【错误】：委托记录缺失:[节点ID="+adjustParam.getNodeId()+",节点质押块号="+adjustParam.getStakingBlockNum()+",委托人="+adjustParam.getAddr()+"]");
        // 验证金额是否正确前，检查各项必须的数据是否存在
        if(!errors.isEmpty()) return;

        if(
            delegation.getDelegateHes().compareTo(adjustParam.getHes())<0
            ||delegation.getDelegateLocked().compareTo(adjustParam.getLock())<0
        ){
            errors.add(extraContextInfo());
        }

        if(delegation.getDelegateHes().compareTo(adjustParam.getHes())<0){
            errors.add("【错误】：委托记录犹豫期金额【"+delegation.getDelegateHes()+"】小于调账犹豫期金额【"+adjustParam.getHes()+"】！");
        }
        if(delegation.getDelegateLocked().compareTo(adjustParam.getLock())<0){
            errors.add("【错误】：委托记录锁定期金额【"+delegation.getDelegateHes()+"】小于调账锁定期金额【"+adjustParam.getLock()+"】！");
        }
    }

    @Override
    String extraContextInfo() {
        String extra = "委托记录：\n"+JSON.toJSONString(delegation,true);
        return extra;
    }
}
