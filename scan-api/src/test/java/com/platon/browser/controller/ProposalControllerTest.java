package com.platon.browser.controller;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.ApiTestBase;
import com.platon.browser.request.PageReq;
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
public class ProposalControllerTest  extends ApiTestBase {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }

    @Test
    public void proposalList() throws Exception{
    	PageReq pageReq = new PageReq();
    	pageReq.setPageNo(0);
    	pageReq.setPageSize(10);
        mockMvc.perform(MockMvcRequestBuilders.post("/proposal/proposalList")
        	.contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(JSONObject.toJSONString(pageReq).getBytes()))
        	.andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void proposalDetails() throws Exception {
        String requestBody = "{\"proposalHash\":\"0xeab60656668daceb7572117fa4ecbe906b91da744677b5c8dc5581426388cb86\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/proposal/proposalDetails")
        	.contentType(MediaType.APPLICATION_JSON_UTF8)
        	.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void voteList() throws Exception {
        String requestBody = "{\"proposalHash\":\"0xeab60656668daceb7572117fa4ecbe906b91da744677b5c8dc5581426388cb86\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/proposal/voteList")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }

}
