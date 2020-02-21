package com.platon.browser.now.service.impl;

import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.config.RedisFactory;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Slash;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.mapper.SlashMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.DelegationRewardESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.now.service.CommonService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.redis.RedisCommands;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.staking.QueryClaimByStakingReq;
import com.platon.browser.util.I18nUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionServiceTest {

	@Mock
	private TransactionESRepository transactionESRepository;
	@Mock
	private DelegationRewardESRepository delegationRewardESRepository;
	@Mock
	private I18nUtil i18n;
	@Mock
	private StakingMapper stakingMapper;
	@Mock
	private SlashMapper slashMapper;
	@Mock
	private ProposalMapper proposalMapper;
	@Mock
	private StatisticCacheService statisticCacheService;
	@Mock
	private BlockChainConfig blockChainConfig;
	@Mock
	private CommonService commonService;
	@Mock
	private RedisFactory redisFactory;

	@Spy
    private TransactionServiceImpl target;

	@Before
	public void setup() {
		ReflectionTestUtils.setField(target,"transactionESRepository",transactionESRepository);
		ReflectionTestUtils.setField(target,"delegationRewardESRepository",delegationRewardESRepository);
		ReflectionTestUtils.setField(target,"i18n",i18n);
		ReflectionTestUtils.setField(target,"stakingMapper",stakingMapper);
		ReflectionTestUtils.setField(target,"slashMapper",slashMapper);
		ReflectionTestUtils.setField(target,"proposalMapper",proposalMapper);
		ReflectionTestUtils.setField(target,"statisticCacheService",statisticCacheService);
		ReflectionTestUtils.setField(target,"blockChainConfig",blockChainConfig);
		ReflectionTestUtils.setField(target,"commonService",commonService);
		ReflectionTestUtils.setField(target,"redisFactory",redisFactory);
		RedisCommands redisCommands = mock(RedisCommands.class);
		when(redisFactory.createRedisCommands()).thenReturn(redisCommands);
		when(redisCommands.get(anyString())).thenReturn("test");
		when(commonService.getNodeName(any(),any())).thenReturn("test-name");
	}

	@Test
	public void test() throws IOException {
		TransactionDetailsReq req = new TransactionDetailsReq();
		req.setTxHash("0xddd");
		//doThrow(new IOException()).when(transactionESRepository).get(any(),any());
		//target.transactionDetails(req);
		Transaction transaction = new Transaction();
		transaction.setCost("999");
		transaction.setNum(33L);
		transaction.setGasLimit("3333");
		transaction.setGasUsed("3333");
		transaction.setId(333L);
		transaction.setToType(Transaction.ToTypeEnum.ACCOUNT.getCode());
		transaction.setType(Transaction.TypeEnum.EVM_CONTRACT_CREATE.getCode());
		transaction.setHash("0xddd");
		transaction.setTime(new Date());
		transaction.setInfo("{\"endVotingRound\":10,\"newVersion\":2064,\"nodeName\":\"sansan3.3.3.3\",\"pIDID\":\"S8\",\"verifier\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\"}");
		transaction.setGasPrice("3333");
		transaction.setValue("4333");
		transaction.setStatus(Transaction.StatusEnum.FAILURE.getCode());
		when(transactionESRepository.get(any(),any())).thenReturn(transaction);
		ESResult<Object> first = new ESResult<>();
		List<Object> transactionList = new ArrayList<>();
		transactionList.add(transaction);
		first.setRsData(transactionList);
		first.setTotal(33L);
		when(transactionESRepository.search(any(),any(),anyInt(),anyInt())).thenReturn(first);

		transaction.setType(Transaction.TypeEnum.STAKE_MODIFY.getCode());
		transaction.setInfo("{\"benefitAddress\":\"0x60ceca9c1290ee56b98d4e160ef0453f7c40d219\",\"details\":\"Node of CDM\",\"externalId\":\"5FD68B690010632B\",\"nodeId\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7\",\"nodeName\":\"cdm-004\",\"website\":\"WWW.CCC.COM\",\"DelegateRewardPer\":3}");
		target.transactionDetails(req);

		transaction.setType(Transaction.TypeEnum.STAKE_INCREASE.getCode());
		transaction.setInfo("{\"amount\":5000000000000000000000000,\"nodeId\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7\",\"nodeName\":\"cdm-004\",\"stakingBlockNum\":814,\"type\":0}");
		target.transactionDetails(req);

		transaction.setType(Transaction.TypeEnum.STAKE_EXIT.getCode());
		transaction.setInfo("{\"amount\":5000000000000000000000000,\"nodeId\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\",\"nodeName\":\"sansan33\",\"stakingBlockNum\":50767}");
		Staking staking = new Staking();
		staking.setStakingReduction(BigDecimal.ONE);
		staking.setStatus(CustomStaking.StatusEnum.EXITING.getCode());
		when(stakingMapper.selectByPrimaryKey(any())).thenReturn(staking);
		when(blockChainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.ONE);
		when(blockChainConfig.getUnStakeRefundSettlePeriodCount()).thenReturn(BigInteger.ONE);
		target.transactionDetails(req);
		staking.setStatus(CustomStaking.StatusEnum.EXITED.getCode());
		target.transactionDetails(req);

		transaction.setType(Transaction.TypeEnum.PROPOSAL_TEXT.getCode());
		transaction.setInfo("{\"nodeName\":\"cdm-004\",\"pIDID\":\"11\",\"verifier\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7\"}");
		target.transactionDetails(req);

		transaction.setType(Transaction.TypeEnum.PROPOSAL_UPGRADE.getCode());
		transaction.setInfo("{\"endVotingRound\":10,\"newVersion\":2064,\"nodeName\":\"sansan3.3.3.3\",\"pIDID\":\"S8\",\"verifier\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\"}");
		target.transactionDetails(req);

		transaction.setType(Transaction.TypeEnum.PROPOSAL_CANCEL.getCode());
		transaction.setInfo("{\"canceledProposalID\":\"0x7cb32dc2cc4202388b04641bd5d13372cc0a9a0a7b0d05dcd509232f07493a52\",\"endVotingRound\":12,\"nodeName\":\"sansan33\",\"pIDID\":\"asdfrrrrr\",\"verifier\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\"}");
		Proposal proposal = mock(Proposal.class);
		when(proposalMapper.selectByPrimaryKey(anyString())).thenReturn(proposal);
		target.transactionDetails(req);

		transaction.setType(Transaction.TypeEnum.PROPOSAL_VOTE.getCode());
		transaction.setInfo("{\"nodeName\":\"sansan33\",\"option\":\"2\",\"pIDID\":\"c4\",\"programVersion\":\"1797\",\"proposalId\":\"0xa5abb6a7ebc1b7f5c24952f489c4a95c8091710109cac35ecee1e6c35f33a5af\",\"proposalType\":\"3\",\"verifier\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\",\"versionSign\":\"0xb109d4db2991b0c35d0d1a70b02032eefefc820a7c8077e43859c0799ddf9c681e2a2f316ff9d1f055ea266e5a04a3b9bee2a01f00f3f4da7d3e4e3404b446fa00\"}");
		target.transactionDetails(req);

		transaction.setType(Transaction.TypeEnum.PROPOSAL_VOTE.getCode());
		transaction.setInfo("{\"nodeName\":\"sansan33\",\"option\":\"2\",\"pIDID\":\"c4\",\"programVersion\":\"1797\",\"proposalId\":\"0xa5abb6a7ebc1b7f5c24952f489c4a95c8091710109cac35ecee1e6c35f33a5af\",\"proposalType\":\"3\",\"verifier\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\",\"versionSign\":\"0xb109d4db2991b0c35d0d1a70b02032eefefc820a7c8077e43859c0799ddf9c681e2a2f316ff9d1f055ea266e5a04a3b9bee2a01f00f3f4da7d3e4e3404b446fa00\"}");
		when(proposalMapper.selectByPrimaryKey(anyString())).thenReturn(proposal);
		target.transactionDetails(req);

		transaction.setType(Transaction.TypeEnum.VERSION_DECLARE.getCode());
		transaction.setInfo("{\"activeNode\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7\",\"nodeName\":\"cdm-004\",\"version\":1797,\"versionSigns\":\"0x473f7ab72b2922b325ce5532e64edbbf20dd90431372ff4d3ac48003ff2d64a77255151771eead81daad64edf28e55edb959929c536e523da0737a71201845e400\"}");
		target.transactionDetails(req);

		transaction.setType(Transaction.TypeEnum.REPORT.getCode());
		transaction.setInfo("{\"data\":\"{\\\"prepareA\\\":{\\\"epoch\\\":0,\\\"viewNumber\\\":0,\\\"blockHash\\\":\\\"0xba7748d6811436c3e004ba27bd1baa9bc9a9133cdc4b228a89938f69c9ee5c5b\\\",\\\"blockNumber\\\":41090,\\\"blockIndex\\\":0,\\\"blockData\\\":\\\"0xbf393fb8b2de3befdb3419cf0653f11af5a3d69f44b91ae527c9bc6150234819\\\",\\\"validateNode\\\":{\\\"index\\\":0,\\\"address\\\":\\\"0xa887aa12d6edfef9b3dac37816c88eec6f5af1a8\\\",\\\"nodeId\\\":\\\"4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\\\",\\\"blsPubKey\\\":\\\"e311da21c30acbc9889ac7b5e2e3aef7a4e565074889560e3252d1916ab9235d2e26782dae2c43d2e5ed569d55435a15faf267d8c95cd22fa09603672eea7e73b6469895822475c9876fbd3772a83ac61bc46a52f587b0ab8375f4a9067b8893\\\"},\\\"signature\\\":\\\"0x39f58205d810f39bdf2f5e72e031aec39c1bb4803fd47b823e1e8fe82d8538a808f49709fb3a8b35f5c56f430e23f69500000000000000000000000000000000\\\"},\\\"prepareB\\\":{\\\"epoch\\\":0,\\\"viewNumber\\\":0,\\\"blockHash\\\":\\\"0xf03d431c33419dc0c91a35514daf0cf6dce2fa512d642c8d09c6c76542bd3b79\\\",\\\"blockNumber\\\":41090,\\\"blockIndex\\\":0,\\\"blockData\\\":\\\"0x8298037ebcb4eeae6bb5cd96a8cd4b1b0b387de84bcab38d964b166ac7f38167\\\",\\\"validateNode\\\":{\\\"index\\\":0,\\\"address\\\":\\\"0xa887aa12d6edfef9b3dac37816c88eec6f5af1a8\\\",\\\"nodeId\\\":\\\"4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\\\",\\\"blsPubKey\\\":\\\"e311da21c30acbc9889ac7b5e2e3aef7a4e565074889560e3252d1916ab9235d2e26782dae2c43d2e5ed569d55435a15faf267d8c95cd22fa09603672eea7e73b6469895822475c9876fbd3772a83ac61bc46a52f587b0ab8375f4a9067b8893\\\"},\\\"signature\\\":\\\"0x562a88749c3beef694532b4a7b1dd520faf948add07e204fafecf71e9fc60843ed4669de5205172b83285f801c9beb9800000000000000000000000000000000\\\"}}\",\"nodeName\":\"sansan33\",\"stakingBlockNum\":636,\"type\":1,\"verify\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\"}");
		when(slashMapper.selectByPrimaryKey(anyString())).thenReturn(mock(Slash.class));
		target.transactionDetails(req);

		transaction.setType(Transaction.TypeEnum.RESTRICTING_CREATE.getCode());
		transaction.setInfo("{\"account\":\"0x60ceca9c1290ee56b98d4e160ef0453f7c40d219\",\"plans\":[{\"amount\":5000000000000000000,\"epoch\":1},{\"amount\":600000000000000000,\"epoch\":2}]}");
		target.transactionDetails(req);

		assertTrue(true);
	}

	@Test
	public void testQueryClaimByAddress() throws IOException {
		String address = "0x60ceca9c1290ee56b98d4e160ef0453f7c40d219";
		TransactionListByAddressRequest req = new TransactionListByAddressRequest();
		req.setAddress(address);
		req.setPageNo(1);
		req.setPageSize(20);
		ESResult<Object> delegationRewards = new ESResult<>();
		List<Object> lists = new ArrayList<>();
		DelegationReward delegationReward = new DelegationReward();
		delegationReward.setAddr(address);
		delegationReward.setHash("0x1");
		delegationReward.setCreTime(new Date());
		delegationReward.setExtra("[{\"nodeId\":\"0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050\",\"nodeName\":\"chendai-node3\",\"reward\":\"0\"}]");
		delegationReward.setTime(new Date());
		lists.add(delegationReward);
		delegationRewards.setRsData(lists);
		delegationRewards.setTotal(1l);
		when(delegationRewardESRepository.search(any(),any(),anyInt(),anyInt())).thenReturn(delegationRewards);
		target.queryClaimByAddress(req);
		QueryClaimByStakingReq queryClaimByStakingReq = new QueryClaimByStakingReq();
		queryClaimByStakingReq.setNodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050");
		queryClaimByStakingReq.setPageNo(1);
		queryClaimByStakingReq.setPageSize(10);
		target.queryClaimByStaking(queryClaimByStakingReq);

		assertTrue(true);
	}
}
