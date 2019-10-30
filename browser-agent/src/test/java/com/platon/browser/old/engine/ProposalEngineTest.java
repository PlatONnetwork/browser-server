package com.platon.browser.old.engine;

import com.platon.browser.TestBase;
import com.platon.browser.dao.mapper.CustomProposalMapper;
import com.platon.browser.dao.mapper.CustomVoteMapper;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.old.engine.cache.CacheHolder;
import com.platon.browser.old.engine.cache.ProposalCache;
import com.platon.browser.old.engine.handler.proposal.*;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.old.exception.BusinessException;
import com.platon.browser.old.exception.CacheConstructException;
import com.platon.browser.exception.NoSuchBeanException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 委托处理业务测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalEngineTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(ProposalEngineTest.class);
    @Spy
    private ProposalEngine target;
    @Mock
    private CustomProposalMapper customProposalMapper;
    @Mock
    private CustomVoteMapper customVoteMapper;
    @Mock
    private CacheHolder cacheHolder;

    /*********************业务事件处理器*********************/

    @Mock
    private ProposalTextHandler proposalTextHandler;
    @Mock
    private ProposalCancelHandler proposalCancelHandler;
    @Mock
    private ProposalUpgradeHandler proposalUpgradeHandler;
    @Mock
    private VotingProposalHandler votingProposalHandler;
    @Mock
    private DeclareVersionHandler declareVersionHandler;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "customProposalMapper", customProposalMapper);
        ReflectionTestUtils.setField(target, "customVoteMapper", customVoteMapper);
        ReflectionTestUtils.setField(target, "cacheHolder", cacheHolder);
        ReflectionTestUtils.setField(target, "proposalTextHandler", proposalTextHandler);
        ReflectionTestUtils.setField(target, "proposalCancelHandler", proposalCancelHandler);
        ReflectionTestUtils.setField(target, "proposalUpgradeHandler", proposalUpgradeHandler);
        ReflectionTestUtils.setField(target, "votingProposalHandler", votingProposalHandler);
        ReflectionTestUtils.setField(target, "declareVersionHandler", declareVersionHandler);
    }

    @Test
    public void testInit () throws CacheConstructException {
        ProposalCache proposalCache = mock(ProposalCache.class);
        when(cacheHolder.getProposalCache()).thenReturn(proposalCache);
        when(customProposalMapper.selectAll()).thenReturn(proposals);
        when(customVoteMapper.selectAll()).thenReturn(Collections.emptyList());

        target.init();
        verify(target, times(1)).init();
    }

    @Test
    public void testExecute () throws NoSuchBeanException, BusinessException {
        target.execute(transactions.get(0));
        verify(target, times(1)).execute(any(CustomTransaction.class));
    }
}
