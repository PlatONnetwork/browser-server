package com.platon.browser.task;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platon.browser.BrowserApiApplication;
import com.platon.browser.config.BrowserCache;
import com.platon.browser.response.home.ChainStatisticNewResp;
import com.platon.browser.serializer.CustomBeanSerializerModifier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class, value = "spring.profiles.active=dev", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StompPushTaskTest {

    @Autowired
    private StompPushTask stompPushTask;

    @Test
    public void test_PushChainStatisticNew() {
        stompPushTask.pushChainStatisticNew();
    }

    @Test
    public void test_PushBlockStatisticNew() {
        stompPushTask.pushBlockStatisticNew();
    }

    @Test
    public void test_PushStakingListNew() {
        stompPushTask.pushStakingListNew();
    }

    @Test
    public void test_PushStakingStatisticNew() {
        stompPushTask.pushStakingStatisticNew();
    }

    @Test
    public void test_PushStakingChangeNew() {
        List<String> userList = new ArrayList<>();
        userList.add("1");
        BrowserCache.getKeys().put("1|10||all", userList);
        try {
            stompPushTask.pushStakingChangeNew();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_json() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializerFactory(mapper.getSerializerFactory().withSerializerModifier(new CustomBeanSerializerModifier()));
        ChainStatisticNewResp chainStatisticNewResp = new ChainStatisticNewResp();
        chainStatisticNewResp.setBlockList(null);
        chainStatisticNewResp.setNodeId(null);
        chainStatisticNewResp.setAddressQty(null);
        assertNotNull(mapper.writeValueAsString(chainStatisticNewResp));
        chainStatisticNewResp.setBlockList(new ArrayList<>());
        assertNotNull(mapper.writeValueAsString(chainStatisticNewResp));
    }

    @Test
    public void test_Eswarm() {
        stompPushTask.eswarm();
    }

    @Test
    public void test_UpdateConfig() {
        stompPushTask.updateConfig();
    }


}
