package com.platon.browser.agent.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platon.browser.common.constant.ConfigConst;
import com.platon.browser.common.dto.agent.*;
import com.platon.browser.common.dto.mq.Message;
import com.platon.browser.common.enums.MqMessageTypeEnum;
import com.platon.browser.common.enums.StatisticsEnum;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.datatypes.Int;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * User: dongqile
 * Date: 2018/10/30
 * Time: 18:28
 */
@Component
public class DBStorageService {
    private final Logger logger = LoggerFactory.getLogger(DBStorageService.class);

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private PendingTxMapper pendingTxMapper;

    @Autowired
    private NodeRankingMapper nodeRankingMapper;

    @Autowired
    private NodeMapper nodeMapper;

    @Autowired
    private StatisticsMapper statisticsMapper;

    @RabbitListener(queues = "#{platonQueue.name}")
    public void receive ( String msg ) {
        logger.debug(msg);
        Message message = JSON.parseObject(msg, Message.class);
        String a = message.getChainId();
        if (!message.getChainId().equals(ConfigConst.getChainId())) {
            return;
        }
        switch (MqMessageTypeEnum.valueOf(message.getType().toUpperCase())) {
            case BLOCK:
                logger.debug("STOMP区块信息入库: {}", msg);
                BlockDto blockDto = JSON.parseObject(message.getStruct(), BlockDto.class);
                //构建dto结构，转存数据库结构
                //区块相关block
                Block block = bulidBlock(blockDto, message);
                try {
                    blockMapper.insertSelective(block);
                    logger.debug("block number :" + block.getNumber());
                    logger.debug("block data insert...");
                } catch (DuplicateKeyException e) {
                    logger.debug("block data repeat...", e.getMessage());
                    return;
                }

                //交易相关transaction
                if (blockDto.getTransaction().size() > 0) {
                    List <TransactionWithBLOBs> transactionList = buildTransaction(blockDto, message);
                    try {
                        transactionMapper.batchInsert(transactionList);
                        logger.debug("transaction data insert...");
                    } catch (DuplicateKeyException e) {
                        logger.debug("transaction data repeat...,update data", e.getMessage());
                        return;
                    }
                }
                break;

            case PENDING:
                logger.debug("STOMP挂起交易信息入库: {}", msg);
                //获取信息中pending交易列表
                List <PendingTransactionDto> list = JSON.parseArray(message.getStruct(), PendingTransactionDto.class);
                List <PendingTx> pendingTxes = buidPendingTx(list, message);
                for (PendingTx pendingTx : pendingTxes) {
                    try {
                        pendingTxMapper.insert(pendingTx);
                        logger.debug("pendingtransaction data insert...");
                    } catch (DuplicateKeyException e) {
                        logger.debug("pendingtransaction data repeat...", e.getMessage(), pendingTx.getHash());
                        continue;
                    }
                }
                break;

            case NODE:
                logger.debug("STOMP节点信息入库: {}", msg);
                //获取队列中的节点数据
                List <CandidateDto> candidateDtoList = JSON.parseArray(message.getStruct(), CandidateDto.class);
                List <NodeRanking> nodeRankings = buildNodeInfo(candidateDtoList, message);
                //delete noderangking table
                NodeRankingExample nodeRankingExample = new NodeRankingExample();
                nodeRankingExample.createCriteria().andChainIdEqualTo(message.getChainId());
                nodeRankingMapper.deleteByExample(nodeRankingExample);
                //insert noderangking table
                for(NodeRanking nodeRanking : nodeRankings){
                    try {
                        nodeRankingMapper.insert(nodeRanking);
                        logger.debug("nodeRanking data insert...");
                    }catch (DuplicateKeyException e){
                        logger.debug("nodeRanking data repeat...", e.getMessage(), nodeRanking.getId());
                        continue;
                    }
                }

                List <Node> nodeList = new ArrayList <>();
                nodeRankings.forEach(nodeRanking -> {
                    Node node = new Node();
                    node.setId(nodeRanking.getId());
                    node.setIp(nodeRanking.getIp());
                    node.setPort(nodeRanking.getPort());
                    node.setChainId(nodeRanking.getChainId());
                    node.setNodeStatus(ping(nodeRanking.getIp()));
                    node.setAddress(nodeRanking.getAddress());
                    node.setRewardRatio(nodeRanking.getRewardRatio());
                    nodeList.add(node);
                });

                for (Node node : nodeList) {
                    try {
                        nodeMapper.insert(node);
                        statisticeInfoInsert(node, message);
                        logger.debug("node data insert...");
                    } catch (DuplicateKeyException e) {
                        logger.debug("node data repeat...", e.getMessage(), node.getId());
                        continue;
                    }
                }
                break;
        }
    }

    private Block bulidBlock ( BlockDto blockDto, Message message ) {
        Block block = new Block();
        try {
            BeanUtils.copyProperties(blockDto, block);
            block.setNumber(Long.valueOf(blockDto.getNumber()));
            block.setTimestamp(new Date(blockDto.getTimestamp()));
            block.setSize((int) blockDto.getSize());//考虑转换格式类型，高精度转低精度可能会导致数据失准
            block.setChainId(message.getChainId());
            block.setEnergonAverage(blockDto.getEnergonAverage().toString());
            block.setEnergonLimit(blockDto.getEnergonLimit().toString());
            block.setEnergonUsed(blockDto.getEnergonUsed().toString());
            block.setTransactionNumber(blockDto.getTransactionNumber());
            block.setCreateTime(new Date());
            block.setUpdateTime(new Date());
            /*block.setBlockReward("");*/
            List <TransactionDto> transactionDtos = blockDto.getTransaction();
            if (transactionDtos.size() > 0 && null != transactionDtos) {
                BigInteger sum = new BigInteger("0");
                for (TransactionDto transactionDto : transactionDtos) {
                    sum = sum.add(transactionDto.getActualTxCoast());
                }
                block.setActualTxCostSum(sum.toString());
            } else {
                block.setActualTxCostSum("");
            }
        } catch (Exception e) {
            logger.error("数据转化异常", e.getMessage());
        }
        return block;
    }

    private List <TransactionWithBLOBs> buildTransaction ( BlockDto blockDto, Message message ) {
        List <TransactionWithBLOBs> transactionList = new ArrayList <>();
        List <TransactionDto> transactionDtos = blockDto.getTransaction();
        for (TransactionDto transactionDto : transactionDtos) {
            TransactionWithBLOBs transaction = new TransactionWithBLOBs();
            BeanUtils.copyProperties(transactionDto, transaction);
            transaction.setActualTxCost(transactionDto.getActualTxCoast().toString());
            transaction.setBlockNumber(Long.valueOf(transactionDto.getBlockNumber().toString()));
            transaction.setChainId(message.getChainId());
            transaction.setCreateTime(new Date());
            transaction.setUpdateTime(new Date());
            transaction.setEnergonLimit(transactionDto.getEnergonLimit().toString());
            transaction.setEnergonPrice(transactionDto.getEnergonPrice().toString());
            transaction.setEnergonUsed(transactionDto.getEnergonUsed().toString());
            transaction.setTxReceiptStatus(Integer.parseInt(transactionDto.getTxReceiptStatus().substring(2), 16));
            transaction.setTransactionIndex(transactionDto.getTransactionIndex().intValue());
            transaction.setReceiveType(transactionDto.getReceiveType());
            transaction.setInput(transactionDto.getInput() != null ? transactionDto.getInput() : "0x");
            transactionList.add(transaction);
        }
        return transactionList;
    }

    private List <PendingTx> buidPendingTx ( List <PendingTransactionDto> list, Message message ) {
        List <PendingTx> pendingTxes = new ArrayList <>();
        if (list.size() > 0) {
            for (PendingTransactionDto pendingTransactionDto : list) {
                PendingTx pendingTx = new PendingTx();
                BeanUtils.copyProperties(pendingTransactionDto, pendingTx);
                pendingTx.setUpdateTime(new Date());
                pendingTx.setCreateTime(new Date());
                pendingTx.setEnergonLimit(pendingTransactionDto.getEnergonLimit().toString());
                pendingTx.setEnergonPrice(pendingTransactionDto.getEnergonPrice().toString());
                pendingTx.setReceiveType(pendingTransactionDto.getReceiveType());
                pendingTx.setEnergonUsed("pending");
                pendingTx.setTimestamp(new Date(pendingTransactionDto.getTimestamp()));
                pendingTx.setChainId(message.getChainId());
                pendingTxes.add(pendingTx);
            }
        }
        return pendingTxes;
    }

    private List <NodeRanking> buildNodeInfo ( List <CandidateDto> list, Message message ) {
        List <NodeRanking> nodeList = new ArrayList <>();
        for (int i = 0; i < list.size(); i++) {
            NodeRanking nodeRanking = new NodeRanking();
            nodeRanking.setChainId(message.getChainId());
            nodeRanking.setIp(list.get(i).getHost());
            nodeRanking.setId(list.get(i).getCandidateId());
            nodeRanking.setPort(Integer.valueOf(list.get(i).getPort()));
            nodeRanking.setAddress(list.get(i).getOwner());
            nodeRanking.setUpdateTime(new Date());
            nodeRanking.setCreateTime(new Date());
            CandidateDetailDto candidateDetailDto = null;
            if (list.get(i).getExtra().length() > 0 && null != list.get(i).getExtra()) {
                candidateDetailDto = buildDetail(list.get(i).getExtra());
            }
            nodeRanking.setName(candidateDetailDto.getNodeName());
            nodeRanking.setDeposit(candidateDetailDto.getNodeDiscription());
            nodeRanking.setJoinTime(new Date(candidateDetailDto.getTime()));
            nodeRanking.setOrgName(candidateDetailDto.getNodeDepartment());
            nodeRanking.setOrgWebsite(candidateDetailDto.getOfficialWebsite());
            nodeRanking.setRewardRatio((double) list.get(i).getFee() / 10000);
            nodeRanking.setUrl(candidateDetailDto.getNodePortrait() != null ?  candidateDetailDto.getNodePortrait() : "test");
            nodeRanking.setRanking(i + 1);
            nodeRanking.setType(1);
            nodeList.add(nodeRanking);
        }

        return nodeList;
    }

    private CandidateDetailDto buildDetail ( String extra ) {
        String data = hexStringToString(extra.substring(2, extra.length()));
        CandidateDetailDto candidateDetailDto = JSONObject.parseObject(data, CandidateDetailDto.class);
        return candidateDetailDto;
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

    private void statisticeInfoInsert ( Node node, Message message ) {
        try {
            Statistics blockReawrd = new Statistics();
            blockReawrd.setChainId(message.getChainId());
            blockReawrd.setCreateTime(new Date());
            blockReawrd.setUpdateTime(new Date());
            blockReawrd.setNodeId(node.getId());
            blockReawrd.setValue("");
            blockReawrd.setType(StatisticsEnum.block_reward.name());
            statisticsMapper.insert(blockReawrd);
            Statistics blockCount = new Statistics();
            BeanUtils.copyProperties(blockReawrd, blockCount);
            blockCount.setType(StatisticsEnum.block_count.name());
            statisticsMapper.insert(blockCount);
            Statistics rewardAmount = new Statistics();
            BeanUtils.copyProperties(blockReawrd, rewardAmount);
            rewardAmount.setType(StatisticsEnum.reward_amount.name());
            statisticsMapper.insert(rewardAmount);
            Statistics profitAmount = new Statistics();
            BeanUtils.copyProperties(blockReawrd, profitAmount);
            profitAmount.setType(StatisticsEnum.profit_amount.name());
            statisticsMapper.insert(profitAmount);
        } catch (DuplicateKeyException e) {
            logger.error("insert nodeStatistice exception!...", e.getMessage());
        }
    }



    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}