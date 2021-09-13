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
public class InternalAddressControllerTest extends ApiTestBase {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }

    @Test
    public void getFoundationInfo() throws Exception {
        String requestBody = "{\"pageNo\":\"1\",\"pageSize\":\"20\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/internalAddress/foundationInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.getBytes())).andExpect(status().isOk()).andDo(print());
    }

}
