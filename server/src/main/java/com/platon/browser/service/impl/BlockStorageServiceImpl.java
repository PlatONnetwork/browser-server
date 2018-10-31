package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.constant.MQConstant;
import com.platon.browser.common.dto.agent.BlockDto;
import com.platon.browser.common.dto.mq.Message;
import com.platon.browser.common.enums.MqMessageTypeEnum;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.BlockStorageService;
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

import java.util.Date;

/**
 * User: dongqile
 * Date: 2018/10/30
 * Time: 18:28
 */
@Service
public class BlockStorageServiceImpl implements BlockStorageService{
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
        BaseResp resp;
        switch (MqMessageTypeEnum.valueOf(message.getType().toUpperCase())){
            case BLOCK:
                logger.info("STOMP区块信息入库: {}",msg);
                BlockDto blockDto = JSON.parseObject(message.getStruct(),BlockDto.class);
                resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),blockDto);
                messagingTemplate.convertAndSend("/topic/block/new?cid="+message.getChainId(), resp);
                //构建dto结构，转存数据库结构
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
/*                block.setEnergonAverage(blockDto.getEnergonAverage());//考虑下换成BigInteger
                block.setEnergonLimit(blockDto.getEnergonLimit());//考虑下换成BigInteger
                block.setEnergonUsed(blockDto.getEnergonUsed());//考虑下换成BigInteger*/
                break;

            case PENDING:
                break;

        }
    }
}