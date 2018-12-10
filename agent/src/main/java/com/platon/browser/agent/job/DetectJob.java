package com.platon.browser.agent.job;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.platon.browser.common.constant.ConfigConst;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.NodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StopWatch;

import java.net.InetAddress;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/12/7
 * Time: 13:54
 */
public class DetectJob extends AbstractTaskJob {

    private static Logger logger = LoggerFactory.getLogger(DetectJob.class);

    @Autowired
    private NodeMapper nodeMapper;

    @Value("${platon.redis.node.cache.key}")
    private String nodeCacheKeyTemplate;

    @Autowired
    private RedisTemplate <String, String> redisTemplate;

    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            ConfigConst.loadConfigPath();
            NodeExample nodeExample = new NodeExample();
            nodeExample.createCriteria().andChainIdEqualTo(ConfigConst.getChainId());
            List <Node> nodeList = nodeMapper.selectByExample(nodeExample);
            if (nodeList.size() > 0) {
                logger.debug("detect node!..");
                nodeList.forEach(node -> {
                    int resulte = ping(node.getIp());
                    if (1 == resulte) {
                        logger.debug("nodeIp : " + node.getIp() + "node status is normal");
                        node.setNodeStatus(1);
                    } else {
                        logger.debug("nodeIp : " + node.getIp() + "node status is abnormal");
                        node.setNodeStatus(2);
                    }
                    nodeMapper.updateByPrimaryKey(node);
                    logger.debug("update DB succ!...");
                });
            }
            updateRedisInfo(nodeList, ConfigConst.getChainId());
        } catch (Exception e) {
            logger.error("DetectJob is accomplish!...",e.getMessage());
        } finally {
            stopWatch.stop();
        }
    }

    private int ping ( String ip ) {
        InetAddress address = null;
        int timeOut = 3000;
        try {
            address = InetAddress.getByName(ip);
            if (address.isReachable(timeOut)) {
                //success
                return 1;
            } else {
                //fail
                return 2;
            }
        } catch (Exception e) {
            logger.error("Link node exception!...", e.getMessage());
            return 2;
        }
    }

    private void updateRedisInfo ( List <Node> nodes, String chainId ) {
        try {
            String cacheKey = nodeCacheKeyTemplate.replace("{}", chainId);
            String data = JSON.toJSONString(nodes);
            redisTemplate.opsForValue().set(cacheKey, data);
            logger.debug("update redis information succ!...");
        } catch (Exception e) {
            logger.debug("update DB succ but update redis infomation fail!...");
        }
    }
}