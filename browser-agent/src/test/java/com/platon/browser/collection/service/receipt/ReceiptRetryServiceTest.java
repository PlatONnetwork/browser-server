package com.platon.browser.collection.service.receipt;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.exception.HttpRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class ReceiptRetryServiceTest extends AgentTestBase {
    @Mock
    private PlatOnClient platOnClient;
    @Mock
    private ReceiptRetryService target;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(target, "platOnClient", platOnClient);
    }

    @Test
    public void testNormal() throws InterruptedException, HttpRequestException {
        when(target.getReceipt(anyLong())).thenCallRealMethod();
        ReceiptResult rr = receiptResultList.get(0);
        when(target.getReceiptResult(any())).thenReturn(rr);
        target.getReceipt(1L);

        verify(target, times(1)).getReceipt(any());
    }

    @Test(expected = RuntimeException.class)
    public void getBlockException() throws HttpRequestException, InterruptedException {
        when(target.getReceipt(anyLong())).thenCallRealMethod();
        when(target.getReceiptResult(any())).thenThrow(new RuntimeException(""));
        target.getReceipt(1L);
    }
}
