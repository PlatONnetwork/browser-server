package com.platon.browser.common.complement.dto.statistic;

import java.math.BigDecimal;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * 网络统计变更消息<br/>
 * 入库时直接替换下面字段 <br/>
 * <pre>
insert into `network_stat` 
	(`id`, 
	`cur_number`, 
	`node_id`, 
	`node_name`, 
	`tx_qty`, 
	`cur_tps`, 
	`max_tps`, 
	`issue_value`, 
	`turn_value`, 
	`proposal_qty`,  
	`block_reward`, 
	`staking_reward`, 
	`add_issue_begin`, 
	`add_issue_end`, 
	`next_settle`
	)
	values
	('id', 
	'cur_number', 
	'node_id', 
	'node_name', 
	'tx_qty', 
	'cur_tps', 
	'max_tps', 
	'issue_value', 
	'turn_value', 
	'proposal_qty', 
	'block_reward', 
	'staking_reward', 
	'add_issue_begin', 
	'add_issue_end', 
	'next_settle'
	)
	on duplicate key update
	`cur_number` = @, 
	`node_id` = @, 
	`node_name` = @,
	`tx_qty` = @,
	`cur_tps` = @,
	`max_tps` = @,
	`issue_value` = @, 
	`turn_value` = @,
	`proposal_qty` = @,
	`block_reward` = @,
	`staking_reward` = @,
	`add_issue_begin` = @,
	`add_issue_end` = @,
	`next_settle` = @;
 * <pre/>
 * @author chendai
 */
@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class NetworkStatChange extends BusinessParam {
    private Integer id;
    private Long curNumber;
    private String nodeId;
    private String nodeName;
    private Integer txQty;
    private Integer curTps;
    private Integer maxTps;
    private BigDecimal issueValue;
    private BigDecimal turnValue;
    private Integer proposalQty;
    private BigDecimal blockReward;
    private BigDecimal stakingReward;
    private Long addIssueBegin;
    private Long addIssueEnd;
    private Long nextSettle;
	
	@Override
	public BusinessType getBusinessType() {
		return BusinessType.NETWORK_STATISTIC;
	}
}
