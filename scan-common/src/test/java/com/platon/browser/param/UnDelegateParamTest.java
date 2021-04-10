//package com.platon.browser.param;
//
//import static org.junit.Assert.assertTrue;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class UnDelegateParamTest {
//
//	private UnDelegateParam param;
//
//	@Before
//	public void setUp() throws Exception {
//		param = new UnDelegateParam();
//		param.init(1024L,"nodeId","1000000000000000000");
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
