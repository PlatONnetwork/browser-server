package com.platon.browser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 验证节点列表websocket连接配置
 *  @file WebSocketCnf.java
 *  @description
 *	@author zhangrj
 *  @data 2019年12月19日
 */
@Configuration
public class WebSocketCnf {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}