package com.platon.browser.common.spring;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.dto.mq.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MQSender {

    private final static Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanout;

    @PostConstruct
    private void init(){
        rabbitTemplate.setReturnCallback(((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationIdString();
            logger.info("消息：{}发送失败，应答码：{},原因：{}，交换机：{}, 路由器：{}",
                    correlationId,replyCode,replyText,exchange,routingKey);
        }));
    }

    /**
     * 发送消息
     * @param msg
     */
    public void send(String msg) {
        rabbitTemplate.convertAndSend(fanout.getName(), "", msg);
    }

    /**
     *
     * @param chainId 链ID
     * @param type 消息类型
     * @param struct 消息具体结构 json字符串
     */
    public void send(String chainId,String type, Object struct) {
        Message message = new Message();
        message.setChainId(chainId);
        message.setType(type);
        message.setStruct(JSON.toJSONString(struct));
        send(JSON.toJSONString(message));
    }
}
