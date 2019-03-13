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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.platon.browser.utils.CacheTool.NODEID_TO_AVGTIME;

/**
 * User: dongqile
 * Date: 2019/1/28
 * Time: 16:49
 */
@Component
public class NodeAvgTimeJob {
    private static Logger logger = LoggerFactory.getLogger(NodeAvgTimeJob.class);

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private PlatonClient platon;
    @Value("${platon.chain.active}")
    private String chainId;

    @Scheduled(cron = "0/5 * * * * ?")
    protected void statistics () {
        logger.debug("*** In the NodeAvgTimeJob *** ");
        try{
            NodeRankingExample nodeCon = new NodeRankingExample();
            nodeCon.createCriteria().andChainIdEqualTo(chainId).andIsValidEqualTo(1);
            List<NodeRanking> nodes = nodeRankingMapper.selectByExample(nodeCon);
            nodes.forEach(node->{
                // 查询节点最近3600个块
                BlockExample blockExample = new BlockExample();
                blockExample.createCriteria().andNodeIdEqualTo(node.getNodeId());
                blockExample.setOrderByClause("timestamp DESC");
                PageHelper.startPage(1,3600);
                List<Block> afterBlocks = blockMapper.selectByExample(blockExample);
                Map<Long,Block> afterMap = new HashMap<>();
                afterBlocks.forEach(block->afterMap.put(block.getNumber(),block));
                List<Long> beforeNumbers = new ArrayList<>();
                afterMap.keySet().forEach(afterNumber->{if(afterNumber>1) beforeNumbers.add(afterNumber-1);});
                if(beforeNumbers.size()==0) return;

                // 取出3600个块中每个块的前一个块
                blockExample = new BlockExample();
                blockExample.createCriteria().andNumberIn(beforeNumbers);
                List<Block> beforeBlocks = blockMapper.selectByExample(blockExample);

                AtomicLong sumTime = new AtomicLong(0);
                beforeBlocks.forEach(before->{
                    Block after = afterMap.get(before.getNumber()+1);
                    long time = after.getTimestamp().getTime()-before.getTimestamp().getTime();
                    sumTime.addAndGet(time);
                });

                BigDecimal avgTime = BigDecimal.valueOf(sumTime.get()).divide(BigDecimal.valueOf(afterBlocks.size()),2, RoundingMode.HALF_UP);
                avgTime = avgTime.divide(BigDecimal.valueOf(1000),4, RoundingMode.HALF_UP);
                NODEID_TO_AVGTIME.put(node.getNodeId(),avgTime.doubleValue());
                logger.debug("node:{},sumTime:{}ms,avgTime:{}s",node.getName(),sumTime, avgTime.doubleValue());
            });
        } catch (Exception e) {
            logger.error("NodeAvgTimeJob Exception:{}", e.getMessage());
        }
        logger.debug("*** End the NodeAvgTimeJob *** ");
    }
}