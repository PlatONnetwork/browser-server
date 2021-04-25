//package com.platon.browser.dao.mapper;
//
//import com.github.pagehelper.Page;
//import com.platon.browser.TestBase;
//import com.platon.browser.dto.CustomDelegation;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//public class CustomDelegationMapperTest extends TestBase {
//
//	@Autowired
//	private CustomDelegationMapper delegationMapper;
//
//	@Test
//	public void testSelectByNodeId() {
//		List<CustomDelegation> list = delegationMapper.selectByNodeId("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
//		assertNotNull(list);
//	}
//
//	@Test
//	public void testSelectByNodeIdList() {
//		List<String> nodeIds = new ArrayList<String>();
//		nodeIds.add("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
//		List<CustomDelegation> list = delegationMapper.selectByNodeIdList(nodeIds);
//		assertNotNull(list);
//	}
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
////		List<Delegation> list = mapper.selectByExample(null);
////		Set<Delegation> set = new HashSet<>(list);
////		int num = delegationMapper.batchInsertOrUpdateSelective(set, Delegation.Column.values());
////		assertTrue(num>0);
//	}
//
//	@Test
//	public void testSelectDelegationAndStakingByExample() {
//		String address = "0x60ceca9c1290ee56b98d4e160ef0453f7c40d219";
//		Page<DelegationStaking> page = delegationMapper.selectDelegationByExample(address);
//		assertNotNull(page);
//	}
//
//	@Test
//	public void testSelectStakingByExample() {
//		String nodeId = "0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7";
//		Page<DelegationStaking> page = delegationMapper.selectStakingByExample(nodeId,null);
//		assertNotNull(page);
//	}
//
//	@Test
//	public void testSelectSumDelegateByExample() {
//		String nodeId = "0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7";
//		List<DelegationStaking> list = delegationMapper.selectSumDelegateByExample(nodeId,166l);
//		assertTrue(list.size()>0);
//	}
//
////	@Test
////	public void testSelectSumDelegateByAddress() {
////		List<DelegationStaking> list = delegationMapper.selectSumDelegateByAddress("0x60ceca9c1290ee56b98d4e160ef0453f7c40d219");
////		assertTrue(list.size()>0);
////	}
//
//}
