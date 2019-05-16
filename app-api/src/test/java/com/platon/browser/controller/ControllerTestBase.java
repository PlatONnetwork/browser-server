package com.platon.browser.controller;

import com.alibaba.fastjson.JSON;
import com.platon.browser.TestBase;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.service.TransactionService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerTestBase extends TestBase {
    protected MockMvc mockMvc;
    @Autowired
    protected TransactionService transactionService;
    @Autowired
    protected WebApplicationContext context;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected ChainsConfig chainsConfig;
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    protected void sendPost(String url, Object req) throws Exception {
        MockHttpServletRequestBuilder builder = post(url)
                .contentType(MediaType.APPLICATION_JSON).content(JSON.toJSONString(req))
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print());
    }

    protected void sendGet(String url) throws Exception {
        MockHttpServletRequestBuilder builder = get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print());
    }
}
