package com.platon.browser.controller;

import com.platon.browser.BrowserApiApplication;
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
public class AppDocStakingControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }
    
    @Test
    public void stakingListNew() throws Exception{
    	mockMvc.perform(MockMvcRequestBuilders.post("/staking/statistic")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    public void aliveStakingList() throws Exception{
    	String requestBody = "{\"key\":\"aa\",\"queryStatus\":\"all\"}";
    	mockMvc.perform(MockMvcRequestBuilders.post("/staking/aliveStakingList")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    public void historyStakingList() throws Exception{
    	String requestBody = "{\"key\":\"aa\"}";
    	mockMvc.perform(MockMvcRequestBuilders.post("/staking/historyStakingList")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    public void stakingDetails() throws Exception{
    	String requestBody = "{\"nodeId\":\"aaa\"}";
    	mockMvc.perform(MockMvcRequestBuilders.post("/staking/stakingDetails")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    public void stakingOptRecordList() throws Exception{
    	String requestBody = "{\"nodeId\":\"aaa\"}";
    	mockMvc.perform(MockMvcRequestBuilders.post("/staking/stakingOptRecordList")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    public void delegationListByStaking() throws Exception{
    	String requestBody = "{\"nodeId\":\"aaa\",\"stakingBlockNum\":\"121\"}";
    	mockMvc.perform(MockMvcRequestBuilders.post("/staking/delegationListByStaking")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    public void delegationListByAddress() throws Exception{
    	String requestBody = "{\"address\":500}";
    	mockMvc.perform(MockMvcRequestBuilders.post("/staking/delegationListByAddress")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    
}