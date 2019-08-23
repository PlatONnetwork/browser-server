package com.platon.browser.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import com.platon.browser.BrowserApiApplication;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
@AutoConfigureMockMvc
public class AppDocHomeControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }
    
    @Test
    public void queryNavigation() throws Exception{
    	String requestBody = "{\"parameter\":\"plan\"}";
    	mockMvc.perform(MockMvcRequestBuilders.post("/home/queryNavigation")
    		.contentType(MediaType.APPLICATION_JSON_UTF8)
    		.content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }
    
}