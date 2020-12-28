/*
Navicat MySQL Data Transfer

Source Server         : 16.171
Source Server Version : 50727
Source Host           : 192.168.16.171:3306
Source Database       : platon_browser_0.12.0.0_test

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2020-04-23 10:15:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sta_dele
-- ----------------------------
DROP TABLE IF EXISTS `sta_dele`;
CREATE TABLE `sta_dele` (
  `dele_address` varchar(255) NOT NULL COMMENT '委托人钱包',
  `node_id` varchar(255) NOT NULL COMMENT '节点id',
  `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
  `block_number` bigint(20) NOT NULL DEFAULT '0' COMMENT '区块号',
  `stake_block_number` bigint(20) DEFAULT NULL COMMENT '质押区块号',
  `time` timestamp NULL DEFAULT NULL COMMENT '交易时间',
  `dele_amount` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '委托金额',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dele_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sta_file
-- ----------------------------
DROP TABLE IF EXISTS `sta_file`;
CREATE TABLE `sta_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `statiscal_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计日期',
  `type` int(2) NOT NULL COMMENT '类型\r\n1：节点收益\r\n2：节点签名率\r\n3：双签\r\n4：升级\r\n5：全网压测\r\n6：DB回滚\r\n7：节点在线率\r\n11：幸运区块\r\n12：委托奖励',
  `status` int(2) NOT NULL COMMENT '状态 1：待统计\r\n2：已统计',
  `file_url` varchar(255) DEFAULT NULL COMMENT '文件全路径',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名字',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_summary` int(2) DEFAULT '0' COMMENT '是否已汇总统计: 0-否,1-是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sta_guess
-- ----------------------------
DROP TABLE IF EXISTS `sta_guess`;
CREATE TABLE `sta_guess` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `contract_Address` varchar(255) NOT NULL COMMENT '合约地址',
  `from_Address` varchar(255) NOT NULL COMMENT '发送地址',
  `tx_hash` varchar(255) NOT NULL COMMENT '交易hash',
  `block_number` bigint(20) NOT NULL DEFAULT '0' COMMENT '开奖区块号',
  `status` int(2) NOT NULL COMMENT '状态 1：已开奖\r\n2：未开奖\r\n3：失败',
  `partake_num` int(11) DEFAULT '0' COMMENT '本期参与人数',
  `prize_num` int(11) DEFAULT '0' COMMENT '本期中奖人数',
  `lucky_num` int(11) DEFAULT '0' COMMENT '幸运号数',
  `total_amount` decimal(65,0) DEFAULT '0' COMMENT '开奖总金额',
  `avg_amount` decimal(65,0) DEFAULT '0' COMMENT '平均金额',
  `draw_hash` varchar(255) DEFAULT NULL COMMENT '开奖交易hash',
  `fail_reason` varchar(255) DEFAULT NULL COMMENT '失败原因',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sta_guess_list
-- ----------------------------
DROP TABLE IF EXISTS `sta_guess_list`;
CREATE TABLE `sta_guess_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `guess_id` bigint(20) NOT NULL COMMENT '活动id',
  `address` varchar(255) DEFAULT NULL COMMENT '中奖钱包地址',
  `buy_num` int(11) NOT NULL COMMENT '中奖票号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2455 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sta_incite_block
-- ----------------------------
DROP TABLE IF EXISTS `sta_incite_block`;
CREATE TABLE `sta_incite_block` (
  `block_number` bigint(20) NOT NULL DEFAULT '0' COMMENT '区块号',
  `status` int(2) NOT NULL COMMENT '状态 1：已统计\r\n2：未统计',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`block_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sta_incite_reward
-- ----------------------------
DROP TABLE IF EXISTS `sta_incite_reward`;
CREATE TABLE `sta_incite_reward` (
  `tx_hash` varchar(255) NOT NULL COMMENT '交易hash',
  `address` varchar(255) DEFAULT NULL COMMENT '钱包地址',
  `tx_type` int(11) NOT NULL COMMENT '状态 同PlatScan的类型',
  `block_number` bigint(20) NOT NULL DEFAULT '0' COMMENT '区块号',
  `time` timestamp NULL DEFAULT NULL COMMENT '交易时间',
  `amount` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '奖励金额',
  `index` bigint(20) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tx_count` int(11) DEFAULT NULL COMMENT '中奖区间段内的总交易数',
  `addr_count` int(11) DEFAULT NULL COMMENT '中奖区间段内的交易发送者总地址数',
  `section` varchar(64) DEFAULT NULL COMMENT '中奖区间段: <起始块号>-<结束块号>',
  PRIMARY KEY (`tx_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sta_reward
-- ----------------------------
DROP TABLE IF EXISTS `sta_reward`;
CREATE TABLE `sta_reward` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `statiscal_date` timestamp NULL DEFAULT NULL COMMENT '统计日期',
  `type` int(2) NOT NULL COMMENT '统计类型\r\n11：幸运区块\r\n12：委托奖励',
  `node_id` varchar(255) NOT NULL COMMENT '节点id',
  `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
  `dele_address` varchar(255) DEFAULT NULL COMMENT '委托人钱包',
  `block_number` bigint(20) NOT NULL DEFAULT '0' COMMENT '区块号',
  `stake_block_number` bigint(20) DEFAULT NULL COMMENT '质押区块号',
  `time` timestamp NULL DEFAULT NULL COMMENT '交易时间',
  `dele_amount` decimal(65,0) NOT NULL DEFAULT '0' COMMENT '委托金额',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19237 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sta_switch
-- ----------------------------
DROP TABLE IF EXISTS `sta_switch`;
CREATE TABLE `sta_switch` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `online_flag` int(2) DEFAULT NULL COMMENT '是否开启节点在线率统计1-是，2-否',
  `sta_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报表输出时间',
  `sta_flag` int(2) DEFAULT NULL COMMENT '是否开启报表输出1-是，2-否',
  `version` varchar(255) DEFAULT NULL,
  `proposalId` varchar(255) DEFAULT NULL,
  `cur_block_num` bigint(20) NOT NULL COMMENT '当前已采区块号',
  `new_block` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
