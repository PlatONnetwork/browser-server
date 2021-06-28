package com.platon.browser.v0160.service;

import com.platon.browser.AgentApplication;
import com.platon.browser.AgentTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest(classes = AgentApplication.class, value = "spring.profiles.active=hrpatp201018v10000", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DelegateBalanceAdjustmentServiceTest {

    @Resource
    private DelegateBalanceAdjustmentService delegateBalanceAdjustmentService;

    @Test
    public void adjust() throws Exception {
        delegateBalanceAdjustmentService.adjust();
    }

}
