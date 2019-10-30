//package com.platon.browser.dto;
//
//import static org.junit.Assert.*;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.platon.browser.dto.CustomStaking.StatusEnum;
//import com.platon.browser.dto.CustomStaking.YesNoEnum;
//
//public class CustomStakingTest {
//
//	private CustomStaking staking;
//
//	@Before
//	public void setUp() throws Exception {
//		staking = new CustomStaking();
//	}
//
//	@Test
//	public void testEquals() {
//		assertTrue(staking.equals(new CustomStaking()));
//	}
//
//	@Test
//	public void testHashCode() {
//		assertNotNull(staking.hashCode());
//	}
//
//	@Test
//	public void testCustomStaking() {
//		assertNotNull(staking);
//	}
//
//	@Test
//	public void testDecimalStakingLocked() {
//		assertNotNull(staking.decimalStakingLocked());
//	}
//
//	@Test
//	public void testDecimalStakingHas() {
//		assertNotNull(staking.decimalStakingHas());
//	}
//
//	@Test
//	public void testDecimalBlockRewardValue() {
//		assertNotNull(staking.decimalBlockRewardValue());
//	}
//
//	@Test
//	public void testDecimalStakingRewardValue() {
//		assertNotNull(staking.decimalStakingRewardValue());
//	}
//
//	@Test
//	public void testDecimalStakingReduction() {
//		assertNotNull(staking.decimalStakingReduction());
//	}
//
//	@Test
//	public void testDecimalStatDelegateHas() {
//		assertNotNull(staking.decimalStatDelegateHas());
//	}
//
//	@Test
//	public void testDecimalStatDelegateLocked() {
//		assertNotNull(staking.decimalStatDelegateLocked());
//	}
//
//	@Test
//	public void testDecimalStatDelegateReduction() {
//		assertNotNull(staking.decimalStatDelegateReduction());
//	}
//
//	@Test
//	public void testIntegerStakingLocked() {
//		assertNotNull(staking.integerStakingLocked());
//	}
//
//	@Test
//	public void testIntegerStakingHas() {
//		assertNotNull(staking.integerStakingHas());
//	}
//
//	@Test
//	public void testIntegerBlockRewardValue() {
//		assertNotNull(staking.integerBlockRewardValue());
//	}
//
//	@Test
//	public void testIntegerStakingRewardValue() {
//		assertNotNull(staking.integerStakingRewardValue());
//	}
//
//	@Test
//	public void testIntegerStakingReduction() {
//		assertNotNull(staking.integerStakingReduction());
//	}
//
//	@Test
//	public void testIntegerStatDelegateHas() {
//		assertNotNull(staking.integerStatDelegateHas());
//	}
//
//	@Test
//	public void testIntegerStatDelegateLocked() {
//		assertNotNull(staking.integerStatDelegateLocked());
//	}
//
//	@Test
//	public void testIntegerStatDelegateReduction() {
//		assertNotNull(staking.integerStatDelegateReduction());
//	}
//
//	@Test
//	public void testIntegerStakingBlockNum() {
//		staking.setStakingBlockNum(1024L);
//		assertNotNull(staking.integerStakingBlockNum());
//	}
//
//	@Test
//	public void testGetStatusEnum() {
//		StatusEnum en = staking.getStatusEnum();
//		assertNotNull(en);
//	}
//
//	@Test
//	public void testStatusEnum() {
//		StatusEnum en = StatusEnum.valueOf("CANDIDATE");
//		assertNotNull(en);
//	}
//
//	@Test
//	public void testYesNoEnum() {
//		YesNoEnum en = YesNoEnum.valueOf("YES");
//		assertNotNull(en);
//	}
//
//	@Test
//	public void testGetDelegations() {
//		assertNotNull(staking.getDelegations());
//	}
//
//}
