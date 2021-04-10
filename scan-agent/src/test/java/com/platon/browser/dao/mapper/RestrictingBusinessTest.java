//package com.platon.browser.dao.mapper;
//
//import com.platon.browser.AgentApplication;
//import com.platon.browser.AgentTestBase;
//import com.platon.browser.dao.mapper.RestrictingBusinessMapper;
//import com.platon.browser.dao.param.ppos.RestrictingCreate;
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
// * @Description: 锁仓相关入库测试类
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AgentApplication.class, value = "spring.profiles.active=unit")
//@SpringBootApplication
//public class RestrictingBusinessTest extends AgentTestBase {
//    @Autowired
//    private RestrictingBusinessMapper restrictingBusinessMapper;
//
//    /**
//     * 创建锁仓计划
//     */
//    @Test
//    public void restrictingCrateMapper () {
//        RestrictingCreate restrictingCreate = restrictingCreateParam();
//        restrictingBusinessMapper.create(restrictingCreate);
//    }
//}