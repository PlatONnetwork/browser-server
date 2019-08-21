package com.platon.browser.controller;

import com.platon.browser.BrowserApiApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
@AutoConfigureMockMvc
public class AppDocProposalControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }
    @Test
    public void proposalList() throws Exception{
                mockMvc.perform(MockMvcRequestBuilders.post("/proposal/proposalList")).
                andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void proposalDetails()  throws Exception{
        String requestBody="{\"proposalHash\":\"addvdfbnghm\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/proposal/proposalDetails").
                contentType(MediaType.APPLICATION_JSON_UTF8).
                content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void voteList()  throws Exception{
        String requestBody="{\"proposalHash\":\"null\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/proposal/voteList").
                contentType(MediaType.APPLICATION_JSON_UTF8).
                content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
}