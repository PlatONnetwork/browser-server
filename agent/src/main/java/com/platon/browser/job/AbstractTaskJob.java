package com.platon.browser.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务父类
 *
 * @author chenxf
 */
abstract class AbstractTaskJob implements SimpleJob {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info(shardingContext.getJobName() + " start running.");
        doJob(shardingContext);
        logger.info(shardingContext.getJobName() + " end running.");
    }

    protected abstract void doJob(ShardingContext shardingContext);

}
