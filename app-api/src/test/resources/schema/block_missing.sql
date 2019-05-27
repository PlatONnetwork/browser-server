CREATE TABLE `block_missing` (
         `number` bigint(20) NOT NULL COMMENT '异常块高',
         `chain_id` varchar(64) NOT NULL COMMENT '链ID',
         PRIMARY KEY (`number`)
);
