package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.AgentApplication;
import com.platon.browser.AgentTestBase;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.complement.dao.param.delegate.DelegateCreate;
import com.platon.browser.complement.dao.param.delegate.DelegateExit;
import com.platon.browser.complement.dao.param.stake.StakeCreate;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @Auther: dongqile
 * @Date: 2019/10/31
 * @Description: 委托相关入库测试类
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgentApplication.class, value = "spring.profiles.active=test")
@SpringBootApplication
public class DelegateBusinessTest extends AgentTestBase {


    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;

    @Autowired
    private StakingMapper stakingMapper;

    @Autowired
    private NodeMapper nodeMapper;


    public void deleteCreateStaking ( StakeCreate param ) {
        //删除staking数据
        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(param.getNodeId());
        stakingKey.setStakingBlockNum(param.getStakingBlockNum().longValue());
        stakingMapper.deleteByPrimaryKey(stakingKey);
        //删除node数据
        nodeMapper.deleteByPrimaryKey(param.getNodeId());

    }

    /**
     * 创建委托
     */
    @Test
    public void delegationCreateMapper () {
        DelegateCreate delegateCreate = delegateCreateParam();
        delegateBusinessMapper.create(delegateCreate);
        verify(delegateBusinessMapper, times(1)).create(any(DelegateCreate.class));
    }

    /**
     * 退出委托
     */
    @Test
    public void delegationExitMapper () {
        DelegateExit delegateExit = delegateExitParam();
        delegateBusinessMapper.exit(delegateExit);
        verify(delegateBusinessMapper, times(1)).exit(any(DelegateExit.class));
    }
}