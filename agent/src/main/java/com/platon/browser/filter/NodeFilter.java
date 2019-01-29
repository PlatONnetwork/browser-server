//package com.platon.browser.filter;
//
//import com.alibaba.fastjson.JSON;
//import com.platon.browser.bean.NodeRankingBean;
//import com.platon.browser.client.PlatonClient;
//import com.platon.browser.common.dto.agent.CandidateDto;
//import com.platon.browser.common.util.CalculatePublicKey;
//import com.platon.browser.dao.entity.NodeRanking;
//import com.platon.browser.dao.entity.NodeRankingExample;
//import com.platon.browser.dao.mapper.NodeRankingMapper;
//import com.platon.browser.thread.AnalyseThread;
//import com.platon.browser.utils.FilterTool;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.web3j.platon.contracts.CandidateContract;
//
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * User: dongqile
// * Date: 2019/1/7
// * Time: 14:27
// */
//@Component
//public class NodeFilter {
//
//    private static Logger logger = LoggerFactory.getLogger(NodeFilter.class);
//
//    @Autowired
//    private PlatonClient platon;
//    @Value("${platon.redis.key.block}")
//    private String blockCacheKeyTemplate;
//    @Autowired
//    private NodeRankingMapper nodeRankingMapper;
//
//    public final static Map<String,AtomicLong> BLOCK_COUNTS = new HashMap<>();
//
//    public List<NodeRanking> analyse(AnalyseThread.AnalyseParam param) {
//
//        List<NodeRanking> returnData = new ArrayList<>();
//        if (param.ethBlock!=null) {
//            CandidateContract candidateContract = platon.getCandidateContract();
//            String nodeInfo = null;
//            try {
//                nodeInfo = candidateContract.CandidateList(param.ethBlock.getBlock().getNumber()).send();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            String publicKey = CalculatePublicKey.getPublicKey(param.ethBlock);
//            AtomicLong count = BLOCK_COUNTS.get(publicKey);
//            if (count == null) {
//                count = new AtomicLong(0);
//                BLOCK_COUNTS.put(publicKey,count);
//            }
//            count.incrementAndGet();
//
//            AtomicInteger rank = new AtomicInteger(0);
//            List<CandidateDto> candidates = JSON.parseArray(nodeInfo, CandidateDto.class);
//            candidates.forEach(candidate->{
//                NodeRankingBean bean = new NodeRankingBean();
//                bean.init(candidate);
//                BigDecimal rate = new BigDecimal(bean.getRewardRatio());
//                bean.setChainId(platon.getChainId());
//                bean.setBeginNumber(param.ethBlock.getBlock().getNumber().longValue());
//                bean.setJoinTime(new Date(param.ethBlock.getBlock().getTimestamp().longValue()));
//                bean.setBlockReward(FilterTool.getBlockReward(param.ethBlock.getBlock().getNumber().toString()));
//                bean.setProfitAmount(new BigDecimal(FilterTool.getBlockReward(param.ethBlock.getBlock().getNumber().toString())).multiply(rate).toString());
//                bean.setRewardAmount(new BigDecimal(FilterTool.getBlockReward(param.ethBlock.getBlock().getNumber().toString())).multiply(BigDecimal.ONE.subtract(rate)).toString());
//
//                if(publicKey.equals(bean.getNodeId())){
//                    bean.setBlockCount(1l);
//                }else {
//                    bean.setBlockCount(0l);
//                }
//
//                int ranking = rank.incrementAndGet();
//                bean.setRanking(ranking);
//                bean.setType(1);
//                // Set the node election status according to the ranking
//                // 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名
//                /**
//                 * The first 100：candidate nodes
//                 * After 100：alternative nodes
//                 * **/
//                int electionStatus = 1;
//                if (1 <= ranking && ranking < 25) electionStatus = 3;
//                if (26 <= ranking && ranking < 100) electionStatus = 1;
//                if (ranking >= 100) electionStatus = 4;
//                bean.setElectionStatus(electionStatus);
//                bean.setIsValid(1);
//
//                returnData.add(bean);
//            });
//        }
//        return returnData;
//    }
//
//
//    public List<NodeRanking> aggregateNodes(Map<Long,List<NodeRanking>> groups){
//        List<NodeRanking> aggregate = new ArrayList<>();
//        if(groups.keySet().size()>0){
//            AtomicLong maxNumber = new AtomicLong();
//            groups.keySet().forEach(number->{
//                if(number>maxNumber.get()) maxNumber.set(number);
//            });
//            // 取出块高最高的一组节点信息
//            List<NodeRanking> newGroup = groups.get(maxNumber.get());
//            Map<String,NodeRanking> idNodeMap1 = new HashMap<>();
//            newGroup.forEach(node -> idNodeMap1.put(node.getNodeId(),node));
//            // 所有非最高块的节点全部归为一组
//            List<NodeRanking> oldGroup = new ArrayList<>();
//            groups.remove(maxNumber.get());
//            groups.forEach((blockNumber,oldNodes)->oldGroup.addAll(oldNodes));
//            // 把非最高组信息聚合至最高组, 获得链上取回来的节点的聚合
//            aggregate.addAll(newGroup);
//            oldGroup.forEach(oldNode->{
//                NodeRanking newNode = idNodeMap1.get(oldNode.getNodeId());
//                if(newNode==null){
//                    oldNode.setIsValid(0);
//                    aggregate.add(oldNode);
//                }
//                if(newNode!=null) {
//                    newNode.setBlockCount(newNode.getBlockCount()+oldNode.getBlockCount());
//                }
//            });
//
//            Map<String,NodeRanking> chainNodeIdToNodeRankingMap = new HashMap<>();
//            aggregate.forEach(node->chainNodeIdToNodeRankingMap.put(node.getNodeId(),node));
//            // 查询出数据库中所有有效的节点
//            NodeRankingExample example = new NodeRankingExample();
//            example.createCriteria().andChainIdEqualTo(platon.getChainId()).andIsValidEqualTo(1);
//            List<NodeRanking> dbGroup = nodeRankingMapper.selectByExample(example);
//            Map<String,NodeRanking> dbNodeIdToNodeRankingMap = new HashMap<>();
//            dbGroup.forEach(node->dbNodeIdToNodeRankingMap.put(node.getNodeId(),node));
//
//            dbGroup.forEach(dbNode->{
//                NodeRanking chainNode = chainNodeIdToNodeRankingMap.get(dbNode.getNodeId());
//                if(chainNode!=null){
//                    // 库里有效属性保留
//                    AtomicLong count = BLOCK_COUNTS.get(chainNode.getNodeId());
//                    chainNode.setBlockCount(count==null?0l:count.get());
//                    chainNode.setJoinTime(dbNode.getJoinTime());
//                    chainNode.setBeginNumber(dbNode.getBeginNumber());
//                    chainNode.setId(dbNode.getId());
//                }else {
//                    dbNode.setEndNumber(maxNumber.get());
//                    dbNode.setIsValid(0);
//                    aggregate.add(dbNode);
//                }
//            });
//        }
//        return aggregate;
//    }
//}