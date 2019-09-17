package com.platon.browser.task;

import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.CandidateService;
import com.platon.browser.service.DbService;
import com.platon.browser.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;

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
    private BlockSyncTask blockSyncTask;
    @Mock
    private CustomBlockMapper customBlockMapper;
    @Mock
    private DbService dbService;
    @Mock
    private BlockChain blockChain;
    @Mock
    private PlatonClient client;
    @Mock
    private BlockService blockService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private CandidateService candidateService;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(blockSyncTask, "customBlockMapper", customBlockMapper);
        ReflectionTestUtils.setField(blockSyncTask, "dbService", dbService);
        ReflectionTestUtils.setField(blockSyncTask, "blockChain", blockChain);
        ReflectionTestUtils.setField(blockSyncTask, "client", client);
        ReflectionTestUtils.setField(blockSyncTask, "blockService", blockService);
        ReflectionTestUtils.setField(blockSyncTask, "transactionService", transactionService);
        ReflectionTestUtils.setField(blockSyncTask, "candidateService", candidateService);
        ReflectionTestUtils.setField(blockSyncTask, "collectBatchSize", 10);
    }

    @Test
    public void testInit() throws Exception {
        CandidateService.CandidateResult cr = new CandidateService.CandidateResult();
        cr.setPre(verifiers);
        cr.setCur(verifiers);
        when(candidateService.getVerifiers(anyLong())).thenReturn(cr);
        when(candidateService.getValidators(anyLong())).thenReturn(cr);
        when(blockChain.getPreVerifier()).thenReturn(new HashMap<>());
        when(blockChain.getCurVerifier()).thenReturn(new HashMap<>());
        when(blockChain.getPreValidator()).thenReturn(new HashMap<>());
        when(blockChain.getCurValidator()).thenReturn(new HashMap<>());
        blockSyncTask.init();

        verify(blockSyncTask, times(1)).init();

        when(customBlockMapper.selectMaxBlockNumber()).thenReturn(null);
        CandidateService.InitParam initParam = new CandidateService.InitParam();
        initParam.setNodes(nodes);
        initParam.setStakings(stakings);
        when(candidateService.getInitParam()).thenReturn(initParam);
        blockSyncTask.init();
    }
}
