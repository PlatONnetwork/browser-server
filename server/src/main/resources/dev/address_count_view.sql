CREATE VIEW address_count_view AS;
SELECT DISTINCT tr.`chain_id`, tr.`from` FROM `transaction` tr WHERE tr.`tx_type` = 'transfer' GROUP BY tr.`chain_id`,tr.`from`
UNION
SELECT DISTINCT tr.`chain_id`, tr.`to` FROM `transaction` tr WHERE tr.`tx_type` = 'transfer' GROUP BY tr.`chain_id`,tr.`to`