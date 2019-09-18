package com.platon.browser.engine.handler.epoch;

import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.service.CandidateService;
import com.platon.browser.utils.HexTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.platon.bean.Node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 20:29
 * @Description: 共识周期切换
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class NewConsensusEpochHandlerTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(NewConsensusEpochHandlerTest.class);
    @Spy
    private NewConsensusEpochHandler handler;
    @Mock
    private BlockChain bc;
    @Mock
    private PlatonClient client;
    @Mock
    private SpecialContractApi sca;
    @Mock
    private CandidateService candidateService;
    @Mock
    private CacheHolder cacheHolder;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(handler, "bc", bc);
        ReflectionTestUtils.setField(handler, "client", client);
        ReflectionTestUtils.setField(handler, "sca", sca);
        ReflectionTestUtils.setField(handler, "candidateService", candidateService);
        ReflectionTestUtils.setField(handler, "cacheHolder", cacheHolder);
    }

    @Test
    public void testHandle() throws Exception {
        NodeCache nodeCache = mock(NodeCache.class);
        when(cacheHolder.getNodeCache()).thenReturn(nodeCache);
        when(nodeCache.getNode(anyString())).thenReturn(nodes.get(0));
        when(nodeCache.getStakingByStatus(any(CustomStaking.StatusEnum.class))).thenReturn(stakings);
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        when(bc.getCurBlock()).thenReturn(blocks.get(0));
        when(bc.getCurSettingEpoch()).thenReturn(BigInteger.ONE);
        when(bc.getCurConsensusExpectBlockCount()).thenReturn(BigDecimal.TEN);
        Map<String, Node> validatorMap = new HashMap<>();
        validators.forEach(validator->validatorMap.put(HexTool.prefix(validator.getNodeId()),validator));
        when(bc.getCurValidator()).thenReturn(validatorMap);

        // updateValidator()
        when(sca.getHistoryValidatorList(any(),any(BigInteger.class))).thenReturn(validators);
        when(candidateService.getCurValidators()).thenReturn(validators);

        EventContext context = new EventContext();
        handler.handle(context);

        verify(handler, times(1)).handle(any(EventContext.class));
    }
}
