package com.platon.browser.service.account;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.exception.BusinessException;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.Request;
import com.platon.protocol.core.methods.response.PlatonGetBalance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountServiceTest extends AgentTestBase {
    @Mock
    private PlatOnClient platOnClient;
    @Spy
    private AccountService target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "platOnClient", platOnClient);
        Web3jWrapper web3jWrapper = mock(Web3jWrapper.class);
        when(platOnClient.getWeb3jWrapper()).thenReturn(web3jWrapper);
        Web3j web3j = mock(Web3j.class);
        when(web3jWrapper.getWeb3j()).thenReturn(web3j);
        Request request = mock(Request.class);
        when(web3j.platonGetBalance(anyString(),any())).thenReturn(request);
        PlatonGetBalance pgb = mock(PlatonGetBalance.class);
        when(request.send()).thenReturn(pgb);
        when(pgb.getBalance()).thenReturn(BigInteger.TEN);
    }

    /**
     * 根据区块号获取激励池余额
     */
    @Test(expected = BusinessException.class)
    public void getInciteBalance() {
        BigDecimal balance = target.getInciteBalance(BigInteger.valueOf(501));
        assertEquals(BigDecimal.TEN,balance);

        doThrow(new BusinessException("")).when(platOnClient).getWeb3jWrapper();
        target.getInciteBalance(BigInteger.ONE);
    }

    /**
     * 根据区块号获取锁仓池余额
     */
    @Test(expected = BusinessException.class)
    public void getLockCabinBalance() {
        BigDecimal balance = target.getLockCabinBalance(BigInteger.valueOf(501));
        assertEquals(BigDecimal.TEN,balance);

        doThrow(new BusinessException("")).when(platOnClient).getWeb3jWrapper();
        target.getInciteBalance(BigInteger.ONE);
    }

    /**
     * 根据区块号获取质押池余额
     */
    @Test(expected = BusinessException.class)
    public void getStakingBalance() {
        BigDecimal balance = target.getStakingBalance(BigInteger.valueOf(501));
        assertEquals(BigDecimal.TEN,balance);

        doThrow(new BusinessException("")).when(platOnClient).getWeb3jWrapper();
        target.getInciteBalance(BigInteger.ONE);
    }
}
