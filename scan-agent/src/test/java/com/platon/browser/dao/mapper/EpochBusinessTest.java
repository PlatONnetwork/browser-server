//package com.platon.browser.dao.mapper;
//
//import com.platon.browser.AgentApplication;
//import com.platon.browser.AgentTestBase;
//import com.platon.browser.dao.mapper.EpochBusinessMapper;
//import com.platon.browser.dao.mapper.NewBlockMapper;
//import com.platon.browser.dao.param.epoch.Consensus;
//import com.platon.browser.dao.param.epoch.Election;
//import com.platon.browser.dao.param.epoch.NewBlock;
//import com.platon.browser.dao.param.epoch.Settle;
//import com.platon.browser.dao.entity.Node;
//import com.platon.browser.dao.entity.Staking;
//import com.platon.browser.dao.entity.StakingExample;
//import com.platon.browser.dao.mapper.NodeMapper;
//import com.platon.browser.dao.mapper.StakingMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
///**
// * @Auther: dongqile
// * @Date: 2019/10/31
// * @Description: 周期切换相关测试类
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AgentApplication.class, value = "spring.profiles.active=unit")
//@SpringBootApplication
//public class EpochBusinessTest extends AgentTestBase {
//    @Autowired
//    private NewBlockMapper newBlockMapper;
//    @Autowired
//    private EpochBusinessMapper epochBusinessMapper;
//    @Autowired
//    private StakingMapper stakingMapper;
//    @Autowired
//    private NodeMapper nodeMapper;
//
//    /**
//     * 新增区块
//     */
//    @Test
//    public void newBlockMapper () {
//        NewBlock blockParam = blockParam();
//        Staking stakingBefore = getStaking(blockParam.getNodeId(), blockParam.getStakingBlockNum().longValue());
//        Node nodeBefore = nodeMapper.selectByPrimaryKey(blockParam.getNodeId());
//        newBlockMapper.newBlock(blockParam);
//        Staking stakingAfter = getStaking(blockParam.getNodeId(), blockParam.getStakingBlockNum().longValue());
//        //staking更新数据验证
//        assertEquals(stakingBefore.getBlockRewardValue(), stakingAfter.getBlockRewardValue().subtract(blockParam.getBlockRewardValue()));
//        assertEquals(stakingBefore.getFeeRewardValue(), stakingAfter.getFeeRewardValue().subtract(blockParam.getFeeRewardValue()));
//        //node更新数据验证
//        Node nodeAfter = nodeMapper.selectByPrimaryKey(blockParam.getNodeId());
//        assertEquals(nodeBefore.getStatBlockRewardValue(), nodeAfter.getStatBlockRewardValue().subtract(blockParam.getBlockRewardValue()));
//        assertEquals(nodeBefore.getStatFeeRewardValue(), nodeAfter.getStatFeeRewardValue().subtract(blockParam.getFeeRewardValue()));
//    }
//
//    /**
//     * 共识周期切换
//     */
//    @Test
//    public void newConsensusEpochMapper () {
//        Consensus newConsensusParam = consensusParam();
//        epochBusinessMapper.consensus(newConsensusParam);
//        StakingExample stakingExample = new StakingExample();
//        stakingExample.createCriteria()
//                .andStatusEqualTo(1);
//        List <Staking> stakingList = stakingMapper.selectByExample(stakingExample);
//        //staking更新数据验证
//        assertEquals(0, stakingList.get(0).getCurConsBlockQty().longValue());
//    }
//
//    /**
//     * 结算周期切换
//     */
//    @Test
//    public void newReductionEpochMapper () {
//        Settle settleParam = settleParam();
//        epochBusinessMapper.settle(settleParam);
//    }
//
//    /**
//     * 选举周期切换-查询待踢出验证人
//     */
//    @Test
//    public void newElectionEpochMapperQuerySlashNode () {
//        List<String> nodeList = electionQuerySlashNodeParam();
//        epochBusinessMapper.querySlashNode(nodeList);
//    }
//
//    /**
//     * 选举周期切换
//     */
//    @Test
//    public void newElectionEpochMapper () {
//        Election electionParam = electionSlashNodeParam();
//        epochBusinessMapper.slashNode(electionParam);
//    }
//}