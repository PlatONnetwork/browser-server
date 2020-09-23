/* Alter table in target */
ALTER TABLE `address`
    CHANGE `address` `address` varchar(42)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址' first ,
    CHANGE `contract_name` `contract_name` varchar(125)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '合约名称' after `delegate_released` ,
    CHANGE `contract_create` `contract_create` varchar(125)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '合约创建者地址' after `contract_name` ,
    CHANGE `contract_createHash` `contract_createHash` varchar(72)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '创建合约的交易Hash' after `contract_create` ,
    CHANGE `contract_destroy_hash` `contract_destroy_hash` varchar(72)  COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '销毁合约的交易Hash' after `contract_createHash` ,
    CHANGE `contract_bin` `contract_bin` longtext  COLLATE utf8mb4_general_ci NULL COMMENT '合约bincode数据(通过web3j查询出来的合约代码)' after `contract_destroy_hash` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `config`
    CHANGE `module` `module` varchar(64)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数模块名' after `id` ,
    CHANGE `name` `name` varchar(128)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名' after `module` ,
    CHANGE `init_value` `init_value` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统初始值' after `name` ,
    CHANGE `stale_value` `stale_value` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '旧值' after `init_value` ,
    CHANGE `value` `value` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '新值' after `stale_value` ,
    CHANGE `range_desc` `range_desc` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数取值范围描述' after `value` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `delegation`
    CHANGE `delegate_addr` `delegate_addr` varchar(42)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '委托交易地址' first ,
    CHANGE `node_id` `node_id` varchar(130)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id' after `staking_block_num` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `gas_estimate`
    CHANGE `addr` `addr` varchar(42)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '委托交易地址' first ,
    CHANGE `node_id` `node_id` varchar(130)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id' after `addr` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `gas_estimate_log`
    CHANGE `json` `json` longtext  COLLATE utf8mb4_general_ci NOT NULL after `seq` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `n_opt_bak`
    CHANGE `node_id` `node_id` varchar(130)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id' after `id` ,
    CHANGE `tx_hash` `tx_hash` varchar(72)  COLLATE utf8mb4_general_ci NULL COMMENT '交易hash' after `type` ,
    CHANGE `desc` `desc` varchar(2500)  COLLATE utf8mb4_general_ci NULL COMMENT '操作描述' after `time` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `network_stat`
    CHANGE `cur_block_hash` `cur_block_hash` varchar(66)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '当前区块Hash' after `cur_number` ,
    CHANGE `node_id` `node_id` varchar(130)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点ID' after `cur_block_hash` ,
    CHANGE `node_name` `node_name` varchar(128)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点名称' after `node_id` ,
    CHANGE `issue_rates` `issue_rates` varchar(2048)  COLLATE utf8mb4_general_ci NULL COMMENT '增发比例' after `node_opt_seq` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `proposal`
    CHANGE `hash` `hash` varchar(72)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '提案交易hash' first ,
    CHANGE `node_id` `node_id` varchar(130)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '提交提案验证人(节点ID)' after `type` ,
    CHANGE `node_name` `node_name` varchar(128)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '提交提案验证人名称(节点名称)' after `node_id` ,
    CHANGE `url` `url` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '提案URL' after `node_name` ,
    CHANGE `new_version` `new_version` varchar(64)  COLLATE utf8mb4_general_ci NULL COMMENT '新提案版本' after `url` ,
    CHANGE `pip_num` `pip_num` varchar(128)  COLLATE utf8mb4_general_ci NOT NULL COMMENT 'pip编号(需要组装 PIP-编号)' after `status` ,
    CHANGE `pip_id` `pip_id` varchar(128)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '提案id' after `pip_num` ,
    CHANGE `topic` `topic` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '提案主题' after `pip_id` ,
    CHANGE `description` `description` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '提案描述' after `topic` ,
    CHANGE `canceled_pip_id` `canceled_pip_id` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '被取消提案id' after `description` ,
    CHANGE `canceled_topic` `canceled_topic` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '被取消的提案的主题' after `canceled_pip_id` ,
    CHANGE `module` `module` varchar(64)  COLLATE utf8mb4_general_ci NULL COMMENT '参数模块(参数提案专有属性)' after `completion_flag` ,
    CHANGE `name` `name` varchar(128)  COLLATE utf8mb4_general_ci NULL COMMENT '参数名称(参数提案专有属性)' after `module` ,
    CHANGE `stale_value` `stale_value` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '原参数值' after `name` ,
    CHANGE `new_value` `new_value` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '参数值(参数提案专有属性)' after `stale_value` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `rp_plan`
    CHANGE `address` `address` varchar(42)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '发布锁仓计划地址' after `id` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `slash`
    CHANGE `hash` `hash` varchar(72)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '举报交易hash' first ,
    CHANGE `node_id` `node_id` varchar(130)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id' after `hash` ,
    CHANGE `slash_rate` `slash_rate` varchar(64)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '扣除比例' after `node_id` ,
    CHANGE `benefit_addr` `benefit_addr` varchar(42)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '收取举报奖励地址' after `reward` ,
    CHANGE `data` `data` text  COLLATE utf8mb4_general_ci NOT NULL COMMENT '举报证据' after `benefit_addr` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `sta_dele`
    CHANGE `dele_address` `dele_address` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '委托人钱包' first ,
    CHANGE `node_id` `node_id` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id' after `dele_address` ,
    CHANGE `node_name` `node_name` varchar(128)  COLLATE utf8mb4_general_ci NULL COMMENT '节点名称' after `node_id` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `sta_file`
    CHANGE `file_url` `file_url` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '文件全路径' after `status` ,
    CHANGE `file_name` `file_name` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '文件名字' after `file_url` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `sta_guess`
    CHANGE `contract_Address` `contract_Address` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '合约地址' after `id` ,
    CHANGE `from_Address` `from_Address` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送地址' after `contract_Address` ,
    CHANGE `tx_hash` `tx_hash` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '交易hash' after `from_Address` ,
    CHANGE `draw_hash` `draw_hash` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '开奖交易hash' after `avg_amount` ,
    CHANGE `fail_reason` `fail_reason` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '失败原因' after `draw_hash` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `sta_guess_list`
    CHANGE `address` `address` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '中奖钱包地址' after `guess_id` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `sta_incite_block` DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `sta_incite_reward`
    CHANGE `tx_hash` `tx_hash` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '交易hash' first ,
    CHANGE `address` `address` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '钱包地址' after `tx_hash` ,
    CHANGE `section` `section` varchar(64)  COLLATE utf8mb4_general_ci NULL COMMENT '中奖区间段: <起始块号>-<结束块号>' after `addr_count` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `sta_reward`
    CHANGE `node_id` `node_id` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id' after `type` ,
    CHANGE `node_name` `node_name` varchar(128)  COLLATE utf8mb4_general_ci NULL COMMENT '节点名称' after `node_id` ,
    CHANGE `dele_address` `dele_address` varchar(255)  COLLATE utf8mb4_general_ci NULL COMMENT '委托人钱包' after `node_name` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `sta_switch`
    CHANGE `version` `version` varchar(255)  COLLATE utf8mb4_general_ci NULL after `sta_flag` ,
    CHANGE `proposalId` `proposalId` varchar(255)  COLLATE utf8mb4_general_ci NULL after `version` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `node`
    CHANGE `node_id` `node_id` varchar(130)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id' first ,
    CHANGE `node_name` `node_name` varchar(128)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点名称(质押节点名称)' after `staking_reduction_epoch` ,
    CHANGE `node_icon` `node_icon` varchar(255)  COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '节点头像(关联external_id,第三方软件获取)' after `node_name` ,
    CHANGE `external_id` `external_id` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '第三方社交软件关联id' after `node_icon` ,
    CHANGE `external_name` `external_name` varchar(128)  COLLATE utf8mb4_general_ci NULL COMMENT '第三方社交软件关联用户名' after `external_id` ,
    CHANGE `staking_addr` `staking_addr` varchar(42)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '发起质押的账户地址' after `external_name` ,
    CHANGE `benefit_addr` `benefit_addr` varchar(42)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收益地址' after `staking_addr` ,
    CHANGE `web_site` `web_site` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点的第三方主页' after `big_version` ,
    CHANGE `details` `details` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点的描述' after `web_site` ,
    CHANGE `status` `status` int(2)   NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定' after `leave_time` ,
    CHANGE `annualized_rate_info` `annualized_rate_info` longtext  COLLATE utf8mb4_general_ci NULL COMMENT '最近几个结算周期收益和质押信息' after `predict_staking_reward` ,
    ADD COLUMN `zero_produce_freeze_duration` int(11)   NULL COMMENT '零出块节点锁定结算周期数' after `un_stake_end_block` ,
    ADD COLUMN `zero_produce_freeze_epoch` int(11)   NULL COMMENT '零出块锁定时所在结算周期' after `zero_produce_freeze_duration` ,
    ADD COLUMN `low_rate_slash_count` int(11)   NOT NULL DEFAULT 0 COMMENT '节点零出块次数' after `zero_produce_freeze_epoch` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `staking`
    CHANGE `node_id` `node_id` varchar(130)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '质押节点地址' first ,
    CHANGE `node_name` `node_name` varchar(128)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点名称(质押节点名称)' after `staking_reduction_epoch` ,
    CHANGE `node_icon` `node_icon` varchar(255)  COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '节点头像(关联external_id，第三方软件获取)' after `node_name` ,
    CHANGE `external_id` `external_id` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '第三方社交软件关联id' after `node_icon` ,
    CHANGE `external_name` `external_name` varchar(128)  COLLATE utf8mb4_general_ci NULL COMMENT '第三方社交软件关联用户名' after `external_id` ,
    CHANGE `staking_addr` `staking_addr` varchar(42)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '发起质押的账户地址' after `external_name` ,
    CHANGE `benefit_addr` `benefit_addr` varchar(42)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收益地址' after `staking_addr` ,
    CHANGE `program_version` `program_version` varchar(10)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '程序版本' after `annualized_rate` ,
    CHANGE `big_version` `big_version` varchar(10)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '大程序版本' after `program_version` ,
    CHANGE `web_site` `web_site` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点的第三方主页' after `big_version` ,
    CHANGE `details` `details` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点的描述' after `web_site` ,
    CHANGE `status` `status` int(2)   NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定' after `leave_time` ,
    CHANGE `annualized_rate_info` `annualized_rate_info` longtext  COLLATE utf8mb4_general_ci NULL COMMENT '最近几个结算周期收益和质押信息' after `pre_cons_block_qty` ,
    ADD COLUMN `zero_produce_freeze_duration` int(11)   NULL COMMENT '零出块节点锁定结算周期数' after `un_stake_end_block` ,
    ADD COLUMN `zero_produce_freeze_epoch` int(11)   NULL COMMENT '零出块锁定时所在结算周期' after `zero_produce_freeze_duration` ,
    ADD COLUMN `low_rate_slash_count` int(11)   NOT NULL DEFAULT 0 COMMENT '节点零出块次数' after `zero_produce_freeze_epoch` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `staking_history`
    CHANGE `node_id` `node_id` varchar(130)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '质押节点地址' first ,
    CHANGE `node_name` `node_name` varchar(128)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点名称(质押节点名称)' after `staking_reduction_epoch` ,
    CHANGE `node_icon` `node_icon` varchar(255)  COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '节点头像(关联external_id，第三方软件获取)' after `node_name` ,
    CHANGE `external_id` `external_id` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '第三方社交软件关联id' after `node_icon` ,
    CHANGE `external_name` `external_name` varchar(128)  COLLATE utf8mb4_general_ci NULL COMMENT '第三方社交软件关联用户名' after `external_id` ,
    CHANGE `staking_addr` `staking_addr` varchar(42)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '发起质押的账户地址' after `external_name` ,
    CHANGE `benefit_addr` `benefit_addr` varchar(42)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收益地址' after `staking_addr` ,
    CHANGE `program_version` `program_version` varchar(10)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '程序版本' after `annualized_rate` ,
    CHANGE `big_version` `big_version` varchar(10)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '大程序版本' after `program_version` ,
    CHANGE `web_site` `web_site` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点的第三方主页' after `big_version` ,
    CHANGE `details` `details` varchar(255)  COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '节点的描述' after `web_site` ,
    CHANGE `status` `status` int(2)   NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定' after `leave_time`,
    CHANGE `annualized_rate_info` `annualized_rate_info` longtext  COLLATE utf8mb4_general_ci NULL COMMENT '最近几个结算周期收益和质押信息' after `pre_cons_block_qty` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `tx_bak`
    CHANGE `hash` `hash` varchar(72)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '交易Hash' after `id` ,
    CHANGE `info` `info` text  COLLATE utf8mb4_general_ci NULL COMMENT '交易信息' after `num` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;

/* Alter table in target */
ALTER TABLE `vote`
    CHANGE `hash` `hash` varchar(72)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '投票交易Hash(如果此值带有\"-\",则表示投票操作是通过普通合约代理执行的,\"-\"号前面的是合约交易hash)' first ,
    CHANGE `node_id` `node_id` varchar(130)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '投票验证人(节点ID)' after `hash` ,
    CHANGE `node_name` `node_name` varchar(128)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '投票验证人名称' after `node_id` ,
    CHANGE `proposal_hash` `proposal_hash` varchar(72)  COLLATE utf8mb4_general_ci NOT NULL COMMENT '提案交易Hash' after `option` , DEFAULT CHARSET='utf8mb4', COLLATE ='utf8mb4_general_ci' ;
