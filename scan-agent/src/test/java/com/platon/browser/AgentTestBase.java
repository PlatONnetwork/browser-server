package com.platon.browser;//package com.platon.browser;


import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dao.param.epoch.Consensus;
import com.platon.browser.dao.param.epoch.Election;
import com.platon.browser.dao.param.epoch.NewBlock;
import com.platon.browser.dao.param.ppos.*;
import com.platon.browser.dao.param.statistic.AddressStatChange;
import com.platon.browser.dao.param.statistic.AddressStatItem;
import com.platon.browser.enums.AppStatus;
import com.platon.browser.utils.AppStatusUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/7 16:35
 * @Description:
 */
public class AgentTestBase extends AgentTestData {
    @Autowired
    protected StakingMapper stakingMapper;
    @Autowired
    protected PlatOnClient platOnClient;

    public static Map<Integer, BigDecimal> subsidies = new HashMap<>();

    static {
        AppStatusUtil.setStatus(AppStatus.STOPPED);
        subsidies.put(1,BigDecimal.valueOf(62215742));
        subsidies.put(2,BigDecimal.valueOf(55965742));
        subsidies.put(3,BigDecimal.valueOf(49559492));
        subsidies.put(4,BigDecimal.valueOf(42993086));
        subsidies.put(5,BigDecimal.valueOf(36262520));
        subsidies.put(6,BigDecimal.valueOf(29363689));
        subsidies.put(7,BigDecimal.valueOf(22292388));
        subsidies.put(8,BigDecimal.valueOf(15044304));
        subsidies.put(9,BigDecimal.valueOf(7615018));
    }

    public Staking getStaking (String nodeId, long stakingBlockNumber ) {
        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(nodeId);
        stakingKey.setStakingBlockNum(stakingBlockNumber);
        return stakingMapper.selectByPrimaryKey(stakingKey);
    }

    public StakeCreate stakingParam () {
        StakeCreate createStakingParam = StakeCreate.builder()
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .stakingHes(new BigDecimal("5000"))
                .nodeName("testNode01")
                .externalId("externalId01")
                .benefitAddr("0xff48d9712d8a55bf603dab28f4645b6985696a61")
                .programVersion("1794")
                .bigVersion("1700")
                .webSite("web_site01")
                .details("details01")
                .isInit(1)
                .stakingBlockNum(new BigInteger("200"))
                .stakingTxIndex(0)
                .stakingAddr("0xb58c7fd25437e2fcf038b948963ffb80276bd44d")
                .joinTime(new Date(System.currentTimeMillis()))
                .txHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7")
                .build();
        return createStakingParam;
    }

    public StakeModify modifyStakingParam () {
        StakeModify modifyStakingParam = StakeModify.builder()
                .nodeName("testNode02")
                .externalId("externalId02")
                .benefitAddr("0xff48d9712d8a55bf603dab28f4645b6985696a61")
                .webSite("web_site01")
                .details("details01")
                .isInit(2)
                .stakingBlockNum(new BigInteger("200"))
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .build();
        return modifyStakingParam;
    }

    public StakeIncrease addStakingParam () {
        StakeIncrease addStakingParam = StakeIncrease.builder()
                .amount(new BigDecimal("500000000000000000000000000"))
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .stakingBlockNum(new BigInteger("200"))
                .build();
        return addStakingParam;
    }

    public StakeExit withdrewStakingParam () {
        StakeExit withdrewStakingParam = StakeExit.builder()
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .stakingBlockNum(new BigInteger("200"))
                .time(new Date(System.currentTimeMillis()))
                .stakingReductionEpoch(3)
                .build();
        return withdrewStakingParam;
    }

    public Report reportDuplicateSignParam () {
        Report reportDuplicateSignParam = Report.builder()
                .time(new Date(System.currentTimeMillis()))
                .settingEpoch(3)
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .stakingBlockNum(new BigInteger("200"))
                .txHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7")
                .slashRate(new BigDecimal("0.5"))
                .slashData("json")
                .benefitAddr("0x02fba14f5e72092c8fca6ced087cd4e7be0d8fc5")
                .codeRemainRedeemAmount(new BigDecimal("50000000"))
                .codeStatus(2)
                .codeRewardValue(new BigDecimal("1000000000"))
                .build();
        return reportDuplicateSignParam;
    }

    public NewBlock blockParam () {
        NewBlock newBlockParam = NewBlock.builder()
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .blockRewardValue(new BigDecimal("100000000000"))
                .feeRewardValue(new BigDecimal("10000000000"))
                .stakingBlockNum(new BigInteger("200"))
                .build();
        return newBlockParam;
    }

    public Consensus consensusParam () {
        List <String> nodeIdList = new ArrayList <>();
        nodeIdList.add("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        Consensus consensus = Consensus.builder()
                .validatorList(nodeIdList)
                .expectBlockNum(new BigInteger("10"))
                .build();
        return consensus;
    }

    /*public Settle settleParam () {
        List <String> curVerifierList = new ArrayList <>();
        curVerifierList.add("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        curVerifierList.add("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
        List <String> preVerifierList = new ArrayList <>();
        preVerifierList.add("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        preVerifierList.add("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
        Settle settle = Settle.builder()
                .curVerifierList(curVerifierList)
                .preVerifierList(preVerifierList)
//                .annualizedRate(Double.valueOf("10"))
//                .annualizedRateInfo("json")
                .settingEpoch(3)
                .stakingLockEpoch(3)
                .stakingReward(new BigDecimal("100000000000"))
                .build();
        return settle;
    }*/

    public Election electionSlashNodeParam () {
        List <Staking> slashNodeList = new ArrayList <>();
        Staking staking = new Staking();
        staking.setNodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        staking.setStakingBlockNum(0L);
        slashNodeList.add(staking);

        staking.setNodeId("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
        staking.setStakingBlockNum(0L);
        slashNodeList.add(staking);

        Election election = Election.builder()
                .settingEpoch(3)
                .lockedNodeList(slashNodeList).build();
        return election;
    }

    public List<String> electionQuerySlashNodeParam () {
        List <String> slashNodeList = new ArrayList <>();
        slashNodeList.add("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        slashNodeList.add("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
        return slashNodeList;
    }

    public DelegateCreate delegateCreateParam () {
        DelegateCreate delegateCreate = DelegateCreate.builder()
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .blockNumber(new BigInteger("300"))
                .amount(new BigDecimal("100000000000000"))
                .sequence(new BigInteger("10000000"))
                .stakingBlockNumber(new BigInteger("200"))
                .txFrom("0xff48d9712d8a55bf603dab28f4645b6985696a61")
                .build();
        return delegateCreate;
    }

    public DelegateExit delegateExitParam () {
        DelegateExit delegateExit = DelegateExit.builder()
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .stakingBlockNumber(new BigInteger("200"))
                .minimumThreshold(new BigDecimal("500000"))
                .txFrom("0xff48d9712d8a55bf603dab28f4645b6985696a61")
                .codeIsHistory(2)
                .codeNodeIsLeave(false)
                .build();
        return delegateExit;
    }

    public ProposalText proposalTextParam () {
        ProposalText proposalText = ProposalText.builder()
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .pIDID("100")
                .url("https://github.com/danielgogo/PIPs/PIP-100.md")
                .pipNum("PIP-100")
                .endVotingBlock(new BigInteger("2000"))
                .topic("inquiry")
                .description("inquiry")
                .txHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7")
                .blockNumber(new BigInteger("300"))
                .timestamp(new Date(System.currentTimeMillis()))
                .stakingName("test01")
                .build();
        return proposalText;
    }


    public ProposalVote proposalVoteParam () {
        ProposalVote proposalVote = ProposalVote.builder()
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .txHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7")
                .bNum(new BigInteger("300"))
                .timestamp(new Date(System.currentTimeMillis()))
                .proposalHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7")
                .voteOption(1)
                .stakingName("test01")
                .build();
        return proposalVote;
    }


    public ProposalUpgrade proposalUpgradeOrCancelParam () {
        ProposalUpgrade proposalUpgradeOrCancel = ProposalUpgrade.builder()
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .pIDID("100")
                .url("https://github.com/danielgogo/PIPs/PIP-100.md")
                .pipNum("PIP-100")
                .endVotingBlock(new BigInteger("2000"))
                .activeBlock(new BigInteger("3000"))
                .topic("inquiry")
                .description("inquiry")
                .txHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be1")
                .blockNumber(new BigInteger("300"))
                .timestamp(new Date(System.currentTimeMillis()))
                .stakingName("test01")
                .newVersion("1000")
                .build();
        return proposalUpgradeOrCancel;
    }


    public AddressStatChange addressStatChangeParam () {
        List <AddressStatItem> addressStatItems = new ArrayList <>();
        for (int i=0;i<3;i++) {
            AddressStatItem addressStatItem = AddressStatItem.builder()
                    .address("0xff48d9712d8a55bf603dab28f4645b6985696a61")
                    .contractCreate("0xff48d9712d8a55bf603dab28f4645b6985696a61")
                    .contractCreatehash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be9")
                    .contractName("test")
                    .delegateQty(i)
                    .proposalQty(i)
                    .stakingQty(i)
                    .transferQty(i)
                    .txQty(i)
                    .type(i)
                    .build();
            addressStatItems.add(addressStatItem);
        }
        AddressStatChange addressStatChange = AddressStatChange.builder()
                .addressStatItemList(addressStatItems)
                .build();
        return addressStatChange;
    }


    public RestrictingCreate restrictingCreateParam () {
        List <RestrictingItem> restrictingItems = new ArrayList <>();
        for(int i=0;i<3;i++){
            RestrictingItem restrictingItem = RestrictingItem.builder()
                    .address("0xff48d9712d8a55bf603dab28f4645b6985696a61")
                    .amount(new BigDecimal("100000000000000"))
                    .epoch(BigInteger.valueOf(i))
                    .number(new BigInteger("100"))
                    .build();
            restrictingItems.add(restrictingItem);
        }
        RestrictingCreate restrictingCreate = RestrictingCreate.builder()
                .itemList(restrictingItems)
                .build();
        return restrictingCreate;
    }

    public NetworkStat networkStatChangeParam () {
        NetworkStat networkStat = new NetworkStat();
               networkStat.setId(1);
               networkStat.setAddIssueBegin(1L);
               networkStat.setAddIssueEnd(2L);
               networkStat.setBlockReward(new BigDecimal("100000000000000"));
               networkStat.setCurNumber(100L);
               networkStat.setCurTps(100);
               networkStat.setNextSettle(10L);
               networkStat.setNodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
               networkStat.setNodeName("testNode01");
               networkStat.setProposalQty(1);
               networkStat.setStakingReward(new BigDecimal("100000000000000"));
               networkStat.setTxQty(1);
               networkStat.setMaxTps(1);
        return networkStat;
    }



}
