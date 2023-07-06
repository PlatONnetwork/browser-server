//package com.platon.browser.dao.mapper;
//
//import com.platon.browser.AgentApplication;
//import com.platon.browser.AgentTestBase;
//import com.platon.browser.dao.mapper.StatisticBusinessMapper;
//import com.platon.browser.dao.param.statistic.AddressStatChange;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
///**
// * @Auther: dongqile
// * @Date: 2019/10/31
// * @Description: 统计数据测试类
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AgentApplication.class, value = "spring.profiles.active=unit")
//@SpringBootApplication
//public class StatisticBusinessTest extends AgentTestBase {
//    @Autowired
//    private StatisticBusinessMapper statisticBusinessMapper;
//
//    /**
//     * 地址数据统计
//     */
//    @Test
//    public void addressChangeMapper(){
//        AddressStatChange addressStatChange = addressStatChangeParam();
//        statisticBusinessMapper.addressChange(addressStatChange);
//    }
//}