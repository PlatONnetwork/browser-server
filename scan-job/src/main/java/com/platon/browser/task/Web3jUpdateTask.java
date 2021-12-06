package com.platon.browser.task;

import com.platon.browser.client.JobPlatOnClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class Web3jUpdateTask {

    @Resource
    private JobPlatOnClient jobPlatOnClient;

    @Scheduled(cron = "0/10 * * * * ?")
    public void cron() {
        try {
            jobPlatOnClient.updateCurrentWeb3jWrapper();
        } catch (Exception e) {
            log.error("detect exception:{}", e);
        }
    }

}
