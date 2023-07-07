package com.platon.browser.service.receipt;

import com.platon.browser.AgentApplication;
import com.platon.browser.bean.ReceiptResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;


/**
 * 重要说明：
 * 重要说明：
 * 正常情况下不需要AgentApplicationTest.class这个文件，由于项目中的AgentApplication.class在启动后，会进入run方法中的for{}循环，开始自动工作，这个不符合这里单元测试所要求的场景。
 * 因此，单独写这个AgentApplicationTest.class以便来加载ApplicationContext，
 * 同时加上excludeFilters以便排除扫描到AgentApplication.class，
 * 但是发现这样不奏效，而是需要注释掉AgentApplication.class的@Configuration，@SpringBootApplication这两个注解才行。
 * >>>>!!!在测试完成后，需要把AgentApplication.class的@Configuration，@SpringBootApplication这两个注解打开!!!<<<
 *
 */

@Slf4j
@SpringBootTest(classes = { AgentApplication.class })
@ActiveProfiles("platon")
public class RetryServiceTest {
    @Resource
    ReceiptRetryService receiptRetryService;

    @Test
    public void testGetReceipt(){
        log.debug("开始测试");
        ReceiptResult receiptResult = receiptRetryService.getReceipt(28017078L);

        System.out.println(receiptResult);
    }
}
