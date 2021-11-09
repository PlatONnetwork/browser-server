USE `scan_platon`;
ALTER TABLE network_stat add COLUMN `year_num` int(11) DEFAULT 1 COMMENT '年份';
ALTER TABLE network_stat add COLUMN `total_issue_value` decimal(65,0) NOT NULL DEFAULT '10000000000' COMMENT '总发行量';