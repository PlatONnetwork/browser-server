package com.platon.browser.old.engine.handler.proposal;//package com.platon.browser.engine.handler.proposal;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.config.BlockChainConfig;
//import com.platon.browser.dto.CustomNode;
//import com.platon.browser.dto.CustomTransaction;
//import com.platon.browser.engine.BlockChain;
//import com.platon.browser.engine.cache.CacheHolder;
//import com.platon.browser.engine.cache.NodeCache;
//import com.platon.browser.engine.cache.ProposalCache;
//import com.platon.browser.engine.handler.EventContext;
//import com.platon.browser.engine.stage.BlockChainStage;
//import com.platon.browser.exception.BeanCreateOrUpdateException;
//import com.platon.browser.exception.BusinessException;
//import com.platon.browser.exception.CacheConstructException;
//import com.platon.browser.exception.NoSuchBeanException;
//import com.platon.browser.utils.HexTool;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.web3j.platon.bean.Node;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
///**
// * @Auther: dongqile
// * @Date: 2019/9/5
// * @Description: 文本提案业务测试类
// */
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class ProposalTextHandlerTest extends TestBase {
//    private static Logger logger = LoggerFactory.getLogger(ProposalTextHandlerTest.class);
//    @Spy
//    private ProposalTextHandler handler;
//    @Mock
//    private BlockChain bc;
//    @Mock
//    private BlockChainConfig chainConfig;
//    @Mock
//    private CacheHolder cacheHolder;
//
//    /**
//     * 测试开始前，设置相关行为属性
//     * @throws IOException
//     * @throws BeanCreateOrUpdateException
//     */
//    @Before
//    public void setup() {
//        ReflectionTestUtils.setField(handler, "bc", bc);
//        ReflectionTestUtils.setField(handler, "chainConfig", chainConfig);
//        ReflectionTestUtils.setField(handler, "cacheHolder", cacheHolder);
//    }
//
//    /**
//     *  文本提案测试方法
//     */
//    @Test
//    public void testHandle () throws BusinessException, NoSuchBeanException {
//        NodeCache nodeCache = mock(NodeCache.class);
//        when(cacheHolder.getNodeCache()).thenReturn(nodeCache);
//        BlockChainStage stageData = new BlockChainStage();
//        when(cacheHolder.getStageData()).thenReturn(stageData);
//        ProposalCache proposalCache = mock(ProposalCache.class);
//        when(cacheHolder.getProposalCache()).thenReturn(proposalCache);
//        when(proposalCache.getProposal(any())).thenReturn(proposals.get(0));
//        CustomNode node = mock(CustomNode.class);
//        when(nodeCache.getNode(any())).thenReturn(node);
//        when(node.getLatestStaking()).thenReturn(stakings.get(0));
//        Map<String, Node> validatorMap = new HashMap<>();
//        validators.forEach(validator->validatorMap.put(HexTool.prefix(validator.getNodeId()),validator));
//        when(bc.getCurValidator()).thenReturn(validatorMap);
//        when(chainConfig.getProposalUrlTemplate()).thenReturn("https://wwww.platon.network");
//        when(chainConfig.getConsensusPeriodBlockCount()).thenReturn(BigInteger.valueOf(250));
//        when(chainConfig.getVersionProposalActiveConsensusRounds()).thenReturn(BigDecimal.valueOf(20));
//
//
//        EventContext context = new EventContext();
//
//        transactions.stream()
//                .filter(tx->CustomTransaction.TxTypeEnum.CREATE_PROPOSAL_TEXT.getCode().equals(tx.getTxType()))
//                .forEach(context::setTransaction);
//        handler.handle(context);
//
//        verify(handler, times(1)).handle(any(EventContext.class));
//    }
//}
