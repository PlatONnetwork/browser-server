package com.platon.browser.service.impl.cache;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.service.cache.NodeCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


@Component
public class NodeCacheServiceImpl extends CacheBase implements NodeCacheService {

    private final Logger logger = LoggerFactory.getLogger(NodeCacheServiceImpl.class);

    @Value("${platon.redis.key.node}")
    private String nodeCacheKeyTemplate;
    @Value("${platon.redis.key.nodeid-maxblocknum}")
    private String nodeidMaxblocknumCacheKeyTemplate;
    @Value("${platon.fake.location.filename}")
    private String fakeLocationFilename;

    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public final static Map<String,NodePushItem> NODEID_TO_FAKE_NODES = new HashMap<>();

    @PostConstruct
    private void init(){loadFakeLocation();}

    private void loadFakeLocation() {
        // 加载虚假节点地理位置

        String path = System.getProperty("user.home") + File.separator + fakeLocationFilename;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            br.lines().forEach(line->sb.append(line));
            logger.info("Loading Fake Location Config: {}",sb.toString());
            List<NodePushItem> nodes = JSON.parseArray(sb.toString(),NodePushItem.class);
            NODEID_TO_FAKE_NODES.clear();
            nodes.forEach(node->NODEID_TO_FAKE_NODES.put(node.getNodeId(),node));
        } catch (FileNotFoundException e) {
            logger.error("Fake Location Config not found: {}",e.getMessage());
            //e.printStackTrace();
        }
    }

    /**
     * 清除节点推送缓存
     * @param chainId
     */
    @Override
    public void clearNodePushCache(String chainId) {
        String cacheKey = nodeCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.delete(cacheKey);
    }

    @Override
    public void updateNodeIdMaxBlockNum(String chainId,Map<String,Long> nodeIdMaxBlockNumMap){
        String cacheKeyTemplate = nodeidMaxblocknumCacheKeyTemplate.replace("{}",chainId);
        nodeIdMaxBlockNumMap.forEach((nodeId,blockNumber)->{
            String cacheKey = cacheKeyTemplate.replace("{nodeId}",nodeId);
            redisTemplate.opsForValue().set(cacheKey,blockNumber.toString());
        });
    }

    @Override
    public String getNodeMaxBlockNum(String chainId,String nodeId){
        String cacheKeyTemplate = nodeidMaxblocknumCacheKeyTemplate.replace("{}",chainId);
        String cacheKey = cacheKeyTemplate.replace("{nodeId}",nodeId);
        String res = redisTemplate.opsForValue().get(cacheKey);
        return res;
    }

    /**
     * 更新节点推送缓存
     * @param chainId
     */
    @Override
    public void updateNodePushCache(String chainId, Set<NodeRanking> items) {
        if(!validateParam(chainsConfig,chainId,items))return;
        String cacheKey = nodeCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.delete(cacheKey);
        List<String> nodes = new ArrayList<>();
        items.forEach(initData -> {
            NodePushItem bean = new NodePushItem();
            bean.init(initData);
            nodes.add(JSON.toJSONString(bean));
        });
        if(nodes.size()>0){
            redisTemplate.opsForList().leftPushAll(cacheKey,nodes);
        }
    }

    /**
     * 重置节点推送缓存
     * @param chainId
     */
    @Override
    public void resetNodePushCache(String chainId, boolean clearOld) {
        loadFakeLocation();

        clearNodePushCache(chainId);
        NodeRankingExample condition = new NodeRankingExample();
        condition.createCriteria().andChainIdEqualTo(chainId)
                .andIsValidEqualTo(1);
        List<NodeRanking> data = nodeRankingMapper.selectByExample(condition);
        if(data.size()==0) return;

        updateNodePushCache(chainId,new HashSet<>(data));
    }

    /**
     * 获取节点推送缓存
     * @param chainId
     */
    @Override
    public List<NodePushItem> getNodePushCache(String chainId) {
        List<NodePushItem> returnData = new LinkedList<>();
        String cacheKey = nodeCacheKeyTemplate.replace("{}",chainId);
        List<String> cacheData = redisTemplate.opsForList().range(cacheKey,0,-1);
        if(cacheData.size()==0){
            return returnData;
        }
        cacheData.forEach(nodeStr -> {
            NodePushItem bean = JSON.parseObject(nodeStr,NodePushItem.class);
            returnData.add(bean);
        });
        Collections.sort(returnData,(n1,n2)->{
            if(n1.getRanking()>n2.getRanking()) return 1;
            if(n1.getRanking()<n2.getRanking()) return -1;
            return 0;
        });
        return returnData;
    }
}
