package com.platon.browser.controller;

import com.platon.browser.ApiTestBase;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
public class AppDocWebsocketControllerTest  extends ApiTestBase {
	@Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        setMockMvc(MockMvcBuilders.webAppContextSetup(wac).build()); //初始化MockMvc对象
    }
    @Test
    public void websocket() throws Exception{
    	Map<String, String> map = new HashMap<>();
    	String url = "ws://127.0.0.1:9061/websocket/";
    	map.put("message", "1|10||all");
        URI uri = new URI(url);
    	WebSocketClient webSocketClient = new WebSocketClient(uri, map) {
			
			@Override
			public void onOpen(ServerHandshake handshakedata) {
				
			}
			
			@Override
			public void onMessage(String message) {
				
			}
			
			@Override
			public void onError(Exception ex) {
				
			}
			
			@Override
			public void onClose(int code, String reason, boolean remote) {
				
			}
		};
		webSocketClient.connect();
//		webSocketClient.send("1|10||all");
		webSocketClient.close();
    }
	public MockMvc getMockMvc() {
		return mockMvc;
	}
	public void setMockMvc(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}
    
}