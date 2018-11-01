CREATE VIEW avg_transaction_view AS
SELECT tmp.`chain_id`, SUM(tmp.transaction_count)/COUNT(0) AS avgTransaction
FROM (
	SELECT tr.`chain_id`, tr.`block_number`, COUNT(0) AS transaction_count FROM `transaction` tr GROUP BY tr.`chain_id`, tr.`block_number`
) tmp GROUP BY tmp.`chain_id`;