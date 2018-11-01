package com.platon.browser.message;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.dto.mq.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SubscribePushService {

    private final Logger logger = LoggerFactory.getLogger(SubscribePushService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "#{platonQueue.name}")
    public void receive(String msg) {
        //Message message = JSON.parseObject(msg,Message.class);
        logger.info(msg);
        /*BaseResp resp;
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
        }*/
    }
}
