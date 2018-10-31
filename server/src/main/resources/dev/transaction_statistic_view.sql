CREATE VIEW transaction_statistic_view AS
SELECT bl.number AS height, bl.`timestamp` AS `time`, COUNT(0) AS `transaction`
FROM `block` bl LEFT JOIN `transaction` tr ON tr.`block_number`=bl.`number`
GROUP BY bl.`number`,bl.`timestamp` ORDER BY bl.`timestamp` DESC LIMIT 3600;