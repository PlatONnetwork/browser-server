package com.platon.browser.task;

import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.cache.ProposalCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/22 15:35
 * @Description: 缓存维护任务
 */
@Component
public class CacheMaintenanceTask {

    private static final NodeCache NODE_CACHE = BlockChain.NODE_CACHE;
    private static final ProposalCache PROPOSAL_CACHE = BlockChain.PROPOSALS_CACHE;

    @Scheduled(cron="0/10 * * * * ?")
    private void start(){
        NODE_CACHE.sweep();
        PROPOSAL_CACHE.sweep();
    }
}
