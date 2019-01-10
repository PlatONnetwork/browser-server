package com.platon.browser.controller;

import com.alibaba.fastjson.JSON;
import com.platon.browser.ServerApplication;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.req.block.*;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.NodeService;
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

import javax.servlet.http.HttpServletResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=1")
public class BlockControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(BlockControllerTest.class);

    private MockMvc mockMvc;
    @Autowired
    private BlockService blockService;
    @Autowired
    WebApplicationContext context;
    @Autowired
    HttpServletResponse response;

    protected String chainId = "1";

    @Before
    public void setup() {
        mockMvc =MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    private void sendRequest(String url, Object req) throws Exception {
        MockHttpServletRequestBuilder builder = post(url)
                .contentType(MediaType.APPLICATION_JSON).content(JSON.toJSONString(req))
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void blockList() throws Exception{
        BlockPageReq req = new BlockPageReq();
        req.setCid(chainId);
        sendRequest("/block/blockList",req);

        req.setPageNo(2);
        req.setPageSize(5);
        sendRequest("/block/blockList",req);
    }

    @Test
    public void blockDetail() throws Exception{
        BlockListItem block = getOneBlock();
        BlockDetailReq req = new BlockDetailReq();
        req.setCid(chainId);
        req.setHeight(block.getHeight());
        sendRequest("/block/blockDetails",req);
    }

    @Test
    public void blockDetailNavigate() throws Exception{
        BlockListItem block = getOneBlock();
        BlockDetailNavigateReq req = new BlockDetailNavigateReq();
        req.setCid(chainId);
        req.setDirection("next");
        req.setHeight(block.getHeight()-10);
        sendRequest("/block/blockDetailNavigate",req);

        req.setDirection("prev");
        req.setHeight(block.getHeight());
        sendRequest("/block/blockDetailNavigate",req);
    }



    private BlockListItem getOneBlock(){
        BlockPageReq req = new BlockPageReq();
        req.setCid(chainId);
        req.setPageSize(1);
        req.setPageSize(1);
        RespPage<BlockListItem> data = blockService.getPage(req);
        if(data.getData().size()==0){
            return null;
        }
        return data.getData().get(0);
    }

}
