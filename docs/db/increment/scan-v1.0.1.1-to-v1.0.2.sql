USE `scan_platon`;

alter table token modify total_supply varchar(128) DEFAULT NULL COMMENT '总供应量';

alter table token_holder modify balance varchar(128) DEFAULT NULL COMMENT '地址代币余额, ERC20为金额，ERC721为tokenId数';

alter table token_inventory drop primary key;

alter table token_inventory add id bigint(20) primary key AUTO_INCREMENT COMMENT '自增id';

ALTER TABLE token_inventory ADD UNIQUE (`token_address`,`token_id`);

alter table node add node_settle_statis_info text DEFAULT NULL COMMENT '节点结算周期的出块统计信息';