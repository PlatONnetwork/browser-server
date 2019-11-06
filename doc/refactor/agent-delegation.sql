-- 发起委托（1004）
-- 入参（input中参数）
set @node_id = '0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18';
set @amount = '100000000000000';
-- 入参（交易中参数）
set @block_number = '300';
set @tx_from = '0xff48d9712d8a55bf603dab28f4645b6985696a61';
set @dele_sequence = "10000000";            -- @block_number * 100000 + @tx_index
-- 入参（进程缓存）
set @sbn = 200;         -- 质押对应的区块高度

-- 1. node 更新
update `node`
set `total_value` = `total_value` + @amount,
    `stat_delegate_value` = `stat_delegate_value` + @amount,
    `stat_valid_addrs` = if((select 1 from delegation where is_history = 2 and delegate_addr = @tx_from  and node_id = @node_id) is null,stat_valid_addrs + 1,stat_valid_addrs)
where `node_id` = @node_id;

-- 2.staking 更新
update `staking`
set `stat_delegate_hes` = `stat_delegate_hes` + @amount
where `node_id` = @node_id
   and `staking_block_num` = @sbn;

-- 3.delegation 创建或更新
insert into `delegation` 
	(
	`delegate_addr`, 
	`staking_block_num`, 
	`node_id`, 
	`delegate_hes`,
	`sequence`, 
	`cur_delegation_block_num`
	)
	values(
	@tx_from,
	@sbn,
	@node_id,
	@amount,
	@dele_sequence,
	@block_number
	)
	on duplicate key update
	`delegate_hes` = `delegate_hes` + @amount,
	`is_history` = 2,
	`cur_delegation_block_num` = @block_number;



-- 减持/撤销委托（1005）
-- 入参（input中参数）
set @node_id = '0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18';
set @amount = '100000000000000';
set @stakingBlockNum = '200';
set @MinimumThreshold = '500000';   --最新委托阀值
-- 入参（交易中参数）
set @block_number = '300';
set @tx_from = '0xff48d9712d8a55bf603dab28f4645b6985696a61';

-- 1. 查询 delegation 信息
select 
	delegate_hes,
	delegate_locked,
	delegate_released
from delegation
where delegate_addr = @tx_from
   and staking_block_num = @stakingBlockNum
   and node_id = @node_id
   
-- 2.程序计算逻辑
set code_delegate_hes;        --当前犹豫金额
set code_rm_delegate_hes;     --扣减犹豫金额
set code_delegate_locked;     --当前锁定金额
set code_rm_delegate_locked;  --扣减锁定金额
set code_delegate_released;   --当前待赎回金额
set code_rm_delegate_released;--扣减待赎回金额
set code_is_history;          --当前是否为历史
set code_real_amount;   	  --真正退款金额
set code_node_is_leave=false; --节点是否退出

isRefundAll = delegate_hes + delegate_locked + delegate_released - @amount < @MinimumThreshold
if(delegate_released > 0 ){
	code_node_is_leave = true;
}
if (isRefundAll = true){
	code_is_history = 1
	code_real_amount = delegate_hes + delegate_locked + delegate_released;	
	code_delegate_hes = 0;
	code_delegate_locked = 0;
	code_delegate_released = 0;
}else {
	code_is_history = 2;
	code_real_amount = @amount;
	if(delegate_released > 0){
		code_delegate_released = delegate_released - @amount;
	}else if(delegate_hes >=  @amount){
		code_delegate_hes = delegate_hes - @amount;
		code_delegate_locked = delegate_locked;
	}else{
	 	code_delegate_hes = 0;
	  	code_delegate_locked = delegate_locked + delegate_hes - @amount; 
	}
}
code_rm_delegate_hes = delegate_hes - code_delegate_hes;
code_rm_delegate_locked = delegate_locked - code_delegate_locked;
code_rm_delegate_released = delegate_released - code_delegate_released;


-- 3. node 更新
update `node`
set `total_value` = `total_value` - @code_rm_delegate_hes - @code_rm_delegate_locked,
    `stat_delegate_value` = `stat_delegate_value` - @code_rm_delegate_hes - @code_rm_delegate_locked,
	`stat_delegate_released` = `stat_delegate_released` - @code_rm_delegate_released,
	-- code_is_history = 2 and code_node_is_leave = false 时执行
	`stat_valid_addrs` = `stat_valid_addrs`- 1,
	-- code_is_history = 2 and code_node_is_leave = true 时执行
	`stat_invalid_addrs` = `stat_invalid_addrs` - 1
where `node_id` = @node_id;

-- 4.delegation 更新
update `delegation` 
set `delegate_hes` = @code_delegate_hes,
    `delegate_locked` = @code_delegate_locked,
	`delegate_released` = @code_rm_delegate_released,
    `is_history` = @code_is_history
where
    `delegate_addr` = @tx_from
    and `node_id` = @node_id
    and `staking_block_num` = @stakingBlockNum;

-- 5.staking 更新
update `staking`
set `stat_delegate_hes` = `stat_delegate_hes` - @code_rm_delegate_hes,
    `stat_delegate_locked` = `stat_delegate_locked` - @code_rm_delegate_locked,
    `stat_delegate_released` = `stat_delegate_released` - @code_rm_delegate_released,
where s.`node_id` = @node_id
     and s.staking_block_num =  @stakingBlockNum;