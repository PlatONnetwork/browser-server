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
public class StakingControllerTest  extends ApiTestBase  {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build(); //初始化MockMvc对象
    }

    @Test
    public void stakingListNew() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.post("/staking/statistic")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void aliveStakingList() throws Exception{
    	String requestBody = "{\"key\":\"aa\",\"queryStatus\":\"all\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/staking/aliveStakingList")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void historyStakingList() throws Exception{
    	String requestBody = "{\"key\":\"aa\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/staking/historyStakingList")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void stakingDetails() throws Exception{
    	String requestBody = "{\"nodeId\":\"0x53242dec8799f3f4f8882b109e1a3ebb4aa8c2082d000937d5876365414150c5337aa3d3d41ead1ac873f4e0b19cb9238d2995598207e8d571f0bd5dd843cdf3\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/staking/stakingDetails")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    @Test
    public void stakingOptRecordList() throws Exception{
    	String requestBody = "{\"nodeId\":\"0x53242dec8799f3f4f8882b109e1a3ebb4aa8c2082d000937d5876365414150c5337aa3d3d41ead1ac873f4e0b19cb9238d2995598207e8d571f0bd5dd843cdf3\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/staking/stakingOptRecordList")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    @Test
    public void delegationListByStaking() throws Exception{
    	String requestBody = "{\"nodeId\":\"0x53242dec8799f3f4f8882b109e1a3ebb4aa8c2082d000937d5876365414150c5337aa3d3d41ead1ac873f4e0b19cb9238d2995598207e8d571f0bd5dd843cdf3\",\"stakingBlockNum\":\"121\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/staking/delegationListByStaking")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void delegationListByAddress() throws Exception {
        String requestBody = "{\"address\":\"lax1cfar6ln7w2wf4thazagqz6xuex6rmhczwfy0te\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/staking/delegationListByAddress")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void lockedStakingList() throws Exception {
        String requestBody = "{\"key\":\"aa\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/staking/lockedStakingList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }

}
