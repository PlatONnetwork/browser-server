/*
package com.platon.browser.job;

import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.NodeRankingBean;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockKey;
import com.platon.browser.dao.mapper.BlockMapper;
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
import org.web3j.protocol.core.methods.response.PlatonBlock;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.platon.browser.utils.CacheTool.NODEID_TO_NAME;

*/
/**
 * User: dongqile
 * Date: 2019/1/22
 * Time: 11:55
 *//*

@Component
public class NodeAnalyseJob {
    private static Logger logger = LoggerFactory.getLogger(Web3DetectJob.class);
    @Autowired
    private BlockMapper blockMapper;
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
    private static final class NodeInfoFromChain {
        // <节点ID-节点对象>映射Map, 用于存放提名节点或候选节点对象映射信息
        Map<String, CandidateDto> nodeIdToNodeMap4NomineeOrCandidate = new HashMap <>();
        // <节点ID-节点类型>映射Map，用于存放所有节点的类型映射信息
        Map<String, String> nodeIdToNodeTypeMap = new HashMap <>();
        // 合并后的节点列表
        List<CandidateDto> mergedNodes = new ArrayList <>();
        // <节点ID-有效票数>映射Map
        Map<String, Integer> nodeIdToValidTicketCountMap = new HashMap <>();
    }

    */
/**
     * 分析节点数据
     *//*

    @Scheduled(cron = "0/1 * * * * ?")
    protected void analyseNode () {
        logger.debug("*** In the NodeAnalyseJob *** ");
        try {
            long startTime = System.currentTimeMillis();
            logger.debug("getBlockNumber---------------------------------->{}", System.currentTimeMillis() - startTime);
            // 从链上获取区块信息
            PlatonBlock ethBlock = platon.getWeb3j(chainId).platonGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
            // 从链上获取节点信息
            List<NodeRanking> nodeRankingsFromChain = getNodeRankingsFromChain(ethBlock);

            // 从数据库获取节点信息
            List<NodeRanking> nodeRankingsFromDB = getNodeRankingsFromDB();

            // 数据库节点信息与链上节点信息对比，的出插入和更新结果列表
            List<NodeRanking> insertOrUpdateData = getInsertOrUpdateData(nodeRankingsFromChain,nodeRankingsFromDB,ethBlock);

            //TODO:verifierList存在问题，目前错误解决办法，待底层链修复完毕后在进行修正
            int consensusCount = 0;

            Set <NodeRanking> redisNode = new HashSet <>();
            for (NodeRanking nodeRanking : insertOrUpdateData) {
                if (nodeRanking.getIsValid() == 1) {
                    consensusCount++;
                    NODEID_TO_NAME.put(nodeRanking.getNodeId(), nodeRanking.getName());
                    // 把验证节点放到redis缓存，让browser-api推送给websocket端
                    redisNode.add(nodeRanking);
                }
            }

            if (!insertOrUpdateData.isEmpty()) {
                customNodeRankingMapper.insertOrUpdate(insertOrUpdateData);
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


    */
/**
     * 从数据库获取NodeRanking数据列表
     * @return
     *//*

    private List <NodeRanking> getNodeRankingsFromDB(){
        // 从数据库查询有效节点信息
        NodeRankingExample condition = new NodeRankingExample();
        condition.createCriteria().andChainIdEqualTo(chainId).andIsValidEqualTo(1);
        List <NodeRanking> nodeRankingsFromDB = nodeRankingMapper.selectByExample(condition);
        // |-- <节点ID-节点名称>映射
        nodeRankingsFromDB.forEach(node->NODEID_TO_NAME.put(node.getNodeId(), node.getName()));
        return nodeRankingsFromDB;
    }

    */
/**
     * 从链获取节点信息，并构造NodeRanking数据列表
     * @return
     *//*

    private List<NodeRanking> getNodeRankingsFromChain(PlatonBlock ethBlock) throws Exception {

        NodeInfoFromChain nodeInfoFromChain = getNodeInfoFromChain(ethBlock);
        if (nodeInfoFromChain.getMergedNodes().size()==0) return Collections.EMPTY_LIST;
        List<CandidateDto> nodesFromChain = nodeInfoFromChain.mergedNodes;
        Map<String,String> nodeIdToNodeTypeMap = nodeInfoFromChain.nodeIdToNodeTypeMap;
        Map<String,Integer> nodeIdToValidTicketCountMap = nodeInfoFromChain.nodeIdToValidTicketCountMap;

        // 存储通过链上节点信息构造出来的NodeRanking信息
        List <NodeRanking> nodeRankingsFromChain = new ArrayList <>();

        // 从区块中查询手续费总和
        BlockKey key = new BlockKey();
        key.setChainId(chainId);
        key.setHash(ethBlock.getBlock().getHash());
        Block block = blockMapper.selectByPrimaryKey(key);
        BigDecimal actualTxCostSum = BigDecimal.ZERO;
        if (block != null) {
            actualTxCostSum = new BigDecimal(block.getActualTxCostSum());
        }

        int rankingIndex = 1; // 排序号
        for (CandidateDto nodeFromChain : nodesFromChain) {
            NodeRankingBean nodeRanking = new NodeRankingBean();
            nodeRanking.init(nodeFromChain);

            String nodeId = nodeRanking.getNodeId();

            //设置节点类型
            String nodeType = nodeIdToNodeTypeMap.get(nodeId);
            nodeRanking.setNodeType(nodeType!=null?nodeType:NodeTypeEnum.UNKNOWN.name().toLowerCase());

            // nodeRanking.init()中获取不到平均出块时间时，把平均出块时间设置为全局的(redis统计缓存中的平均出块时间)
            StatisticsCache statisticsCache = statisticCacheService.getStatisticsCache(chainId);
            nodeRanking.setAvgTime(statisticsCache.getAvgTime().doubleValue());

            BigDecimal rate = new BigDecimal(nodeRanking.getRewardRatio());
            nodeRanking.setChainId(chainId);
            nodeRanking.setJoinTime(new Date(ethBlock.getBlock().getTimestamp().longValue()));
            nodeRanking.setBlockReward(FilterTool.getBlockReward(ethBlock.getBlock().getNumber().toString()));
            */
/*
             * 统计当前块中：
             * profitAmount累计收益 = 区块奖励 * 分红比例 + 当前区块的手续费总和
             * RewardAmount分红收益 = 区块奖励 * （1-分红比例）
             *//*

            nodeRanking.setProfitAmount(new BigDecimal(FilterTool.getBlockReward(ethBlock.getBlock().getNumber().toString())).
                    multiply(rate).
                    add(actualTxCostSum).toString());

            nodeRanking.setRewardAmount(new BigDecimal(FilterTool.getBlockReward(ethBlock.getBlock().getNumber().toString())).multiply(BigDecimal.ONE.subtract(rate)).toString());
            nodeRanking.setRanking(rankingIndex);
            nodeRanking.setType(1);



            // 设置节点有效票数
            Integer count = nodeIdToValidTicketCountMap.get(nodeId);
            if (null == count) {
                nodeRanking.setCount(0L);
            } else {
                nodeRanking.setCount(count.longValue());
            }

            nodeRanking.setElectionStatus(0); // 此字段已废除，使用node_type代替
            nodeRanking.setIsValid(1);
            nodeRanking.setBeginNumber(ethBlock.getBlock().getNumber().longValue());
            nodeRankingsFromChain.add(nodeRanking);
            rankingIndex++;
        }
        return nodeRankingsFromChain;
    }


    private List<NodeRanking> getInsertOrUpdateData(List<NodeRanking> nodeRankingsFromChain,List<NodeRanking> nodeRankingsFromDB,PlatonBlock ethBlock) throws Exception {

        // 计算公钥（节点ID，不带0x）
        BigInteger publicKey = CalculatePublicKey.testBlock(ethBlock);

        // 构造插入列表
        List<NodeRanking> insertData = new ArrayList <>();
        // 链上节点信息构造出来的NodeRanking的<节点ID-节点信息>映射
        Map <String, NodeRanking> chainNodeIdToNodeRankingMap = new HashMap <>();
        nodeRankingsFromChain.forEach(nodeRanking -> {
            chainNodeIdToNodeRankingMap.put(nodeRanking.getNodeId(), nodeRanking);
            insertData.add(nodeRanking);
        });

        // 构造更新列表
        List<NodeRanking> updateData = new ArrayList <>();
        if (nodeRankingsFromDB.size()>0) {
            for (int index = 0; index < nodeRankingsFromDB.size(); index++) {
                NodeRanking nodeFromDB = nodeRankingsFromDB.get(index);
                NodeRanking nodeFromChain = chainNodeIdToNodeRankingMap.get(nodeFromDB.getNodeId());
                if (nodeFromChain!=null) {
                    // 链上查询回来的节点信息在数据库里有记录，则数据里的记录某些属性需要保留
                    nodeFromChain.setBlockCount(nodeFromDB.getBlockCount());
                    nodeFromChain.setJoinTime(nodeFromDB.getJoinTime());
                    nodeFromChain.setBeginNumber(nodeFromDB.getBeginNumber());
                    nodeFromChain.setId(nodeFromDB.getId());
                    nodeFromChain.setAvgTime(nodeFromDB.getAvgTime());
                    if (publicKey.equals(new BigInteger(nodeFromChain.getNodeId().replace("0x", ""), 16))) {
                        // 如果当前块的出块节点ID与当前链上节点ID一样则，当前链上节点的出块数加一，相应奖励也增加
                        nodeFromChain.setBlockCount(nodeFromChain.getBlockCount() + 1);
                        nodeFromChain.setProfitAmount(new BigDecimal(nodeFromChain.getProfitAmount()).add(new BigDecimal(nodeFromDB.getProfitAmount())).toString());
                        nodeFromChain.setRewardAmount(new BigDecimal(nodeFromChain.getRewardAmount()).add(new BigDecimal(nodeFromDB.getRewardAmount())).toString());
                        nodeFromChain.setBlockReward(new BigDecimal(nodeFromChain.getBlockReward()).add(new BigDecimal(nodeFromDB.getBlockReward())).toString());
                    } else {
                        // 如果当前块的出块节点不是当前链上节点，则奖励信息以此节点上一次数据库的数据为准
                        nodeFromChain.setProfitAmount(nodeFromDB.getProfitAmount());
                        nodeFromChain.setRewardAmount(nodeFromDB.getRewardAmount());
                        nodeFromChain.setBlockReward(nodeFromDB.getBlockReward());
                    }
                } else {
                    // 链上没有，但数据库里有的记录需要设置为无效
                    nodeFromDB.setEndNumber(ethBlock.getBlock().getNumber().longValue());
                    nodeFromDB.setIsValid(0);
                    updateData.add(nodeFromDB);
                }
            }
        }
        List<NodeRanking> insertOrUpdateData = new ArrayList<>();
        insertOrUpdateData.addAll(insertData);
        insertOrUpdateData.addAll(updateData);
        return insertOrUpdateData;
    }

    */
/**
     * 从链上获取节点列表信息
     *//*

    private NodeInfoFromChain getNodeInfoFromChain ( PlatonBlock ethBlock ) throws Exception {

        // 调用候选合约获取提名和候选节点信息
        CandidateContract candidateContract = platon.getCandidateContract(chainId);
        String nodesStr = candidateContract.GetCandidateList().send();
        List <String> nodesStrArr = JSON.parseArray(nodesStr, String.class);

        // <节点ID-节点对象>映射Map, 用于存放提名节点或候选节点对象映射信息
        Map <String, CandidateDto> nodeIdToNodeMap4NomineeOrCandidate = new HashMap <>();
        // <节点ID-节点类型>映射Map，用于存放所有节点的类型映射信息
        Map <String, String> nodeIdToNodeTypeMap = new HashMap <>();

        // 取得提名节点列表
        List <CandidateDto> nominees = JSON.parseArray(nodesStrArr.get(0), CandidateDto.class);
        if (nominees!=null&&nominees.size()>0) {
            nominees.forEach(nominee -> {
                //根据节点不同类型将节点id-节点类型形式放入nodeTypeMap中
                String nodeId = nominee.getCandidateId().startsWith("0x")?nominee.getCandidateId():"0x"+nominee.getCandidateId();
                nodeIdToNodeMap4NomineeOrCandidate.put(nodeId, nominee);
                nodeIdToNodeTypeMap.put(nodeId, NodeTypeEnum.NOMINEES.name().toLowerCase());
            });
        }

        // candidates—候选节点
        List <CandidateDto> candidates = JSON.parseArray(nodesStrArr.get(1), CandidateDto.class);
        if (candidates!=null&&candidates.size()>0) {
            candidates.forEach(candidate -> {
                //根据节点不同类型将节点id-节点类型形式放入nodeTypeMap中
                String nodeId = candidate.getCandidateId().startsWith("0x")?candidate.getCandidateId():"0x"+candidate.getCandidateId();
                nodeIdToNodeMap4NomineeOrCandidate.put(nodeId, candidate);
                nodeIdToNodeTypeMap.put(nodeId, NodeTypeEnum.CANDIDATES.name().toLowerCase());
            });
        }

        // 用于存放：提名列表+候选列表+其他，即所有节点
        List<CandidateDto> mergedNodes = new ArrayList<>();
        mergedNodes.addAll(nominees);
        mergedNodes.addAll(candidates);

        // 取当前轮验证节点列表，validator-验证节点
        nodesStr = candidateContract.GetVerifiersList().send();
        List <CandidateDto> verifiers = JSON.parseArray(nodesStr, CandidateDto.class);
        verifiers.forEach(verifier -> {
            //根据节点不同类型将节点id-节点类型形式放入nodeTypeMap中
            String nodeId = verifier.getCandidateId().startsWith("0x")?verifier.getCandidateId():"0x"+verifier.getCandidateId();
            nodeIdToNodeTypeMap.put(nodeId, NodeTypeEnum.VALIDATOR.name().toLowerCase());
            //判断验证人列表是否在当前的（提名列表+候选列表）池中，如果没有则将验证人的信息添加到合并列表最后
            CandidateDto nomineeOrCandidate = nodeIdToNodeMap4NomineeOrCandidate.get(nodeId);
            if (nomineeOrCandidate == null) {
                nomineeOrCandidate.setCandidateId(nodeId);
                mergedNodes.add(nomineeOrCandidate);
            }
        });


        NodeInfoFromChain nodeInfoFromChain = new NodeInfoFromChain();
        nodeInfoFromChain.setNodeIdToNodeMap4NomineeOrCandidate(nodeIdToNodeMap4NomineeOrCandidate);
        nodeInfoFromChain.setNodeIdToNodeTypeMap(nodeIdToNodeTypeMap);

        if (mergedNodes.size() > 0) {
            nodeInfoFromChain.setMergedNodes(mergedNodes);
            // 获取每个节点ID对应的有效票数映射
            StringBuffer sb = new StringBuffer();
            mergedNodes.forEach(node -> sb.append(node.getCandidateId()).append(":"));
            String nodeIds = sb.toString();
            nodeIds = nodeIds.substring(0, nodeIds.lastIndexOf(":"));
            TicketContract ticketContract = platon.getTicketContract(chainId);
            String res = ticketContract.GetCandidateTicketCount(nodeIds).send();
            Map<String, Integer> countMap = JSON.parseObject(res, Map.class);
            Map<String, Integer> nodeIdToValidTicketCountMap = new HashMap<>();
            countMap.forEach((nodeId,count)->nodeIdToValidTicketCountMap.put(nodeId.startsWith("0x")?nodeId:"0x"+nodeId,count));
            nodeInfoFromChain.setNodeIdToValidTicketCountMap(nodeIdToValidTicketCountMap);
            return nodeInfoFromChain;
        }
        return nodeInfoFromChain;
    }


}
*/
