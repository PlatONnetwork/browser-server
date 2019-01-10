package com.platon.browser.controller;

import com.alibaba.fastjson.JSON;
import com.platon.browser.ServerApplication;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.req.search.SearchReq;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.SearchService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=1")
public class SearchControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(SearchControllerTest.class);

    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;

    protected String chainId = "1";

    @Before
    public void setup() {
        mockMvc =MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void search() throws Exception{
        SearchReq req = new SearchReq();
        req.setCid(chainId);
        req.setParameter("52");
        sendRequest(req);

        req.setParameter("0x25455");
        sendRequest(req);

        req.setParameter("0x64f34a510b8aba47af32383499c6f364c963ab9436865dc32aad9ab74a1cab5f");
        sendRequest(req);

        req.setParameter("0x6ce6f581afc92989fb9e367a66c4a4b5557a9a1e7fe1978fcf020d32254d589d");
        sendRequest(req);

        req.setParameter("0x1f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2840e429");
        sendRequest(req);

        req.setParameter("dd");
        sendRequest(req);
    }

    private void sendRequest(SearchReq req) throws Exception {
        MockHttpServletRequestBuilder builder = post("/home/query")
                .contentType(MediaType.APPLICATION_JSON).content(JSON.toJSONString(req))
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print());
    }
}
