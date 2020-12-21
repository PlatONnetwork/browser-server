package com.platon.browser.adjustment.context;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 质押调账上下文
 * 必须数据：
 * 1、质押信息
 * 2、节点信息
 */
@Slf4j
@Data
public class StakingAdjustContext extends AbstractAdjustContext {
    /**
     * 校验质押相关金额是否满足调账要求
     */
    @Override
    public void validateAmount(){
        // 验证金额是否正确前，检查各项必须的数据是否存在
        if (!errors.isEmpty()) return;
        if(
            staking.getStakingHes().compareTo(adjustParam.getHes())<0
            ||staking.getStakingLocked().compareTo(adjustParam.getLock())<0
        ){
            errors.add("质押记录：\n"+ JSON.toJSONString(staking,true));
        }

        if(staking.getStakingHes().compareTo(adjustParam.getHes())<0){
            errors.add("【错误】：质押记录犹豫期金额【"+staking.getStakingHes()+"】小于调账犹豫期金额【"+adjustParam.getHes()+"】！");
        }
        if(staking.getStakingLocked().compareTo(adjustParam.getLock())<0){
            errors.add("【错误】：质押记录锁定期金额【"+staking.getStakingLocked()+"】小于调账锁定期金额【"+adjustParam.getLock()+"】！");
        }
    }

    @Override
    String extraContextInfo() {
        return null;
    }
}
