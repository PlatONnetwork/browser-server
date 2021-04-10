package com.platon.browser.controller;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.ApiTestBase;
import com.platon.browser.request.token.QueryHolderTokenListReq;
import com.platon.browser.request.token.QueryTokenHolderListReq;
import com.platon.browser.request.token.QueryTokenTransferRecordListReq;
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
public class AppDocErc20TokenTransferControllerTest  extends ApiTestBase {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build(); //初始化MockMvc对象
    }

    //@Test
    public void tokenTransferList() throws Exception {
        QueryTokenTransferRecordListReq req = new QueryTokenTransferRecordListReq();
        req.setPageNo(0);
        req.setPageSize(10);
        req.setAddress("lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e");
        req.setContract("lax1jzcc0xqvkglwmr3txeaf2c9jxp6pzmse3gxk9n");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/token/tokenTransferList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content((JSONObject.toJSONString(req)).getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    //@Test
    public void tokenHolderList() throws Exception {
        QueryTokenHolderListReq req = new QueryTokenHolderListReq();
        req.setPageNo(0);
        req.setPageSize(10);
        req.setContract("lax1jzcc0xqvkglwmr3txeaf2c9jxp6pzmse3gxk9n");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/token/tokenHolderList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(req).getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    //@Test
    public void holderTokenList() throws Exception {
        QueryHolderTokenListReq req = new QueryHolderTokenListReq();
        req.setPageNo(0);
        req.setPageSize(10);
        req.setAddress("lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/token/holderTokenList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(req).getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    //@Test
    public void exportTokenTransferList() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/token/exportTokenTransferList")
                        .param("address", "lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e")
                        .param("contract", "lax1jzcc0xqvkglwmr3txeaf2c9jxp6pzmse3gxk9n")
                        .param("date", "1571813653000")
                        .param("local", "en")
                        .param("timeZone", "+8")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(result -> {
                    result.getResponse().setCharacterEncoding("UTF-8");
                    MockHttpServletResponse contentRespon = result.getResponse();
                    InputStream in = new ByteArrayInputStream(contentRespon.getContentAsByteArray());
                    FileOutputStream fos = new FileOutputStream(new File("build/ccc.csv"));
                    byte[] byteBuf = new byte[1024];
                    while (in.read(byteBuf) != -1) {
                        fos.write(byteBuf);
                    }
                    fos.close();
                });
    }


    //@Test
    public void exportTokenHolderList() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/token/exportTokenHolderList")
                        .param("contract", "lax1jzcc0xqvkglwmr3txeaf2c9jxp6pzmse3gxk9n")
                        .param("date", "1571813653000")
                        .param("local", "en")
                        .param("timeZone", "+8")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(result -> {
                    result.getResponse().setCharacterEncoding("UTF-8");
                    MockHttpServletResponse contentRespon = result.getResponse();
                    InputStream in = new ByteArrayInputStream(contentRespon.getContentAsByteArray());
                    FileOutputStream fos = new FileOutputStream(new File("build/ddd.csv"));
                    byte[] byteBuf = new byte[1024];
                    while (in.read(byteBuf) != -1) {
                        fos.write(byteBuf);
                    }
                    fos.close();
                });
    }

    @Test
    public void exportHolderTokenList() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/token/holder-token/export")
                        .param("address", "lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e")
                        .param("date", "1571813653000")
                        .param("local", "en")
                        .param("timeZone", "+8")
                        .param("type", "erc20")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(result -> {
                    result.getResponse().setCharacterEncoding("UTF-8");
                    MockHttpServletResponse contentRespon = result.getResponse();
                    InputStream in = new ByteArrayInputStream(contentRespon.getContentAsByteArray());
                    FileOutputStream fos = new FileOutputStream(new File("build/eee.csv"));
                    byte[] byteBuf = new byte[1024];
                    while (in.read(byteBuf) != -1) {
                        fos.write(byteBuf);
                    }
                    fos.close();
                });
    }

}