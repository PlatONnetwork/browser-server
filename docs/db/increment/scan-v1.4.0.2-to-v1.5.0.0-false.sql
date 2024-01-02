USE `scan_platon`;
ALTER TABLE `tx_bak`
    ADD COLUMN `chain_Id` bigint(20) COMMENT '链id' AFTER `contract_address`,
    ADD COLUMN `raw_eth_tx_type` int(4) COMMENT '0 - 传统交易（旧） 1 - AccessList交易 2 - DynamicFee交易' AFTER `chain_Id`,
    ADD COLUMN `max_fee_per_gas` varchar(255) COMMENT '10进制数字当字符串存储' AFTER `raw_eth_tx_type`,
    ADD COLUMN `max_priority_fee_per_gas` varchar(255) COMMENT '10进制数字当字符串存储' AFTER `max_fee_per_gas`,
    ADD COLUMN `access_list_info` longtext COMMENT 'access_list的json序列化存储' AFTER `max_priority_fee_per_gas`;
