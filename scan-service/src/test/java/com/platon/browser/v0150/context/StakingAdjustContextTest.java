package com.platon.browser.v0150.context;

import com.platon.browser.TestData;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.v0150.bean.AdjustParam;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class StakingAdjustContextTest extends TestData {
    private StakingAdjustContext target;
    private AdjustParam adjustParam;
    private Node node;
    private Staking staking;

    @Before
    public void setup(){
        target = new StakingAdjustContext();
        for (AdjustParam param : adjustParamList) {
            if ("staking".equals(param.getOptType())) {
                adjustParam = param;
                break;
            }
        }
        node = nodeList.get(0);
        staking = stakingList.get(0);
        // 默认是候选中状态
        staking.setStatus(Staking.StatusEnum.CANDIDATE.getCode());
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
        Assert.assertTrue(errorInfo.contains("【错误】：质押记录缺失"));
        Assert.assertTrue(errorInfo.contains("【错误】：节点记录缺失"));
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

    // ********************节点状态是候选中或锁定中********************
    @Test
    public void stakingHesEqualTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.ONE);
        staking.setStakingHes(BigDecimal.TEN);
        staking.setStakingLocked(BigDecimal.TEN);

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        List<String> errors = target.validate();
        Assert.assertEquals(0,errors.size());
    }

    @Test
    public void stakingLockedEqualTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.ONE);
        adjustParam.setLock(BigDecimal.TEN);
        staking.setStakingHes(BigDecimal.TEN);
        staking.setStakingLocked(BigDecimal.TEN);

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        List<String> errors = target.validate();
        Assert.assertEquals(0,errors.size());
    }

    @Test
    public void stakingHesNotEnoughTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.TEN);
        staking.setStakingHes(BigDecimal.ONE);
        staking.setStakingLocked(BigDecimal.valueOf(100));

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        List<String> errors = target.validate();
        Assert.assertEquals(1,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：质押记录[犹豫期金额"));
    }

    @Test
    public void stakingLockedNotEnoughTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.valueOf(200));
        staking.setStakingHes(BigDecimal.valueOf(100));
        staking.setStakingLocked(BigDecimal.TEN);

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        List<String> errors = target.validate();
        Assert.assertEquals(1,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：质押记录[锁定期金额"));
    }

    @Test
    public void stakingBothNotEnoughTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.TEN);
        staking.setStakingHes(BigDecimal.ONE);
        staking.setStakingLocked(BigDecimal.ONE);

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        List<String> errors = target.validate();
        Assert.assertEquals(2,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：质押记录[犹豫期金额"));
        Assert.assertTrue(errorInfo.contains("【错误】：质押记录[锁定期金额"));
    }

    @Test
    public void stakingBothEnoughTest() throws BlockNumberException {
        adjustParam.setHes(BigDecimal.ONE);
        adjustParam.setLock(BigDecimal.ONE);
        staking.setStakingHes(BigDecimal.TEN);
        staking.setStakingLocked(BigDecimal.TEN);

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        List<String> errors = target.validate();
        Assert.assertEquals(0,errors.size());
    }

    // ********************节点状态是退出中或已退出********************
    @Test
    public void stakingReductionEqualTest() throws BlockNumberException {
        staking.setStatus(Staking.StatusEnum.EXITED.getCode());
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.TEN);
        staking.setStakingReduction(BigDecimal.valueOf(20));

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        List<String> errors = target.validate();
        Assert.assertEquals(0,errors.size());
    }

    @Test
    public void stakingReductionNotEqualTest() throws BlockNumberException {
        staking.setStatus(Staking.StatusEnum.EXITED.getCode());
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.TEN);
        staking.setStakingReduction(BigDecimal.valueOf(50));

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        List<String> errors = target.validate();
        Assert.assertEquals(0,errors.size());
    }

    @Test
    public void stakingReductionNotEnoughTest() throws BlockNumberException {
        staking.setStatus(Staking.StatusEnum.EXITED.getCode());
        adjustParam.setHes(BigDecimal.TEN);
        adjustParam.setLock(BigDecimal.TEN);
        staking.setStakingReduction(BigDecimal.valueOf(15));

        target.setChainConfig(blockChainConfig);
        target.setAdjustParam(adjustParam);
        target.setNode(node);
        target.setStaking(staking);
        List<String> errors = target.validate();
        Assert.assertEquals(1,errors.size());
        String errorInfo = target.errorInfo();
        Assert.assertTrue(errorInfo.contains("【错误】：质押记录[退回中金额"));
    }
}
