//package com.platon.browser.dao.mapper;
//
//import com.alibaba.fastjson.JSON;
//import com.platon.browser.AgentApplication;
//import com.platon.browser.AgentTestBase;
//import com.platon.browser.dao.entity.*;
//import com.platon.browser.dao.mapper.*;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.omg.CORBA.PRIVATE_MEMBER;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
///**
// * @Auther: dongqile
// * @Date: 2019/10/31
// * @Description: 委托相关入库测试类
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AgentApplication.class, value = "spring.profiles.active=localTest")
//@SpringBootApplication
//public class DelegateBusinessTest extends AgentTestBase {
//
//
//    @Autowired
//    private NodeMapper nodeMapper;
//
//    @Autowired
//    private StakingMapper stakingMapper;
//
//    @Autowired
//    private NetworkStatMapper networkStatMapper;
//    @Autowired
//    private DelegationMapper delegationMapper;
//    @Autowired
//    private ConfigMapper configMapper;
//    @Autowired
//    private ProposalMapper proposalMapper;
//    @Autowired
//    private SlashMapper slashMapper;
//    @Autowired
//    private VoteMapper voteMapper;
//    @Autowired
//    private RpPlanMapper rpPlanMapper;
//
//
//    @Test
//    public void test () {
//
//        Node node = nodeMapper.selectByPrimaryKey("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
//        String nodeStr = JSON.toJSONString(node,true);
//        System.out.println(nodeStr);
//
//        StakingKey key = new Staking();
//        key.setNodeId("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
//        key.setStakingBlockNum(64L);
//        Staking staking = stakingMapper.selectByPrimaryKey(key);
//        String stakingStr = JSON.toJSONString(staking,true);
//        System.out.println(stakingStr);
//
//
//        NetworkStat networkStat = networkStatMapper.selectByPrimaryKey(1);
//        String statStr = JSON.toJSONString(networkStat,true);
//        System.out.println(statStr);
//
//        DelegationKey key1 = new DelegationKey();
//        key1.setNodeId("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
//        key1.setDelegateAddr("0x3b3476d30925f07518d8ef5d939747d10de25bca");
//        key1.setStakingBlockNum(2159L);
//        Delegation delegate.estpl.yml = delegationMapper.selectByPrimaryKey(key1);
//        String delegationStr = JSON.toJSONString(delegate.estpl.yml,true);
//        System.out.println(delegationStr);
//
//        List<Config> configList = configMapper.selectByExample(null);
//        String configStr = JSON.toJSONString(configList,true);
//        System.out.println(configStr);
//
//
//        Proposal proposal = proposalMapper.selectByPrimaryKey("0x1e9262ca9d89a85e834c9740a9251f3bbe3c5bb87db41485a86af413d03d2975");
//        String proposalStr = JSON.toJSONString(proposal,true);
//        System.out.println(proposalStr);
//
//        Vote vote = voteMapper.selectByPrimaryKey("0x06de7af3e68b69f16464d0daa193183d4b15b22474acaf621bc9d1c6abb42d2b");
//        String voteStr = JSON.toJSONString(vote,true);
//        System.out.println(voteStr);
//
//        RpPlan rpPlan = rpPlanMapper.selectByPrimaryKey(1L);
//        String rpPlanStr = JSON.toJSONString(rpPlan,true);
//        System.out.println(rpPlanStr);
//
//        Slash slash = slashMapper.selectByPrimaryKey("0x5edfa25b3f18700b10f00cbd1fccbf7fb1d5d4a1577385d9e372b842ae943a21");
//        String slashStr = JSON.toJSONString(slash,true);
//        System.out.println(slashStr);
//    }
//
//}