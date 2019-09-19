package com.platon.browser.service;

import com.platon.browser.TestBase;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.engine.stage.BlockChainStage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/7 09:32
 * @Description: 区块服务单元测试
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class DbServiceTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(DbServiceTest.class);

    @Spy
    private DbService dbService;

    @Mock
    private BlockMapper blockMapper;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private RpPlanMapper rpPlanMapper;
    @Mock
    private CustomNodeMapper customNodeMapper;
    @Mock
    private CustomStakingMapper customStakingMapper;
    @Mock
    private CustomDelegationMapper customDelegationMapper;
    @Mock
    private CustomUnDelegationMapper customUnDelegationMapper;
    @Mock
    private CustomNodeOptMapper customNodeOptMapper;
    @Mock
    private CustomSlashMapper customSlashMapper;
    @Mock
    private CustomProposalMapper customProposalMapper;
    @Mock
    private CustomVoteMapper customVoteMapper;
    @Mock
    private CustomAddressMapper customAddressMapper;
    @Mock
    private CustomNetworkStatMapper customNetworkStatMapper;

    @Mock
    private BlockCacheService blockCacheService;
    @Mock
    private TransactionCacheService transactionCacheService;
    @Mock
    private NetworkStatCacheService networkStatCacheService;

    /**
     * 测试开始前，设置相关行为属性
     */
    @Before
    public void setup() throws InterruptedException {
        ReflectionTestUtils.setField(dbService, "blockMapper", blockMapper);
        ReflectionTestUtils.setField(dbService, "transactionMapper", transactionMapper);
        ReflectionTestUtils.setField(dbService, "rpPlanMapper", rpPlanMapper);
        ReflectionTestUtils.setField(dbService, "customNodeMapper", customNodeMapper);
        ReflectionTestUtils.setField(dbService, "customStakingMapper", customStakingMapper);
        ReflectionTestUtils.setField(dbService, "customDelegationMapper", customDelegationMapper);
        ReflectionTestUtils.setField(dbService, "customUnDelegationMapper", customUnDelegationMapper);
        ReflectionTestUtils.setField(dbService, "customNodeOptMapper", customNodeOptMapper);
        ReflectionTestUtils.setField(dbService, "customSlashMapper", customSlashMapper);
        ReflectionTestUtils.setField(dbService, "customProposalMapper", customProposalMapper);
        ReflectionTestUtils.setField(dbService, "customVoteMapper", customVoteMapper);
        ReflectionTestUtils.setField(dbService, "customAddressMapper", customAddressMapper);
        ReflectionTestUtils.setField(dbService, "customNetworkStatMapper", customNetworkStatMapper);
        ReflectionTestUtils.setField(dbService, "blockCacheService", blockCacheService);
        ReflectionTestUtils.setField(dbService, "transactionCacheService", transactionCacheService);
        ReflectionTestUtils.setField(dbService, "networkStatCacheService", networkStatCacheService);
    }

    /**
     * 执行区块搜集测试
     */
    @Test
    public void testInsertOrUpdate() {
        BlockChainStage bcs = new BlockChainStage();
        bcs.getStakingStage().insertNode(nodes.get(0));
        bcs.getStakingStage().insertStaking(stakings.get(0));
        bcs.getStakingStage().insertDelegation(delegations.get(0));
        bcs.getStakingStage().insertUnDelegation(unDelegations.get(0));
        dbService.insertOrUpdate(blocks,bcs);

        bcs = new BlockChainStage();
        bcs.getStakingStage().updateNode(nodes.get(0));
        bcs.getStakingStage().updateStaking(stakings.get(0));
        bcs.getStakingStage().updateDelegation(delegations.get(0));
        bcs.getStakingStage().updateUnDelegation(unDelegations.get(0));
        dbService.insertOrUpdate(blocks,bcs);
        verify(dbService, times(2)).insertOrUpdate(anyList(),any(BlockChainStage.class));

    }
}
