ALTER TABLE `node`
ADD COLUMN `next_reward_per` INT   NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例' AFTER `reward_per` ,
ADD COLUMN `next_reward_per_mod_epoch` INT   NULL DEFAULT 0 COMMENT '【下一结算周期委托奖励比例】修改所在结算周期' AFTER `next_reward_per`;

ALTER TABLE `staking`
ADD COLUMN `next_reward_per` INT   NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例' AFTER `reward_per` ,
ADD COLUMN `next_reward_per_mod_epoch` INT   NULL DEFAULT 0 COMMENT '【下一结算周期委托奖励比例】修改所在结算周期' AFTER `next_reward_per`;

ALTER TABLE `staking_history`
ADD COLUMN `next_reward_per` INT   NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例' AFTER `reward_per` ,
ADD COLUMN `next_reward_per_mod_epoch` INT   NULL DEFAULT 0 COMMENT '【下一结算周期委托奖励比例】修改所在结算周期' AFTER `next_reward_per`;

UPDATE `node` SET next_reward_per=reward_per;
UPDATE `staking` SET next_reward_per=reward_per;
UPDATE `staking_history` SET next_reward_per=reward_per;

ALTER TABLE `erc20_token` ADD KEY `idx_holder`(`holder`) ;
ALTER TABLE `erc20_token_address_rel` ADD KEY `idx_address`(`address`) , ADD KEY `idx_contract`(`contract`) ;