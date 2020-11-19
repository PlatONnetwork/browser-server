ALTER TABLE `node`
ADD COLUMN `next_reward_per` int   NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例' after `reward_per` ,
ADD COLUMN `next_reward_per_mod_epoch` int   NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例修改所在结算周期' after `next_reward_per`;

ALTER TABLE `staking`
ADD COLUMN `next_reward_per` int   NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例' after `reward_per` ,
ADD COLUMN `next_reward_per_mod_epoch` int   NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例修改所在结算周期' after `next_reward_per`;

ALTER TABLE `staking_history`
ADD COLUMN `next_reward_per` int   NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例' after `reward_per` ,
ADD COLUMN `next_reward_per_mod_epoch` int   NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例修改所在结算周期' after `next_reward_per`;