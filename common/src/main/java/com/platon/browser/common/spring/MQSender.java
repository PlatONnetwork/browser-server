package com.platon.browser.common.spring;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.dto.mq.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQSender {

    private final static Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanout;

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
        if(StringUtils.isBlank(chainId)){
            logger.info("CHAIN ID : {}", chainId);
        }
        Message message = new Message();
        message.setChainId(chainId);
        message.setType(type);
        message.setStruct(JSON.toJSONString(struct));
        send(JSON.toJSONString(message));
    }
}
