package com.platon.browser.old.engine.handler.epoch;//package com.platon.browser.engine.handler.epoch;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.client.PlatOnClient;
//import com.platon.browser.client.SpecialContractApi;
//import com.platon.browser.config.BlockChainConfig;
//import com.platon.browser.dto.CustomDelegation;
//import com.platon.browser.dto.CustomUnDelegation;
//import com.platon.browser.engine.BlockChain;
//import com.platon.browser.engine.cache.CacheHolder;
//import com.platon.browser.engine.cache.NodeCache;
//import com.platon.browser.engine.handler.EventContext;
//import com.platon.browser.engine.stage.BlockChainStage;
//import com.platon.browser.service.CandidateService;
//import com.platon.browser.utils.HexTool;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.web3j.platon.bean.Node;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.http.HttpService;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//
///**
// * @Auther: dongqile
// * @Date: 2019/9/5
// * @Description:
// */
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class NewSettleEpochHandlerTest extends TestBase {
//    private static Logger logger = LoggerFactory.getLogger(NewConsensusEpochHandlerTest.class);
//    @Spy
//    private NewSettleEpochHandler handler;
//    @Mock
//    private BlockChain bc;
//    @Mock
//    private BlockChainConfig chainConfig;
//    @Mock
//    private CacheHolder cacheHolder;
//    @Mock
//    private CandidateService candidateService;
//    @Mock
//    private SpecialContractApi sca;
//    @Mock
//    private PlatOnClient client;
//
//    @Before
//    public void setup() {
//        ReflectionTestUtils.setField(handler, "bc", bc);
//        ReflectionTestUtils.setField(handler, "chainConfig", chainConfig);
//        ReflectionTestUtils.setField(handler, "cacheHolder", cacheHolder);
//        ReflectionTestUtils.setField(handler, "candidateService", candidateService);
//        ReflectionTestUtils.setField(handler, "sca", sca);
//        ReflectionTestUtils.setField(handler, "client", client);
//    }
//
//    @Test
//    public void testHandle() throws Exception {
//        NodeCache nodeCache = mock(NodeCache.class);
//        when(cacheHolder.getNodeCache()).thenReturn(nodeCache);
//        BlockChainStage stageData = new BlockChainStage();
//        when(cacheHolder.getStageData()).thenReturn(stageData);
//        when(bc.getCurBlock()).thenReturn(blocks.get(0));
//        when(client.getWeb3j()).thenReturn(Web3j.build(new HttpService("")));
//        when(sca.getHistoryVerifierList(any(Web3j.class),any(BigInteger.class))).thenReturn(verifiers);
//        Map<String, Node> verifierMap = new HashMap<>();
//        verifiers.forEach(verifier->verifierMap.put(HexTool.prefix(verifier.getNodeId()),verifier));
//        when(bc.getPreVerifier()).thenReturn(verifierMap);
//        when(candidateService.getCurVerifiers()).thenReturn(verifiers);
//        when(bc.getCurVerifier()).thenReturn(verifierMap);
//        when(nodeCache.getDelegationByIsHistory(any(CustomDelegation.YesNoEnum.class))).thenReturn(delegations);
//        when(nodeCache.getUnDelegationByStatus(any(CustomUnDelegation.StatusEnum.class))).thenReturn(unDelegations);
//
//        when(bc.getSettleReward()).thenReturn(BigDecimal.valueOf(2000000));
//        when(bc.getCurSettingEpoch()).thenReturn(BigInteger.TEN);
//        when(chainConfig.getUnStakeRefundSettlePeriodCount()).thenReturn(BigInteger.ONE);
//        when(nodeCache.getNode(anyString())).thenReturn(nodes.get(0));
//        when(chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat()).thenReturn(BigInteger.ONE);
//        when(chainConfig.getSettlePeriodCountPerIssue()).thenReturn(BigInteger.TEN);
//
//        EventContext context = new EventContext();
//        handler.handle(context);
//
//        verify(handler, times(1)).handle(any(EventContext.class));
//    }
//}
