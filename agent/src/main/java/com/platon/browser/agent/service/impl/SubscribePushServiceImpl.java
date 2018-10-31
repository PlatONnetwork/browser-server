package com.platon.browser.agent.service.impl;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.constant.MQConstant;
import com.platon.browser.common.dto.mq.Message;
import com.platon.browser.common.enums.MqMessageTypeEnum;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.service.SubscribePushService;
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

@Service
public class SubscribePushServiceImpl implements SubscribePushService {

    private final Logger logger = LoggerFactory.getLogger(SubscribePushServiceImpl.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    @RabbitListener(containerFactory = "rabbitListenerContainerFactory",
            bindings = @QueueBinding(
                    value = @Queue(value = MQConstant.PLATON_BROWSER_QUEUE_PUSH, durable = "true", autoDelete="true"),
                    exchange = @Exchange(value = MQConstant.PLATON_BROWSER_EXCHANGE, type = ExchangeTypes.TOPIC),
                    key = MQConstant.PLATON_BROWSER_BIND_KEY)
    )
    public void receive(String msg) {
        Message message = JSON.parseObject(msg,Message.class);
        BaseResp resp;
        switch (MqMessageTypeEnum.valueOf(message.getType().toUpperCase())){
            case NODE:
                logger.info("STOMP推送节点信息: {}",msg);
                NodeInfo node = JSON.parseObject(message.getStruct(),NodeInfo.class);
                resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),node);
                messagingTemplate.convertAndSend("/topic/node/new?cid="+message.getChainId(), resp);
                break;
            case BLOCK:
                logger.info("STOMP推送区块信息: {}",msg);
                BlockInfo block = JSON.parseObject(message.getStruct(),BlockInfo.class);
                resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),block);
                messagingTemplate.convertAndSend("/topic/block/new?cid="+message.getChainId(), resp);
                break;
            case PENDING:

                break;
            case TRANSACTION:
                logger.info("STOMP推送交易信息: {}",msg);
                TransactionInfo transaction = JSON.parseObject(message.getStruct(),TransactionInfo.class);
                resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),transaction);
                messagingTemplate.convertAndSend("/topic/transaction/new?cid="+message.getChainId(), resp);
                break;
        }
    }
}
