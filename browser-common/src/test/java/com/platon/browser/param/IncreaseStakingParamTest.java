//package com.platon.browser.param;
//
//import static org.junit.Assert.assertTrue;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class IncreaseStakingParamTest {
//
//	private IncreaseStakingParam param;
//
//	@Before
//	public void setUp() throws Exception {
//		param = new IncreaseStakingParam();
//		param.init("nodeId",1,"1000000000000000000","1024");
//	}
//
//	@Test
//	public void testInit() {
//		assertTrue(param.getNodeId().equals("nodeId"));
//	}
//
//	@Test
//	public void testDecimalAmount() {
//		assertTrue(param.decimalAmount().longValue()==1000000000000000000l);
//	}
//
//	@Test
//	public void testIntegerAmount() {
//		assertTrue(param.integerAmount().longValue()==1000000000000000000l);
//	}
//
//}
