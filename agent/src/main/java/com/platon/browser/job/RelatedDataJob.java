package com.platon.browser.job;

import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: dongqile
 * Date: 2019/1/28
 * Time: 16:49
 */
//@Component
public class RelatedDataJob {
    private static Logger logger = LoggerFactory.getLogger(RelatedDataJob.class);

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private PlatonClient platon;

    @Value("${platon.chain.active}")
    private String chainId;

    public static Map <String, Double> nodeAvgTimeMap = new ConcurrentHashMap <>();

    //@Scheduled(cron = "0 */1 * * * ?")
    //Scheduled(cron = "0/2 * * * * ?")
    //Scheduled(cron = "0/2 * * * * ?")
    protected void statistics () {
        logger.debug("*** In the RelatedDataJob ***");
        Map <Long, Date> blockOfNodeinfo = new HashMap <>();
        List <Long> beforeNumberList = new ArrayList <>();
        try {
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(chainId).andIsValidEqualTo(1);
            //有效节点信息列表
            List <NodeRanking> nodeRankingList = nodeRankingMapper.selectByExample(nodeRankingExample);
            nodeRankingList.forEach(nodeRanking -> {
                BlockExample blockExample = new BlockExample();
                blockExample.createCriteria()
                        .andChainIdEqualTo(chainId)
                        .andNodeIdEqualTo(nodeRanking.getNodeId());
                blockExample.setOrderByClause("timestamp DESC");
                PageHelper.startPage(1, 4);
                List <Block> blocks = blockMapper.selectByExample(blockExample);
                if (blocks.size() == 0) return;
                //查询出的nodeid所属的区块列表遍历，并将节点k-v（number-time）存储到map，将该节点所出的区块-1,找到每个区块的前一个块，并查询列表
                blocks.forEach(block -> {
                    blockOfNodeinfo.put(block.getNumber(), block.getTimestamp());
                    if (block.getNumber() > 1) {
                        beforeNumberList.add(block.getNumber() - 1);
                    } else beforeNumberList.add(block.getNumber());
                });
                BlockExample before = new BlockExample();
                before.createCriteria()
                        .andChainIdEqualTo(chainId)
                        .andNumberIn(beforeNumberList);
                before.setOrderByClause("timestamp DESC");
                List <Block> beforBlockList = blockMapper.selectByExample(before);
                long diff = 0L;
                for (Block block : beforBlockList) {
                    diff = diff + blockOfNodeinfo.get(block.getNumber() + 1).getTime() - block.getTimestamp().getTime();
                }
                BigDecimal avgTime = BigDecimal.valueOf(Math.abs(diff)).divide(BigDecimal.valueOf(blocks.size()), 4, RoundingMode.HALF_UP);
                BigDecimal res = avgTime.divide(BigDecimal.valueOf(1000L), 4, RoundingMode.HALF_UP);
                nodeAvgTimeMap.put(nodeRanking.getNodeId(), res.doubleValue());
            });
        } catch (Exception e) {
            //e.printStackTrace();
            logger.debug("*** RelatedDataJob Exception ***");
        }
        logger.debug("*** End the RelatedDataJob ***");
    }
}

