ALTER TABLE `node` CHANGE `status` `status` int(2)   NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定' after `leave_time` ;
ALTER TABLE `staking` CHANGE `status` `status` int(2)   NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定' after `leave_time` ;
ALTER TABLE `staking_history` CHANGE `status` `status` int(2)   NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定' after `leave_time` ;