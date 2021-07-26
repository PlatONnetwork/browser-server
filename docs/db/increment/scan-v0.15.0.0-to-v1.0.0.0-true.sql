USE `scan_platon`;
-- 增量脚本
alter table address add erc721_tx_qty int(11) NOT NULL DEFAULT '0' COMMENT 'erc721 token对应的交易数';
alter table address add erc20_tx_qty int(11) NOT NULL DEFAULT '0' COMMENT 'erc721 token对应的交易数';

DROP TABLE IF EXISTS `erc20_token`;
DROP TABLE IF EXISTS `erc20_token_address_rel`;
DROP TABLE IF EXISTS `erc20_token_detail`;
DROP TABLE IF EXISTS `erc20_token_transfer_record`;

alter table network_stat drop token_qty;
alter table network_stat add erc721_tx_qty int(11) NOT NULL DEFAULT '0' COMMENT 'erc721 token对应的交易数';
alter table network_stat add erc20_tx_qty int(11) NOT NULL DEFAULT '0' COMMENT 'erc20 token对应的交易数';

DROP TABLE IF EXISTS `token`;
CREATE TABLE `token` (
                         `address` varchar(64) NOT NULL COMMENT '合约地址',
                         `type` varchar(64) NOT NULL COMMENT '合约类型 erc20 erc721',
                         `name` varchar(64) DEFAULT NULL COMMENT '合约名称',
                         `symbol` varchar(64) DEFAULT NULL COMMENT '合约符号',
                         `total_supply` decimal(64,0) DEFAULT NULL COMMENT '供应总量',
                         `decimal` int(11) DEFAULT NULL COMMENT '合约精度',
                         `is_support_erc165` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否支持erc165接口： 0-不支持 1-支持',
                         `is_support_erc20` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否支持erc20接口： 0-不支持 1-支持',
                         `is_support_erc721` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否支持erc721接口： 0-不支持 1-支持',
                         `is_support_erc721_enumeration` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否支持erc721 enumeration接口： 0-不支持 1-支持',
                         `is_support_erc721_metadata` tinyint(1) NOT NULL COMMENT '是否支持metadata接口： 0-不支持 1-支持',
                         `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `token_tx_qty` int(11) NOT NULL DEFAULT '0' COMMENT 'token对应的交易数',
                         `holder` int(11) NOT NULL DEFAULT '0' COMMENT 'token对应的持有人的数量',
                         PRIMARY KEY (`address`),
                         UNIQUE KEY `token_address` (`address`)
);

DROP TABLE IF EXISTS `token_expand`;
CREATE TABLE `token_expand` (
                                `address` varchar(64) NOT NULL COMMENT '合约地址',
                                `icon` text COMMENT '合约图标',
                                `web_site` varchar(256) DEFAULT NULL COMMENT '合约地址',
                                `details` varchar(256) DEFAULT NULL COMMENT '合约官网',
                                `is_show_in_aton` tinyint(1) DEFAULT '0' COMMENT 'aton中是否显示，0-隐藏 1-展示',
                                `is_show_in_scan` tinyint(1) DEFAULT '0' COMMENT 'scan中是否显示，0-隐藏 1-展示',
                                `is_can_transfer` tinyint(1) DEFAULT '0' COMMENT '是否可转账 0-不可转账 1-可转账',
                                `create_id` bigint(20) NOT NULL COMMENT '创建者',
                                `create_name` varchar(50) NOT NULL COMMENT '创建者名称',
                                `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_id` bigint(20) NOT NULL COMMENT '更新者',
                                `update_name` varchar(50) NOT NULL COMMENT '更新者名称',
                                `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`address`)
);

DROP TABLE IF EXISTS `token_holder`;
CREATE TABLE `token_holder` (
                                `token_address` varchar(64) NOT NULL COMMENT '合约地址',
                                `address` varchar(64) NOT NULL COMMENT '用户地址',
                                `balance` decimal(64,0) DEFAULT NULL COMMENT '地址代币余额, ERC20为金额，ERC721为tokenId数',
                                `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `token_tx_qty` int(11) NOT NULL DEFAULT '0' COMMENT 'erc721 token对应的交易数',
                                PRIMARY KEY (`token_address`,`address`)
);

DROP TABLE IF EXISTS `token_inventory`;
CREATE TABLE `token_inventory` (
                                   `token_address` varchar(64) NOT NULL COMMENT '合约地址',
                                   `token_id` bigint(80) NOT NULL COMMENT 'token id',
                                   `owner` varchar(64) NOT NULL COMMENT 'token id 对应持有者地址',
                                   `name` varchar(256) DEFAULT NULL COMMENT 'Identifies the asset to which this NFT represents',
                                   `description` varchar(256) DEFAULT NULL COMMENT 'Describes the asset to which this NFT represents',
                                   `image` varchar(256) DEFAULT NULL COMMENT 'A URI pointing to a resource with mime type image/* representing the asset to which this NFT represents. Consider making any images at a width between 320 and 1080 pixels and aspect ratio between 1.91:1 and 4:5 inclusive.',
                                   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   `token_tx_qty` int(11) NOT NULL DEFAULT '0' COMMENT 'token对应的交易数',
                                   `token_owner_tx_qty` int(11) DEFAULT '0' COMMENT 'owner对该tokenaddress和tokenid的对应交易数',
                                   PRIMARY KEY (`token_address`,`token_id`)
);

ALTER TABLE token_inventory MODIFY COLUMN token_id VARCHAR ( 128 );

ALTER TABLE token_inventory MODIFY COLUMN description longtext COMMENT 'Describes the asset to which this NFT represents';

ALTER TABLE token_inventory ADD COLUMN small_image varchar(256) DEFAULT NULL COMMENT '缩略图';

ALTER TABLE token_inventory ADD COLUMN medium_image varchar(256) DEFAULT NULL COMMENT '中等缩略图';