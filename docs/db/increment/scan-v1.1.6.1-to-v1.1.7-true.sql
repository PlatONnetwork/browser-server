USE `scan_platon`;
    -- token_url, image, 都只需要一个字符作为前缀索引即可，能标识is null就可以满足查询需求
ALTER TABLE `token_inventory` ADD INDEX (`token_url`(1)), ADD INDEX (`image`(1)), ADD INDEX (`retry_num`);
    -- token_url, image, 都只需要一个字符作为前缀索引即可，能标识is null就可以满足查询需求
ALTER TABLE `token_1155_inventory` ADD INDEX (`token_url`(1)), ADD INDEX (`image`(1)), ADD INDEX (`retry_num`);
    -- contract_destroy_hash, 都只需要一个字符作为前缀索引即可，能标识is null就可以满足查询需求; 需要create_time排序，所以要加索引
ALTER TABLE `address` ADD INDEX (`contract_destroy_hash`(1)), ADD INDEX (`create_time`);
