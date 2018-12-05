package com.platon.browser.agent.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platon.browser.common.constant.ConfigConst;
import com.platon.browser.common.dto.agent.*;
import com.platon.browser.common.dto.mq.Message;
import com.platon.browser.common.enums.MqMessageTypeEnum;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @RabbitListener(queues = "#{platonQueue.name}")
    public void receive ( String msg ) {
        logger.debug(msg);
        Message message = JSON.parseObject(msg, Message.class);
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
                List <Node> nodeList = buildNodeInfo(candidateDtoList, message);
                //更新排名表
                List<NodeRanking> nodeRankingList = new ArrayList <>();
                nodeRankingMapper.deleteByExample(new NodeRankingExample());
                nodeList.forEach(node -> {
                    NodeRanking nodeRanking = new NodeRanking();
                    nodeRanking.setNodeId(node.getId());
                    nodeRankingList.add(nodeRanking);
                });
                nodeRankingMapper.batchInsert(nodeRankingList);

                //获取数据库节点存量列表
                List<Node> nodes = nodeMapper.selectByExample(new NodeExample());
                //TODO:


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

    private List <Node> buildNodeInfo ( List <CandidateDto> list, Message message ) {
        List <Node> nodeList = new ArrayList <>();
        list.forEach(candidateDto -> {
            Node node = new Node();
            node.setChainId(message.getChainId());
            node.setIp(candidateDto.getHost());
            node.setId(candidateDto.getCandidateId());
            node.setPort(Integer.valueOf(candidateDto.getPort()));
            node.setAddress(candidateDto.getOwner());
            node.setUpdateTime(new Date());
            node.setCreateTime(new Date());
            CandidateDetailDto candidateDetailDto = null;
            if (candidateDto.getExtra().length() > 0 && null != candidateDto.getExtra()) {
                candidateDetailDto = buildDetail(candidateDto.getExtra());
            }
            node.setName(candidateDetailDto.getNodeName());
            node.setDeposit(candidateDetailDto.getNodeDiscription());
            node.setJoinTime(new Date(candidateDetailDto.getTime()));
            node.setOrgName(candidateDetailDto.getNodeDepartment());
            node.setOrgWebsite(candidateDetailDto.getOfficialWebsite());
            node.setRewardRatio((double) candidateDto.getFee() / 10000);
            node.setNodeStatus(ping(candidateDto.getHost()));
        });
        return nodeList;
    }

    private CandidateDetailDto buildDetail ( String extra ) {
        CandidateDetailDto candidateDetailDto = JSONObject.parseObject(extra, CandidateDetailDto.class);
        return candidateDetailDto;
    }

    private int ping ( String ip ) {
        InetAddress address = null;
        int timeOut = 3000;
        try {
            address = InetAddress.getByAddress(ip.getBytes());
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

}