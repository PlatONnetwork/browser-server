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
    private SearchService searchService;
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
        mockMvc.perform(post("/home/query")
                .contentType(MediaType.APPLICATION_JSON).content(JSON.toJSONString(req))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
