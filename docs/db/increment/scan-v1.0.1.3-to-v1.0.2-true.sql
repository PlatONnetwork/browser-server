USE `scan_platon`;

alter table token modify total_supply varchar(128) DEFAULT NULL COMMENT '总供应量';

alter table token_holder modify balance varchar(128) DEFAULT NULL COMMENT '地址代币余额, ERC20为金额，ERC721为tokenId数';

alter table token_inventory drop primary key;

alter table token_inventory add id bigint(20) primary key AUTO_INCREMENT COMMENT '自增id';

ALTER TABLE token_inventory AUTO_INCREMENT=1;

ALTER TABLE token_inventory ADD UNIQUE (`token_address`,`token_id`);

alter table node add node_settle_statis_info text DEFAULT NULL COMMENT '节点结算周期的出块统计信息';

ALTER TABLE token_expand add COLUMN `is_show_in_aton_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否显示在aton管理台，1为显示，0不显示，默认是0';
ALTER TABLE token_expand add COLUMN `is_show_in_scan_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否显示在scan管理台，1为显示，0不显示，默认是0';
update token_expand set `is_show_in_aton_admin` = 1 where `is_show_in_aton` = 1;
update token_expand set `is_show_in_scan_admin` = 1 where `is_show_in_scan` = 1;