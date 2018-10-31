CREATE VIEW avg_transaction_view AS
SELECT SUM(tmp.transaction_count)/COUNT(0) AS avgTransaction
FROM (
	SELECT tr.`block_number`, COUNT(0) AS transaction_count FROM `transaction` tr GROUP BY tr.`block_number`
) tmp