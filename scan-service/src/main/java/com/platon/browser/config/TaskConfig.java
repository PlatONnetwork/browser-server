package com.platon.browser.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 *    使用线程池运行定时任务，定线程池数目
 *  @file ScheduleConfig.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
@Configuration
@ConfigurationProperties(prefix="task")
public class TaskConfig implements SchedulingConfigurer {
    private int addressBatchSize; //地址统计任务批次大小
    private int gapForAdjust; //agent与实际链上区块号相差多少个块号时触发调整操作
    private int esRedisNotCatchupBatchSize; //未追上链,批量入ES和Redis的缓存大小
    private int esRedisCatchupBatchSize; //已追上链,批量入ES和Redis的缓存大小

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5));
    }
}
