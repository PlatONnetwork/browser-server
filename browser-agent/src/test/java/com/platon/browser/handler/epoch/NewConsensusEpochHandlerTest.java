//package com.platon.browser.handler.epoch;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.client.PlatonClient;
//import com.platon.browser.client.SpecialContractApi;
//import com.platon.browser.dto.CustomNode;
//import com.platon.browser.engine.BlockChain;
//import com.platon.browser.engine.cache.NodeCache;
//import com.platon.browser.engine.handler.EventContext;
//import com.platon.browser.engine.handler.epoch.NewConsensusEpochHandler;
//import com.platon.browser.engine.stage.StakingStage;
//import com.platon.browser.exception.CacheConstructException;
//import com.platon.browser.service.CandidateService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.powermock.api.mockito.PowerMockito;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.web3j.platon.bean.Node;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.platon.browser.engine.cache.CacheTool.STAGE_DATA;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//
///**
// * @Auther: Chendongming
// * @Date: 2019/9/9 20:29
// * @Description:
// */
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class NewConsensusEpochHandlerTest extends TestBase {
//    private static Logger logger = LoggerFactory.getLogger(NewConsensusEpochHandlerTest.class);
//    @Spy
//    private NewConsensusEpochHandler newConsensusEpochHandler;
//    @Mock
//    private BlockChain bc;
//    @Mock
//    private PlatonClient client;
//    @Mock
//    private SpecialContractApi sca;
//    @Mock
//    private CandidateService candidateService;
//
//    @Before
//    public void setup() throws Exception {
//        ReflectionTestUtils.setField(newConsensusEpochHandler, "bc", bc);
//        ReflectionTestUtils.setField(newConsensusEpochHandler, "client", client);
//        ReflectionTestUtils.setField(newConsensusEpochHandler, "sca", sca);
//        ReflectionTestUtils.setField(newConsensusEpochHandler, "candidateService", candidateService);
//        when(bc.getCurBlock()).thenReturn(blocks.get(0));
//        when(bc.getCurConsensusExpectBlockCount()).thenReturn(BigDecimal.TEN);
//
//        NodeCache nodeCache = spy(NODE_CACHE);
//        // updateStaking()
//        doReturn(nodes.get(0)).when(nodeCache).getNode(anyString());
//        Map<String, Node> validatorMap = new HashMap<>();
//        validators.forEach(validator->validatorMap.put(validator.getNodeId(),validator));
//        when(bc.getCurValidator()).thenReturn(validatorMap);
//
//        // updateValidator()
//        when(sca.getHistoryValidatorList(any(),any(BigInteger.class))).thenReturn(validators);
//        when(candidateService.getCurValidators()).thenReturn(validators);
//    }
//
//    @Test
//    public void testUpdateStaking() throws Exception {
//
//
//        EventContext context = new EventContext();
//        context.setStakingStage(STAGE_DATA.getStakingStage());
//        newConsensusEpochHandler.handle(context);
//
//        verify(newConsensusEpochHandler, times(1)).handle(any(EventContext.class));
//    }
//}
