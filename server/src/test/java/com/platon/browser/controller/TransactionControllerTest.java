package com.platon.browser.controller;

import com.alibaba.fastjson.JSON;
import com.platon.browser.ServerApplication;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.block.BlockDetailNavigateReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.block.BlockPageReq;
import com.platon.browser.req.transaction.TransactionListReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.TransactionService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=1")
public class TransactionControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(TransactionControllerTest.class);

    private MockMvc mockMvc;
    @Autowired
    private TransactionService transactionService;
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
        TransactionListReq req = new TransactionListReq();
        req.setCid(chainId);
        sendRequest("/transaction/transactionList",req);

        req.setHeight(10l);
        sendRequest("/transaction/transactionList",req);

        req.setPageNo(2);
        req.setPageSize(3);
        sendRequest("/transaction/transactionList",req);
    }

    @Test
    public void getDetail() throws Exception{
        TransactionListItem data = getOneTransaction();
        if(data==null){
            Assert.fail("No data in the database.");
            return;
        }

        BlockDetailReq req = new BlockDetailReq();
        req.setCid(chainId);
        req.setHeight(data.getBlockHeight());
        sendRequest("/transaction/transactionDetails",req);
    }

    @Test
    public void detailNavigate() throws Exception{

        TransactionListItem data = getOneTransaction();
        if(data==null){
            Assert.fail("No data in the database.");
            return;
        }

        BlockDetailNavigateReq req = new BlockDetailNavigateReq();
        req.setCid(chainId);
        req.setDirection("next");
        req.setHeight(data.getBlockHeight()-10);
        sendRequest("/transaction/transactionDetailNavigate",req);

        req.setDirection("prev");
        req.setHeight(data.getBlockHeight());
        sendRequest("/transaction/transactionDetailNavigate",req);
    }



    private TransactionListItem getOneTransaction(){
        TransactionListReq req = new TransactionListReq();
        req.setCid(chainId);
        req.setPageSize(1);
        req.setPageSize(1);
        RespPage<TransactionListItem> data = transactionService.getPage(req);
        if(data.getData().size()==0){
            return null;
        }
        return data.getData().get(0);
    }

}
