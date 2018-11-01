package com.platon.browser.common.spring;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.dto.mq.Message;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQSender {

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
        Message message = new Message();
        message.setChainId(chainId);
        message.setType(type);
        message.setStruct(JSON.toJSONString(struct));
        send(JSON.toJSONString(message));
    }
}
