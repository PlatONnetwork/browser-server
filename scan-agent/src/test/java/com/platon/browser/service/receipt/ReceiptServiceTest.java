package com.platon.browser.service.receipt;

import com.platon.browser.AgentTestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class ReceiptServiceTest extends AgentTestBase {
    @Mock
    private ReceiptRetryService retryService;
    @Spy
    private ReceiptService target;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "retryService", retryService);
    }

    @Test
    public void test() throws Exception {
        target.getReceiptAsync(1L);
        when(retryService.getReceipt(any())).thenThrow(new RuntimeException(""));
        target.getReceiptAsync(1L);

        verify(target, times(2)).getReceiptAsync(any());
    }

}
