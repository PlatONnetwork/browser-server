CREATE VIEW all_transaction AS
SELECT
	tr.`chain_id`,
	tr.`hash` AS tx_hash,
	tr.`timestamp` AS block_time,
	tr.`from`,
	tr.`to`,
	tr.`value`,
	tr.`energon_used`,
	tr.`energon_limit`,
	tr.`energon_price`,
	tr.`transaction_index`,
	tr.`input`,
	tr.`actual_tx_cost`,
	tr.`tx_receipt_status`,
	tr.`tx_type` AS tx_type,
	CURRENT_TIMESTAMP() AS server_time,
	tr.`fail_reason`,
	tr.`create_time`,
	tr.`update_time`
FROM `transaction` tr
UNION ALL
SELECT
	pt.`chain_id`,
	pt.`hash` AS tx_hash,
	pt.`timestamp` AS block_time,
	pt.`from`,
	pt.`to`,
	pt.`value`,
	pt.`energon_used`,
	pt.`energon_limit`,
	pt.`energon_price`,
	-1 `transaction_index`,
	pt.`input`,
	'pending' AS actual_tx_cost,
	-1 AS tx_receipt_status,
	pt.`tx_type`,
	CURRENT_TIMESTAMP() AS server_time,
	'' AS fail_reason,
	pt.`create_time`,
	pt.`update_time`
FROM `pending_tx` pt;