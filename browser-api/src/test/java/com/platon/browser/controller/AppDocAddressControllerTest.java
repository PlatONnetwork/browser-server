package com.platon.browser.controller;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.BrowserApiApplication;
import com.platon.browser.req.address.QueryRPPlanDetailRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AppDocAddressControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }
    
    @Test
    public void details() throws Exception{
    	String requestBody = "{\"address\":\"0x60ceca9c1290ee56b98d4e160ef0453f7c40d219\"}";
    	mockMvc.perform(MockMvcRequestBuilders.post("/address/details")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
	public void rpplanDetail() throws Exception {
		QueryRPPlanDetailRequest req = new QueryRPPlanDetailRequest();
		req.setPageNo(0);
		req.setPageSize(10);
		req.setAddress("0x60ceca9c1290ee56b98d4e160ef0453f7c40d219");
		String requestBody = JSONObject.toJSONString(req);
    	mockMvc.perform(MockMvcRequestBuilders.post("/address/rpplanDetail")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
	}
    
}