package com.platon.browser.job;

import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.NodeRankingBean;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockKey;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CustomNodeRankingMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.StatisticsCache;
import com.platon.browser.dto.agent.CandidateDto;
import com.platon.browser.enums.NodeTypeEnum;
import com.platon.browser.service.cache.NodeCacheService;
import com.platon.browser.service.cache.StatisticCacheService;
import com.platon.browser.util.CalculatePublicKey;
import com.platon.browser.utils.FilterTool;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.platon.browser.utils.CacheTool.NODEID_TO_NAME;

/**
 * User: dongqile
 * Date: 2019/1/22
 * Time: 11:55
 */
@Component
public class NodeAnalyseJob {
    private static Logger logger = LoggerFactory.getLogger(Web3DetectJob.class);
    private static Long beginNumber = 0L;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private CustomNodeRankingMapper customNodeRankingMapper;
    @Autowired
    private StatisticCacheService statisticCacheService;
    @Autowired
    private NodeCacheService nodeCacheService;
    @Autowired
    private PlatonClient platon;
    @Value("${platon.chain.active}")
    private String chainId;

    @PostConstruct
    private void init () {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Data
    private static final class ChainInfo {
        Map <String, CandidateDto> allMap = new HashMap <>();
        List <CandidateDto> nodes = new ArrayList <>();
        Map <String, String> nodeTypeMap = new HashMap <>();
        Map <String, Integer> countMap = new HashMap <>();
    }

    /**
     * 分析节点数据
     */
    @Scheduled(cron = "0/1 * * * * ?")
    protected void analyseNode () {
        logger.debug("*** In the NodeAnalyseJob *** ");
        try {
            long startTime = System.currentTimeMillis();
            logger.debug("getBlockNumber---------------------------------->{}", System.currentTimeMillis() - startTime);
            // 从数据库查询有效节点信息，放入本地缓存
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria()
                    .andChainIdEqualTo(chainId)
                    .andIsValidEqualTo(1);
            List <NodeRanking> dbNodes = nodeRankingMapper.selectByExample(nodeRankingExample);
            dbNodes.forEach(n -> NODEID_TO_NAME.put(n.getNodeId(), n.getName()));

            EthBlock ethBlock = platon.getWeb3j(chainId).ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
            BigInteger publicKey = CalculatePublicKey.testBlock(ethBlock);
            ChainInfo chainInfo = getChainInfo(ethBlock);

            if (null == chainInfo.getNodes() && chainInfo.getNodes().size() < 0) return;

            nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(chainId).andIsValidEqualTo(1);
            //find NodeRanking info by condition on database
            List <NodeRanking> dbList = nodeRankingMapper.selectByExample(nodeRankingExample);

            List <NodeRanking> nodeList = new ArrayList <>();
            int i = 1;
            BlockKey key = new BlockKey();
            key.setChainId(chainId);
            key.setHash(ethBlock.getBlock().getHash());
            Block block = blockMapper.selectByPrimaryKey(key);
            for (CandidateDto candidateDto : chainInfo.getNodes()) {
                NodeRankingBean nodeRanking = new NodeRankingBean();
                nodeRanking.init(candidateDto);

                //设置节点类型
                String nodeType = chainInfo.getNodeTypeMap().get(candidateDto.getCandidateId());
                nodeRanking.setNodeType(nodeType != null ? nodeType : " ");

                // nodeRanking.init()中获取不到平均出块时间时，把平均出块时间设置为全局的(redis统计缓存中的平均出块时间)
                StatisticsCache statisticsCache = statisticCacheService.getStatisticsCache(chainId);
                nodeRanking.setAvgTime(statisticsCache.getAvgTime().doubleValue());

                BigDecimal rate = new BigDecimal(nodeRanking.getRewardRatio());
                nodeRanking.setChainId(chainId);
                nodeRanking.setJoinTime(new Date(ethBlock.getBlock().getTimestamp().longValue()));
                nodeRanking.setBlockReward(FilterTool.getBlockReward(ethBlock.getBlock().getNumber().toString()));
                /*
                 * 统计当前块中：
                 * profitAmount累计收益 = 区块奖励 * 分红比例 + 当前区块的手续费总和
                 * RewardAmount分红收益 = 区块奖励 * （1-分红比例）
                 */
                //todo:数据不一致的问题
                if (block != null) {
                    BigDecimal actualTxCostsum = new BigDecimal(block.getActualTxCostSum());
                }
                nodeRanking.setProfitAmount(new BigDecimal(FilterTool.getBlockReward(ethBlock.getBlock().getNumber().toString())).
                        multiply(rate).
                        add(BigDecimal.ZERO).toString());

                nodeRanking.setRewardAmount(new BigDecimal(FilterTool.getBlockReward(ethBlock.getBlock().getNumber().toString())).multiply(BigDecimal.ONE.subtract(rate)).toString());
                nodeRanking.setRanking(i);
                nodeRanking.setType(1);
                Long count = chainInfo.getCountMap().get(nodeRanking.getNodeId().replace("0x", "")).longValue();
                if (null == count) {
                    nodeRanking.setCount(0L);
                } else {
                    nodeRanking.setCount(count);
                }

                // Set the node election status according to the ranking
                // 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名
                /**
                 * The first 100：candidate nodes
                 * After 100：alternative nodes
                 * **/
                int electionStatus = 1;
                if (1 <= i && i < 25) electionStatus = 3;
                if (26 <= i && i < 100) electionStatus = 1;
                if (i >= 100) electionStatus = 4;
                nodeRanking.setElectionStatus(electionStatus);
                nodeRanking.setIsValid(1);
                nodeRanking.setBeginNumber(ethBlock.getBlock().getNumber().longValue());
                nodeList.add(nodeRanking);
                i = i + 1;
            }
            //this time update database struct
            List <NodeRanking> updateList = new ArrayList <>();
            //data form database and node status is vaild

            Map <String, NodeRanking> dbNodeIdToNodeRankingMap = new HashMap <>();
            nodeList.forEach(e -> {
                dbNodeIdToNodeRankingMap.put(e.getNodeId(), e);
                updateList.add(e);
            });


            if (dbList.size() > 0 && dbList != null) {
                for (int j = 0; j < dbList.size(); j++) {
                    NodeRanking dbNode = dbList.get(j);
                    NodeRanking chainNode = dbNodeIdToNodeRankingMap.get(dbNode.getNodeId());
                    if (chainNode != null) {
                        // 库里有效属性保留
                        chainNode.setBlockCount(dbNode.getBlockCount());
                        chainNode.setJoinTime(dbNode.getJoinTime());
                        chainNode.setBeginNumber(dbNode.getBeginNumber());
                        chainNode.setId(dbNode.getId());
                        chainNode.setAvgTime(dbNode.getAvgTime());
                        if (publicKey.equals(new BigInteger(chainNode.getNodeId().replace("0x", ""), 16))) {
                            chainNode.setBlockCount(chainNode.getBlockCount() + 1);
                            chainNode.setProfitAmount(new BigDecimal(chainNode.getProfitAmount()).add(new BigDecimal(dbNode.getProfitAmount())).toString());
                            chainNode.setRewardAmount(new BigDecimal(chainNode.getRewardAmount()).add(new BigDecimal(dbNode.getRewardAmount())).toString());
                            chainNode.setBlockReward(new BigDecimal(chainNode.getBlockReward()).add(new BigDecimal(dbNode.getBlockReward())).toString());
                        } else {
                            chainNode.setProfitAmount(dbNode.getProfitAmount());
                            chainNode.setRewardAmount(dbNode.getRewardAmount());
                            chainNode.setBlockReward(dbNode.getBlockReward());
                        }
                    } else {
                        dbNode.setEndNumber(ethBlock.getBlock().getNumber().longValue());
                        dbNode.setIsValid(0);
                        updateList.add(dbNode);
                    }
                }
            }
            //TODO:publickey 0.4.0解析存在问题
            //FilterTool.currentBlockOwner(updateList, publicKey);

            //FilterTool.dateStatistics(updateList, publicKey, ethBlock.getBlock().getNumber().toString());

            //TODO:verifierList存在问题，目前错误解决办法，待底层链修复完毕后在进行修正
            int consensusCount = 0;

            Set <NodeRanking> redisNode = new HashSet <>();

            for (NodeRanking nodeRanking : updateList) {
                if (nodeRanking.getIsValid() == 1) {
                    consensusCount++;
                    NODEID_TO_NAME.put(nodeRanking.getNodeId(), nodeRanking.getName());
                }
                if (nodeRanking.getIsValid().equals(1)) {
                    // 把验证节点放到redis缓存，让browser-api推送给websocket端
                    redisNode.add(nodeRanking);
                }
            }

            if (!updateList.isEmpty()) {
                Map <String, Integer> countMap = new HashMap <>();
                updateList.forEach(nodeRanking -> {
                    Integer count = countMap.get(nodeRanking.getNodeId());
                    if (count == null) count = 0;
                    countMap.put(nodeRanking.getNodeId(), ++count);
                });
                countMap.forEach(( nodeId, count ) -> {
                    if (count > 1) {
                        logger.error("Node [{}] 节点数：{}", nodeId, count);
                    }
                });
                customNodeRankingMapper.insertOrUpdate(updateList);
            }
            logger.debug("insertOrUpdate---------------------------------->{}", System.currentTimeMillis() - startTime);

            nodeCacheService.updateNodePushCache(chainId, redisNode);
            //beginNumber++;
            logger.debug("NodeInfoSynJob---------------------------------->{}", System.currentTimeMillis() - startTime);
            //}


        } catch (Exception e) {
            logger.error("NodeAnalyseJob Exception:{}", e.getMessage());
            e.printStackTrace();
        }
        logger.debug("*** End the NodeAnalyseJob *** ");
    }


    private ChainInfo getChainInfo ( EthBlock ethBlock ) throws Exception {
        //节点id对应节点类型名称Map
        Map <String, String> nodeTypeMap = new HashMap <>();
        CandidateContract candidateContract = platon.getCandidateContract(chainId);
        String nodeInfo = candidateContract.GetCandidateList().send();
        List <String> candidateStrArr = JSON.parseArray(nodeInfo, String.class);
        List <CandidateDto> nList = new ArrayList <>();
        //候选人+被选人Map
        Map <String, CandidateDto> allMap = new HashMap <>();
        // 候选
        List <CandidateDto> nodes = JSON.parseArray(candidateStrArr.get(0), CandidateDto.class);
        if (null != nodes && nodes.size() > 0) {
            nodes.forEach(candidate -> {
                //根据节点不同类型将节点id-节点类型形式放入nodeTypeMap中
                nodeTypeMap.put("0x" + candidate.getCandidateId(), NodeTypeEnum.NOMINEES.name().toLowerCase());
                nList.add(candidate);
                allMap.put("0x" + candidate.getCandidateId(), candidate);
            });
        }


        // 备选
        List <CandidateDto> alternates = JSON.parseArray(candidateStrArr.get(1), CandidateDto.class);
        if (null != alternates && alternates.size() > 0) {
            alternates.forEach(candidate -> {
                //根据节点不同类型将节点id-节点类型形式放入nodeTypeMap中
                nodeTypeMap.put("0x" + candidate.getCandidateId(), NodeTypeEnum.CANDIDATES.name().toLowerCase());
                nList.add(candidate);
                allMap.put("0x" + candidate.getCandidateId(), candidate);
            });
        }

        if (null != alternates && alternates.size() > 0) {
            alternates.forEach(candidate -> {
                nodes.add(candidate);
            });
        }

        //当前轮验证人
        String verifiers = candidateContract.GetVerifiersList().send();
        List <CandidateDto> verifierList = JSON.parseArray(verifiers, CandidateDto.class);


        verifierList.forEach(candidateNode -> {
            //根据节点不同类型将节点id-节点类型形式放入nodeTypeMap中
            nodeTypeMap.put("0x" + candidateNode.getCandidateId(), NodeTypeEnum.VALIDATOR.name().toLowerCase());
            //判断验证人列表是否在当前的（候选+备选）池中，如果没有则将验证人的信息添加到候选列表最后
            CandidateDto node = allMap.get("0x" + candidateNode.getCandidateId());
            if (node == null) {
                candidateNode.setCandidateId("0x" + candidateNode.getCandidateId());
                nodes.add(candidateNode);
            }

        });

        nList.forEach(allNode -> {
            allNode.setCandidateId("0x" + allNode.getCandidateId());
            allMap.put(allNode.getCandidateId(), allNode);
        });

        ChainInfo chainInfo = new ChainInfo();
        chainInfo.setAllMap(allMap);
        chainInfo.setNodeTypeMap(nodeTypeMap);
        //查询有效票
        StringBuffer strBuffer = new StringBuffer();
        if (nodes.size() > 0) {
            nodes.forEach(node -> strBuffer.append(node.getCandidateId()).append(":"));
            String nodeIds = strBuffer.toString();
            nodeIds = nodeIds.substring(0, nodeIds.lastIndexOf(":"));
            TicketContract ticketContract = platon.getTicketContract(chainId);
            String res = ticketContract.GetCandidateTicketCount(nodeIds).send();
            Map <String, Integer> countMap = JSON.parseObject(res, Map.class);
            chainInfo.setCountMap(countMap);
            chainInfo.setNodes(nodes);
            return chainInfo;
        }
        return chainInfo;
    }


}
