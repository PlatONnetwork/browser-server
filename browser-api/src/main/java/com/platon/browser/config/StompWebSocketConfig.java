package com.platon.browser.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * stomp websocket连接配置
 *  @file WebSocketConfig.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年12月19日
 */
@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 注册stomp的端点,暴露节点用于连接。
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/platon-websocket")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

	/**
     * 	配置消息代理
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	// 订阅Broker名称 /topic 代表发布广播，即群发 /queue 代表点对点，即发指定用户  基于内存的单机stomp
        config.enableSimpleBroker("/topic");
        // 全局使用的消息前缀（客户端订阅路径上会体现出来）
        config.setApplicationDestinationPrefixes("/app");

//        设置单独发送到某个user需要添加的前缀，用户订阅地址/user/topic/td1地址后会去掉/user，并加上用户名（需要springsecurity支持）等唯一标识组成新的目的地发送回去，
//        对于这个url来说 加上后缀之后走代理。发送时需要制定用户名:convertAndSendToUser或者sendtouser注解.
    }


    
    /**设置客户端线程组*/
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(8);
    }

  /**设置客户端线程组*/
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(8);
    }

}
