#
# XXL-JOB v2.3.1-SNAPSHOT
# Copyright (c) 2015-present, xuxueli.

CREATE database if NOT EXISTS `xxl_job` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `xxl_job`;
DELETE FROM `xxl_job_info` WHERE  executor_handler = 'incrementUpdateTokenHolderBalanceJobHandler';
DELETE FROM `xxl_job_info` WHERE  executor_handler = 'totalUpdateTokenHolderBalanceJobHandler';
DELETE FROM `xxl_job_info` WHERE  executor_handler = 'incrementUpdateTokenInventoryJobHandler';
DELETE FROM `xxl_job_info` WHERE  executor_handler = 'contractDestroyUpdateBalanceJobHandler';
DELETE FROM `xxl_job_info` WHERE  executor_handler = 'proposalDetailUpdateJobHandler';
DELETE FROM `xxl_job_info` WHERE  executor_handler = 'proposalInfoJobHandler';
DELETE FROM `xxl_job_info` WHERE  executor_handler = 'updateAddressQtyJobHandler';
DELETE FROM `xxl_job_info` WHERE  executor_handler = 'updateTokenQtyJobHandler';

UPDATE `xxl_job_info` SET `schedule_conf` = '0 */10 * * * ?' where job_group =2 and executor_handler = 'totalUpdateTokenInventoryJobHandler';

INSERT INTO `xxl_job_info`(`job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (2, '提案信息更新任务', '2021-11-29 14:38:46', '2021-11-30 10:48:55', 'admin', '', 'CRON', '0/15  * * * * ?', 'DO_NOTHING', 'FIRST', 'proposalUpdateJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-29 14:38:46', '', 0, 0, 0);
INSERT INTO `xxl_job_info`(`job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (2, '统计token的持有人数量任务', '2021-11-29 14:38:46', '2021-11-30 10:48:55', 'admin', '', 'CRON', '0 */40 * * * ?', 'DO_NOTHING', 'FIRST', 'updateTokenHolderCountJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-29 14:38:46', '', 0, 0, 0);
INSERT INTO `xxl_job_info`(`job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (2, '更新token的URL', '2021-11-29 14:38:46', '2021-11-30 10:48:55', 'admin', '', 'CRON', '0 */2 * * * ?', 'DO_NOTHING', 'FIRST', 'updateTokenUrlJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-11-29 14:38:46', '', 0, 0, 0);


commit;
