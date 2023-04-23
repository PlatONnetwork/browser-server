package com.platon.browser.v0150.context;

import com.platon.browser.TestData;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.v0150.bean.AdjustParam;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class DelegateAdjustContextTest extends TestData {
    private DelegateAdjustContext target;
    private AdjustParam adjustParam;
    private Node node;
    private Staking staking;
    private Delegation delegation;

    @Before
    public void setup(){
        target = new DelegateAdjustContext();
        for (AdjustParam param : adjustParamList) {
            if ("delegate".equals(param.getOptType())) {
                adjustParam = param;
                break;
            }
        }
        node = nodeList.get(0);
        staking = stakingList.get(0);
        // 默认是候选中状态
        staking.setStatus(Staking.StatusEnum.CANDIDATE.getCode());
        delegation = delegationList.get(0);
    }

    @Test
    public void blockChainConfigNullTest() throws BlockNumberException {
        List<String> errors = target.validate();
        Assert.assertEquals(1, errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("BlockChainConfig缺失"));
    }

    @Test
    public void adjustParamNullTest() throws BlockNumberException {
        target.setChainConfig(blockChainConfig);
        List<String> errors = target.validate();
        Assert.assertEquals(1,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：调账数据缺失"));
    }

    @Test
    public void nodeStakingNullTest() throws BlockNumberException {
        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        List<String> errors = target.validate();
        Assert.assertEquals(2,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：节点记录缺失"));
        Assert.assertTrue(errorInfo.contains("【错误】：质押记录缺失"));
    }

    @Test
    public void stakingNullTest() throws BlockNumberException {
        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        List<String> errors = target.validate();
        Assert.assertEquals(1,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：质押记录缺失"));
    }

    @Test
    public void nodeNullTest() throws BlockNumberException {
        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setStaking(staking);
        List<String> errors = target.validate();
        Assert.assertEquals(1,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：节点记录缺失"));
    }

    @Test
    public void delegateNullTest() throws BlockNumberException {
        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        List<String> errors = target.validate();
        Assert.assertEquals(1,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：委托记录缺失"));
    }

    // ********************节点状态是候选中或锁定中********************
    @Test
    public void delegateHesEqualTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.ONE);
        delegation.setDelegateHes(BigDecimal.TEN);
        delegation.setDelegateLocked(BigDecimal.TEN);

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        target.setDelegation(delegation);
        List<String> errors = target.validate();
        Assert.assertEquals(0,errors.size());
    }

    @Test
    public void delegateLockedEqualTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.ONE);
        adjustParam.setLock(BigDecimal.TEN);
        delegation.setDelegateHes(BigDecimal.TEN);
        delegation.setDelegateLocked(BigDecimal.TEN);

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        target.setDelegation(delegation);
        List<String> errors = target.validate();
        Assert.assertEquals(0,errors.size());
    }

    @Test
    public void delegateHesNotEnoughTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.TEN);
        delegation.setDelegateHes(BigDecimal.ONE);
        delegation.setDelegateLocked(BigDecimal.valueOf(100));

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        target.setDelegation(delegation);
        List<String> errors = target.validate();
        Assert.assertEquals(1,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：委托记录[犹豫期金额"));
    }

    @Test
    public void delegateLockedNotEnoughTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.TEN);
        delegation.setDelegateHes(BigDecimal.valueOf(100));
        delegation.setDelegateLocked(BigDecimal.ONE);

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        target.setDelegation(delegation);
        List<String> errors = target.validate();
        Assert.assertEquals(1,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：委托记录[锁定期金额"));
    }

    @Test
    public void delegateBothNotEnoughTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.TEN);
        delegation.setDelegateHes(BigDecimal.ONE);
        delegation.setDelegateLocked(BigDecimal.ONE);

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        target.setDelegation(delegation);
        List<String> errors = target.validate();
        Assert.assertEquals(2,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：委托记录[犹豫期金额"));
        Assert.assertTrue(errorInfo.contains("【错误】：委托记录[锁定期金额"));
    }

    @Test
    public void delegateBothEnoughTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.ONE);
        adjustParam.setLock(BigDecimal.ONE);
        delegation.setDelegateHes(BigDecimal.TEN);
        delegation.setDelegateLocked(BigDecimal.TEN);

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        target.setDelegation(delegation);
        List<String> errors = target.validate();
        Assert.assertEquals(0,errors.size());
    }


    // ********************节点状态是退出中或已退出********************
    @Test
    public void delegateReleasedEqualTest() throws BlockNumberException {
        staking.setStatus(Staking.StatusEnum.EXITED.getCode());
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.TEN);
        delegation.setDelegateReleased(BigDecimal.valueOf(20));

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        target.setDelegation(delegation);
        List<String> errors = target.validate();
        Assert.assertEquals(0,errors.size());
    }

    @Test
    public void delegateReleasedNotEqualTest() throws BlockNumberException {
        staking.setStatus(Staking.StatusEnum.EXITED.getCode());
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.TEN);
        delegation.setDelegateReleased(BigDecimal.valueOf(50));

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        target.setDelegation(delegation);
        List<String> errors = target.validate();
        Assert.assertEquals(0,errors.size());
    }

    @Test
    public void delegateReleasedNotEnoughTest() throws BlockNumberException {
        staking.setStatus(Staking.StatusEnum.EXITED.getCode());
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.TEN);
        delegation.setDelegateReleased(BigDecimal.valueOf(15));

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        target.setDelegation(delegation);
        List<String> errors = target.validate();
        Assert.assertEquals(1,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("委托记录[待提取金额"));
    }
}
