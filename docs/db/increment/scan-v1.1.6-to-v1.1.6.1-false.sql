USE
    `scan_platon`;
alter table node
    add column `node_apr` text COMMENT '节点apr' after `node_settle_statis_info`;
alter table node
    add column `leave_num` bigint(20) DEFAULT NULL COMMENT '退出中的块高' after `leave_time`;
alter table staking
    add column `node_settle_statis_info` text COMMENT '节点结算周期的出块统计信息' after `low_rate_slash_count`;
alter table staking
    add column `node_apr` text COMMENT '节点apr' after `node_settle_statis_info`;
alter table staking
    add column `leave_num` bigint(20) DEFAULT NULL COMMENT '退出中的块高' after `leave_time`;
ALTER TABLE `token`
    MODIFY COLUMN `is_support_erc1155` tinyint(1) DEFAULT 0 COMMENT '是否支持erc1155接口： 0-不支持 1-支持';
ALTER TABLE `token`
    MODIFY COLUMN `is_support_erc1155_metadata` tinyint(1) DEFAULT 0 COMMENT '是否支持erc1155 metadata接口： 0-不支持 1-支持';
