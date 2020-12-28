/*
SQLyog Ultimate v8.32 
MySQL - 5.7.27 : Database - platon_agent
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`platon_agent` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `platon_agent`;

/*Table structure for table `address` */

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
  `address` varchar(255) NOT NULL COMMENT '地址',
  `type` int(11) NOT NULL COMMENT '地址类型\r\n1：账号\r\n2：合约\r\n3：内置合约',
  `balance` varchar(255) NOT NULL DEFAULT '0' COMMENT '余额',
  `restricting_balance` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '锁仓余额',
  `staking_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '质押的金额',
  `delegate_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '委托的金额',
  `redeemed_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '赎回中的金额  委托+质押',
  `tx_qty` int(11) NOT NULL DEFAULT '0' COMMENT '交易总数',
  `transfer_qty` int(11) NOT NULL DEFAULT '0' COMMENT '转账交易总数',
  `delegate_qty` int(11) NOT NULL DEFAULT '0' COMMENT '委托交易总数',
  `staking_qty` int(11) NOT NULL DEFAULT '0' COMMENT '质押交易总数',
  `proposal_qty` int(11) NOT NULL DEFAULT '0' COMMENT '治理交易总数',
  `candidate_count` int(11) NOT NULL DEFAULT '0' COMMENT '已委托的验证人数',
  `delegate_has` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '未锁定委托',
  `delegate_locked` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '已锁定委托',
  `delegate_released` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '已经解锁',
  `contract_name` varchar(255) NOT NULL DEFAULT '' COMMENT '合约名称',
  `contract_create` varchar(255) NOT NULL DEFAULT '' COMMENT '合约创建者地址',
  `contract_createHash` varchar(255) NOT NULL DEFAULT '0' COMMENT '创建合约hash',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`address`),
  KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `delegation` */

DROP TABLE IF EXISTS `delegation`;

CREATE TABLE `delegation` (
  `delegate_addr` varchar(64) NOT NULL COMMENT '委托交易地址',
  `staking_block_num` bigint(20) NOT NULL COMMENT '最新的质押交易块高',
  `node_id` varchar(255) NOT NULL COMMENT '节点id',
  `delegate_has` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '未锁定委托金额',
  `delegate_locked` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '已锁定委托金额',
  `delegate_released` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '待提取的金额',
  `is_history` int(4) NOT NULL DEFAULT '2' COMMENT '是否为历史\r\n1：是\r\n2：否',
  `sequence` bigint(20) NOT NULL COMMENT 'blockNum*100000+tx_index',
  `cur_delegation_block_num` bigint(20) NOT NULL COMMENT '最新委托交易块高',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`delegate_addr`,`staking_block_num`,`node_id`),
  KEY `node_id` (`node_id`) USING BTREE,
  KEY `staking_block_num` (`staking_block_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `network_stat` */

DROP TABLE IF EXISTS `network_stat`;

CREATE TABLE `network_stat` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `current_number` bigint(20) NOT NULL COMMENT '当前块高',
  `node_name` varchar(255) NOT NULL DEFAULT '' COMMENT '节点名称',
  `node_id` varchar(255) NOT NULL COMMENT '节点ID',
  `tx_qty` int(11) NOT NULL DEFAULT '0' COMMENT '交易总数',
  `current_tps` int(11) NOT NULL DEFAULT '0' COMMENT '当前最新块的交易数',
  `max_tps` int(11) NOT NULL DEFAULT '0' COMMENT '历史块中的最大交易数',
  `issue_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '当前发行量',
  `turn_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '当前流通量',
  `staking_delegation_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '实时质押委托总数',
  `staking_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '实时质押总数',
  `doing_proposal_qty` int(11) NOT NULL DEFAULT '0' COMMENT '进行中提案总数',
  `proposal_qty` int(11) NOT NULL DEFAULT '0' COMMENT '提案总数',
  `address_qty` int(11) NOT NULL DEFAULT '0' COMMENT '地址数',
  `block_reward` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '当前出块奖励',
  `staking_reward` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '当前质押奖励',
  `add_issue_begin` bigint(20) NOT NULL COMMENT '当前增发周期的开始快高',
  `add_issue_end` bigint(20) NOT NULL COMMENT '当前增发周期的结束快高',
  `next_setting` bigint(20) NOT NULL COMMENT '离下个结算周期的剩余快高',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `node` */

DROP TABLE IF EXISTS `node`;

CREATE TABLE `node` (
  `node_id` varchar(255) NOT NULL COMMENT '节点id',
  `stat_slash_multi_qty` int(11) NOT NULL DEFAULT '0' COMMENT '多签举报次数',
  `stat_slash_low_qty` int(11) NOT NULL DEFAULT '0' COMMENT '出块率低举报次数',
  `stat_block_qty` bigint(20) NOT NULL DEFAULT '0' COMMENT '节点处块数统计',
  `stat_expect_block_qty` bigint(20) NOT NULL DEFAULT '0' COMMENT '节点期望出块数',
  `stat_verifier_time` int(11) NOT NULL DEFAULT '0' COMMENT '进入共识验证轮次数',
  `is_recommend` int(4) NOT NULL DEFAULT '2' COMMENT '官方推荐 \r\n1：是\r\n2：否',
  `total_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '有效的质押委托总数',
  `staking_block_num` bigint(20) NOT NULL COMMENT '质押区块高度',
  `staking_tx_index` int(11) NOT NULL COMMENT '发起质押交易的索引',
  `staking_has` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '质押金额(犹豫期金额)',
  `staking_locked` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '质押金额(锁定金额)',
  `staking_reduction` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '质押金额(退回中金额)',
  `staking_reduction_epoch` int(11) NOT NULL DEFAULT '0' COMMENT '结算周期标识',
  `staking_name` varchar(64) NOT NULL DEFAULT '' COMMENT '节点名称(质押节点名称)',
  `staking_icon` varchar(255) DEFAULT '' COMMENT '节点头像(关联external_id，第三方软件获取)',
  `external_id` varchar(255) NOT NULL DEFAULT '' COMMENT '第三方社交软件关联id',
  `external_name` varchar(128) DEFAULT NULL COMMENT '第三方社交软件关联用户名',
  `staking_addr` varchar(64) NOT NULL COMMENT '发起质押的账户地址',
  `benefit_addr` varchar(255) NOT NULL DEFAULT '' COMMENT '收益地址',
  `expected_income` varchar(255) NOT NULL DEFAULT '0' COMMENT '预计年化率',
  `program_version` int(11) NOT NULL DEFAULT '0' COMMENT '程序版本',
  `big_version` int(11) NOT NULL DEFAULT '0' COMMENT '大程序版本',
  `web_site` varchar(255) NOT NULL DEFAULT '' COMMENT '节点的第三方主页',
  `details` varchar(255) NOT NULL DEFAULT '' COMMENT '节点的描述',
  `join_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '加入时间',
  `leave_time` timestamp NULL DEFAULT NULL COMMENT '离开时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '节点状态 \r\n1：候选中\r\n2：退出中\r\n3：已退出',
  `is_consensus` int(4) NOT NULL DEFAULT '2' COMMENT '是否共识周期验证人\r\n1：是\r\n2：否',
  `is_setting` int(4) NOT NULL DEFAULT '2' COMMENT '是否结算周期验证人\r\n1：是\r\n2：否',
  `is_init` int(4) NOT NULL DEFAULT '2' COMMENT '是否为链初始化时内置的候选人\r\n1：是\r\n2：否',
  `stat_delegate_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '有效的委托金额',
  `stat_delegate_released` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '待提取的委托金额',
  `stat_valid_addrs` int(11) NOT NULL DEFAULT '0' COMMENT '有效委托地址数',
  `stat_invalid_addrs` int(11) NOT NULL DEFAULT '0' COMMENT '待提取委托地址数',
  `stat_block_reward_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '出块奖励统计（激励池）',
  `stat_fee_reward_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '出块奖励统计（手续费）',
  `stat_staking_reward_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '质押奖励统计（激励池）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`node_id`),
  KEY `node_id` (`node_id`) USING BTREE,
  KEY `status` (`status`),
  KEY `staking_addr` (`staking_addr`),
  KEY `benefit_addr` (`benefit_addr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `node_opt` */

DROP TABLE IF EXISTS `node_opt`;

CREATE TABLE `node_opt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `node_id` varchar(255) NOT NULL COMMENT '节点id',
  `type` varchar(255) NOT NULL COMMENT '操作类型（code表示）\r\n1 create 创建 \r\n2 modify 修改\r\n3 quit 退出\r\n4 proposals 提案\r\n5 vote 投票\r\n6 signatures 双签\r\n7 lowBlockRate 出块率低',
  `desc` varchar(2500) DEFAULT NULL COMMENT '操作描述',
  `tx_hash` varchar(128) DEFAULT NULL COMMENT '交易hash',
  `block_number` bigint(20) DEFAULT NULL COMMENT '交易所在区块高度',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `node_id` (`node_id`) USING BTREE,
  KEY `tx_hash` (`tx_hash`) USING BTREE,
  KEY `block_number` (`block_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `proposal` */

DROP TABLE IF EXISTS `proposal`;

CREATE TABLE `proposal` (
  `hash` varchar(255) NOT NULL COMMENT '提案交易hash',
  `type` varchar(255) NOT NULL COMMENT '提案类型 1:文本提案 2:升级提案 4:取消提案',
  `verifier` varchar(255) NOT NULL COMMENT '提交提案验证人地址',
  `verifier_name` varchar(128) NOT NULL COMMENT '提交提案验证人名称',
  `url` varchar(255) NOT NULL COMMENT '提案URL',
  `new_version` varchar(255) DEFAULT NULL COMMENT '新提案版本',
  `end_voting_block` varchar(255) NOT NULL COMMENT '提案结束区块',
  `active_block` varchar(255) DEFAULT NULL COMMENT '提案生效区块',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '提案时间',
  `yeas` bigint(20) NOT NULL DEFAULT '0' COMMENT '赞成票',
  `nays` bigint(20) NOT NULL DEFAULT '0' COMMENT '反对票',
  `abstentions` bigint(20) NOT NULL DEFAULT '0' COMMENT '弃权票',
  `accu_verifiers` bigint(20) NOT NULL DEFAULT '0' COMMENT '在整个投票期内有投票资格的验证人总数',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '提案状态\r\n1：投票中\r\n2：通过\r\n3：失败\r\n4：预升级\r\n 5：生效\r\n 6：被取消\r\n',
  `pip_num` varchar(128) NOT NULL COMMENT 'pip编号(需要组装 PIP-编号)',
  `pip_id` varchar(128) NOT NULL COMMENT '提案id',
  `topic` varchar(255) DEFAULT NULL COMMENT '提案主题',
  `description` varchar(255) DEFAULT NULL COMMENT '提案描述',
  `canceled_pip_id` varchar(255) DEFAULT NULL COMMENT '被取消提案id',
  `canceled_topic` varchar(255) DEFAULT NULL COMMENT '被取消的提案的主题',
  `block_number` varchar(255) NOT NULL COMMENT '议案交易所在区块',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`hash`),
  KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `rp_plan` */

DROP TABLE IF EXISTS `rp_plan`;

CREATE TABLE `rp_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `address` varchar(255) NOT NULL DEFAULT '0' COMMENT '发布锁仓计划地址',
  `epoch` bigint(20) NOT NULL COMMENT '锁仓计划周期',
  `amount` decimal(65,0) NOT NULL COMMENT '区块上待释放的金额',
  `number` bigint(20) NOT NULL COMMENT '锁仓计划所在区块',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `slash` */

DROP TABLE IF EXISTS `slash`;

CREATE TABLE `slash` (
  `hash` varchar(128) NOT NULL COMMENT '举报交易hash',
  `node_id` varchar(255) NOT NULL COMMENT '节点id',
  `slash_rate` varchar(64) NOT NULL COMMENT '扣除比例',
  `slash_value` decimal(65,0) NOT NULL COMMENT '惩罚的金额',
  `reward` decimal(65,0) NOT NULL COMMENT '举报成功的奖励',
  `denefit_addr` varchar(64) NOT NULL COMMENT '收取举报奖励地址',
  `data` text NOT NULL COMMENT '举报证据',
  `is_quit` int(4) NOT NULL DEFAULT '0' COMMENT '是否退出\r\n1：是\r\n2：否',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`hash`),
  KEY `node_id` (`node_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `staking` */

DROP TABLE IF EXISTS `staking`;

CREATE TABLE `staking` (
  `node_id` varchar(255) NOT NULL COMMENT '质押节点地址',
  `staking_block_num` bigint(20) NOT NULL COMMENT '质押区块高度',
  `staking_tx_index` int(11) NOT NULL COMMENT '发起质押交易的索引',
  `staking_has` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '质押金额(犹豫期金额)',
  `staking_locked` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '质押金额(锁定金额)',
  `staking_reduction` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '质押金额(退回中金额)',
  `staking_reduction_epoch` int(11) NOT NULL DEFAULT '0' COMMENT '结算周期标识',
  `staking_name` varchar(64) NOT NULL DEFAULT '' COMMENT '节点名称(质押节点名称)',
  `staking_icon` varchar(255) DEFAULT '' COMMENT '节点头像(关联external_id，第三方软件获取)',
  `external_id` varchar(255) NOT NULL DEFAULT '' COMMENT '第三方社交软件关联id',
  `external_name` varchar(128) DEFAULT NULL COMMENT '第三方社交软件关联用户名',
  `staking_addr` varchar(64) NOT NULL COMMENT '发起质押的账户地址',
  `benefit_addr` varchar(255) NOT NULL DEFAULT '' COMMENT '收益地址',
  `expected_income` varchar(255) NOT NULL DEFAULT '0' COMMENT '预计年化率',
  `program_version` int(11) NOT NULL DEFAULT '0' COMMENT '程序版本',
  `big_version` int(11) NOT NULL DEFAULT '0' COMMENT '大程序版本',
  `web_site` varchar(255) NOT NULL DEFAULT '' COMMENT '节点的第三方主页',
  `details` varchar(255) NOT NULL DEFAULT '' COMMENT '节点的描述',
  `join_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '加入时间',
  `leave_time` timestamp NULL DEFAULT NULL COMMENT '离开时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '节点状态 \r\n1：候选中\r\n2：退出中\r\n3：已退出',
  `is_consensus` int(4) NOT NULL DEFAULT '2' COMMENT '是否共识周期验证人\r\n1：是\r\n2：否',
  `is_setting` int(4) NOT NULL DEFAULT '2' COMMENT '是否结算周期验证人\r\n1：是\r\n2：否',
  `is_init` int(4) NOT NULL DEFAULT '2' COMMENT '是否为链初始化时内置的候选人\r\n1：是\r\n2：否',
  `stat_delegate_has` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '未锁定的委托',
  `stat_delegate_locked` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '锁定的委托',
  `stat_delegate_released` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '待提取的委托',
  `block_reward_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '出块奖励统计（激励池）',
  `fee_reward_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '出块奖励统计（手续费）',
  `staking_reward_value` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '质押奖励',
  `cur_cons_block_qty` bigint(20) NOT NULL DEFAULT '0' COMMENT '当前共识周期出块数',
  `pre_cons_block_qty` bigint(20) NOT NULL DEFAULT '0' COMMENT '上个共识周期出块数',
  `annualized_rate_info` longtext COMMENT '最近几个结算周期收益和质押信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`node_id`,`staking_block_num`),
  KEY `staking_addr` (`staking_addr`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vote` */

DROP TABLE IF EXISTS `vote`;

CREATE TABLE `vote` (
  `hash` varchar(255) NOT NULL COMMENT '提案投票hash',
  `verifier` varchar(255) NOT NULL COMMENT '投票验证人',
  `verifier_name` varchar(128) NOT NULL COMMENT '投票验证人名称',
  `option` varchar(255) NOT NULL COMMENT '投票选项 1:支持  2:反对 3:弃权',
  `proposal_hash` varchar(128) NOT NULL COMMENT '提案交易hash',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '提案时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`hash`),
  KEY `verifier` (`verifier`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
