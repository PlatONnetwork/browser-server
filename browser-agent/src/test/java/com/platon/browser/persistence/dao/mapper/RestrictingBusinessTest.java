package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.AgentApplication;
import com.platon.browser.TestBase;
import com.platon.browser.complement.dao.mapper.RestrictingBusinessMapper;
import com.platon.browser.complement.dao.param.restricting.RestrictingCreate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: dongqile
 * @Date: 2019/10/31
 * @Description: 锁仓相关入库测试类
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgentApplication.class, value = "spring.profiles.active=test")
@SpringBootApplication
public class RestrictingBusinessTest extends TestBase {


    @Autowired
    private RestrictingBusinessMapper restrictingBusinessMapper;


    /**
     * 创建锁仓计划
     */
    @Test
    public void restrcingCrateMapper () {
        RestrictingCreate restrictingCreate = restrictingCreateParam();
        restrictingBusinessMapper.create(restrictingCreate);
    }


}