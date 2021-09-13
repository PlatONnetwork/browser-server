package com.platon.browser.dao.param.epoch;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

/**
 * 共识周期变更消息 <br/>
 * <pre>
-- 1. staking 更新
update `staking` 
set `is_consensus` = if(`node_id` in @validator_list, 1,  2), -- 伪代码
    `pre_cons_block_qty` = `cur_cons_block_qty`,
    `cur_cons_block_qty` = 0
where `status` = 1;

-- 2. node 更新
update `node` 
set `is_consensus` = if(`node_id` in @validator_list, 1,  2), 
	`stat_verifier_time` = if(`node_id` in @validator_list, `stat_verifier_time` + 1,  `stat_verifier_time`),
	`stat_expect_block_qty` = if(`node_id` in @validator_list, `stat_expect_block_qty` + @expect_block_num,  `stat_expect_block_qty`),  
where `status` = 1;
 * <pre/>
 * @author chendai
 */
@Data
@Builder
@Accessors(chain = true)
public class Consensus implements BusinessParam {
    //每个验证人期望出块数 共识周期出块数/当轮验证人数量
    private BigInteger expectBlockNum;
    //当前共识周期验证人
    private List<String> validatorList;

    @Override
    public BusinessType getBusinessType () {
        return BusinessType.CONSENSUS_EPOCH;
    }
}