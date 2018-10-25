package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.constant.MQConstant;
import com.platon.browser.common.dto.mq.Message;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PushReceiver {

    @RabbitListener(containerFactory = "rabbitListenerContainerFactory",
            bindings = @QueueBinding(
                    value = @Queue(value = MQConstant.PLATON_BROWSER_QUEUE_PUSH, durable = "true", autoDelete="true"),
                    exchange = @Exchange(value = MQConstant.PLATON_BROWSER_EXCHANGE, type = ExchangeTypes.TOPIC),
                    key = MQConstant.PLATON_BROWSER_BIND_KEY)
    )
    public void receive(String msg) {
        Message message = JSON.parseObject(msg,Message.class);
        System.out.println("[PushReceiver] 接收到消息: " + msg);
    }
}
