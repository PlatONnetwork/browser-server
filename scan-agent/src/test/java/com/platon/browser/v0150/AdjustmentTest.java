//package com.platon.browser.v015;
//
//import com.alaya.protocol.Web3j;
//import com.alaya.protocol.http.HttpService;
//import com.alibaba.fastjson.JSON;
//import com.platon.browser.AgentApplication;
//import com.platon.browser.client.PlatOnClient;
//import com.platon.browser.client.SpecialApi;
//import com.platon.browser.utils.NetworkParams;
//import com.platon.browser.v015.bean.AdjustParam;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import javax.annotation.Resource;
//import java.math.BigInteger;
//import java.util.List;
//
//@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AgentApplication.class,properties = {"spring.profiles.active=dev"})
//public class AdjustmentTest {
//
//    @Resource
//    private SpecialApi specialApi;
//    @Resource
//    private PlatOnClient platOnClient;
//
//    @Test
//    public void testSpc() throws Exception {
//        List<AdjustParam> params = specialApi.getStakingDelegateAdjustDataList(platOnClient.getWeb3jWrapper().getWeb3j(), BigInteger.valueOf(9641));
//
//        log.debug(JSON.toJSONString(params));
//    }
//}