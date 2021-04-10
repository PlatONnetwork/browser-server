package com.platon.browser.controller;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.ApiTestBase;
import com.platon.browser.request.address.QueryRPPlanDetailRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AddressControllerTest extends ApiTestBase {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }
    
    @Test
    public void details() throws Exception{
    	String requestBody = "{\"address\":\"lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e\"}";
    	mockMvc.perform(MockMvcRequestBuilders.post("/address/details")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
	public void rpplanDetail() throws Exception {
		QueryRPPlanDetailRequest req = new QueryRPPlanDetailRequest();
		req.setPageNo(0);
		req.setPageSize(10);
		req.setAddress("lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e");
		String requestBody = JSONObject.toJSONString(req);
    	mockMvc.perform(MockMvcRequestBuilders.post("/address/rpplanDetail")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
	}
    
}