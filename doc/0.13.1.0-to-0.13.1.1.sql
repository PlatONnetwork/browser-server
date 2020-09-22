ALTER TABLE `address` CHANGE `type` `type` int(2)   NOT NULL COMMENT '地址类型 :1账号,2内置合约 ,3EVM合约,4WASM合约,5EVM-ERC合约,4WASM-ERC合约' after `address` ;


-- ----------------------------
-- Table structure for erc_contract
-- ----------------------------
DROP TABLE IF EXISTS `erc_contract`;
CREATE TABLE `erc_contract` (
  `address` varchar(42) NOT NULL COMMENT '地址',
  `name` varchar(125) NOT NULL DEFAULT '' COMMENT 'erc20 名称',
  `symbol` varchar(64) NOT NULL DEFAULT '' COMMENT 'erc20 符号',
  `decimals` int(8) NOT NULL DEFAULT '0' COMMENT '小数点位数',
  `total_supply` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '发行总量',
  `icon` varchar(64) NOT NULL DEFAULT '' COMMENT '合约图标',
  `webSite` varchar(64) NOT NULL DEFAULT '' COMMENT '官网地址',
  `inner_qty` varchar(64) NOT NULL DEFAULT '' COMMENT '合约内部交易量',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`address`),
  KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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