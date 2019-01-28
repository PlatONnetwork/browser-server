//package com.platon.browser.filter;
//
//import com.alibaba.fastjson.JSON;
//import com.platon.browser.bean.NodeRankingBean;
//import com.platon.browser.client.PlatonClient;
//import com.platon.browser.common.dto.agent.CandidateDto;
//import com.platon.browser.dao.entity.NodeRanking;
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
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
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
//            AtomicInteger rank = new AtomicInteger(0);
//            List<CandidateDto> candidates = JSON.parseArray(nodeInfo, CandidateDto.class);
//            candidates.forEach(candidate->{
//                NodeRankingBean nodeRanking = new NodeRankingBean();
//                nodeRanking.init(candidate);
//                BigDecimal rate = new BigDecimal(nodeRanking.getRewardRatio());
//                nodeRanking.setChainId(platon.getChainId());
//                nodeRanking.setBeginNumber(param.ethBlock.getBlock().getNumber().longValue());
//                nodeRanking.setJoinTime(new Date(param.ethBlock.getBlock().getTimestamp().longValue()));
//                nodeRanking.setBlockReward(FilterTool.getBlockReward(param.ethBlock.getBlock().getNumber().toString()));
//                nodeRanking.setProfitAmount(new BigDecimal(FilterTool.getBlockReward(param.ethBlock.getBlock().getNumber().toString())).multiply(rate).toString());
//                nodeRanking.setRewardAmount(new BigDecimal(FilterTool.getBlockReward(param.ethBlock.getBlock().getNumber().toString())).multiply(BigDecimal.ONE.subtract(rate)).toString());
//
//                int ranking = rank.incrementAndGet();
//                nodeRanking.setRanking(ranking);
//                nodeRanking.setType(1);
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
//                nodeRanking.setElectionStatus(electionStatus);
//                nodeRanking.setIsValid(1);
//
//                returnData.add(nodeRanking);
//            });
//        }
//        return returnData;
//    }
//}