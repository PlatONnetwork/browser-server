package com.platon.browser.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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
    private int maxAddressCount;
    private int maxBatchSize;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(3));
    }
}
