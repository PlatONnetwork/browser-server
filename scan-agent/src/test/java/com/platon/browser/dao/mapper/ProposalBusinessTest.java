//package com.platon.browser.dao.mapper;
//
//import com.platon.browser.AgentApplication;
//import com.platon.browser.AgentTestBase;
//import com.platon.browser.dao.mapper.DelegateBusinessMapper;
//import com.platon.browser.dao.param.ppos.DelegateCreate;
//import com.platon.browser.dao.param.ppos.DelegateExit;
//import com.platon.browser.dao.entity.Staking;
//import com.platon.browser.dao.entity.StakingKey;
//import com.platon.browser.dao.mapper.StakingMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
///**
// * @Auther: dongqile
// * @Date: 2019/10/31
// * @Description: 提案相关入库测试类
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AgentApplication.class, value = "spring.profiles.active=unit")
//@SpringBootApplication
//public class ProposalBusinessTest extends AgentTestBase {
//    @Autowired
//    private DelegateBusinessMapper delegateBusinessMapper;
//    @Autowired
//    private StakingMapper stakingMapper;
//
//    /**
//     * 创建委托
//     */
//    @Test
//    public void delegationCreateMapper () {
//        DelegateCreate delegateCreate = delegateCreateParam();
//        delegateBusinessMapper.create(delegateCreate);
//    }
//
//    /**
//     * 退出委托
//     */
//    @Test
//    public void delegationExitMapper () {
//        DelegateExit delegateExit = delegateExitParam();
//        delegateBusinessMapper.exit(delegateExit);
//    }
//
//    public Staking getStaking ( String nodeId, long stakingBlockNumber ) {
//        StakingKey stakingKey = new StakingKey();
//        stakingKey.setNodeId(nodeId);
//        stakingKey.setStakingBlockNum(stakingBlockNumber);
//        return stakingMapper.selectByPrimaryKey(stakingKey);
//    }
//
//}