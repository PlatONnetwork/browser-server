ALTER TABLE `n_opt_bak` CHANGE `type` `type` int(2)   NOT NULL COMMENT '操作类型:1创建,2修改,3退出,4提案,5投票,6双签,7出块率低,11解除锁定' after `node_id` ;
ALTER TABLE `network_stat` CHANGE `issue_rates` `issue_rates` varchar(2048)  COLLATE utf8_general_ci NULL COMMENT '增发比例' after `node_opt_seq` ;
ALTER TABLE `node` CHANGE `status` `status` int(2)   NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定' after `leave_time` ,
    ADD COLUMN `zero_produce_freeze_duration` int(11)   NULL COMMENT '零出块节点锁定结算周期数' after `un_stake_end_block` ,
    ADD COLUMN `zero_produce_freeze_epoch` int(11)   NULL COMMENT '零出块锁定时所在结算周期' after `zero_produce_freeze_duration` ,
    ADD COLUMN `low_rate_slash_count` int(11)   NOT NULL DEFAULT 0 COMMENT '节点零出块次数' after `zero_produce_freeze_epoch` ;
ALTER TABLE `staking` CHANGE `status` `status` int(2)   NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定' after `leave_time` ,
    ADD COLUMN `zero_produce_freeze_duration` int(11)   NULL COMMENT '零出块节点锁定结算周期数' after `un_stake_end_block` ,
    ADD COLUMN `zero_produce_freeze_epoch` int(11)   NULL COMMENT '零出块锁定时所在结算周期' after `zero_produce_freeze_duration` ,
    ADD COLUMN `low_rate_slash_count` int(11)   NOT NULL DEFAULT 0 COMMENT '节点零出块次数' after `zero_produce_freeze_epoch` ;
ALTER TABLE `staking_history` CHANGE `status` `status` int(2)   NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定' after `leave_time` ;