package com.platon.browser.erc.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.dto.ERCData;
import com.platon.browser.utils.NetworkParms;

/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-09-29 16:42
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ERCClientTest {

    @Spy
    private ERCClient target;

    @Mock
    private PlatOnClient platOnClient;

    @Mock
    private Web3jWrapper web3jWrapper;

    @Mock
    private Web3j web3j;

    @Mock
    private Erc20TokenMapper erc20TokenMapper;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(this.target, "platOnClient", this.platOnClient);
        ReflectionTestUtils.setField(this.target, "erc20TokenMapper", this.erc20TokenMapper);
        when(this.platOnClient.getWeb3jWrapper()).thenReturn(this.web3jWrapper);
        when(this.web3jWrapper.getWeb3j()).thenReturn(this.web3j);
    }

    @Test
    public void test_getErcData() throws Exception {
        ERC20Client erc20Client = mock(ERC20Client.class);
        PowerMockito.whenNew(ERC20Client.class)
            .withArguments("123", this.platOnClient.getWeb3jWrapper().getWeb3j(),
                Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7"),
                NetworkParms.getChainId())
            .thenReturn(erc20Client);
        RemoteCall<String> remoteCall = mock(RemoteCall.class);

        when(erc20Client.name()).thenReturn(remoteCall);
        when(remoteCall.send()).thenReturn("1");
        when(erc20Client.name().send()).thenReturn("1");
        ERCData ercData = this.target.getErcData("123");
    }

    // @Test
    // public void test_initContractData() throws Exception {
    // ERC20Client erc20Client = mock(ERC20Client.class);
    // PowerMockito.whenNew(ERC20Client.class)
    // .withArguments("123", this.platOnClient.getWeb3jWrapper().getWeb3j(),
    // Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7"),
    // NetworkParms.getChainId())
    // .thenReturn(erc20Client);
    // RemoteCall<String> remoteCall = mock(RemoteCall.class);
    //
    // when(erc20Client.name()).thenReturn(remoteCall);
    // when(remoteCall.send()).thenReturn("1");
    // when(erc20Client.name().send()).thenReturn("1");
    // ERCData ercData = this.target.initContractData("123");
    // }

}
