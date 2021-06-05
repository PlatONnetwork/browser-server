package com.platon.browser.controller.token;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.ApiTestBase;
import com.platon.browser.request.token.QueryHolderTokenListReq;
import com.platon.browser.request.token.QueryTokenIdDetailReq;
import com.platon.browser.request.token.QueryTokenIdListReq;
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
public class HolderTokenControllerTest extends ApiTestBase {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build(); //初始化MockMvc对象
    }

    @Test
    public void holderTokenList() throws Exception {
        QueryHolderTokenListReq req = new QueryHolderTokenListReq();
        req.setPageNo(1);
        req.setPageSize(10);
        req.setAddress("atp100nalg9dlhd88y8rwvejwlrdyvkx0zdyrquu9u");
        req.setType("erc721");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/token/holder-token/list")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content((JSONObject.toJSONString(req)).getBytes()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void holderTokenExport() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/token/holder-token/export")
                        .param("address", "0x8b77ac9fabb6fe247ee91ca07ea4f62c6761e79b")
                        .param("token", "")
                        .param("type", "")
                        .param("local", "en")
                        .param("timeZone", "+8")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(result -> {
                    result.getResponse().setCharacterEncoding("UTF-8");
                    MockHttpServletResponse contentRespon = result.getResponse();
                    InputStream in = new ByteArrayInputStream(contentRespon.getContentAsByteArray());
                    FileOutputStream fos = new FileOutputStream(new File("build/bbb.csv"));
                    byte[] byteBuf = new byte[1024];
                    while(in.read(byteBuf)!=-1) {
                        fos.write(byteBuf);
                    }
                    fos.close();
                });
    }


}