package com.platon.browser.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("platon.fanout");
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
