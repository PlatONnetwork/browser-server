//package com.platon.browser.service;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.client.PlatOnClient;
//import com.platon.browser.client.SpecialContractApi;
//import com.platon.browser.config.BlockChainConfig;
//import com.platon.browser.old.engine.BlockChain;
//import com.platon.browser.old.service.CandidateService;
//import com.platon.browser.old.service.DbService;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.web3j.platon.contracts.NodeContract;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//
///**
// * @Auther: Chendongming
// * @Date: 2019/9/9 20:30
// * @Description:
// */
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class CandidateServiceTest extends TestBase {
//
//    @Mock
//    private CandidateService candidateService;
//    @Mock
//    private BlockChain blockChain;
//    @Mock
//    private BlockChainConfig chainConfig;
//    @Mock
//    private PlatOnClient client;
//    @Mock
//    private NodeContract nodeContract;
//    @Mock
//    private DbService dbService;
//    @Mock
//    private SpecialContractApi sca;
//
//    @Rule
//    public ExpectedException thrown= ExpectedException.none();
//
//    @Before
//    public void setup() throws Exception {
//        ReflectionTestUtils.setField(candidateService, "blockChain", blockChain);
//        ReflectionTestUtils.setField(candidateService, "chainConfig", chainConfig);
//        ReflectionTestUtils.setField(candidateService, "client", client);
//        ReflectionTestUtils.setField(candidateService, "sca", sca);
//        when(sca.getHistoryVerifierList(any(),any(BigInteger.class))).thenReturn(verifiers);
//        when(sca.getHistoryValidatorList(any(),any(BigInteger.class))).thenReturn(validators);
//        when(candidateService.getCurCandidates()).thenReturn(candidates);
//    }
//
//    @Test
//    public void testInitParam() throws Exception {
//        when(candidateService.getInitParam()).thenCallRealMethod();
//        when(chainConfig.getExpectBlockCount()).thenReturn(BigInteger.TEN);
//        when(candidateService.getCurVerifiers()).thenReturn(verifiers);
//        when(chainConfig.getDefaultStakingLockedAmount()).thenReturn(BigDecimal.valueOf(10000000));
//        when(blockChain.getCurConsensusExpectBlockCount()).thenReturn(BigDecimal.TEN);
//        CandidateService.InitParam initParam = candidateService.getInitParam();
//        assertEquals(verifiers.size(),initParam.getNodes().size());
//    }
//
//    @Test
//    public void testGetVerifiers() throws Exception {
//        // 模拟正常流程
//        when(blockChain.getCurSettingEpoch()).thenReturn(BigInteger.valueOf(2));
//        when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.valueOf(1600));
//        when(candidateService.getCurVerifiers()).thenReturn(verifiers);
//        when(candidateService.getVerifiers(anyLong())).thenCallRealMethod();
//        CandidateService.CandidateResult cr = candidateService.getVerifiers(20000L);
//        assertEquals(cr.getPre().size(),verifiers.size());
//    }
//
//    @Test
//    public void testGetValidators() throws Exception {
//        when(blockChain.getCurConsensusEpoch()).thenReturn(BigInteger.valueOf(2));
//        when(chainConfig.getConsensusPeriodBlockCount()).thenReturn(BigInteger.valueOf(40));
//        when(candidateService.getCurValidators()).thenReturn(validators);
//        when(candidateService.getValidators(anyLong())).thenCallRealMethod();
//        CandidateService.CandidateResult cr = candidateService.getValidators(20000L);
//        assertEquals(cr.getPre().size(),validators.size());
//    }
//}
