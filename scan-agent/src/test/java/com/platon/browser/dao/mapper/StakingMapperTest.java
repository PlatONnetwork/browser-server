//package com.platon.browser.dao.mapper;
//
//import com.platon.browser.AgentApplication;
//import com.platon.browser.AgentTestBase;
//import com.platon.browser.dao.mapper.StakeBusinessMapper;
//import com.platon.browser.dao.param.ppos.StakeCreate;
//import com.platon.browser.dao.param.ppos.StakeExit;
//import com.platon.browser.dao.param.ppos.StakeIncrease;
//import com.platon.browser.dao.param.ppos.StakeModify;
//import com.platon.browser.dao.entity.*;
//import com.platon.browser.dao.mapper.DelegationMapper;
//import com.platon.browser.dao.mapper.NOptBakMapper;
//import com.platon.browser.dao.mapper.NodeMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.math.BigInteger;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * @Auther: dongqile
// * @Date: 2019/10/31
// * @Description: 质押相关入库测试类
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AgentApplication.class, value = "spring.profiles.active=unit")
//@SpringBootApplication
//public class StakingMapperTest extends AgentTestBase {
//    @Autowired
//    private StakeBusinessMapper stakeBusinessMapper;
//    @Autowired
//    private NOptBakMapper nOptBakMapper;
//    @Autowired
//    private NodeMapper nodeMapper;
//    @Autowired
//    private DelegationMapper delegationMapper;
//
//    @Test
//    public void createStakingMapper () {
//        StakeCreate createStakingParam = stakingParam();
//        //删除数据
//        deleteCreateStaking(createStakingParam);
//        //数据插入
//        stakeBusinessMapper.create(createStakingParam);
//        //staking数据插入验证
//        Staking staking = getStaking(createStakingParam.getNodeId(), createStakingParam.getStakingBlockNum().longValue());
//        assertEquals(createStakingParam.getNodeId(), staking.getNodeId());
//        //node数据插入验证
//        Node node = nodeMapper.selectByPrimaryKey(createStakingParam.getNodeId());
//        assertEquals(createStakingParam.getStakingBlockNum(), new BigInteger(node.getStakingBlockNum().toString()));
//        //opt数据插入验证
//        NOptBakExample nodeOptExample = new NOptBakExample();
//        nodeOptExample.createCriteria().andNodeIdEqualTo(createStakingParam.getNodeId())
//                .andBNumEqualTo(createStakingParam.getStakingBlockNum().longValue());
//        List <NOptBak> nodeOptList = nOptBakMapper.selectByExample(nodeOptExample);
//        assertEquals(nodeOptList.get(0).getNodeId(), createStakingParam.getNodeId());
//    }
//
//    public void deleteCreateStaking ( StakeCreate param ) {
//        //删除staking数据
//        StakingKey stakingKey = new StakingKey();
//        stakingKey.setNodeId(param.getNodeId());
//        stakingKey.setStakingBlockNum(param.getStakingBlockNum().longValue());
//        stakingMapper.deleteByPrimaryKey(stakingKey);
//        //删除node数据
//        nodeMapper.deleteByPrimaryKey(param.getNodeId());
//        //删除opt数据
//        NOptBakExample nodeOptExample = new NOptBakExample();
//        nodeOptExample.createCriteria().andNodeIdEqualTo(param.getNodeId()).andBNumEqualTo(param.getStakingBlockNum().longValue());
//        nOptBakMapper.deleteByExample(nodeOptExample);
//    }
//
//    @Test
//    public void modifyStakingMapper () {
//        StakeModify modifyStakingParam = modifyStakingParam();
//        stakeBusinessMapper.modify(modifyStakingParam);
//        //staking数据更新验证
//        Staking staking = getStaking(modifyStakingParam.getNodeId(), modifyStakingParam.getStakingBlockNum().longValue());
//        assertEquals(modifyStakingParam.getNodeName(), staking.getNodeName());
//        assertEquals(modifyStakingParam.getExternalId(), staking.getExternalId());
//        assertEquals(modifyStakingParam.getBenefitAddr(), staking.getBenefitAddr());
//        assertEquals(modifyStakingParam.getWebSite(), staking.getWebSite());
//        assertEquals(modifyStakingParam.getDetails(), staking.getDetails());
//        assertEquals(modifyStakingParam.getIsInit(), staking.getIsInit().intValue());
//        //node数据更新验证
//        Node node = nodeMapper.selectByPrimaryKey(modifyStakingParam.getNodeId());
//        assertEquals(modifyStakingParam.getNodeName(), node.getNodeName());
//        assertEquals(modifyStakingParam.getExternalId(), node.getExternalId());
//        assertEquals(modifyStakingParam.getBenefitAddr(), node.getBenefitAddr());
//        assertEquals(modifyStakingParam.getDetails(), node.getDetails());
//        assertEquals(modifyStakingParam.getWebSite(), node.getWebSite());
//        assertEquals(modifyStakingParam.getIsInit(), node.getIsInit().intValue());
//        //opt插入数据验证
//        NOptBakExample nodeOptExample = new NOptBakExample();
//        nodeOptExample.createCriteria().andNodeIdEqualTo(modifyStakingParam.getNodeId())
//                .andBNumEqualTo(modifyStakingParam.getStakingBlockNum().longValue());
//        List <NOptBak> nodeOptList = nOptBakMapper.selectByExample(nodeOptExample);
//        assertEquals(modifyStakingParam.getNodeId(), nodeOptList.get(0).getNodeId());
//    }
//
//    @Test
//    public void addStakingMapper () {
//        StakeIncrease addStakingParam = addStakingParam();
//        Staking stakingAfter = getStaking(addStakingParam.getNodeId(), addStakingParam.getStakingBlockNum().longValue());
//        Node nodeAfter = nodeMapper.selectByPrimaryKey(addStakingParam.getNodeId());
//
//        stakeBusinessMapper.increase(addStakingParam);
//        //staking数据更新验证
//        Staking staking = getStaking(addStakingParam.getNodeId(), addStakingParam.getStakingBlockNum().longValue());
//        assertEquals(stakingAfter.getStakingHes(), staking.getStakingHes().subtract(addStakingParam.getAmount()));
//        //node数据更新验证
//
//        Node nodeBefore = nodeMapper.selectByPrimaryKey(addStakingParam.getNodeId());
//        assertEquals(nodeAfter.getTotalValue().longValue(), nodeBefore.getTotalValue().subtract(addStakingParam.getAmount()).longValue());
//        assertEquals(nodeAfter.getStakingHes().longValue(), nodeBefore.getStakingHes().subtract(addStakingParam.getAmount()).longValue());
//
//        //opt插入数据验证
//        NOptBakExample nodeOptExample = new NOptBakExample();
//        nodeOptExample.createCriteria().andNodeIdEqualTo(addStakingParam.getNodeId())
//                .andBNumEqualTo(addStakingParam.getStakingBlockNum().longValue());
//        List <NOptBak> nodeOptList = nOptBakMapper.selectByExample(nodeOptExample);
//        assertEquals(addStakingParam.getNodeId(), nodeOptList.get(0).getNodeId());
//    }
//
//    @Test
//    public void withdrewStakingMapper () {
//        StakeExit withdrewStakingParam = withdrewStakingParam();
//        stakeBusinessMapper.exit(withdrewStakingParam);
//        //delegation数据更新验证
//        DelegationKey delegationKey = new Delegation();
//        delegationKey.setNodeId(withdrewStakingParam.getNodeId());
//        delegationKey.setStakingBlockNum(withdrewStakingParam.getStakingBlockNum().longValue());
//        Delegation delegate.estpl.yml = delegationMapper.selectByPrimaryKey(delegationKey);
//        assertEquals(0,delegate.estpl.yml.getDelegateHes().longValue());
//        assertEquals(0,delegate.estpl.yml.getDelegateLocked().longValue());
//        assertEquals(0,delegate.estpl.yml.getDelegateReleased().longValue());
//        //node数据更新验证
//        Node node = nodeMapper.selectByPrimaryKey(withdrewStakingParam.getNodeId());
//        assertEquals(withdrewStakingParam.getStakingReductionEpoch(), node.getStakingReductionEpoch().intValue());
//        //staking数据验证
//        Staking staking = getStaking(withdrewStakingParam.getNodeId(), withdrewStakingParam.getStakingBlockNum().longValue());
//        assertEquals(withdrewStakingParam.getStakingReductionEpoch(), staking.getStakingReductionEpoch().intValue());
//        //opt数据验证
//        NOptBakExample nodeOptExample = new NOptBakExample();
//        nodeOptExample.createCriteria().andNodeIdEqualTo(withdrewStakingParam.getNodeId())
//                .andBNumEqualTo(withdrewStakingParam.getStakingBlockNum().longValue());
//        List <NOptBak> nodeOptList = nOptBakMapper.selectByExample(nodeOptExample);
//        assertEquals(withdrewStakingParam.getNodeId(), nodeOptList.get(0).getNodeId());
//    }
//}