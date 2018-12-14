package com.platon.browser.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange(exchangeName);
    }

    @Bean
    public Queue platonQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding(FanoutExchange fanout, Queue platonQueue) {
        return BindingBuilder.bind(platonQueue).to(fanout);
    }

}
