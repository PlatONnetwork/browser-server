package com.platon.browser.service;

import com.platon.browser.BrowserApiApplication;
import com.platon.browser.client.SpecialApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class,value = "spring.profiles.active=platon",webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpecialApiTest {

    @Resource
    private CommonService commonService;

    @Resource
    private SpecialApi specialApi;

    @Test
    public void geIssueValueTest() {
        try {
            BigDecimal issueValue = commonService.getCurrentBlockIssueValue(54045109L);
            System.out.println("issueValue = " + issueValue.toPlainString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void getCirculationValueTest() {
        try {
            BigDecimal issueValue = commonService.getCirculationValue(54045109L);
            System.out.println("issueValue = " + issueValue.toPlainString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
