--网络统计变更消息<br/>
--入库时直接替换下面字段

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


--地址统计变更消息

insert into `address`
	(`address`,
	`type`,
	`tx_qty`,
	`transfer_qty`,
	`delegate_qty`,
	`staking_qty`,
	`proposal_qty`,
	`contract_name`,
	`contract_create`,
	`contract_createHash`
	)
	values
	('address',
	'type',
	'tx_qty',
	'transfer_qty',
	'delegate_qty',
	'staking_qty',
	'proposal_qty',
	'contract_name',
	'contract_create',
	'contract_createHash'
	)
	on duplicate key update
	`tx_qty` = `tx_qty` + @,
	`transfer_qty` = `transfer_qty` + @,
	`delegate_qty` = `delegate_qty` + @,
	`staking_qty` = `staking_qty` + @,
	`proposal_qty` = `proposal_qty` + @;