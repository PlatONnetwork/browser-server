package com.platon.browser.task;

import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.*;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 20:29
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class BlockSyncTaskTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(BlockSyncTaskTest.class);

    @Spy
    private BlockSyncTask target;
    @Mock
    private CustomBlockMapper customBlockMapper;
    @Mock
    private DbService dbService;
    @Mock
    private BlockChain blockChain;
    @Mock
    private BlockService blockService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private CandidateService candidateService;
    @Mock
    private CacheHolder cacheHolder;
    @Mock
    private AddressCacheUpdater addressCacheUpdater;
    @Mock
    private StakingCacheUpdater stakingCacheUpdater;
    @Mock
    private TaskCacheService taskCacheService;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(target, "cacheHolder", cacheHolder);
        ReflectionTestUtils.setField(target, "customBlockMapper", customBlockMapper);
        ReflectionTestUtils.setField(target, "dbService", dbService);
        ReflectionTestUtils.setField(target, "blockChain", blockChain);
        ReflectionTestUtils.setField(target, "blockService", blockService);
        ReflectionTestUtils.setField(target, "transactionService", transactionService);
        ReflectionTestUtils.setField(target, "candidateService", candidateService);
        ReflectionTestUtils.setField(target, "collectBatchSize", 10);
        ReflectionTestUtils.setField(target, "stakingCacheUpdater", stakingCacheUpdater);
        ReflectionTestUtils.setField(target, "addressCacheUpdater", addressCacheUpdater);
        ReflectionTestUtils.setField(target, "taskCacheService", taskCacheService);
    }

    @Test
    public void testInit() throws Exception {

        NodeCache nodeCache = new NodeCache();
        nodeCache.init(nodes,stakings,delegations,unDelegations);
        when(cacheHolder.getNodeCache()).thenReturn(nodeCache);

        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        ProposalCache proposalCache = mock(ProposalCache.class);
        when(cacheHolder.getProposalCache()).thenReturn(proposalCache);

        CandidateService.CandidateResult cr = new CandidateService.CandidateResult();
        cr.setPre(verifiers);
        cr.setCur(verifiers);
        when(candidateService.getVerifiers(anyLong())).thenReturn(cr);
        when(candidateService.getValidators(anyLong())).thenReturn(cr);
        when(blockChain.getPreVerifier()).thenReturn(new HashMap<>());
        when(blockChain.getCurVerifier()).thenReturn(new HashMap<>());
        when(blockChain.getPreValidator()).thenReturn(new HashMap<>());
        when(blockChain.getCurValidator()).thenReturn(new HashMap<>());

        BlockChainStage bcs = new BlockChainStage();
        bcs.getStakingStage().insertNode(nodes.get(0));
        bcs.getStakingStage().insertStaking(stakings.get(0));
        bcs.getStakingStage().insertDelegation(delegations.get(0));
        bcs.getStakingStage().insertUnDelegation(unDelegations.get(0));
        when(blockChain.exportResult()).thenReturn(bcs);

        List<Address> addrList = new ArrayList<>(addresses);

        target.init();

        when(customBlockMapper.selectMaxBlockNumber()).thenReturn(null);
        CandidateService.InitParam initParam = new CandidateService.InitParam();
        initParam.setNodes(nodes);
        initParam.setStakings(stakings);
        when(candidateService.getInitParam()).thenReturn(initParam);
        target.init();
        verify(target, times(2)).init();
    }

    @Test
    public void testCollect() throws Exception {
        when(blockService.getLatestNumber()).thenReturn(BigInteger.TEN);
        boolean success = target.collect();
        assertTrue(success);
    }
}
