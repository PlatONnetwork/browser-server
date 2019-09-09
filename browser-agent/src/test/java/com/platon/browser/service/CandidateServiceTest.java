package com.platon.browser.service;

import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.engine.BlockChain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.platon.bean.Node;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.verify;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 20:30
 * @Description:
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { CandidateService.class })
public class CandidateServiceTest extends TestBase {

    @Mock
    private CandidateService candidateService;
    @Mock
    private BlockChain blockChain;
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private PlatonClient client;
    @Mock
    private DbService dbService;

    @Before
    public void setup() throws Exception {
        mockStatic(SpecialContractApi.class);

        List<Node> verifiers = new ArrayList<>();
        Node node = new Node();
        verifiers.add(node);
        when(SpecialContractApi
                .getHistoryValidatorList(Mockito.any(Web3j.class),Mockito.any(BigInteger.class)).data)
                .thenReturn(verifiers);
        List<Node> result = SpecialContractApi.getHistoryValidatorList(mock(Web3j.class),mock(BigInteger.class)).data;
        assertEquals(result,verifiers);
        verify(SpecialContractApi.class);
    }

    @Test
    public void testInit() throws Exception {
        candidateService.init();
    }

}
