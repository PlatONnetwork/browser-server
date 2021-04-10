package com.platon.browser.controller;

import com.platon.browser.ApiTestBase;
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
public class AppDocHomeControllerTest  extends ApiTestBase {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }
    
    @Test
    public void queryNavigation() throws Exception{
    	String requestBody = "{\"parameter\":\"plan\"}";
    	mockMvc.perform(MockMvcRequestBuilders.post("/home/queryNavigation")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    public void blockStatisticNew() throws Exception{
    	mockMvc.perform(MockMvcRequestBuilders.post("/home/blockStatistic")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    public void chainStatisticNew() throws Exception{
    	mockMvc.perform(MockMvcRequestBuilders.post("/home/chainStatistic")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    public void stakingListNew() throws Exception{
    	mockMvc.perform(MockMvcRequestBuilders.post("/home/stakingList")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andDo(print());
    }
    
}