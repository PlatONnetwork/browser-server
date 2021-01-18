USE `alaya_browser`;

ALTER TABLE `node`
    ADD COLUMN `next_reward_per` INT   NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例' AFTER `reward_per` ,
    ADD COLUMN `next_reward_per_mod_epoch` INT   NULL DEFAULT 0 COMMENT '【下一结算周期委托奖励比例】修改所在结算周期' AFTER `next_reward_per`;

ALTER TABLE `staking`
    ADD COLUMN `next_reward_per` INT   NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例' AFTER `reward_per` ,
    ADD COLUMN `next_reward_per_mod_epoch` INT   NULL DEFAULT 0 COMMENT '【下一结算周期委托奖励比例】修改所在结算周期' AFTER `next_reward_per`;

ALTER TABLE `staking_history`
    ADD COLUMN `next_reward_per` INT   NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例' AFTER `reward_per` ,
    ADD COLUMN `next_reward_per_mod_epoch` INT   NULL DEFAULT 0 COMMENT '【下一结算周期委托奖励比例】修改所在结算周期' AFTER `next_reward_per`;

#添加下一结算周期生效奖励比例字段后，把此字段的值设置为reward_per的值
UPDATE `node` SET next_reward_per=reward_per;
UPDATE `staking` SET next_reward_per=reward_per;
UPDATE `staking_history` SET next_reward_per=reward_per;


ALTER TABLE `node` CHANGE `annualized_rate` `annualized_rate` DOUBLE(64,2) DEFAULT 0.00  NOT NULL   COMMENT '预计年化率';
ALTER TABLE `node` CHANGE `pre_dele_annualized_rate` `pre_dele_annualized_rate` DOUBLE(64,2) DEFAULT 0.00  NOT NULL   COMMENT '前一参与周期预计委托收益率';
ALTER TABLE `node` CHANGE `dele_annualized_rate` `dele_annualized_rate` DOUBLE(64,2) DEFAULT 0.00  NOT NULL   COMMENT '预计委托收益率';


ALTER TABLE `staking` CHANGE `annualized_rate` `annualized_rate` DOUBLE(64,2) DEFAULT 0.00  NOT NULL   COMMENT '预计年化率';
ALTER TABLE `staking` CHANGE `pre_dele_annualized_rate` `pre_dele_annualized_rate` DOUBLE(64,2) DEFAULT 0.00  NOT NULL   COMMENT '前一参与周期预计委托收益率';
ALTER TABLE `staking` CHANGE `dele_annualized_rate` `dele_annualized_rate` DOUBLE(64,2) DEFAULT 0.00  NOT NULL   COMMENT '预计委托收益率';


ALTER TABLE `staking_history` CHANGE `annualized_rate` `annualized_rate` DOUBLE(64,2) DEFAULT 0.00  NOT NULL   COMMENT '预计年化率';
ALTER TABLE `staking_history` CHANGE `pre_dele_annualized_rate` `pre_dele_annualized_rate` DOUBLE(64,2) DEFAULT 0.00  NOT NULL   COMMENT '前一参与周期预计委托收益率';
ALTER TABLE `staking_history` CHANGE `dele_annualized_rate` `dele_annualized_rate` DOUBLE(64,2) DEFAULT 0.00  NOT NULL   COMMENT '预计委托收益率';
