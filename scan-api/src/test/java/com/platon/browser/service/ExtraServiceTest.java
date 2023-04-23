package com.platon.browser.service;


import com.platon.browser.ApiTestMockBase;
import com.platon.browser.dao.entity.Config;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.response.extra.QueryConfigResp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ExtraServiceTest extends ApiTestMockBase {
	@Mock
	private ConfigMapper configMapper;
    @Spy
    private ExtraService target;

    @Before
	public void setup() {
        ReflectionTestUtils.setField(target,"configMapper",configMapper);
    }

	@Test
	public void queryConfig() {
		List<Config> configs = new ArrayList<>();
		Config config = new Config();
		config.setModule("staking");
		config.setName("stakeThreshold");
		config.setInitValue("1000000000000000000000000");
		config.setStaleValue("1000000000000000000000000");
		config.setValue("1000000000000000000000000");
		config.setRangeDesc("minimum amount of stake, range: [1000000000000000000000000, 10000000000000000000000000]");
		config.setActiveBlock(0l);
		configs.add(config);
		Config config1 = new Config();
		config1.setModule("staking");
		config1.setName("operatingThreshold");
		config1.setInitValue("10000000000000000000");
		config1.setStaleValue("10000000000000000000");
		config1.setValue("10000000000000000000");
		config1.setRangeDesc("minimum amount of stake increasing funds, delegation funds, or delegation withdrawing funds, range: [10000000000000000000, 10000000000000000000000]");
		config1.setActiveBlock(0l);
		configs.add(config1);
		Config config2 = new Config();
		config2.setModule("staking");
		config2.setName("maxValidators");
		config2.setInitValue("30");
		config2.setStaleValue("30");
		config2.setValue("30");
		config2.setRangeDesc("maximum amount of validator, range: [4, 201]");
		config2.setActiveBlock(0l);
		configs.add(config2);
		Config config3 = new Config();
		config3.setModule("staking");
		config3.setName("unStakeFreezeDuration");
		config3.setInitValue("5");
		config3.setStaleValue("5");
		config3.setValue("5");
		config3.setRangeDesc("quantity of epoch for skake withdrawal, range: (MaxEvidenceAge, 112]");
		config3.setActiveBlock(0l);
		configs.add(config3);
		Config config4 = new Config();
		config4.setModule("slashing");
		config4.setName("slashFractionDuplicateSign");
		config4.setInitValue("100.0000000000000000");
		config4.setStaleValue("100.0000000000000000");
		config4.setValue("100.0000000000000000");
		config4.setRangeDesc("quantity of base point(1BP=1‱). Node's stake will be deducted(BPs*staking amount*1‱) it the node sign block duplicatlly, range: (0, 10000]");
		config4.setActiveBlock(0l);
		configs.add(config4);
		Config config5 = new Config();
		config5.setModule("slashing");
		config5.setName("duplicateSignReportReward");
		config5.setInitValue("50.00");
		config5.setStaleValue("50.00");
		config5.setValue("50.00");
		config5.setRangeDesc("quantity of base point(1bp=1%). Bonus(BPs*deduction amount for sign block duplicatlly*%) to the node who reported another's duplicated-signature, range: (0, 80]");
		config5.setActiveBlock(0l);
		configs.add(config5);
		Config config6 = new Config();
		config6.setModule("slashing");
		config6.setName("maxEvidenceAge");
		config6.setInitValue("1");
		config6.setStaleValue("1");
		config6.setValue("1");
		config6.setRangeDesc("quantity of epoch. During these epochs after a node duplicated-sign, others can report it, range: (0, UnStakeFreezeDuration)");
		config6.setActiveBlock(0l);
		configs.add(config6);
		Config config7 = new Config();
		config7.setModule("slashing");
		config7.setName("slashBlocksReward");
		config7.setInitValue("0");
		config7.setStaleValue("0");
		config7.setValue("0");
		config7.setRangeDesc("quantity of block, the total bonus amount for these blocks will be deducted from a inefficient node's stake, range: [0, 50000)");
		config7.setActiveBlock(0l);
		configs.add(config7);
		Config config8 = new Config();
		config8.setModule("block");
		config8.setName("maxBlockGasLimit");
		config8.setInitValue("100800000");
		config8.setStaleValue("100800000");
		config8.setValue("100800000");
		config8.setRangeDesc("maximum gas limit per block, range: [4712388, 210000000]");
		config8.setActiveBlock(0l);
		configs.add(config8);
		Config config9 = new Config();
		config9.setModule("staking");
		config9.setName("operatingThreshold");
		config9.setInitValue("10000000000000000000");
		config9.setStaleValue("10000000000000000000");
		config9.setValue("10000000000000000000");
		config9.setRangeDesc("minimum amount of stake increasing funds, delegation funds, or delegation withdrawing funds, range: [10000000000000000000, 10000000000000000000000]");
		config9.setActiveBlock(0l);
		configs.add(config9);
		Config config10 = new Config();
		config10.setModule("slashing");
		config10.setName("zeroProduceCumulativeTime");
		config10.setInitValue("4");
		config10.setStaleValue("4");
		config10.setValue("4");
		config10.setRangeDesc("Time range for recording the number of behaviors of zero production blocks, range: [ZeroProduceNumberThreshold, 4]");
		config10.setActiveBlock(0l);
		configs.add(config10);
		Config config11 = new Config();
		config11.setModule("slashing");
		config11.setName("zeroProduceNumberThreshold");
		config11.setInitValue("3");
		config11.setStaleValue("3");
		config11.setValue("3");
		config11.setRangeDesc("Number of zero production blocks, range: [1, ZeroProduceCumulativeTime]");
		config11.setActiveBlock(0l);
		configs.add(config11);
		when(configMapper.selectByExample(any())).thenReturn(configs);
		QueryConfigResp queryConfigResp = target.queryConfig();
		assertNotNull(queryConfigResp);
	}

}
