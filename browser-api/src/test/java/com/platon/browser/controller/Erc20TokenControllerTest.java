package com.platon.browser.controller;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.BrowserApiApplication;
import com.platon.browser.request.PageReq;
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
@SpringBootTest(classes = BrowserApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Erc20TokenControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build(); //初始化MockMvc对象
    }

    @Test
    public void tokenList() throws Exception {
        PageReq pageReq = new PageReq();
        pageReq.setPageNo(0);
        pageReq.setPageSize(10);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/token/tokenList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content((JSONObject.toJSONString(pageReq)).getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void tokenDetail() throws Exception {
        String requestBody = "{\"address\":\"lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e\"}";
        ;
        this.mockMvc.perform(MockMvcRequestBuilders.post("/token/tokenDetail")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }


}