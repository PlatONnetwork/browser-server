package com.platon.browser.task;

import com.platon.browser.client.JobPlatOnClient;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class Web3jUpdateTask {

    @Resource
    private JobPlatOnClient jobPlatOnClient;

    @XxlJob("web3jUpdateJobHandler")
    public void cron() {
        try {
            jobPlatOnClient.updateCurrentWeb3jWrapper();
        } catch (Exception e) {
            log.error("detect exception:{}", e);
        }
    }

}
