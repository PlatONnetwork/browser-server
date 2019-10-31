-- 实时验证人列表（*）
select
   `node_id`,
   `staking_name`,
   `staking_icon`,
   `status`,
   `is_setting`,
   `is_consensus`,
   `total_value`,
   `stat_delegate_value`,
   `stat_valid_addrs`,
   `stat_slash_low_qty`,
   `stat_slash_multi_qty`,
   `stat_block_qty`,
   `expected_income`,
   `is_recommend`,
   `is_init`
from `node`
where `status` = 1
  	and `is_consensus` = 2
   	and `is_setting` = 2
	and `staking_name` like '%'
order by `big_version` desc,
	`total_value` desc, 
	`staking_block_num` asc, 
	`staking_tx_index` asc;

-- 历史验证人列表（*）
select
   `node_id`,
   `staking_name`,
   `staking_icon`,
   `status`,
   `stat_delegate_released`,
   `stat_slash_low_qty`,
   `stat_slash_multi_qty`,
   `stat_block_qty`,
   `leave_time`
from `node`
where `status` in (2,3)
	and `staking_name` like '%'
order by `leave_time` desc;

-- 节点详情（*）
select
	`node_id`,
	`staking_name`,
	`staking_icon`,
	`is_init`,
	`join_time`,
	`leave_time`,
	`status`,
	`is_setting`,
	`is_consensus`,
	`staking_addr`,
	`benefit_addr`,
	`web_site`,
	`details`,
	`external_id`,
	`external_name`,
	`stat_verifier_time`,
	`stat_block_reward_value`,
	`stat_fee_reward_value`,
	`stat_staking_reward_value`,
	`stat_block_qty`,
	`stat_expect_block_qty`,
	`expected_income`,
	`stat_slash_low_qty`,
	`stat_slash_multi_qty`,
	`total_value`,
	`staking_has`,   
	`staking_locked`,    
	`stat_delegate_value`,          
	`stat_valid_addrs`,                
	`staking_reduction`,    
	`stat_delegate_released`,     
	`stat_invalid_addrs`             
from `node`
where `node_id` = "0x001";

-- 验证人相关的委托列表（*）
select 
     `delegate_addr`,
     sum(`delegate_has`),
     sum(`delegate_locked`),
     sum(`delegate_released`)
from `delegation`
where `is_history` = 2
   and `node_id` = '0x'
group by `delegate_addr`
order by `sequence`;

select 
   `stat_delegate_value`,
   `stat_valid_addrs`,
   `stat_invalid_addrs`
from `node`
where `node_id` = '0x';
	
-- 地址相关的委托列表（*）
select
   d.`node_id`,
   n.`staking_name`,
   sum(d.`delegate_has`),
   sum(d.`delegate_locked`),
   sum(d.`delegate_released`)
from `delegation` d inner join `node` n on d.`node_id` = n.`node_id`
where d.`is_history`=2  
    and d.`delegate_addr` = "0x01"
group by d.`node_id`
order by d.`sequence`;

select
   candidate_count,
   delegate_has,
   delegate_locked,
   delegate_released
from address
where address = '0x';

