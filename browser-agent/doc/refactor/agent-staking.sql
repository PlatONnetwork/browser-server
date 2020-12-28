-- 发起质押（1000）
-- 入参（input中参数）
set @node_id = '0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18';
set @staking_has = '500000000000000000000000000';
set @staking_name = 'testNode01';
set @external_id = 'externalId01';
set @benefit_addr = '0xff48d9712d8a55bf603dab28f4645b6985696a61';
set @program_version = '1794';
set @big_version = '1700';
set @web_site = 'web_site01';
set @details = 'details01';
set @is_init = 2;   -- if(benefit_addr = 激励池地址) 1 else 2
-- 入参（交易中参数）
set @staking_block_num = '200';
set @staking_tx_index = '0';
set @staking_addr = '0xb58c7fd25437e2fcf038b948963ffb80276bd44d';
set @join_time = '2019-10-11 07:31:20';
set @tx_hash = '0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7';

-- 1. staking 新增
insert into `staking` 
	(`node_id`, 
	`staking_block_num`, 
	`staking_tx_index`, 
	`staking_addr`, 
	`staking_hes`, 
	`node_name`, 
	`external_id`, 
	`benefit_addr`, 
	`program_version`, 
	`big_version`,
	`web_site`, 
	`details`, 
	`join_time`,
	`is_init`
	)
	values
	(@node_id, 
	@staking_block_num, 
	@staking_tx_index, 
	@staking_addr, 
	@staking_has, 
	@staking_name, 
	@external_id, 
	@benefit_addr, 
	@program_version, 
	@big_version, 
	@web_site, 
	@details, 
	@join_time,
	@is_init
	);

-- 2. node 新增或更新
insert into `node` 
	(`node_id`, 
	`staking_block_num`, 
	`staking_tx_index`, 
	`staking_addr`, 
	`staking_hes`,
	`node_name`, 
	`external_id`, 
	`benefit_addr`,
	`program_version`,	
	`big_version`,
	`web_site`, 
	`details`, 
	`join_time`, 
	`is_init`,
	`total_value`
	)
	values
	(@node_id, 
	@staking_block_num, 
	@staking_tx_index, 
	@staking_addr, 
	@staking_has, 
	@staking_name, 
	@external_id, 
	@benefit_addr, 
	@program_version, 
	@big_version, 
	@web_site, 
	@details, 
	@join_time, 
	@is_init, 
	@staking_has
	)
	on duplicate key update
	`staking_block_num` =  @staking_block_num,
	`staking_tx_index` = @staking_tx_index,
	`staking_addr` = @staking_addr, 
	`staking_hes` = @staking_has,
	`node_name` = @staking_name, 
	`external_id` = @external_id, 
	`benefit_addr` = @benefit_addr, 
	`program_version` = @program_version,
	`big_version` = @big_version,
	`web_site` = @web_site,
	`details` = @details, 
	`join_time` = @join_time, 
	`is_init` = @is_init, 
	`staking_hes` = @staking_has,
	`status` = '1',
	`is_consensus` = '2',
	`is_settle` = '2',
	`annualized_rate` = '0',
	`total_value` = `total_value` + @staking_has;

-- 3. node_opt 新增
insert into `node_opt` 
	(`node_id`, 
	`type`, 
	`tx_hash`, 
	`block_number`, 
	`timestamp`
	)
	values
	(@node_id,
	'1', 
	@tx_hash, 
	@staking_block_num,
	@join_time
	);
	
	
-- 修改质押信息（1001）
-- 入参（input中参数）
set @node_id = '0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18';
set @staking_name = 'testNode02';
set @external_id = 'externalId02';
set @benefit_addr = '0xff48d9712d8a55bf603dab28f4645b6985696a61';
set @web_site = 'web_site01';
set @details = 'details01';
set @is_init = 2;   -- if(benefit_addr = 激励池地址) 1 else 2
-- 入参（交易中参数）
set @tx_hash = '0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7';
set @block_number = '300';
set @tx_timestamp = '2019-10-13 07:31:20';
-- 入参（进程缓存node）
set @sbn = 200;         -- 质押对应的区块高度

-- 1. staking 更新
update `staking`
set `node_name` = @staking_name,
    `external_id` = @external_id,
    `benefit_addr` = @benefit_addr,
    `web_site` = @web_site,
    `details` = @details,
	`is_init` = @is_init
where `node_id` = @node_id 
and `staking_block_num` = @sbn;

-- 2. node 更新
update `node` 
set `node_name` = @staking_name,
	`external_id` =  @external_id,
	`benefit_addr` = @benefit_addr,
	`web_site` =  @web_site,
	`details` = @details,
	`is_init` = @is_init
where `node_id` = @node_id;
	
-- 3. node_opt 新增
insert into `node_opt` 
	(`node_id`, 
	`type`, 
	`tx_hash`, 
	`block_number`, 
	`timestamp`
	)
	values
	(@node_id, 
	'2', 
	@tx_hash, 
	@block_number, 
	@tx_timestamp
	);
		
		
-- 增持质押（1002）
-- 入参（input中参数）
set @node_id = '0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18';
set @amount = '500000000000000000000000000';
-- 入参（交易中参数）
set @tx_hash = '0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7';
set @block_number = '300';
set @tx_timestamp = '2019-10-13 07:31:20';
-- 入参（进程缓存）
set @sbn = 200;         -- 质押对应的区块高度

-- 1. staking 更新
update `staking`
set `staking_hes` = `staking_hes` + @amount
where `node_id` = @node_id
	and `staking_block_num` = @sbn;
									 
-- 2. node 更新
update `node` 
set `total_value` = `total_value` + @amount,
   `staking_hes` = `staking_hes` + @amount
where `node_id` = @node_id;
	
-- 3. node_opt 创建
insert into `node_opt` 
	(`node_id`, 
	`type`, 
	`tx_hash`, 
	`block_number`, 
	`timestamp`
	)
	values
	(@node_id, 
	'2', 
	@tx_hash, 
	@block_number, 
	@tx_timestamp
	);

-- 撤销质押（1003）
-- 入参（input中参数）
set @node_id = '0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18';
-- 入参（交易中参数）
set @tx_hash = '0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7';
set @block_number = '300';
set @tx_timestamp = '2019-10-13 07:31:20';
set @setting_epoch = '3';   -- 通过（block_number/每个结算周期出块数）向上取整
-- 入参（进程缓存）
set @sbn = 200;         -- 质押对应的区块高度

-- 1. 更新delegation，将有效委托迁移到待提取的委托总数
update `delegation` 
set `delegate_has` = 0, 
    `delegate_locked` = 0, 
    `delegate_released` = `delegate_has` + `delegate_locked`
where `node_id` = @node_id
	and `staking_block_num` = @sbn
    and `is_history` = 2;
	
-- 2. 更新node
update `node` 
set `leave_time` = @tx_timestamp,
	`status` = if(`staking_locked` > 0 ,"2","3"),
	`staking_reduction_epoch` = @setting_epoch,
    `staking_reduction` = `staking_locked`,
    `staking_locked` = 0,
	`staking_hes` = 0,
	`total_value` = `total_value` - `staking_locked` - `staking_hes`,
	`stat_delegate_value` = 0,
	`stat_delegate_released` = `stat_delegate_value`,
	`stat_valid_addrs` = 0,
	`stat_invalid_addrs` = select count(1) from delegation where node_id = @node_id and is_history = 2 group by delegate_addr
where `node_id` = @node_id;

-- 3. 更新staking 
update `staking`
set `leave_time` = @tx_timestamp,
    `status` = if(`staking_locked` > 0 ,"2","3"),
    `staking_reduction_epoch` = @setting_epoch,
    `staking_reduction` = `staking_locked`,
    `staking_locked` = 0,
	`staking_hes` = 0,
	`stat_delegate_has` = 0,
	`stat_delegate_locked` = 0,
	`stat_delegate_released` = `stat_delegate_has` + `stat_delegate_locked`
where `node_id` = @node_id
    and `staking_block_num` = @sbn;

-- 4. node_opt 新增
insert into `node_opt` 
	(`node_id`, 
	`type`, 
	`tx_hash`, 
	`block_number`, 
	`timestamp`
	)
	values
	(@node_id, 
	'2', 
	@tx_hash, 
	@block_number, 
	@tx_timestamp
	);
	
	
-- 撤举报多签
-- 入参（input中参数）
set @slash_data = "json";   -- 举报证据
set @node_id = '0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18';  -- 从证据中获取
-- 入参（交易中参数）
set @tx_hash = '0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7';
set @block_number = '300';
set @tx_timestamp = '2019-10-13 07:31:20';
set @tx_from = '0x02fba14f5e72092c8fca6ced087cd4e7be0d8fc5';
set @setting_epoch = '3';   -- 通过（block_number/每个结算周期出块数）向上取整
-- 入参（配置中）
set @slash_rate = "0.5";    -- 双签惩罚比例
set @slash_2_report =  99;  -- 惩罚金分配给举报人比例
-- 入参（进程缓存）
set @sbn = 200;         -- 质押对应的区块高度

- 1. 查询staking信息
select
	staking_locked
from staking
where `node_id` = @node_id
   and `staking_block_num` = @sbn
   
- 2.程序计算逻辑
set code_slash_value;        --惩罚的金额
set code_reward_value;       --奖励的金额
set code_status;             --节点状态
set code_nodeopt_desc;       --节点操作描述  'PERCENT|AMOUNT' 中
set code_cur_staking_locked; --当前锁定的
set code_staking_reduction_epoch; --当前退出中

code_slash_value = staking_locked * @slash_rate;
code_reward_value = code_slash_value * @slash_2_report;
code_nodeopt_desc = 'PERCENT|AMOUNT'.replace("AMOUNT",code_slash_value);
code_cur_staking_locked = staking_locked - code_slash_value；
if(cur_staking_locked > 0 )
	code_status = 2
    code_staking_reduction_epoch = @setting_epoch
else
    code_status = 3
	code_staking_reduction_epoch = 0


-- 1. node 更新
update `node` 
set `leave_time` = @tx_timestamp,
	  `status` =  @code_status,
	  `staking_reduction_epoch` = @code_staking_reduction_epoch,
    `staking_reduction` = @code_cur_staking_locked,
    `staking_locked` = 0,
	  `staking_hes` = 0,
	  `total_value` = `total_value` - `staking_locked` - `staking_hes`,
	  `stat_delegate_value` = 0,
	  `stat_delegate_released` = `stat_delegate_value`,
	  `stat_valid_addrs` = 0,
	  `stat_invalid_addrs` = select count(1) from delegation where node_id = @node_id and is_history = 2 group by delegate_addr
where `node_id` = @node_id;

-- 2. staking 更新
update `staking`
set `leave_time` = @tx_timestamp,
    `status` = @code_status,
    `staking_reduction_epoch` = @code_staking_reduction_epoch,
    `staking_reduction` = @code_cur_staking_locked,
    `staking_locked` = 0,
	  `staking_hes` = 0,
	  `stat_delegate_has` = 0,
	  `stat_delegate_locked` = 0,
	  `stat_delegate_released` = `stat_delegate_has` + `stat_delegate_locked`
where `node_id` = @node_id
    and `staking_block_num` = @sbn;

-- 3. slash 新增
insert into `slash` 
	(`hash`, 
	`node_id`, 
	`slash_rate`, 
    `slash_value`, 
	`reward`, 
	`denefit_addr`, 
	`data`
	)
	values
	(@tx_hash,
	@node_id,
	@slash_rate,
	@code_slash_value,
	@code_reward_value,
	@tx_from, 
	@slash_data
	);

-- 4. node_opt 新增
insert into `node_opt` 
	(`node_id`, 
	`type`, 
	`tx_hash`, 
	`block_number`, 
	`timestamp`,
	`desc`
	)
	values
	(@node_id,
	'6',
	@tx_hash,
	@block_number,
	@tx_timestamp,
	@code_nodeopt_desc
	);


-- 新的区块产生（event）
-- 入参（区块中参数）
set @tx_fee = '10000000000';
set @block_fee = '100000000000';    -- （增发周期激励池余额 * 出块奖励比例）/ 增发周期的块数。 向下取整
-- 入参（进程缓存）
set @sbn = 200;         -- 质押对应的区块高度
set @node_id = '0x111'; -- 

-- 1. staking 更新
update `staking`
set `cur_cons_block_qty` = `cur_cons_block_qty` + 1,
    `block_reward_value` = `block_reward_value` + @block_fee,
    `fee_reward_value` = `fee_reward_value` + @tx_fee
where `node_id` = @node_id
 	and `staking_block_num` = @sbn;
									 
-- 2. node 更新
update `node` 
set `stat_block_qty` = `stat_block_qty` + 1,
	`stat_block_reward_value` = `stat_block_reward_value` + @block_fee,
	`stat_fee_reward_value` = `stat_fee_reward_value` + @tx_fee
where `node_id` = @node_id;



-- 新的共识周期（event）
-- 入参
set @validator_list = ('0x001','0x002');    -- 当前共识周期验证人
set @expect_block_num = 10;                 -- 每个验证人期望出块数 共识周期出块数/当轮验证人数量

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


-- 新的结算周期（event）（收益需求外面计算）
-- 入参
set @cur_verifier_list = ('0x001','0x002');    -- 当前结算周期验证人
set @pre_verifier_list = ('0x001','0x002');    -- 上轮结算周期验证人
set @staking_fee = '100000000000';             --（增发周期激励池余额 * 质押奖励比例）/ 每个增发周期内结算周期个数，向下取整
set @setting_epoch = 3;                        -- 通过（block_number/每个结算周期出块数）向上取整
set @staking_lock_epoch = 3;                   -- 配置，解除质押锁定金额的轮数

-- 1. 查询staking信息 List<Staking>
select
  node_id,
  staking_block_num,
  staking_locked,
  staking_hes,
  staking_reduction,
  staking_reduction_epoch,
  status,
  is_settle,
  annualized_rate_info,
  annualized_rate
from staking 
where status in (1,2);

-- 2. 程序计算逻辑
for(Staking staking:listStakings){
	-- 犹豫期金额
	staking.staking_locked = staking.staking_locked + staking.staking_hes;
	staking.staking_hes = 0;
	-- 退出中记录状态设置
	if(staking.status == 2 && staking.staking_reduction_epoch + @staking_lock_epoch < @setting_epoch){
		staking.staking_reduction = 0;
		staking.status = 3;
	}
	-- 当前质押是上轮结算周期验证人,发放质押奖励  
	if(staking.node_id in @pre_verifier_list){
		staking.staking_fee = @staking_fee;
	}else{
		staking.staking_fee = 0;
	}
	
	-- 当前质押是下轮结算周期验证人
	if(staking.node_id in @cur_verifier_list){
		staking.is_settle = 1;
	}else {
		staking.is_settle = 2;
	}
	
	-- 计算年化率
	if(){
	
	}
	
	if(){
	
	}
	
	staking.annualized_rate_info = annualizedRateInfoMap.get(staking.node_id);
	staking.annualized_rate = annualizedRateMap.get(staking.node_id);
	
	-- 
}

-- 3. staking 批量更新 
update `staking` 
set -- 设置犹豫金额到锁定金额
	`staking_locked` = staking.staking_locked,
    `staking_hes` = staking.staking_hes,
	-- 退出中记录状态设置
    `staking_reduction` = staking.staking_reduction,
    `status` = staking.status,
    -- 当前质押是上轮结算周期验证人,发放质押奖励  
    `staking_reward_value` = `staking_reward_value + staking.staking_fee,
    -- 当前质押是下轮结算周期验证人
    `is_settle` = staking.is_settle,
	`annualized_rate` = staking.annualized_rate,
	`annualized_rate_info` = staking.annualized_rate_info
where `node_id` = staking.node_id 
	and staking_block_num = staking.staking_block_num;

-- 4. node 批量更新
update `node`
set  -- 设置犹豫金额到锁定金额
	`staking_locked` = staking.staking_locked,
    `staking_hes` = staking.staking_hes,
	-- 退出中记录状态设置
    `staking_reduction` = staking.staking_reduction,
    `status` = staking.status,
    -- 当前质押是上轮结算周期验证人,发放质押奖励  
    `stat_staking_reward_value` =  `stat_staking_reward_value`+ staking.staking_fee,
    -- 当前质押是下轮结算周期验证人
    `is_settle` = staking.is_settle,
	`annualized_rate` = staking.annualized_rate,
	`annualized_rate_info` = staking.annualized_rate_info
where `node_id` = staking.node_id ;

-- 5. delegation更新
update `delegation` 
set `delegate_has` = 0,
    `delegate_locked` = `delegate_has` + `delegate_locked`
where `is_history` = 2 
	and `delegate_has` > 0;

-- 6. staking 更新
update `staking`
set `stat_delegate_has` = 0,
    `stat_delegate_locked ` = `stat_delegate_has` + `stat_delegate_locked`
where `stat_delegate_has` > 0;
	

-- 新的选取周期（event:出块率等于0处罚--处罚移除候选列表，不扣钱（治理可能会变更））
-- 入参
set @pre_verify_list = ('0x001','0x002');      -- 上轮共识周期验证人
set @setting_epoch = '3';                      -- 通过（block_number/每个结算周期出块数）向上取整
-- 入参
set @block_number = '300';
set @tx_timestamp = '2019-10-13 07:31:20';

-- 1. 查询被惩罚的节点
select 
	`node_id`
from staking
where `status` = 1
	and `node_id` in @pre_verify_list
	and `pre_cons_block_qty` = 0;	

set nodeList;

-- 2. staking 更新
update `staking`
set `staking_hes` = 0,
    `staking_locked` = 0,
    `staking_reduction` = `staking_locked`,
    `staking_reduction_epoch` = @setting_epoch,
    `status` = if(`staking_locked` > 0, 2, 3), 
    `is_consensus` = 2,
    `is_settle` = 2,
    `leave_time` = @tx_timestamp
where `status` = 1
	and `node_id` in @pre_verifier_list
	and `pre_cons_block_qty` = 0;	

-- 3. node 更新
update `node`
set `staking_hes` = 0,
    `staking_locked` = 0,
    `staking_reduction` = `staking_locked`,
    `staking_reduction_epoch` = @setting_epoch,
    `status` = if(`staking_locked` > 0, 2, 3), 
    `is_consensus` = 2,
    `is_settle` = 2,
    `leave_time` = @tx_timestamp,
	`stat_slash_low_qty` = `stat_slash_low_qty` +1
	`total_value` = `total_value` - `staking_hes` - `staking_locked`
where `node_id` in nodeList;

-- 4. node_opt 循环 for(nodeId in nodeList)
insert into `node_opt` 
	(`node_id`, 
	`type`, 
	`block_number`, 
	`timestamp`,
	`desc`
	)
	values
	(
	@nodeId,
	'7',
	@block_number,
	@tx_timestamp,
	'0|6|100000|1'
	)

