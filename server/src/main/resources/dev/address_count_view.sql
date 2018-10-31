CREATE VIEW address_count_view AS
SELECT DISTINCT tr.`from` FROM `transaction` tr WHERE tr.`tx_type` = 'transfer'
UNION
SELECT DISTINCT tr.`to` FROM `transaction` tr WHERE tr.`tx_type` = 'transfer'