package com.platon.browser.job;

import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CutsomNodeRankingMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/1/28
 * Time: 16:49
 */
@Component
public class RelatedDataJob {
    private static Logger logger = LoggerFactory.getLogger(RelatedDataJob.class);

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private CutsomNodeRankingMapper customNodeRankingMapper;
    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private PlatonClient platon;

    //@Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0/1 * * * * ?")
    protected void statistics () {
        logger.debug("*** In the RelatedDataJob ***");
        List<NodeRanking> rankings = new ArrayList <>();
        try {
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(platon.getChainId()).andIsValidEqualTo(1);
            //有效节点信息列表
            List<NodeRanking> nodeRankingList = nodeRankingMapper.selectByExample(nodeRankingExample);
            nodeRankingList.forEach(nodeRanking -> {
                BlockExample blockExample = new BlockExample();
                blockExample.createCriteria()
                        .andChainIdEqualTo(platon.getChainId())
                        .andNodeIdEqualTo(nodeRanking.getNodeId());
                blockExample.setOrderByClause("timestamp DESC");
                PageHelper.startPage(1,3600);
                List<Block> blocks = blockMapper.selectByExample(blockExample);
                if(blocks.size()>0) {
                    long diff= 0l;
                    for(int i = 0; i < blocks.size(); i++){
                        //防止下溢出
                        if(i+1 <= blocks.size()){
                            diff = blocks.get(i+1).getTimestamp().getTime() - blocks.get(i).getTimestamp().getTime();
                        }
                    }
                    double avgBlockTime = diff / blocks.size() / 1000;
                    nodeRanking.setAvgTime(avgBlockTime);
                    rankings.add(nodeRanking);
                }

                if(rankings.size()> 0){
                    customNodeRankingMapper.insertOrUpdate(rankings);
                }
            });
        }catch (Exception e){
            logger.debug("*** RelatedDataJob Exception ***");
        }
        logger.debug("*** End the RelatedDataJob ***");
    }
}