package com.platon.browser.task;

import com.platon.browser.publisher.AbstractPublisher;
import com.platon.browser.utils.AppStatusUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther: chendongming@matrixelements.com
 * @Date: 2019/11/16
 * @Description: 环形缓冲区队列监控任务
 */
@Component
@Slf4j
public class RingBufferMonitorTask {

    @Scheduled(cron = "0/10 * * * * ?")
    public void ringBufferMonitor () {
        // 只有程序正常运行才执行任务
        if(AppStatusUtil.isRunning()) start();
    }

    protected void start () {
        Map<String,AbstractPublisher> publisherMap = AbstractPublisher.getPublisherMap();
        log.info("-----------------------------------------环形缓冲区信息-----------------------------------------");
        publisherMap.forEach((name,publisher)->log.info("({}):{}",name,publisher.info()));
    }
}
