#
# XXL-JOB v2.3.1-SNAPSHOT
# Copyright (c) 2015-present, xuxueli.
use `xxl_job`;

delete from xxl_job_info where job_group = 2 and executor_handler = 'totalUpdateTokenHolderBalanceJobHandler';
UPDATE xxl_job_info SET schedule_conf = '*/5 * * * * ?' WHERE job_group = 2 AND executor_handler = 'updateTokenQtyJobHandler';
UPDATE xxl_job_info SET schedule_conf = '*/5 * * * * ?' WHERE job_group = 2 AND executor_handler = 'incrementUpdateTokenHolderBalanceJobHandler';
