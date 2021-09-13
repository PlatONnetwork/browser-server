package com.platon.browser.dao.param.statistic;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

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
public class NetworkStatChange implements BusinessParam {
	/**
	 * id
	 */
    private Integer id;

	/**
	 * 当前块号
	 */
    private Long curNumber;

	/**
	 * 节点ID
	 */
    private String nodeId;

	/**
	 * 节点名称
	 */
    private String nodeName;

	/**
	 * 交易总数
	 */
    private Integer txQty;

	/**
	 * 当前交易TPS
	 */
    private Integer curTps;

	/**
	 * 最大交易TPS
	 */
    private Integer maxTps;

	/**
	 * 提案总数
	 */
    private Integer proposalQty;

	/**
	 * 当前出块奖励(von)
	 */
    private BigDecimal blockReward;

	/**
	 * 当前质押奖励(von)
	 */
    private BigDecimal stakingReward;

	/**
	 * 当前增发周期的开始块号
	 */
    private Long addIssueBegin;

	/**
	 * 当前增发周期的结束块号
	 */
    private Long addIssueEnd;

	/**
	 * 离下个结算周期的剩余块数
	 */
    private Long nextSettle;

	/**
	 * 当前发行量(von)
	 */
	private BigDecimal issueValue;

	/**
	 * 当前流通量(von)
	 */
	private BigDecimal turnValue;

	/**
	 * 节点操作记录最新序号
	 */
	private Long nodeOptSeq;

	@Override
	public BusinessType getBusinessType() {
		return BusinessType.NETWORK_STATISTIC;
	}
}
