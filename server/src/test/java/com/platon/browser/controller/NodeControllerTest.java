package com.platon.browser.controller;

import com.alibaba.fastjson.JSON;
import com.platon.browser.ServerApplication;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.req.search.SearchReq;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.util.TestDataUtil;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.Node;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=1")
public class NodeControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(NodeControllerTest.class);

    private MockMvc mockMvc;
    @Autowired
    private NodeService nodeService;
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
    public void getPage() throws Exception{
        NodePageReq req = new NodePageReq();
        req.setCid(chainId);
        sendRequest("/node/list",req);

        req.setIsValid(0);
        sendRequest("/node/list",req);

        req.setIsValid(1);
        sendRequest("/node/list",req);

        req.setNodeType(1);
        sendRequest("/node/list",req);

        req.setIsValid(null);
        req.setIsValid(1);
        sendRequest("/node/list",req);

        req.setKeyword("aaa");
        sendRequest("/node/list",req);

    }

    @Test
    public void detail() throws Exception{
        NodeListItem node = getOneNode();
        if(node==null){
            Assert.fail("No data in the database.");
            return;
        }
        NodeDetailReq req = new NodeDetailReq();
        req.setCid(chainId);
        req.setId(node.getId());
        sendRequest("/node/detail",req);
    }

    @Test
    public void blockList() throws Exception{
        NodeListItem node = getOneNode();
        if(node==null){
            Assert.fail("No data in the database.");
            return;
        }
        BlockListReq req = new BlockListReq();
        req.setCid(chainId);
        req.setAddress(node.getAddress());
        sendRequest("/node/blockList",req);
    }

    @Test
    public void blockDownload() throws Exception{
        NodeListItem node = getOneNode();
        if(node==null){
            Assert.fail("No data in the database.");
            return;
        }

        BlockDownloadReq req = new BlockDownloadReq();
        req.setCid(chainId);
        req.setAddress(node.getAddress());


        mockMvc.perform(get("/node/blockDownload")
                .param("cid",chainId)
                .param("address",node.getAddress())
                .param("date","2018-01-01")
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    private NodeListItem getOneNode(){
        NodePageReq req = new NodePageReq();
        req.setCid(chainId);
        req.setPageNo(1);
        req.setPageSize(1);
        RespPage<NodeListItem> nodes = nodeService.getPage(req);
        if(nodes.getData().size()>0){
            return nodes.getData().get(0);
        }else{
            return null;
        }
    }
}
