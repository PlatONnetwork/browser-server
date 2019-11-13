package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.AgentApplication;
import com.platon.browser.TestBase;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.complement.dao.param.delegate.DelegateCreate;
import com.platon.browser.complement.dao.param.delegate.DelegateExit;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
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
public class ProposalBusinessTest extends TestBase {
    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;
    @Autowired
    private StakingMapper stakingMapper;

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

    public Staking getStaking ( String nodeId, long stakingBlockNumer ) {
        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(nodeId);
        stakingKey.setStakingBlockNum(stakingBlockNumer);
        return stakingMapper.selectByPrimaryKey(stakingKey);
    }

}