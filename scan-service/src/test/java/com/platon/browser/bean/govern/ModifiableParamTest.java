package com.platon.browser.bean.govern;

import com.platon.browser.dao.entity.Config;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/3
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ModifiableParamTest{

    @Data
    public class TestData{
        private List<Config> configs;
        private Block block;
        private Slashing slashing;
        private Staking staking;
        private Reward reward;
        private Restricting restricting;
    }

    private TestData testDataInit(){
        List <Config> configList = new ArrayList <>();
        Config config = new Config();
        config.setInitValue("100000000");
        config.setActiveBlock(1L);
        config.setId(1);
        config.setModule("testModule");
        config.setRangeDesc("testDec");
        config.setStaleValue("10000000");
        config.setValue("10000000");
        config.setCreateTime(new Date());
        config.setUpdateTime(new Date());
        configList.add(config);
        Block b  = Block.builder()
                .maxBlockGasLimit(BigDecimal.ZERO)
                .build();
        Slashing s = Slashing.builder()
                .maxEvidenceAge(BigDecimal.ZERO)
                .duplicateSignReportReward(BigDecimal.ZERO)
                .slashBlocksReward(BigDecimal.ZERO)
                .slashFractionDuplicateSign(BigDecimal.ZERO)
                .build();
        Staking st = Staking.builder()
                .maxValidators(BigDecimal.ZERO)
                .operatingThreshold(BigDecimal.ZERO)
                .stakeThreshold(BigDecimal.ZERO)
                .unStakeFreezeDuration(BigDecimal.ZERO)
                .build();
        Reward re = Reward.builder()
                .increaseIssuanceRatio(BigDecimal.ZERO)
                .build();
        Restricting res = Restricting.builder().minimumRelease(BigDecimal.ZERO).build();
        TestData testData = new TestData();
        testData.setConfigs(configList);
        testData.setBlock(b);
        testData.setSlashing(s);
        testData.setStaking(st);
        testData.setReward(re);
        testData.setRestricting(res);
        return testData;
    }

    @Test
    public void initStakeThreshold(){
        TestData testData = testDataInit();
        ModifiableParam target = new ModifiableParam(testData.staking,testData.slashing,testData.block,testData.reward,testData.restricting);
        testData.getConfigs().get(0).setName("stakeThreshold");
        target.init(testData.getConfigs());
    }

    @Test
    public void initOperatingThreshold(){
        TestData testData = testDataInit();
        ModifiableParam target = new ModifiableParam(testData.staking,testData.slashing,testData.block,testData.reward,testData.restricting);
        testData.getConfigs().get(0).setName("operatingThreshold");
        target.init(testData.getConfigs());
    }

    @Test
    public void initMaxValidators(){
        TestData testData = testDataInit();
        ModifiableParam target = new ModifiableParam(testData.staking,testData.slashing,testData.block,testData.reward,testData.restricting);
        testData.getConfigs().get(0).setName("maxValidators");
        target.init(testData.getConfigs());
    }

    @Test
    public void initUnStakeFreezeDuration(){
        TestData testData = testDataInit();
        ModifiableParam target = new ModifiableParam(testData.staking,testData.slashing,testData.block,testData.reward,testData.restricting);
        testData.getConfigs().get(0).setName("unStakeFreezeDuration");
        target.init(testData.getConfigs());
    }

    @Test
    public void initSlashFractionDuplicateSign(){
        TestData testData = testDataInit();
        ModifiableParam target = new ModifiableParam(testData.staking,testData.slashing,testData.block,testData.reward,testData.restricting);
        testData.getConfigs().get(0).setName("slashFractionDuplicateSign");
        target.init(testData.getConfigs());
    }

    @Test
    public void initDplicateSignReportReward(){
        TestData testData = testDataInit();
        ModifiableParam target = new ModifiableParam(testData.staking,testData.slashing,testData.block,testData.reward,testData.restricting);
        testData.getConfigs().get(0).setName("duplicateSignReportReward");
        target.init(testData.getConfigs());
    }

    @Test
    public void initMaxEvidenceAge(){
        TestData testData = testDataInit();
        ModifiableParam target = new ModifiableParam(testData.staking,testData.slashing,testData.block,testData.reward,testData.restricting);
        testData.getConfigs().get(0).setName("maxEvidenceAge");
        target.init(testData.getConfigs());
    }

    @Test
    public void initSlashBlocksReward(){
        TestData testData = testDataInit();
        ModifiableParam target = new ModifiableParam(testData.staking,testData.slashing,testData.block,testData.reward,testData.restricting);
        testData.getConfigs().get(0).setName("slashBlocksReward");
        target.init(testData.getConfigs());
    }

    @Test
    public void initMaxBlockGasLimit(){
        TestData testData = testDataInit();
        ModifiableParam target = new ModifiableParam(testData.staking,testData.slashing,testData.block,testData.reward,testData.restricting);
        testData.getConfigs().get(0).setName("maxBlockGasLimit");
        target.init(testData.getConfigs());
    }

}
