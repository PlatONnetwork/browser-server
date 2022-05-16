USE `scan_platon`;
ALTER TABLE token add COLUMN `contract_destroy_block` bigint(20) DEFAULT NULL COMMENT '合约的销毁块高';
ALTER TABLE token add COLUMN `contract_destroy_update` tinyint(1) NOT NULL DEFAULT '0' COMMENT '销毁的合约是否已更新，1为是，0为否，默认是0';