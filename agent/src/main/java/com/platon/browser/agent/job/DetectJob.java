package com.platon.browser.agent.job;

/**
 * User: dongqile
 * Date: 2018/12/7
 * Time: 13:54
 */
public class DetectJob {

    /*private static Logger logger = LoggerFactory.getLogger(DetectJob.class);


    @Value("${chain.id}")
    private String chainId;


    @Value("${platon.redis.node.cache.key}")
    private String nodeCacheKeyTemplate;

    @Autowired
    private RedisTemplate <String, String> redisTemplate;

    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            NodeExample nodeExample = new NodeExample();
            nodeExample.createCriteria().andChainIdEqualTo(chainId);
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
            updateRedisInfo(nodeList, chainId);
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
    }*/
}