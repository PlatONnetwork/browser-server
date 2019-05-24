package com.platon.browser.job;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.service.app.AppNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class UpdateCacheJob {

    private final Logger logger = LoggerFactory.getLogger(UpdateCacheJob.class);

    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private AppNodeService appNodeService;

    /**
     * 更新缓存信息
     */
    @Scheduled(cron="0/10 * * * * ?")
    public void updateCache(){
        chainsConfig.getChainIds().forEach(chainId->appNodeService.updateNodeCache(chainId));
    }
}
