//package com.platon.browser.service.impl;
//
//import com.platon.browser.client.PlatonClient;
//import com.platon.browser.dao.entity.TransactionExample;
//import com.platon.browser.dao.entity.TransactionWithBLOBs;
//import com.platon.browser.dao.mapper.BlockMapper;
//import com.platon.browser.dao.mapper.TransactionMapper;
//import com.platon.browser.dto.transaction.VoteSummary;
//import com.platon.browser.enums.TransactionTypeEnum;
//import com.platon.browser.service.ApiService;
//import com.platon.browser.service.NodeService;
//import lombok.Data;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * User: dongqile
// * Date: 2019/3/19
// * Time: 11:42
// */
//@Service
//public class ApiServiceImpl implements ApiService {
//
//    private final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);
//    @Autowired
//    private PlatonClient platon;
//    @Autowired
//    private TransactionMapper transactionMapper;
//    @Autowired
//    private BlockMapper blockMapper;
//    @Autowired
//    private NodeService nodeService;
//
//
//    @Data
//    static class TicketCount{
//        public TicketCount(Integer count,long timestamp){
//            this.count = count;
//            this.timestamp = timestamp;
//        }
//        private Integer count;
//        private long timestamp;
//    }
//    private final static Map<String,TicketCount> TICKET_COUNT_MAP = new ConcurrentHashMap<>();
//
//
//    @Override
//    public List<VoteSummary> getVoteSummary (List <String> addressList , String chainId ) {
//        logger.debug("getVoteSummary begin");
//        long beginTime = System.currentTimeMillis();
//        List<VoteSummary> voteSummaryList = new ArrayList<>();
//        TransactionExample transactionExample = new TransactionExample();
//        transactionExample.createCriteria().andTxTypeEqualTo(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code)
//                .andFromIn(addressList);
//        List<TransactionWithBLOBs> transactionList = transactionMapper.selectByExampleWithBLOBs(transactionExample);
//        List<String> hashList = new ArrayList <>();
//        transactionList.forEach(transaction -> {
//            hashList.add(transaction.getHash());
//            VoteSummary bean = new VoteSummary();
//            bean.init(transaction);
//            voteSummaryList.add(bean);
//        });
//
//        Map<String,BigDecimal> incomeMap = getIncome(chainId,hashList);
//
//        Map<String,Integer> validVoteMap = getVailInfo(hashList,chainId);
//
//        for(VoteSummary voteSummary : voteSummaryList){
//            BigDecimal inCome = incomeMap.get(voteSummary.getHash());
//            if(null == inCome) voteSummary.setEarnings(BigDecimal.ZERO.toString());
//            else voteSummary.setEarnings(inCome.toString());
//            Integer vaildSum = validVoteMap.get(voteSummary.getHash());
//            voteSummary.setValidNum(String.valueOf(vaildSum));
//        }
//
//        voteSummaryList.forEach(voteSummary -> {
//            Integer vaildSum = validVoteMap.get(voteSummary.getHash());
//            String lock = new BigDecimal(vaildSum).multiply(voteSummary.getTicketPrice()).toString();
//            voteSummary.setLocked(lock);
//        });
//        logger.debug("getVoteSummary Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
//        return  voteSummaryList;
//    }
//
//    @Override
//    public Map <String, Integer> getCandidateTicketCount ( List <String> nodeIds, String chainId ) {
//        return null;
//    }
//
//
//    public Map<String,BigDecimal> getIncome(String chainId,List<String> hashList){
//        logger.debug("getIncome begin");
//        Map<String,BigDecimal> map = new HashMap<>();
//
//        return map;
//
//    }
//
//    @Override
//    public Map <String, Integer> getVailInfo ( List <String> hashList, String chainId ) {
//        return null;
//    }
//
//
//}
