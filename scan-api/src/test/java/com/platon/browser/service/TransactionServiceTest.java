package com.platon.browser.service;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.ApiTestMockBase;
import com.platon.browser.bean.CustomStaking;
import com.platon.browser.cache.TransactionCacheDto;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.config.RedisKeyConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Slash;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.mapper.SlashMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.Erc20Param;
import com.platon.browser.request.PageReq;
import com.platon.browser.request.newtransaction.TransactionDetailsReq;
import com.platon.browser.request.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.request.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.request.staking.QueryClaimByStakingReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.transaction.TransactionListResp;
import com.platon.browser.service.elasticsearch.EsDelegationRewardRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionServiceTest extends ApiTestMockBase {

    @Mock
    private EsDelegationRewardRepository ESDelegationRewardRepository;

    @Mock
    private StakingMapper stakingMapper;

    @Mock
    private SlashMapper slashMapper;

    @Mock
    private ProposalMapper proposalMapper;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private DownFileCommon downFileCommon;

    @Mock
    protected RedisKeyConfig redisKeyConfig;

    @Mock
    private BlockChainConfig blockChainConfig;

    @InjectMocks
    @Spy
    private TransactionService target;

    @Before
    public void setup() {
        ValueOperations valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn("test");
        when(this.commonService.getNodeName(any(), any())).thenReturn("test-name");
    }

    @Test
    public void test() throws IOException {
        TransactionDetailsReq req = new TransactionDetailsReq();
        req.setTxHash("0xddd");
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
        when(this.ESTransactionRepository.get(any(), any())).thenReturn(transaction);
        ESResult<Object> first = new ESResult<>();
        List<Object> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        first.setRsData(transactionList);
        first.setTotal(33L);
        when(this.ESTransactionRepository.search(any(), any(), anyInt(), anyInt())).thenReturn(first);

        transaction.setType(Transaction.TypeEnum.STAKE_CREATE.getCode());
        transaction.setInfo("{\"benefitAddress\":\"0x60ceca9c1290ee56b98d4e160ef0453f7c40d219\",\"details\":\"Node of CDM\",\"externalId\":\"5FD68B690010632B\",\"nodeId\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7\",\"nodeName\":\"cdm-004\",\"programVersion\":2048,\"website\":\"WWW.CCC.COM\",\"DelegateRewardPer\":3}");
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.STAKE_MODIFY.getCode());
        transaction.setInfo("{\"benefitAddress\":\"0x60ceca9c1290ee56b98d4e160ef0453f7c40d219\",\"details\":\"Node of CDM\",\"externalId\":\"5FD68B690010632B\",\"nodeId\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7\",\"nodeName\":\"cdm-004\",\"website\":\"WWW.CCC.COM\",\"DelegateRewardPer\":3}");
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.STAKE_INCREASE.getCode());
        transaction.setInfo("{\"amount\":5000000000000000000000000,\"nodeId\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7\",\"nodeName\":\"cdm-004\",\"stakingBlockNum\":814,\"type\":0}");
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.STAKE_EXIT.getCode());
        transaction.setInfo("{\"amount\":5000000000000000000000000,\"nodeId\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\",\"nodeName\":\"sansan33\",\"stakingBlockNum\":50767,\"withdrawBlockNum\":50767}");
        Staking staking = new Staking();
        staking.setStakingReduction(BigDecimal.ONE);
        staking.setStatus(CustomStaking.StatusEnum.EXITING.getCode());
        when(this.stakingMapper.selectByPrimaryKey(any())).thenReturn(staking);
        this.target.transactionDetails(req);
        staking.setStatus(CustomStaking.StatusEnum.EXITED.getCode());
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.PROPOSAL_TEXT.getCode());
        transaction.setInfo("{\"nodeName\":\"cdm-004\",\"pIDID\":\"11\",\"verifier\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7\"}");
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.PROPOSAL_UPGRADE.getCode());
        transaction.setInfo("{\"endVotingRound\":10,\"newVersion\":2064,\"nodeName\":\"sansan3.3.3.3\",\"pIDID\":\"S8\",\"verifier\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\"}");
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.PROPOSAL_CANCEL.getCode());
        transaction.setInfo("{\"canceledProposalID\":\"0x7cb32dc2cc4202388b04641bd5d13372cc0a9a0a7b0d05dcd509232f07493a52\",\"endVotingRound\":12,\"nodeName\":\"sansan33\",\"pIDID\":\"asdfrrrrr\",\"verifier\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\"}");
        Proposal proposal = mock(Proposal.class);
        when(this.proposalMapper.selectByPrimaryKey(anyString())).thenReturn(proposal);
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.PROPOSAL_VOTE.getCode());
        transaction.setInfo("{\"nodeName\":\"sansan33\",\"option\":\"2\",\"pIDID\":\"c4\",\"programVersion\":\"1797\",\"proposalId\":\"0xa5abb6a7ebc1b7f5c24952f489c4a95c8091710109cac35ecee1e6c35f33a5af\",\"proposalType\":\"3\",\"verifier\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\",\"versionSign\":\"0xb109d4db2991b0c35d0d1a70b02032eefefc820a7c8077e43859c0799ddf9c681e2a2f316ff9d1f055ea266e5a04a3b9bee2a01f00f3f4da7d3e4e3404b446fa00\"}");
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.PROPOSAL_VOTE.getCode());
        transaction.setInfo("{\"nodeName\":\"sansan33\",\"option\":\"2\",\"pIDID\":\"c4\",\"programVersion\":\"1797\",\"proposalId\":\"0xa5abb6a7ebc1b7f5c24952f489c4a95c8091710109cac35ecee1e6c35f33a5af\",\"proposalType\":\"3\",\"verifier\":\"0x4cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b208\",\"versionSign\":\"0xb109d4db2991b0c35d0d1a70b02032eefefc820a7c8077e43859c0799ddf9c681e2a2f316ff9d1f055ea266e5a04a3b9bee2a01f00f3f4da7d3e4e3404b446fa00\"}");
        when(this.proposalMapper.selectByPrimaryKey(anyString())).thenReturn(proposal);
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.VERSION_DECLARE.getCode());
        transaction.setInfo("{\"activeNode\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7\",\"nodeName\":\"cdm-004\",\"version\":1797,\"versionSigns\":\"0x473f7ab72b2922b325ce5532e64edbbf20dd90431372ff4d3ac48003ff2d64a77255151771eead81daad64edf28e55edb959929c536e523da0737a71201845e400\"}");
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.RESTRICTING_CREATE.getCode());
        transaction.setInfo("{\"account\":\"0x60ceca9c1290ee56b98d4e160ef0453f7c40d219\",\"plans\":[{\"amount\":5000000000000000000,\"epoch\":1},{\"amount\":600000000000000000,\"epoch\":2}]}");
        //this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.DELEGATE_CREATE.getCode());
        transaction.setInfo("{\"nodeId\":\"0x\",\"amount\":5000,\"nodeName\":\"test\"}");
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.DELEGATE_EXIT.getCode());
        transaction.setInfo("{\"nodeId\":\"0x\",\"amount\":5000,\"nodeName\":\"test\",\"reward\":200}");
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.PROPOSAL_PARAMETER.getCode());
        transaction.setInfo("{\"verifier\":\"0x\",\"txHash\":\"0x1\",\"nodeName\":\"test\"}");
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.CLAIM_REWARDS.getCode());
        transaction.setInfo("{\"rewardList\":[{\"nodeId\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7\",\"nodeName\":\"zrj-node1\",\"reward\":234884281013318097782607,\"stakingNum\":21319}," +
                "{\"nodeId\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7\",\"nodeName\":\"zrj-node1\",\"reward\":234884281013318097782607,\"stakingNum\":21319}," +
                "{\"nodeId\":\"0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e8\",\"nodeName\":\"zrj-node2\",\"reward\":234884281013318097782607,\"stakingNum\":21319}]}");
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.WASM_CONTRACT_CREATE.getCode());
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.CONTRACT_EXEC.getCode());
        this.target.transactionDetails(req);

        transaction.setType(Transaction.TypeEnum.ERC20_CONTRACT_EXEC.getCode());
        List<Erc20Param> list = new ArrayList<>();
        Erc20Param erc20Param = Erc20Param.builder().innerValue("123").innerTo("122").innerSymbol("t").innerFrom("12").innerContractName("name")
                .innerContractAddr("addr").innerDecimal("10").build();
        Erc20Param erc20Param1 = Erc20Param.builder().innerValue("123").innerTo("122").innerSymbol("t").innerFrom("12").innerContractName("name")
                .innerContractAddr("addr").innerDecimal("10").build();
        list.add(erc20Param);
        list.add(erc20Param1);
        transaction.setInfo(JSONObject.toJSONString(list));
        this.target.transactionDetails(req);

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
        when(this.ESDelegationRewardRepository.search(any(), any(), anyInt(), anyInt())).thenReturn(delegationRewards);
        this.target.queryClaimByAddress(req);
        QueryClaimByStakingReq queryClaimByStakingReq = new QueryClaimByStakingReq();
        queryClaimByStakingReq.setNodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050");
        queryClaimByStakingReq.setPageNo(1);
        queryClaimByStakingReq.setPageSize(10);
        this.target.queryClaimByStaking(queryClaimByStakingReq);

        assertTrue(true);
    }

    @Test
    public void testGetTransactionList() {
        PageReq req = new PageReq();
        TransactionCacheDto transactionCacheDto = new TransactionCacheDto();
        RespPage<?> page = new RespPage<>();
        page.setTotalCount(10l);
        page.setTotalPages(10);
        transactionCacheDto.setPage(page);
        when(this.statisticCacheService.getTransactionCache(any(), any())).thenReturn(transactionCacheDto);
        NetworkStat net = new NetworkStat();
        net.setTxQty(10);
        when(this.statisticCacheService.getNetworkStatCache()).thenReturn(net);
        RespPage<TransactionListResp> resp = this.target.getTransactionList(req);
        assertNotNull(resp);
    }

    @Test
    public void testGetTransactionListByBlock() {
        TransactionListByBlockRequest req = new TransactionListByBlockRequest();
        req.setBlockNumber(10);
        req.setTxType("transfer");
        RespPage<TransactionListResp> resp = this.target.getTransactionListByBlock(req);
        assertNotNull(resp);
    }

    @Test
    public void testGetTransactionListByAddress() {
        TransactionListByAddressRequest req = new TransactionListByAddressRequest();
        req.setAddress("lax");
        req.setTxType("transfer");
        RespPage<TransactionListResp> resp = this.target.getTransactionListByAddress(req);
        assertNotNull(resp);
    }

    @Test
    public void testGetTransactionListByAddressDownload() {
        TransactionListByAddressRequest req = new TransactionListByAddressRequest();
        req.setAddress("lax");
        req.setTxType("transfer");
        when(this.downFileCommon.writeDate(any(), any(), any())).thenReturn(new AccountDownload());
        AccountDownload resp = this.target.transactionListByAddressDownload("0x", new Date().getTime(), "en_US", "+8");
        assertNotNull(resp);
    }

}
