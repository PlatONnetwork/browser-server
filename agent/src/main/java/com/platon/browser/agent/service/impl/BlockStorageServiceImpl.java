package com.platon.browser.agent.service.impl;

import com.alibaba.fastjson.JSON;
import com.platon.browser.agent.service.BlockStorageService;
import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.constant.MQConstant;
import com.platon.browser.common.dto.agent.BlockDto;
import com.platon.browser.common.dto.agent.TransactionDto;
import com.platon.browser.common.dto.mq.Message;
import com.platon.browser.common.enums.MqMessageTypeEnum;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/30
 * Time: 18:28
 */
@Service
public class BlockStorageServiceImpl implements BlockStorageService {
    private final Logger logger = LoggerFactory.getLogger(BlockStorageServiceImpl.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    @RabbitListener(containerFactory = "rabbitListenerContainerFactory",
            bindings = @QueueBinding(
                    value = @Queue(value = MQConstant.PLATON_BROWSER_QUEUE_PERSIST, durable = "true", autoDelete="true"),
                    exchange = @Exchange(value = MQConstant.PLATON_BROWSER_EXCHANGE, type = ExchangeTypes.TOPIC),
                    key = MQConstant.PLATON_BROWSER_BIND_KEY)
    )
    public void receive ( String msg ) {
        Message message = JSON.parseObject(msg,Message.class);
        switch (MqMessageTypeEnum.valueOf(message.getType().toUpperCase())){
            case BLOCK:
                logger.info("STOMP区块信息入库: {}",msg);
                BlockDto blockDto = JSON.parseObject(message.getStruct(),BlockDto.class);
                //构建dto结构，转存数据库结构
                //区块相关block
                Block block = bulidBlock(blockDto,message);
                blockMapper.insert(block);

                //交易相关transaction
                if(blockDto.getTransaction().size() > 0){
                    List<TransactionWithBLOBs> transactionList = buildTransaction(blockDto,message);
                    transactionMapper.batchInsert(transactionList);
                }else {
                    logger.info("STOMP区块信息中交易信息为空: {}",msg);
                }
                break;

            case PENDING:
                break;

        }
    }

    private Block bulidBlock(BlockDto blockDto,Message message){
        Block block = new Block();
        block.setNumber(Long.valueOf(blockDto.getNumber()));
        block.setTimestamp(new Date(blockDto.getTimestamp()));
        block.setSize((int)blockDto.getSize());//考虑转换格式类型，高精度转低精度可能会导致数据失准
        block.setMiner(blockDto.getMiner());
        block.setExtraData(blockDto.getExtraData());
        block.setNonce(blockDto.getNonce());
        block.setParentHash(blockDto.getParentHash());
        block.setChainId(message.getChainId());
        block.setHash(blockDto.getHash());
        block.setBlockReward(blockDto.getBlockReward());
        block.setEnergonAverage(blockDto.getEnergonAverage().toString());
        block.setEnergonLimit(blockDto.getEnergonLimit().toString());
        block.setEnergonUsed(blockDto.getEnergonUsed().toString());
        block.setCreateTime(new Date());
        block.setUpdateTime(new Date());
        return block;
    }

    private List<TransactionWithBLOBs> buildTransaction(BlockDto blockDto,Message message){
        List<TransactionWithBLOBs> transactionList = new ArrayList <>();
        List<TransactionDto> transactionDtos = blockDto.getTransaction();
        for(TransactionDto transactionDto : transactionDtos){
            TransactionWithBLOBs transaction = new TransactionWithBLOBs();
            transaction.setBlockHash(transactionDto.getBlockHash());
            transaction.setActualTxCost(transactionDto.getActualTxCoast().toString());
            transaction.setBlockNumber(Long.valueOf(transactionDto.getBlockNumber().toString()));
            transaction.setChainId(message.getChainId());
            transaction.setCreateTime(new Date());
            transaction.setUpdateTime(new Date());
            transaction.setEnergonLimit(transactionDto.getEnergonLimit().toString());
            transaction.setEnergonPrice(transactionDto.getEnergonPrice().toString());
            transaction.setEnergonUsed(transactionDto.getEnergonUsed().toString());
            transaction.setFrom(transactionDto.getFrom());
            transaction.setTo(transactionDto.getTo());
            transaction.setHash(transactionDto.getHash());
            transaction.setNonce(transactionDto.getNonce());
            transaction.setTimestamp(transaction.getTimestamp());
            transaction.setTransactionIndex(transactionDto.getTransactionIndex().intValue());
            transaction.setTxReceiptStatus(transaction.getTxReceiptStatus());
            transaction.setTxType(transactionDto.getTxType());
            transaction.setValue(transactionDto.getValue());
            transactionList.add(transaction);
        }
        return transactionList;
    }
}