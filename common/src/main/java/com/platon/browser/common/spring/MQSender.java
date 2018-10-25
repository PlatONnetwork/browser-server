package com.platon.browser.common.spring;

import com.platon.browser.common.constant.MQConstant;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息
     *
     */
    public void send(String msg) {
        amqpTemplate.convertAndSend(MQConstant.PLATON_BROWSER_EXCHANGE, MQConstant.PLATON_BROWSER_BIND_KEY, msg);
    }
}
