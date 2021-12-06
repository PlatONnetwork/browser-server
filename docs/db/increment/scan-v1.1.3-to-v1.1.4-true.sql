USE `scan_platon`;
-- 确保show variables like 'innodb_autoinc_lock_mode';是1
DROP TABLE IF EXISTS `point_log`;
CREATE TABLE `point_log` (
                             `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                             `type` int(1) NOT NULL COMMENT '类型,1-mysql,2-es',
                             `name` varchar(255) NOT NULL COMMENT '表名或索引名',
                             `desc` varchar(255) NOT NULL COMMENT '用途描述',
                             `position` varchar(128) NOT NULL COMMENT '已统计的位置(MySQL为自增id,es为seq)',
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`id`)
) COMMENT='断点统计表';
INSERT INTO `point_log`(`id`, `type`, `name`, `desc`, `position`, `create_time`, `update_time`) VALUES (1, 1, 'n_opt_bak', '节点操作迁移至es的断点记录', '0', '2021-12-01 07:50:41', '2021-12-01 07:50:41');
INSERT INTO `point_log`(`id`, `type`, `name`, `desc`, `position`, `create_time`, `update_time`) VALUES (2, 2, 'browser_platon_transaction', '从es统计地址表交易数的断点记录', '0', '2021-12-03 06:33:27', '2021-12-03 06:33:27');
INSERT INTO `point_log`(`id`, `type`, `name`, `desc`, `position`, `create_time`, `update_time`) VALUES (3, 2, 'browser_platon_erc20_tx', '从es统计地址表和token表交易数的断点记录', '0', '2021-12-06 02:47:26', '2021-12-06 02:47:26');
INSERT INTO `point_log`(`id`, `type`, `name`, `desc`, `position`, `create_time`, `update_time`) VALUES (4, 2, 'browser_platon_erc721_tx', '从es统计地址表和token表交易数的断点记录', '0', '2021-12-06 02:47:41', '2021-12-06 02:47:41');


DROP TABLE IF EXISTS `slash`;
CREATE TABLE `slash` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `slash_data` longtext NOT NULL COMMENT '举报证据',
                              `node_id` varchar(130) NOT NULL COMMENT '节点Id',
                              `tx_hash` varchar(128) NOT NULL COMMENT '交易hash',
                              `time` datetime NOT NULL COMMENT '时间',
                              `setting_epoch` int(16) NOT NULL COMMENT '通过（block_number/每个结算周期出块数）向上取整',
                              `staking_block_num` bigint(20) NOT NULL COMMENT '质押交易所在块高',
                              `slash_rate` decimal(65,2) NOT NULL DEFAULT '0' COMMENT '双签惩罚比例',
                              `slash_report_rate` decimal(65,2) NOT NULL DEFAULT '0' COMMENT '惩罚金分配给举报人比例',
                              `benefit_address` varchar(255) NOT NULL COMMENT '交易发送者',
                              `code_remain_redeem_amount` decimal(65,2) NOT NULL DEFAULT '0' COMMENT '双签惩罚后剩下的质押金额，因为双签处罚后节点就被置为退出中，所有金额都会移动到待赎回字段中',
                              `code_reward_value` decimal(65,2) NOT NULL DEFAULT '0' COMMENT '奖励的金额',
                              `code_status` int(1) DEFAULT NULL COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定',
                              `code_staking_reduction_epoch` int(16) DEFAULT NULL COMMENT '当前退出中',
                              `code_slash_value` decimal(65,2) NOT NULL DEFAULT '0' COMMENT '惩罚的金额',
                              `un_stake_freeze_duration` int(16) NOT NULL COMMENT '解质押需要经过的结算周期数',
                              `un_stake_end_block` bigint(20) NOT NULL COMMENT '解质押冻结的最后一个区块：理论结束块与投票结束块中的最大者',
                              `block_num` bigint(20) NOT NULL COMMENT '双签的区块',
                              `is_quit` int(11) NOT NULL DEFAULT '1' COMMENT '是否退出:1是,2否',
                              `is_handle` tinyint(1) NOT NULL COMMENT '是否已处理，1-是，0-否',
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`)
) COMMENT='惩罚记录表';

use `xxl_job`;
INSERT INTO `xxl_job`.`xxl_job_group`(`id`, `app_name`, `title`, `address_type`, `address_list`, `update_time`) VALUES (2, 'scan-job', 'scan-job定时任务', 0, NULL, '2021-11-30 10:54:54');
INSERT INTO `xxl_job`.`xxl_job_group`(`id`, `app_name`, `title`, `address_type`, `address_list`, `update_time`) VALUES (3, 'scan-agent', 'agent定时任务', 0, NULL, '2021-11-30 10:54:54');
INSERT INTO `xxl_job`.`xxl_job_group`(`id`, `app_name`, `title`, `address_type`, `address_list`, `update_time`) VALUES (4, 'scan-api', 'scan-api定时任务', 0, NULL, '2021-11-30 10:54:54');

INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (2, 2, '定时更新Web3j', '2021-11-29 14:33:30', '2021-12-01 17:08:49', 'admin', '', 'CRON', '0/10 * * * * ?', 'DO_NOTHING', 'FIRST', 'web3jUpdateJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-29 14:33:30', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (3, 2, '质押表中的历史数据迁移至数据库任务', '2021-11-29 14:37:06', '2021-11-29 15:17:47', 'admin', '', 'CRON', '0/30  * * * * ?', 'DO_NOTHING', 'FIRST', 'stakingMigrateJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-29 14:37:06', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (4, 2, '提案详情更新任务', '2021-11-29 14:38:46', '2021-11-30 10:48:55', 'admin', '', 'CRON', '0/15  * * * * ?', 'DO_NOTHING', 'FIRST', 'proposalDetailUpdateJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-29 14:38:46', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (5, 2, '更新基金会账户余额', '2021-11-29 14:43:28', '2021-11-29 16:08:44', 'admin', '', 'CRON', '0 0/6 * * * ?', 'DO_NOTHING', 'FIRST', 'balanceUpdateJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-29 14:43:28', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (6, 2, '更新内置合约账户余额', '2021-11-29 14:44:51', '2021-11-29 15:17:39', 'admin', '', 'CRON', '0/10 * * * * ?', 'DO_NOTHING', 'FIRST', 'updateContractAccountJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-29 14:44:51', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (7, 2, '地址表信息补充', '2021-11-29 14:48:41', '2021-12-01 17:38:23', 'admin', '', 'CRON', '0/5  * * * * ?', 'DO_NOTHING', 'FIRST', 'addressUpdateJobHandler', '1000', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-29 14:48:41', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (8, 2, '委托表中的历史数据迁移至ES任务', '2021-11-30 10:44:55', '2021-11-30 10:44:55', 'admin', '', 'CRON', '0/30  * * * * ?', 'DO_NOTHING', 'FIRST', 'delegateMigrateJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-30 10:44:55', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (9, 2, '节点表补充', '2021-11-30 10:47:41', '2021-11-30 10:47:41', 'admin', '', 'CRON', '0/5  * * * * ?', 'DO_NOTHING', 'FIRST', 'nodeUpdateJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-30 10:47:41', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (10, 2, '提案信息更新任务', '2021-11-30 10:48:30', '2021-11-30 10:48:30', 'admin', '', 'CRON', '0/15  * * * * ?', 'DO_NOTHING', 'FIRST', 'proposalInfoJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-30 10:48:30', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (11, 2, '全量更新token的总供应量', '2021-11-30 10:50:12', '2021-11-30 10:50:12', 'admin', '', 'CRON', '0 */5 * * * ?', 'DO_NOTHING', 'FIRST', 'totalUpdateTokenTotalSupplyJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-30 10:50:12', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (12, 2, '增量更新token持有者余额', '2021-11-30 10:51:09', '2021-11-30 10:51:09', 'admin', '', 'CRON', '0 */1 * * * ?', 'DO_NOTHING', 'FIRST', 'incrementUpdateTokenHolderBalanceJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-30 10:51:09', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (13, 2, '全量更新token持有者余额', '2021-11-30 10:51:57', '2021-11-30 10:51:57', 'admin', '', 'CRON', '0 0 0 * * ?', 'DO_NOTHING', 'FIRST', 'totalUpdateTokenHolderBalanceJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-30 10:51:57', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (14, 2, '全量更新token库存信息', '2021-11-30 10:53:01', '2021-12-01 18:06:37', 'admin', '', 'CRON', '0 0 1 */1 * ?', 'DO_NOTHING', 'FIRST', 'totalUpdateTokenInventoryJobHandler', '100', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-30 10:53:01', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (15, 2, '增量更新token库存信息', '2021-11-30 10:53:42', '2021-12-01 18:07:33', 'admin', '', 'CRON', '0 */1 * * * ?', 'DO_NOTHING', 'FIRST', 'incrementUpdateTokenInventoryJobHandler', '100', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-30 10:53:42', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (16, 2, '销毁的721合约更新余额', '2021-11-30 10:54:19', '2021-11-30 10:54:19', 'admin', '', 'CRON', '0 */10 * * * ?', 'DO_NOTHING', 'FIRST', 'contractDestroyUpdateBalanceJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-30 10:54:19', '', 0, 0, 0);
INSERT INTO `xxl_job`.`xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (18, 2, '节点操作备份表迁移到ES任务', '2021-12-01 16:54:32', '2021-12-01 17:02:57', 'admin', '', 'CRON', '0 */10 * * * ?', 'DO_NOTHING', 'FIRST', 'nodeOptMoveToESJobHandler', '100', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-12-01 16:54:32', '', 0, 0, 0);
