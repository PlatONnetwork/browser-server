ALTER TABLE `address`
    CHANGE `type` `type` int(11)   NOT NULL COMMENT '地址类型 :1账号,2内置合约 ,3EVM合约,4WASM合约' after `address`,
    ADD COLUMN `token_qty` int(11)   NOT NULL DEFAULT 0 COMMENT 'erc20 token对应的交易数' after `tx_qty`;
ALTER TABLE `erc20_token`
    ADD COLUMN `holder` int(11)   NOT NULL DEFAULT 0 COMMENT 'erc20 token对应的持有人' after `tx_count`;
ALTER TABLE `erc20_token_address_rel`
    ADD COLUMN `tx_count` int(11)   NOT NULL DEFAULT 0 COMMENT '地址token交易数' after `decimal` ,
    ADD COLUMN `total_supply` decimal(64,0)   NOT NULL DEFAULT 0 COMMENT 'token 总发行量' after `tx_count`;
ALTER TABLE `erc20_token_detail`
    CHANGE `icon` `icon` text NULL COMMENT '合约图标' after `contract`;
ALTER TABLE `staking_history`
    CHANGE `annualized_rate` `annualized_rate` double(16,2)   NOT NULL DEFAULT 0.00 COMMENT '预计年化率' after `benefit_addr` ;