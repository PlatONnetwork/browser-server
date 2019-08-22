package com.platon.browser.task;

import com.platon.browser.dto.CustomProposal;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/22 15:35
 * @Description: 进程业务数据缓存维护任务
 */
@Component
public class CacheMaintenanceTask {

    private static final NodeCache NODE_CACHE = BlockChain.NODE_CACHE;
    //private static final Map<String, CustomProposal> PROPOSAL_CACHE = BlockChain.PROPOSALS_CACHE;


    @Scheduled(cron="0/10 * * * * ?")
    private void start(){

    }
}
