#创建数据库脚本
CREATE DATABASE IF NOT EXISTS `platon_agent`;

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
  `address` varchar(255) NOT NULL COMMENT '地址',
  `type` int(11) NOT NULL COMMENT '地址类型\r\n1：账号\r\n2：合约\r\n3：内置合约',
  `balance` varchar(255) NOT NULL DEFAULT '0' COMMENT '余额',
  `restricting_balance` varchar(255) NOT NULL DEFAULT '0' COMMENT '锁仓余额',
  `staking_value` varchar(255) NOT NULL DEFAULT '0' COMMENT '质押的金额',
  `delegate_value` varchar(255) NOT NULL DEFAULT '0' COMMENT '委托的金额',
  `redeemed_value` varchar(255) NOT NULL DEFAULT '0' COMMENT '赎回中的金额  委托+质押',
  `tx_qty` int(11) NOT NULL DEFAULT '0' COMMENT '交易总数',
  `transfer_qty` int(11) NOT NULL DEFAULT '0' COMMENT '转账交易总数',
  `staking_qty` int(11) NOT NULL DEFAULT '0' COMMENT '质押交易总数',
  `proposal_qty` int(11) NOT NULL DEFAULT '0' COMMENT '治理交易总数',
  `delegate_qty` int(11) NOT NULL DEFAULT '0' COMMENT '委托交易总数',
  `candidate_count` int(11) NOT NULL DEFAULT '0' COMMENT '已委托的验证人数',
  `delegate_hes` varchar(255) NOT NULL DEFAULT '0' COMMENT '未锁定委托',
  `delegate_locked` varchar(255) NOT NULL DEFAULT '0' COMMENT '已锁定委托',
  `delegate_unlock` varchar(255) NOT NULL DEFAULT '0' COMMENT '已经解锁',
  `delegate_reduction` varchar(255) NOT NULL DEFAULT '0' COMMENT '赎回中的',
  `contract_name` varchar(255) NOT NULL DEFAULT '' COMMENT '合约名称',
  `contract_create` varchar(255) NOT NULL DEFAULT '' COMMENT '合约创建者地址',
  `contract_createHash` varchar(255) NOT NULL DEFAULT '0' COMMENT '创建合约hash',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `rp_plan` varchar(2048) DEFAULT NULL COMMENT '锁仓计划',
  PRIMARY KEY (`address`),
  KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `block` */

DROP TABLE IF EXISTS `block`;

CREATE TABLE `block` (
  `number` bigint(20) NOT NULL COMMENT '区块高度',
  `hash` varchar(128) NOT NULL COMMENT '区块hash',
  `parent_hash` varchar(128) NOT NULL COMMENT '父区块hash',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '出块时间（单位：毫秒）',
  `size` int(11) NOT NULL COMMENT '区块大小',
  `gas_limit` varchar(255) NOT NULL COMMENT 'gas限制',
  `gas_used` varchar(255) NOT NULL COMMENT 'gas消耗',
  `extra_data` mediumtext NOT NULL COMMENT '附加数据',
  `stat_tx_qty` int(11) NOT NULL DEFAULT '0' COMMENT '区块内交易数（区块所含交易个数）',
  `stat_transfer_qty` int(11) NOT NULL DEFAULT '0' COMMENT '区块内转账交易总数',
  `stat_staking_qty` int(11) NOT NULL DEFAULT '0' COMMENT '区块内验证人交易总数',
  `stat_proposal_qty` int(11) NOT NULL DEFAULT '0' COMMENT '区块内治理交易总数',
  `stat_delegate_qty` int(11) NOT NULL DEFAULT '0' COMMENT '区块内委托交易总数',
  `stat_tx_gas_limit` varchar(255) NOT NULL DEFAULT '0' COMMENT '区块中交易能量限制',
  `stat_tx_fee` varchar(255) NOT NULL DEFAULT '0' COMMENT '区块中交易实际花费值(手续费)总和，单位：von',
  `node_name` varchar(255) NOT NULL DEFAULT '' COMMENT '节点名称',
  `node_id` varchar(255) NOT NULL COMMENT '节点ID',
  `block_reward` varchar(255) NOT NULL DEFAULT '0' COMMENT '区块奖励，单位：von',
  `miner` varchar(64) NOT NULL COMMENT '收益地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`number`),
  KEY `hash` (`hash`) USING BTREE,
  KEY `node_id` (`node_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `delegation` */

DROP TABLE IF EXISTS `delegation`;

CREATE TABLE `delegation` (
  `delegate_addr` varchar(64) NOT NULL COMMENT '委托交易地址',
  `staking_block_num` bigint(20) NOT NULL COMMENT '最新的质押交易块高',
  `node_id` varchar(255) NOT NULL COMMENT '节点id',
  `delegate_has` varchar(255) NOT NULL DEFAULT '0' COMMENT '未锁定委托金额',
  `delegate_locked` varchar(255) NOT NULL DEFAULT '0' COMMENT '已锁定委托金额',
  `delegate_reduction` varchar(255) NOT NULL DEFAULT '0' COMMENT '赎回中的金额',
  `is_history` int(4) NOT NULL DEFAULT '2' COMMENT '是否为历史\r\n1：是\r\n2：否',
  `sequence` bigint(20) NOT NULL COMMENT 'blockNum*100000+tx_index',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `cur_delegation_block_num` bigint(20) NOT NULL COMMENT '最新委托交易块高',
  PRIMARY KEY (`delegate_addr`,`staking_block_num`,`node_id`),
  KEY `delegate_addr` (`delegate_addr`) USING BTREE,
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
  `issue_value` varchar(255) NOT NULL DEFAULT '0' COMMENT '当前发行量',
  `turn_value` varchar(255) NOT NULL DEFAULT '0' COMMENT '当前流通量',
  `staking_delegation_value` varchar(255) NOT NULL DEFAULT '0' COMMENT '实时质押委托总数',
  `staking_value` varchar(255) NOT NULL DEFAULT '0' COMMENT '实时质押总数',
  `proposal_qty` int(11) NOT NULL DEFAULT '0' COMMENT '提案总数',
  `doing_proposal_qty` int(11) NOT NULL DEFAULT '0' COMMENT '进行中提案总数',
  `address_qty` int(11) NOT NULL DEFAULT '0' COMMENT '地址数',
  `block_reward` varchar(255) NOT NULL DEFAULT '0' COMMENT '当前出块奖励',
  `staking_reward` varchar(255) NOT NULL DEFAULT '0' COMMENT '当前质押奖励',
  `add_issue_begin` bigint(20) NOT NULL COMMENT '当前增发周期的开始快高',
  `add_issue_end` bigint(20) NOT NULL COMMENT '当前增发周期的结束快高',
  `next_setting` bigint(20) NOT NULL COMMENT '离下个结算周期的剩余快高',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `node` */

DROP TABLE IF EXISTS `node`;

CREATE TABLE `node` (
  `node_id` varchar(255) NOT NULL COMMENT '节点id',
  `stat_slash_multi_qty` int(11) NOT NULL DEFAULT '0' COMMENT '多签举报次数',
  `stat_slash_low_qty` int(11) NOT NULL DEFAULT '0' COMMENT '出块率低举报次数',
  `stat_block_qty` bigint(20) NOT NULL DEFAULT '0' COMMENT '节点处块数统计',
  `stat_expect_block_qty` varchar(255) NOT NULL DEFAULT '0' COMMENT '节点期望出块数',
  `stat_reward_value` varchar(255) NOT NULL DEFAULT '0' COMMENT '节点收益统计(出块奖励 + 质押奖励)',
  `stat_verifier_time` int(11) NOT NULL DEFAULT '0' COMMENT '进入共识验证轮次数',
  `is_recommend` int(4) NOT NULL DEFAULT '2' COMMENT '官方推荐 \r\n1：是\r\n2：否',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`node_id`),
  KEY `node_id` (`node_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `node_opt` */

DROP TABLE IF EXISTS `node_opt`;

CREATE TABLE `node_opt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `node_id` varchar(255) NOT NULL COMMENT '节点id',
  `type` varchar(255) NOT NULL COMMENT '操作类型（code表示）\r\n1 create 创建 \r\n2 modify 修改\r\n3 quit 退出\r\n4 proposals 提案\r\n5 vote 投票\r\n6 signatures 双签\r\n7 lowBlockRate 出块率低',
  `tx_hash` varchar(128) DEFAULT NULL COMMENT '交易hash',
  `block_number` bigint(20) DEFAULT NULL COMMENT '交易所在区块高度',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `desc` varchar(2500) DEFAULT NULL COMMENT '操作描述',
  PRIMARY KEY (`id`),
  KEY `node_id` (`node_id`) USING BTREE,
  KEY `tx_hash` (`tx_hash`) USING BTREE,
  KEY `block_number` (`block_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

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
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `pip_num` varchar(128) NOT NULL COMMENT 'pip编号(需要组装 PIP-编号)',
  `pip_id` int(11) NOT NULL COMMENT '提案id',
  `topic` varchar(255) DEFAULT NULL COMMENT '提案主题',
  `description` varchar(255) DEFAULT NULL COMMENT '提案描述',
  `canceled_pip_id` varchar(255) DEFAULT NULL COMMENT '被取消提案id',
  `canceled_topic` varchar(255) DEFAULT NULL COMMENT '被取消的提案的主题',
  PRIMARY KEY (`hash`),
  KEY `hash` (`hash`) USING BTREE,
  KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `rp_plan` */

DROP TABLE IF EXISTS `rp_plan`;

CREATE TABLE `rp_plan` (
  `address` varchar(255) NOT NULL DEFAULT '0' COMMENT '发布锁仓计划地址',
  `epoch` bigint(20) NOT NULL COMMENT '锁仓计划周期',
  `amount` varchar(255) NOT NULL COMMENT '区块上待释放的金额',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `number` bigint(20) NOT NULL COMMENT '锁仓计划所在区块',
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `slash` */

DROP TABLE IF EXISTS `slash`;

CREATE TABLE `slash` (
  `hash` varchar(128) NOT NULL COMMENT '举报交易hash',
  `node_id` varchar(255) NOT NULL COMMENT '节点id',
  `slash_rate` varchar(64) NOT NULL COMMENT '扣除比例',
  `is_quit` int(4) NOT NULL COMMENT '是否退出\r\n1：是\r\n2：否',
  `reward` varchar(255) NOT NULL COMMENT '举报成功的奖励',
  `denefit_addr` varchar(64) NOT NULL COMMENT '收取举报奖励地址',
  `data` text NOT NULL COMMENT '举报证据',
  `status` int(11) NOT NULL COMMENT '举报状态 \r\n1：失败\r\n2：成功',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`hash`),
  KEY `node_id` (`node_id`) USING BTREE,
  KEY `hash` (`hash`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `staking` */

DROP TABLE IF EXISTS `staking`;

CREATE TABLE `staking` (
  `staking_block_num` bigint(20) NOT NULL COMMENT '质押区块高度',
  `node_id` varchar(255) NOT NULL COMMENT '质押节点地址',
  `staking_tx_index` int(11) NOT NULL COMMENT '发起质押交易的索引',
  `staking_addr` varchar(64) NOT NULL COMMENT '发起质押的账户地址',
  `staking_has` varchar(255) NOT NULL DEFAULT '0' COMMENT '质押金额(犹豫期金额)',
  `staking_locked` varchar(255) NOT NULL DEFAULT '0' COMMENT '质押金额(锁定金额)',
  `staking_reduction_epoch` int(11) NOT NULL DEFAULT '0' COMMENT '结算周期标识',
  `staking_reduction` varchar(255) NOT NULL DEFAULT '0' COMMENT '质押金额(退回中金额)',
  `stat_delegate_has` varchar(255) NOT NULL DEFAULT '0' COMMENT '委托交易总金额(犹豫期金额)',
  `stat_delegate_locked` varchar(255) NOT NULL DEFAULT '0' COMMENT '委托交易总金额(锁定期金额)',
  `stat_delegate_reduction` varchar(255) NOT NULL DEFAULT '0' COMMENT '委托交易总金额(退回中金额)',
  `stat_delegate_qty` int(11) NOT NULL DEFAULT '0' COMMENT '委托交易总数(关联的委托交易总数)',
  `stat_verifier_time` int(11) NOT NULL DEFAULT '0' COMMENT '进入共识验证论次数',
  `staking_name` varchar(64) NOT NULL DEFAULT '' COMMENT '节点名称(质押节点名称)',
  `staking_icon` varchar(255) DEFAULT '' COMMENT '节点头像(关联external_id，第三方软件获取)',
  `external_id` varchar(255) NOT NULL DEFAULT '' COMMENT '第三方社交软件关联id',
  `denefit_addr` varchar(255) NOT NULL DEFAULT '' COMMENT '收益地址',
  `expected_income` varchar(255) NOT NULL DEFAULT '0' COMMENT '预计年化率',
  `block_reward_value` varchar(255) NOT NULL DEFAULT '0' COMMENT '出块奖励',
  `pre_cons_block_qty` bigint(20) NOT NULL DEFAULT '0' COMMENT '上个共识周期出块数',
  `cur_cons_block_qty` bigint(20) NOT NULL DEFAULT '0' COMMENT '当前共识周期出块数',
  `program_version` varchar(255) NOT NULL DEFAULT '' COMMENT '程序版本',
  `staking_reward_value` varchar(255) NOT NULL DEFAULT '0' COMMENT '质押奖励',
  `web_site` varchar(255) NOT NULL DEFAULT '' COMMENT '节点的第三方主页',
  `details` varchar(255) NOT NULL DEFAULT '' COMMENT '节点的描述',
  `join_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '加入时间',
  `leave_time` timestamp NULL DEFAULT NULL COMMENT '离开时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '节点状态 \r\n1：候选中\r\n2：退出中\r\n3：已退出',
  `is_consensus` int(4) NOT NULL DEFAULT '2' COMMENT '是否共识周期验证人\r\n1：是\r\n2：否',
  `is_setting` int(4) NOT NULL DEFAULT '2' COMMENT '是否结算周期验证人\r\n1：是\r\n2：否',
  `is_init` int(4) NOT NULL DEFAULT '2' COMMENT '是否为链初始化时内置的候选人\r\n1：是\r\n2：否',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `annualized_rate_info` longtext COMMENT '最近几个结算周期收益和质押信息',
  `external_name` varchar(128) DEFAULT NULL COMMENT '第三方社交软件关联用户名',
  PRIMARY KEY (`staking_block_num`,`node_id`),
  KEY `staking_addr` (`staking_addr`) USING BTREE,
  KEY `node_id` (`node_id`) USING BTREE,
  KEY `staking_block_num` (`staking_block_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `transaction` */

DROP TABLE IF EXISTS `transaction`;

CREATE TABLE `transaction` (
  `hash` varchar(128) NOT NULL COMMENT '交易hash',
  `block_number` bigint(20) NOT NULL COMMENT '区块高度',
  `block_hash` varchar(128) NOT NULL COMMENT '区块hash',
  `transaction_index` int(11) NOT NULL COMMENT '交易在区块中位置',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '交易时间（单位：毫秒）',
  `nonce` varchar(128) NOT NULL COMMENT 'Nonce值',
  `tx_receipt_status` int(11) NOT NULL COMMENT '交易状态 \r\n1 成功 \r\n0 失败',
  `gas_price` varchar(255) NOT NULL COMMENT 'gas限制',
  `gas_used` varchar(255) NOT NULL COMMENT 'gas消耗',
  `gas_limit` varchar(255) NOT NULL COMMENT 'gas限制',
  `from` varchar(64) NOT NULL COMMENT '交易发起方地址',
  `to` varchar(64) NOT NULL DEFAULT '0x0000000000000000000000000000000000000000' COMMENT '交易接收方地址',
  `value` varchar(255) NOT NULL COMMENT '交易金额',
  `input` text COMMENT '交易输入数据',
  `tx_info` text COMMENT '交易信息',
  `tx_type` varchar(32) NOT NULL COMMENT '交易类型\r\n0: 转账  \r\n1: 合约发布(合约创建)  \r\n2: 合约调用(合约执行) \r\n4: 其他  \r\n5: MPC交易  \r\n1000: 发起质押(创建验证人) \r\n1001: 修改质押信息(编辑验证人\r\n1002: 增持质押(增加自有质押)\r\n1003: 撤销质押(退出验证人) \r\n1004: 发起委托(委托)    \r\n1005: 减持/撤销委托(赎回委托\r\n2000: 提交文本提案(创建提案)\r\n2001: 提交升级提案(创建提案)\r\n2005: 提交取消提案(创建提案)\r\n2003: 给提案投票(提案投票) \r\n2004: 版本声明 \r\n3000: 举报多签(举报验证人) \r\n4000: 创建锁仓计划(创建锁仓)',
  `actual_tx_cost` varchar(255) NOT NULL DEFAULT '0' COMMENT '交易实际花费值(手续费)，单位：von',
  `fail_reason` text COMMENT '失败原因',
  `receive_type` varchar(32) NOT NULL COMMENT '交易接收者类型（to是合约还是账户）contract合约、account账户',
  `sequence` bigint(20) NOT NULL COMMENT 'blockNum*100000+tx_index',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`hash`),
  UNIQUE KEY `sequence` (`sequence`) USING BTREE,
  KEY `block_number` (`block_number`) USING BTREE,
  KEY `from` (`from`) USING BTREE,
  KEY `to` (`to`) USING BTREE,
  KEY `tx_type` (`tx_type`) USING BTREE,
  KEY `timestamp` (`timestamp`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `un_delegation` */

DROP TABLE IF EXISTS `un_delegation`;

CREATE TABLE `un_delegation` (
  `hash` varchar(128) NOT NULL COMMENT '解质押交易hash',
  `delegate_addr` varchar(64) NOT NULL COMMENT '委托交易地址',
  `staking_block_num` bigint(20) NOT NULL COMMENT '最新的质押交易块高',
  `node_id` varchar(255) NOT NULL COMMENT '节点id',
  `apply_amount` varchar(255) NOT NULL COMMENT '申请赎回金额',
  `redeem_locked` varchar(255) NOT NULL DEFAULT '0' COMMENT '赎回被锁定的金额',
  `status` int(4) NOT NULL COMMENT '退回状态 \r\n1：退回中\r\n2：退回成功',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `real_amount` varchar(255) NOT NULL COMMENT '实际解委托金额',
  PRIMARY KEY (`hash`),
  KEY `hash` (`hash`) USING BTREE,
  KEY `node_id` (`node_id`) USING BTREE,
  KEY `staking_block_num` (`staking_block_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vote` */

DROP TABLE IF EXISTS `vote`;

CREATE TABLE `vote` (
  `hash` varchar(255) NOT NULL COMMENT '提案投票hash',
  `verifier_name` varchar(128) NOT NULL COMMENT '投票验证人名称',
  `verifier` varchar(255) NOT NULL COMMENT '投票验证人',
  `option` varchar(255) NOT NULL COMMENT '投票选项 1:支持  2:反对 3:弃权',
  `proposal_hash` varchar(128) NOT NULL COMMENT '提案交易hash',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '提案时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`hash`),
  KEY `verifier` (`verifier`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;