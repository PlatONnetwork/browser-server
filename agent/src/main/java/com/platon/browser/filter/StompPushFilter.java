package com.platon.browser.filter;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: dongqile
 * Date: 2019/1/10
 * Time: 11:28
 */
@Component
public class StompPushFilter {

    private static Logger log = LoggerFactory.getLogger(StompPushFilter.class);

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private RedisCacheService redisCacheService;

    public boolean stompPush( Block block ,List<NodeRanking> nodeRankings){
        return redisCacheService.updateStatisticsCache(chainId,block,nodeRankings);
    }
}