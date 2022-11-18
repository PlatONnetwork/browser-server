USE
    `scan_platon`;
alter table node
    add column `node_apr` text COMMENT '节点apr' after `node_settle_statis_info`;
alter table node
    add column `leave_num` bigint(20) COMMENT '退出中的块高' after `leave_time`;
alter table staking
    add column `node_settle_statis_info` text COMMENT '节点结算周期的出块统计信息' after `low_rate_slash_count`;
alter table staking
    add column `node_apr` text COMMENT '节点apr' after `node_settle_statis_info`;
alter table staking
    add column `leave_num` bigint(20) COMMENT '退出中的块高' after `leave_time`;