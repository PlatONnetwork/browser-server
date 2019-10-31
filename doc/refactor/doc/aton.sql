-- 获取可用的验证节点列表（*）
select 
   `node_id`,
   `staking_name`,
   `staking_icon`,
   `total_value`,
   `expected_income`,
   `is_setting`,
   `is_init`
from `node`
where `status` = 1
order by `big_version` desc,
   `total_value` desc, 
   `staking_block_num` asc, 
   `staking_tx_index` asc;

-- 获取验证节点详情（*）
select
   `node_id`,
   `staking_name`,
   `staking_icon`,
   `total_value`,
   `expected_income`,
   `is_setting`,
   `is_init`,
   `web_site`,
   `details`,
   `status`,
   `stat_slash_multi_qty`,
   `stat_slash_low_qty`,
   `stat_delegate_value`,
   `stat_valid_addrs`,
   `stat_block_qty`,
   `stat_expect_block_qty`
from `node`
where `node_id` = "0x01";

-- 我的委托（*）
select 
    `delegate_addr` as walletAddress,
    sum(`delegate_has`+`delegate_locked`+`delegate_released`) as delegated
from `delegation`
where `is_history` = 2 
	and `delegate_addr` in ('0x01','0x02')
group by `delegate_addr`
order by `sequence`;

-- 委托节点详情（*）
select
   d.node_id,
   n.staking_name,
   sum(d.`delegate_has`),
   sum(d.`delegate_locked`),
   sum(d.`delegate_released`)
from delegation d inner join node n on d.node_id = n.node_id
where d.`is_history`=2  
    and d.`delegate_addr` = "0x01"
group by d.node_id
order by d.`sequence`;

-- 获取委托金额列表（*）
select 
    `staking_block_num` as stakingBlockNum,
    `delegate_has` + `delegate_locked` as delegated,
    `delegate_released` as released       
from `delegation`
where `is_history`=2 
	and `delegate_addr` = "0x01" 
	and `node_id` = "0x01"
order by `staking_block_num` desc;
	
-- 是否允许委托及原因-节点状态（*）
select 
    `node_id`,
    `status`,
    `is_setting`
from `node`
where `node_id` = '0x01';

-- 是否允许委托及原因-有效的质押和收益账户（*）
select 
    `node_id`
from `node`
where `status` = 1 
	and (`staking_addr` = '0x01' or `benefit_addr` = '0x02');