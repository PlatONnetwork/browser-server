package com.platon.browser.engine.handler.statistic;

import com.platon.browser.TestBase;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomNetworkStat;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.CacheConstructException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.utils.HexTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.platon.bean.Node;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 节点统计业务测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class NetworkStatStatisticHandlerTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(NetworkStatStatisticHandlerTest.class);
    @Spy
    private NetworkStatStatisticHandler handler;
    @Mock
    private CacheHolder cacheHolder;
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private BlockChain bc;

    /**
     * 测试开始前，设置相关行为属性
     *
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        ReflectionTestUtils.setField(handler, "cacheHolder", cacheHolder);
        ReflectionTestUtils.setField(handler, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(handler, "bc", bc);
    }

    /**
     * 节点统计测试方法
     */
    @Test
    public void testHandler () throws CacheConstructException, NoSuchBeanException {
        NodeCache nodeCache = mock(NodeCache.class);
        when(cacheHolder.getNodeCache()).thenReturn(nodeCache);
        CustomNode node = mock(CustomNode.class);
        when(nodeCache.getNode(anyString())).thenReturn(node);
        when(node.getLatestStaking()).thenReturn(stakings.get(0));
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        CustomNetworkStat networkStatCache = new CustomNetworkStat();
        when(cacheHolder.getNetworkStatCache()).thenReturn(networkStatCache);
        when(bc.getCurBlock()).thenReturn(blocks.get(0));
        when(chainConfig.getAddIssuePeriodBlockCount()).thenReturn(BigInteger.valueOf(21500));
        when(bc.getAddIssueEpoch()).thenReturn(BigInteger.valueOf(4));
        when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.valueOf(160));
        when(bc.getCurSettingEpoch()).thenReturn(BigInteger.valueOf(2));
        when(bc.getSettleReward()).thenReturn(BigDecimal.valueOf(2000000));
        when(bc.getBlockReward()).thenReturn(BigDecimal.valueOf(300000));
        Map<String, Node> verifierMap = new HashMap<>();
        verifiers.forEach(verifier->verifierMap.put(HexTool.prefix(verifier.getNodeId()),verifier));
        when(bc.getPreVerifier()).thenReturn(verifierMap);

        ProposalCache proposalCache = mock(ProposalCache.class);
        when(cacheHolder.getProposalCache()).thenReturn(proposalCache);
        when(proposalCache.getAllProposal()).thenReturn(proposals);

        EventContext context = new EventContext();

        handler.handle(context);

        verify(handler, times(1)).handle(any(EventContext.class));
    }
}
