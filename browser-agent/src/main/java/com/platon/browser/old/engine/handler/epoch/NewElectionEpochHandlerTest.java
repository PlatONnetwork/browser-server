package com.platon.browser.old.engine.handler.epoch;

import com.platon.browser.TestBase;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.stage.BlockChainStage;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 选举周期切换
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class NewElectionEpochHandlerTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(NewConsensusEpochHandlerTest.class);
    @Spy
    private NewElectionEpochHandler handler;
    @Mock
    private BlockChain bc;
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private CacheHolder cacheHolder;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(handler, "bc", bc);
        ReflectionTestUtils.setField(handler, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(handler, "cacheHolder", cacheHolder);
    }

    @Test
    public void testHandle() throws Exception {
        NodeCache nodeCache = mock(NodeCache.class);
        when(cacheHolder.getNodeCache()).thenReturn(nodeCache);
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        when(nodeCache.getStakingByStatus(any(CustomStaking.StatusEnum.class))).thenReturn(stakings);
        when(bc.getCurBlock()).thenReturn(blocks.get(0));
        when(bc.getCurSettingEpoch()).thenReturn(BigInteger.ONE);
        when(bc.getCurConsensusExpectBlockCount()).thenReturn(BigDecimal.TEN);
        Map<String, Node> validatorMap = new HashMap<>();
        validators.forEach(validator->validatorMap.put(HexTool.prefix(validator.getNodeId()),validator));
        when(bc.getPreValidator()).thenReturn(validatorMap);
        when(nodeCache.getNode(anyString())).thenReturn(nodes.get(0));
        when(bc.getBlockReward()).thenReturn(BigDecimal.valueOf(2000000));
        when(bc.getCurSettingEpoch()).thenReturn(BigInteger.ONE);
        when(chainConfig.getStakeThreshold()).thenReturn(BigDecimal.valueOf(10000000L));

        EventContext context = new EventContext();
        handler.handle(context);

        stakings.forEach(staking -> staking.setPreConsBlockQty(0L));
        handler.handle(context);

        when(bc.getPreValidator()).thenReturn(new HashMap<>());
        handler.handle(context);

        verify(handler, times(3)).handle(any(EventContext.class));
    }
}
