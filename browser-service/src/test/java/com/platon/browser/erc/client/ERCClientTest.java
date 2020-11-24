package com.platon.browser.erc.client;

import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.utils.NetworkParms;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-09-29 16:42
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ERCClientTest {

    @Spy
    private ErcServiceImpl target;

    @Mock
    private PlatOnClient platOnClient;

    @Mock
    private Web3jWrapper web3jWrapper;

    @Mock
    private Web3j web3j;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(this.target, "platOnClient", this.platOnClient);
        when(this.platOnClient.getWeb3jWrapper()).thenReturn(this.web3jWrapper);
        when(this.web3jWrapper.getWeb3j()).thenReturn(this.web3j);
    }

    @Test
    public void test_getBalance() throws Exception {
        ERC20Client erc20Client = mock(ERC20Client.class);
        PowerMockito.whenNew(ERC20Client.class)
                .withArguments("123", this.platOnClient.getWeb3jWrapper().getWeb3j(),
                        Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7"),
                        NetworkParms.getChainId())
                .thenReturn(erc20Client);
        RemoteCall<BigInteger> remoteCall = mock(RemoteCall.class);

        when(erc20Client.balanceOf(any())).thenReturn(remoteCall);
        when(remoteCall.send()).thenReturn(BigInteger.ZERO);
        BigInteger balance = this.target.getBalance("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj", "lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
        Assert.assertEquals(balance.longValue(), BigInteger.ZERO.longValue());
    }

}
