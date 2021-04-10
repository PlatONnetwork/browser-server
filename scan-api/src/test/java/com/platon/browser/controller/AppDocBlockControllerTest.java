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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AppDocBlockControllerTest extends ApiTestBase {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }

    @Test
    public void blockList() throws Exception {
        PageReq pageReq = new PageReq();
        pageReq.setPageNo(0);
        pageReq.setPageSize(10);
        mockMvc.perform(MockMvcRequestBuilders.post("/block/blockList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content((JSONObject.toJSONString(pageReq)).getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void blockListByNodeId() throws Exception {
        String requestBody = "{\"nodeId\":\"0xed52e1686606c6b496bf220c0e450003763a9258cdf3111579cb0d2c2a1b89ea84ec38116f53c4d2fda9860c78de5693d11517228eec9c651fad132b15e12d29\"}";
        mockMvc
                .perform(MockMvcRequestBuilders.post("/block/blockListByNodeId")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(requestBody.getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void blockListByNodeIdDownload() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/block/blockListByNodeIdDownload")
                        .param("nodeId", "0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050")
                        .param("date", "1611274441843")
                        .param("local", "en")
                        .param("timeZone", "+8")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(result -> {
                    result.getResponse().setCharacterEncoding("UTF-8");
                    MockHttpServletResponse contentRespon = result.getResponse();
                    InputStream in = new ByteArrayInputStream(contentRespon.getContentAsByteArray());
                    FileOutputStream fos = new FileOutputStream(new File("build/aaa.csv"));
                    byte[] byteBuf = new byte[1024];
                    while (in.read(byteBuf) != -1) {
                        fos.write(byteBuf);
                    }
                    fos.close();
                });
    }

    @Test
    public void blockDetails() throws Exception {
        String requestBody = "{\"number\":123}";
        mockMvc
                .perform(MockMvcRequestBuilders.post("/block/blockDetails")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(requestBody.getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void blockDetailNavigate() throws Exception {
        String requestBody = "{\"number\":123,\"direction\":\"next\"}";
        mockMvc
                .perform(MockMvcRequestBuilders.post("/block/blockDetailNavigate")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(requestBody.getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

}