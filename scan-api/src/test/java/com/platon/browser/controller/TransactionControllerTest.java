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
public class TransactionControllerTest extends ApiTestBase {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }

    @Test
    public void transactionList() throws Exception {
        PageReq pageReq = new PageReq();
        pageReq.setPageNo(0);
        pageReq.setPageSize(10);
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/transactionList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content((JSONObject.toJSONString(pageReq)).getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void transactionListByBlock() throws Exception {
        String requestBody = "{\"blockNumber\":500}";
        ;
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/transactionListByBlock")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void transactionListByAddress() throws Exception {
        String requestBody = "{\"address\":\"lax1cfar6ln7w2wf4thazagqz6xuex6rmhczwfy0te\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/transactionListByAddress")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void blockListByNodeIdDownload() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/transaction/addressTransactionDownload")
                        .param("address", "0x8b77ac9fabb6fe247ee91ca07ea4f62c6761e79b")
                        .param("date", "1571813653000")
                        .param("local", "en")
                        .param("timeZone", "+8")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(result -> {
                    result.getResponse().setCharacterEncoding("UTF-8");
                    MockHttpServletResponse contentRespon = result.getResponse();
                    InputStream in = new ByteArrayInputStream(contentRespon.getContentAsByteArray());
                    FileOutputStream fos = new FileOutputStream(new File("build/bbb.csv"));
                    byte[] byteBuf = new byte[1024];
                    while (in.read(byteBuf) != -1) {
                        fos.write(byteBuf);
                    }
                    fos.close();
                });
    }

    @Test
    public void transactionDetails() throws Exception {
        String requestBody = "{\"txHash\":\"0x10eab2c84392db35f9caf87c19c183a19f12462c0935a5b9a2f502ef32773d19\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/transactionDetails")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void queryClaimByAddress() throws Exception {
        String requestBody = "{\"address\":\"lax1cfar6ln7w2wf4thazagqz6xuex6rmhczwfy0te\",\"pageNo\":\"1\",\"pageSize\":\"20\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/queryClaimByAddress")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void queryClaimByStaking() throws Exception {
        String requestBody = "{\"nodeId\":\"0x53242dec8799f3f4f8882b109e1a3ebb4aa8c2082d000937d5876365414150c5337aa3d3d41ead1ac873f4e0b19cb9238d2995598207e8d571f0bd5dd843cdf3\",\"pageNo\":\"1\",\"pageSize\":\"20\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/queryClaimByStaking")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

}
