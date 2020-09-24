ALTER TABLE `address` CHANGE `type` `type` int(2)   NOT NULL COMMENT '地址类型 :1账号,2内置合约 ,3EVM合约,4WASM合约,5EVM-ERC合约,4WASM-ERC合约' after `address` ;

-- 合约表
CREATE TABLE `erc20_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `address` varchar(64) DEFAULT NULL COMMENT '合约地址',
  `name` varchar(32) DEFAULT NULL COMMENT '合约名称',
  `symbol` varchar(32) DEFAULT NULL COMMENT '合约符号',
  `decimal` int(11) DEFAULT NULL COMMENT '合约精度',
  `total_supply` decimal(64,0) DEFAULT NULL COMMENT '供应总量',
  `icon` varchar(64) DEFAULT NULL COMMENT '合约图标',
  `creator` varchar(64) DEFAULT NULL COMMENT '合约创建者',
  `tx_hash` varchar(128) DEFAULT NULL COMMENT '合约创建所在交易哈希',
  `web_site` varchar(32) DEFAULT NULL COMMENT '官网地址',
  `type` char(1) DEFAULT NULL COMMENT '合约类型 E，evm的代币合约，W，wasm的代币合约',
  `status` int(11) DEFAULT '1' COMMENT '合约状态 1 可见，0 隐藏',
  `block_timestamp` datetime DEFAULT NULL COMMENT '上链时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_address` (`address`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Token合约表 用于记录所有满足ERC20表中的代币合约信息（仅记录满足ERC20标准的代币）';

-- 合约详细信息
CREATE TABLE `erc20_token_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `contract` varchar(64) DEFAULT NULL COMMENT '合约地址',
  `tx_count` int(11) DEFAULT NULL COMMENT '合约内交易数',
  `abi` text COMMENT '合约的ABI',
  `bin_code` text COMMENT '合约bincode',
  `source_code` text COMMENT '合约源码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_contract` (`contract`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Token合约附加数据表 记录满足ERC20合约的附属信息，用于对主合约进行一定的数据补充';

-- 合约内部转账记录表

CREATE TABLE `erc20_token_transfer_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `tx_hash` varchar(128) DEFAULT NULL COMMENT '交易哈希',
  `block_number` bigint(20) DEFAULT NULL COMMENT '区块高度',
  `tx_from` varchar(64) DEFAULT NULL COMMENT '交易发起者from',
  `contract` varchar(64) DEFAULT NULL COMMENT 'Token合约地址',
  `transfer_to` varchar(64) DEFAULT NULL COMMENT '代币接收者',
  `transfer_value` decimal(64,0) DEFAULT NULL COMMENT '转账金额',
  `decimal` int(11) DEFAULT NULL COMMENT '代币精度',
  `name` varchar(32) DEFAULT NULL COMMENT '代币名称',
  `symbol` varchar(32) DEFAULT NULL COMMENT '代币符号',
  `method_sign` varchar(32) DEFAULT NULL COMMENT '函数方法签名',
  `result` int(11) DEFAULT '1' COMMENT '转账结果 1成功，0失败',
  `block_timestamp` datetime DEFAULT NULL COMMENT '转账时间',
  `value` decimal(64,0) DEFAULT NULL COMMENT '交易value金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_contract` (`contract`),
  KEY `idx_from_to_contract` (`tx_from`,`transfer_to`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Token合约交易记录表 主要记录满足ERC20的合约中的transfer交易记录';

-- 副本：临时存储合约与地址的映射关系
CREATE TABLE `erc20_token_address_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `contract` varchar(64) DEFAULT NULL COMMENT '合约地址',
  `address` varchar(64) DEFAULT NULL COMMENT '用户地址',
  `balance` decimal(64,0) DEFAULT NULL COMMENT '地址代币余额',
  `name` varchar(32) DEFAULT NULL COMMENT '合约名称',
  `symbol` varchar(32) DEFAULT NULL COMMENT '合约符号',
  `decimal` int(11) DEFAULT NULL COMMENT '合约精度',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Token合约与地址映射表 主要记录每个token合约的地址与其持有者的映射关系';


-- ----------------------------
-- Table structure for block_node
-- ----------------------------
DROP TABLE IF EXISTS `block_node`;
CREATE TABLE `block_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `node_id` varchar(130) NOT NULL COMMENT '节点id',
  `node_name` varchar(64) NOT NULL DEFAULT '' COMMENT '节点名称(质押节点名称)',
  `staking_consensus_epoch` int(11) NOT NULL DEFAULT '0' COMMENT '共识周期标识',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `staking_consensus_epoch` (`staking_consensus_epoch`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;