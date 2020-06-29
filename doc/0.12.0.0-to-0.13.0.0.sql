ALTER TABLE `network_stat` ADD COLUMN `issue_rates` varchar(1024)   NULL COMMENT '增发比例' after `node_opt_seq`;

ALTER TABLE `address`
    CHANGE `contract_createHash` `contract_createHash` varchar(72)  COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '创建合约的交易Hash' after `contract_create` ,
    CHANGE `contract_destroy_hash` `contract_destroy_hash` varchar(72)  COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '销毁合约的交易Hash' after `contract_createHash` ;

ALTER TABLE `n_opt_bak`
    CHANGE `tx_hash` `tx_hash` varchar(72)  COLLATE utf8_general_ci NULL COMMENT '交易hash' after `type` ;

ALTER TABLE `proposal`
    CHANGE `hash` `hash` varchar(72)  COLLATE utf8_general_ci NOT NULL COMMENT '提案交易hash' first ;

ALTER TABLE `slash`
    CHANGE `hash` `hash` varchar(72)  COLLATE utf8_general_ci NOT NULL COMMENT '举报交易hash' first ;

ALTER TABLE `tx_bak`
    CHANGE `hash` `hash` varchar(72)  COLLATE utf8_general_ci NOT NULL COMMENT '交易Hash' after `id` ;

ALTER TABLE `vote`
    CHANGE `hash` `hash` varchar(72)  COLLATE utf8_general_ci NOT NULL COMMENT '投票交易Hash' first ,
    CHANGE `proposal_hash` `proposal_hash` varchar(72)  COLLATE utf8_general_ci NOT NULL COMMENT '提案交易Hash' after `option` ;
