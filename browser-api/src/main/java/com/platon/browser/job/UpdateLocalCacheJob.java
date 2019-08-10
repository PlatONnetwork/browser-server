package com.platon.browser.job;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class UpdateLocalCacheJob {

    private final Logger logger = LoggerFactory.getLogger(UpdateLocalCacheJob.class);

/*    @Autowired
    private ChainsConfig chainsConfig;*/

    /**
     * 更新缓存信息
     */
    @Scheduled(cron="0/10 * * * * ?")
    public void updateCache(){
        //chainsConfig.getChainIds().forEach(chainId->nodeService.updateLocalNodeCache(chainId));
    }
}
