/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;

/* Alter table in target */
ALTER TABLE `block`
	ADD COLUMN `actual_tx_cost_sum` varchar(255)  COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '区块中交易实际花费值(手续费)总和，单位：wei' after `update_time` ;

/* Alter table in target */
ALTER TABLE `node`
	CHANGE `chain_id` `chain_id` varchar(64)  COLLATE utf8_general_ci NOT NULL COMMENT '链ID' first ,
	ADD COLUMN `id` varchar(255)  COLLATE utf8_general_ci NOT NULL COMMENT '节点ID' after `chain_id` ,
	CHANGE `ip` `ip` varchar(32)  COLLATE utf8_general_ci NOT NULL COMMENT '节点IP' after `id` ,
	ADD COLUMN `address` varchar(64)  COLLATE utf8_general_ci NOT NULL COMMENT '节点地址' after `ip` ,
	ADD COLUMN `port` int(10)   NOT NULL COMMENT '节点端口' after `address` ,
	ADD COLUMN `reward_ratio` double   NULL COMMENT '分红比例:小数' after `port` ,
	ADD COLUMN `node_status` int(4)   NOT NULL COMMENT '节点状态:1-正常 2-异常' after `reward_ratio` ,
	DROP COLUMN `node_id` ,
	DROP COLUMN `create_time` ,
	DROP COLUMN `node_address` ,
	DROP COLUMN `net_state` ,
	DROP COLUMN `node_type` ,
	DROP COLUMN `node_name` ,
	DROP COLUMN `update_time` ,
	DROP KEY `PRIMARY`, ADD PRIMARY KEY(`id`,`chain_id`) ;

/* Create table in target */
CREATE TABLE `node_ranking`(
	`id` varchar(255) COLLATE utf8_general_ci NOT NULL  COMMENT '节点ID' ,
	`ip` varchar(32) COLLATE utf8_general_ci NOT NULL  COMMENT '节点IP' ,
	`port` int(10) NOT NULL  COMMENT '节点端口' ,
	`name` varchar(32) COLLATE utf8_general_ci NULL  COMMENT '节点名称' ,
	`type` int(4) NOT NULL  COMMENT '节点类型' ,
	`intro` varchar(255) COLLATE utf8_general_ci NULL  COMMENT '节点简介' ,
	`address` varchar(64) COLLATE utf8_general_ci NOT NULL  COMMENT '节点地址' ,
	`deposit` varchar(255) COLLATE utf8_general_ci NOT NULL  COMMENT '质押金，单位-ADP' ,
	`org_name` varchar(64) COLLATE utf8_general_ci NULL  COMMENT '机构名称' ,
	`org_website` varchar(255) COLLATE utf8_general_ci NULL  COMMENT '机构官网' ,
	`chain_id` varchar(64) COLLATE utf8_general_ci NOT NULL  COMMENT '链ID' ,
	`join_time` timestamp NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间' ,
	`create_time` timestamp NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
	`update_time` timestamp NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' ,
	`reward_ratio` double NULL  COMMENT '分红比例:小数' ,
	`ranking` int(4) NOT NULL  COMMENT '节点排名' ,
	`election_status` int(4) NOT NULL  COMMENT '竞选状态' ,
	`url` varchar(255) COLLATE utf8_general_ci NOT NULL  COMMENT 'logo上传path' ,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8' COLLATE='utf8_general_ci';


/* Alter table in target */
ALTER TABLE `pending_tx`
	CHANGE `energon_price` `energon_price` varchar(255)  COLLATE utf8_general_ci NOT NULL COMMENT '能量限制' after `energon_limit` ,
	CHANGE `value` `value` varchar(255)  COLLATE utf8_general_ci NOT NULL COMMENT '交易金额' after `tx_type` ,
	CHANGE `create_time` `create_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' after `value` ,
	CHANGE `receive_type` `receive_type` varchar(32)  COLLATE utf8_general_ci NOT NULL COMMENT '交易接收者类型（to是合约还是账户）contract合约、 account账户' after `update_time` ;

/* Create table in target */
CREATE TABLE `statistics`(
	`id` bigint(20) NOT NULL  auto_increment ,
	`chain_id` varchar(64) COLLATE utf8_general_ci NOT NULL  COMMENT '链ID' ,
	`address` varchar(64) COLLATE utf8_general_ci NOT NULL  COMMENT '节点地址' ,
	`create_time` timestamp NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
	`update_time` timestamp NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' ,
	`type` varchar(64) COLLATE utf8_general_ci NOT NULL  COMMENT '统计类型：\r\nreward_amount 累计分红\r\nprofit_amount 累计收益\r\nverify_count 节点验证次数\r\nblock_count 已出块数\r\nblock_reward 区块累计奖励\r\n' ,
	`value` varchar(255) COLLATE utf8_general_ci NOT NULL  COMMENT '数值' ,
	`node_id` varchar(255) COLLATE utf8_general_ci NULL  COMMENT '节点ID' ,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8' COLLATE='utf8_general_ci';

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;