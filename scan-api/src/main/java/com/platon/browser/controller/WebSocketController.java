package com.platon.browser.controller;

import com.platon.browser.config.BrowserCache;
import com.platon.browser.config.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 验证节点的web连接controller
 *  @file WebSocketController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年12月19日
 */
@Slf4j
@RestController
@ServerEndpoint("/websocket/{message}")
@CrossOrigin
public class WebSocketController {
	private String userno = "";
	private Lock lock = new ReentrantLock();

	/**
	 * * 连接建立成功调用的方法 * * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(@PathParam(value = "message") String message, Session session, EndpointConfig config) {
		MessageDto messageDto = new MessageDto();
		messageDto = messageDto.analysisData(message);
		BrowserCache.getWebSocketSet().put(messageDto.getUserNo(), session);// 加入map中
		/**
		 * 用組合key来存储用户list
		 * 判断是否已经拥有key
		 * 如果拥有则直接返回
		 */
		try {
			lock.lock();
			List<String> userList = null;
			if(BrowserCache.getKeys().containsKey(messageDto.getMessageKey())) {
				userList = BrowserCache.getKeys().get(messageDto.getMessageKey());
			} else {
				userList = new ArrayList<>();
			}
			userList.add(messageDto.getUserNo());
			BrowserCache.getKeys().put(messageDto.getMessageKey(),userList);
		} finally {
			lock.unlock();
		}
		userno = messageDto.getUserNo();
		BrowserCache.addOnlineCount();// 在线数加1
		log.debug("有新连接加入！当前在线人数为:{}",BrowserCache.getOnlineCount());
	}

	/**
	 * * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		if (StringUtils.isNotBlank(userno)) {
			/**
			 * 循环去除对应的用户
			 */
			for (Entry<String, List<String>> m : BrowserCache.getKeys().entrySet()) {
				if(m.getValue().contains(userno)) {
					m.getValue().remove(userno);
				}
			}
			BrowserCache.getWebSocketSet().remove(userno); // 从set中删除
		}
		BrowserCache.subOnlineCount(); // 在线数减1
		log.debug("有一连接关闭！当前在线人数为:{}",BrowserCache.getOnlineCount());
	}

	/**
	 * * 收到客户端消息后调用的方法 * * @param message 客户端发送过来的消息 * @param session 可选的参数
	 */

	@OnMessage
	public void onMessage(String message, Session session) {
		log.debug("来自客户端的消息:{}",message);
	}

	/**
	 * * 发生错误时调用 * * @param session * @param error
	 */

	@OnError
	public void onError(Session session, Throwable error) {
		log.error(" error", error);
		if (StringUtils.isNotBlank(userno)) {
			/**
			 * 循环去除对应的用户
			 */
			for (Entry<String, List<String>> m : BrowserCache.getKeys().entrySet()) {
				if(m.getValue().contains(userno)) {
					m.getValue().remove(userno);
				}
			}
			BrowserCache.getWebSocketSet().remove(userno); // 从set中删除
			BrowserCache.subOnlineCount(); // 在线数减1
			log.debug("有一连接关闭！当前在线人数为:{}",BrowserCache.getOnlineCount());
		}
	}
}