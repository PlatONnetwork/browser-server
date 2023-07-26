USE `scan_platon`;

INSERT INTO `point_log`(`id`, `type`, `name`, `desc`, `position`) SELECT 12 AS 'id', 1 AS 'type', 'tx_1155_bak' AS 'name', '从tx_1155_bak交易备份表统计TokenHolder持有者数的断点记录' AS 'desc', tb.`id` AS 'position' FROM `tx_erc_1155_bak` tb ORDER BY tb.`id` DESC LIMIT 1;
