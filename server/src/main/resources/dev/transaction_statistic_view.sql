CREATE VIEW transaction_statistic_view AS
SELECT COUNT(0) AS transactionCount FROM `transaction` tr WHERE tr.`timestamp` >= DATE_SUB(NOW(),INTERVAL 1 DAY) AND tr.`timestamp` <= NOW();