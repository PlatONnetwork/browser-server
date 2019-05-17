package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dao.mapper.VoteTxMapper;
import com.platon.browser.dto.NodeRespPage;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.app.transaction.TransactionDto;
import com.platon.browser.dto.ticket.TxInfo;
import com.platon.browser.dto.transaction.TransactionVoteReq;
import com.platon.browser.dto.transaction.VoteInfo;
import com.platon.browser.dto.transaction.VoteSummary;
import com.platon.browser.dto.transaction.VoteTransaction;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.app.AppTransactionListReq;
import com.platon.browser.req.transaction.TicketCountByTxHashReq;
import com.platon.browser.req.transaction.TransactionListReq;
import com.platon.browser.service.ApiService;
import com.platon.browser.service.NodeService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.contracts.TicketContract;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: dongqile
 * Date: 2019/3/19
 * Time: 11:42
 */
@Service
public class ApiServiceImpl implements ApiService {

    private final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);
    @Autowired
    private PlatonClient platon;
    @Autowired
    private VoteTxMapper txMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private NodeRankingMapper nodeRankingMapper;

    @Data
    static class TicketCount{
        public TicketCount(Integer count,long timestamp){
            this.count = count;
            this.timestamp = timestamp;
        }
        private Integer count;
        private long timestamp;
    }
    private final static Map<String,TicketCount> TICKET_COUNT_MAP = new ConcurrentHashMap<>();


    @Override
    public List<VoteSummary> getVoteSummary (List <String> addressList , String chainId ) {
        logger.debug("getVoteSummary begin");
        long beginTime = System.currentTimeMillis();
        List<VoteSummary> voteSummaryList = new ArrayList<>();
        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo(chainId).andTxTypeEqualTo(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code)
                .andFromIn(addressList);
        List<TransactionWithBLOBs> transactionList = transactionMapper.selectByExampleWithBLOBs(transactionExample);
        List<String> hashList = new ArrayList <>();
        transactionList.forEach(transaction -> {
            hashList.add(transaction.getHash());
            VoteSummary bean = new VoteSummary();
            bean.init(transaction);
            voteSummaryList.add(bean);
        });

        Map<String,BigDecimal> incomeMap = getIncome(chainId,hashList);

        Map<String,Integer> validVoteMap = getVailInfo(hashList,chainId);

        for(VoteSummary voteSummary : voteSummaryList){
            BigDecimal inCome = incomeMap.get(voteSummary.getHash());
            if(null == inCome) voteSummary.setEarnings(BigDecimal.ZERO.toString());
            else voteSummary.setEarnings(inCome.toString());
            Integer vaildSum = validVoteMap.get(voteSummary.getHash());
            voteSummary.setValidNum(String.valueOf(vaildSum));
        }

        voteSummaryList.forEach(voteSummary -> {
            Integer vaildSum = validVoteMap.get(voteSummary.getHash());
            String lock = new BigDecimal(vaildSum).multiply(voteSummary.getTicketPrice()).toString();
            voteSummary.setLocked(lock);
        });
        logger.debug("getVoteSummary Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        return  voteSummaryList;
    }

    @Override
    public RespPage<VoteTransaction> getVoteTransaction (TransactionVoteReq req ) {
        logger.debug("getVoteTransaction begin");
        long beginTime = System.currentTimeMillis();
        //查询交易信息
        Page page = PageHelper.startPage(req.getPageNo(),req.getPageSize());
        List<VoteTransaction> voteTransactions = new ArrayList <>();
        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo(req.getCid()).andTxTypeEqualTo(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code)
                .andFromIn(req.getWalletAddrs());
        List<TransactionWithBLOBs> transactionList = transactionMapper.selectByExampleWithBLOBs(transactionExample);
        List<String> hashList = new ArrayList <>();
        transactionList.forEach(e->{
            hashList.add(e.getHash());
            VoteTransaction bean = new VoteTransaction();
            bean.init(e);
            voteTransactions.add(bean);
        });

        Map<String,BigDecimal> incomeMap = getIncome(req.getCid(),hashList);

        Map<String,Integer> validVoteMap = getVailInfo(hashList, req.getCid());

        for(VoteTransaction voteTransaction : voteTransactions){
            BigDecimal inCome = incomeMap.get(voteTransaction.getTransactionHash());
            if(null == inCome) voteTransaction.setEarnings(BigDecimal.ZERO.toString());
            else voteTransaction.setEarnings(inCome.toString());
            Integer vaildSum = validVoteMap.get(voteTransaction.getTransactionHash());
            voteTransaction.setValidNum(String.valueOf(vaildSum));
        }

        voteTransactions.forEach(b -> {
            BigDecimal inCome = incomeMap.get(b.getTransactionHash());
            if(null == inCome) b.setEarnings(BigDecimal.ZERO.toString());
            else b.setEarnings(inCome.toString());
        });

        RespPage<VoteTransaction> returnData = new RespPage<>();
        returnData.init(page,voteTransactions);
        logger.debug("getVoteTransaction Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        return returnData;
    }

    @Override
    public Map <String, Integer> getCandidateTicketCount ( List <String> nodeIds,String chainId ) {
        logger.debug("getCandidateTicketCount begin");
        long beginTime = System.currentTimeMillis();
        Map<String,Integer> returnData = new HashMap<>();
        if(nodeIds.size() > 0) {

            long currentTime = System.currentTimeMillis();
            nodeIds.forEach(nodeId->{
                TicketCount tc = TICKET_COUNT_MAP.get(nodeId);
                long prevTime = 0;
                if(tc!=null) prevTime = tc.getTimestamp();
                boolean expired = (currentTime-prevTime)>=30*1000;
                if(expired){
                    TicketContract ticketContract = platon.getTicketContract(chainId);
                    // 查最新数据
                    try {
                        String countStr = ticketContract.GetCandidateTicketCount(nodeId).send();
                        Map<String,Integer> map = JSON.parseObject(countStr,Map.class);
                        if(map.size()==1){
                            String key = nodeId.replace("0x","");
                            Integer count = map.get(key);
                            tc = new TicketCount(count,currentTime);
                            TICKET_COUNT_MAP.put(nodeId,tc);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                returnData.put(nodeId,tc.getCount());
            });
        }
        logger.debug("getCandidateTicketCount Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        return returnData;
    }

    @Override
    public RespPage<VoteInfo> getTicketCountByTxHash (TicketCountByTxHashReq req) {
        logger.debug("getTicketCountByTxHash begin");
        long beginTime = System.currentTimeMillis();
        List<String> hashList = req.getHashList();
        String chainId = req.getCid();
        if(hashList.size() > 0){
            logger.debug("Time Consuming-begin: {}ms",System.currentTimeMillis()-beginTime);
            TransactionExample transactionExample = new TransactionExample();
            transactionExample.createCriteria().andChainIdEqualTo(chainId).andHashIn(hashList);

            List<TransactionWithBLOBs> transactionList = transactionMapper.selectByExampleWithBLOBs(transactionExample);
            List<VoteInfo> bean = new ArrayList <>();
            Map<String,Integer> validVoteMap = getVailInfo(hashList,chainId);
            List<String> blockHashList = new ArrayList <>();

            transactionList.forEach(transaction -> {
                VoteInfo date = new VoteInfo();
                date.init(transaction);
                date.setVailVoteCount(validVoteMap.get(transaction.getHash()));
                bean.add(date);
                blockHashList.add(transaction.getBlockHash());

            });




            Map<String,String> priceMap = new HashMap <>();

            if(blockHashList.size()>0){
                BlockExample blockExample = new BlockExample();
                blockExample.createCriteria().andChainIdEqualTo(chainId).andHashIn(blockHashList);
                List<Block> blocks = blockMapper.selectByExample(blockExample);
                blockHashList.forEach(blockNumber->{
                    blocks.forEach(block -> {
                        if(blockNumber.equals(block.getNumber())){}
                        priceMap.put(block.getHash(),block.getVotePrice());
                    });
                });
            }

            List<String> nodeIds = new ArrayList <>();
            bean.forEach(voteInfo ->  {
                nodeIds.add(voteInfo.getNodeId());
            });

            Map<String,String> nodeIdToName=nodeService.getNodeNameMap(chainId,new ArrayList<>(nodeIds));


            bean.forEach(voteInfo -> {
                String nodeName = nodeIdToName.get(voteInfo.getNodeId());
                if(null != nodeName){
                    voteInfo.setNodeName(nodeName);
                }else {
                    voteInfo.setNodeName(" ");
                }
                String price = priceMap.get(voteInfo.getBlockHash());
                if(null != price) {
                    voteInfo.setPrice(price);
                }else {
                    voteInfo.setPrice(BigDecimal.ZERO.toString());
                }

            });
            logger.debug("Time Consuming-middle: {}ms",System.currentTimeMillis()-beginTime);

            Map<String,BigDecimal> incomeMap = getIncome(chainId,hashList);

            bean.forEach(b -> {
                BigDecimal inCome = incomeMap.get(b.getHash());
                if(null == inCome) b.setIncome(BigDecimal.ZERO);
                else b.setIncome(inCome);
            });



            RespPage<VoteInfo> returnData = new RespPage <>();
            returnData.setTotalCount(bean.size());
            returnData.setTotalPages(bean.size()/req.getPageSize());
            returnData.setData(bean);
            return returnData;
        }
        logger.debug("getTicketCountByTxHash Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        return new RespPage <>();
    }

    private Map<String,BigDecimal> getIncome(String chainId,List<String> hashList){
        logger.debug("getIncome begin");
        long beginTime = System.currentTimeMillis();
        if (hashList.size() > 0){
            //根据hash分组计算收益
            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(chainId).andVoteHashIn(hashList);
            List<Block> blocks = blockMapper.selectByExample(blockExample);
            Map<String, List<Block>> groupMap = new HashMap <>();
            blocks.forEach(block -> {
                List<Block> group=groupMap.get(block.getVoteHash());
                if(group==null){
                    group=new ArrayList <>();
                    groupMap.put(block.getVoteHash(),group);
                }
                group.add(block);
            });

            //分组计算收益
            Map<String,BigDecimal> incomeMap = new HashMap <>();
            groupMap.forEach((txHash,group)->{
                BigDecimal txIncome = BigDecimal.ZERO;
                for (Block block:group){
                    txIncome=txIncome.add(new BigDecimal(block.getBlockReward()).multiply(BigDecimal.valueOf(1-block.getRewardRatio())));
                }
                incomeMap.put(txHash,txIncome);
            });

            logger.debug("getIncome Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
            return incomeMap;
        }


        return  new HashMap <>();

    }


    private Map<String,Integer> getVailInfo(List<String> hashList,String chainId){
        logger.debug("getVailInfo begin");
        long beginTime = System.currentTimeMillis();

        if (hashList.size() > 0) {
            //根据hash分析每笔交易有效票数
            Map <String, Integer> validVoteMap = new HashMap <>();

            StringBuffer stringBuffer = new StringBuffer();
            hashList.forEach(hash -> stringBuffer.append(hash).append(":"));
            String hashs = stringBuffer.toString();
            hashs=hashs.substring(0,hashs.lastIndexOf(":"));
            try {
                TicketContract ticketContract = platon.getTicketContract(chainId);
                String vaildTicketList = ticketContract.GetTicketCountByTxHash(hashs).send();
                validVoteMap = JSON.parseObject(vaildTicketList, Map.class);
            } catch (Exception e) {
                for (String a : hashList) {
                    validVoteMap.put(a, 0);
                }
                logger.error("get transaction voteNumber Exception !!!");
            }

            logger.debug("getVailInfo Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

            return validVoteMap;
        }
        return new HashMap <>();
    }



}
