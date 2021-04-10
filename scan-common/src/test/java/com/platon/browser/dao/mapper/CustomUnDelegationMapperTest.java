//package com.platon.browser.dao.mapper;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.dto.CustomUnDelegation;
//
//public class CustomUnDelegationMapperTest extends TestBase {
//
//	@Autowired
//	private CustomUnDelegationMapper customUnDelegationMapper;
//
//	@Test
//	public void testSelectByNodeId() {
//		List<CustomUnDelegation> list = customUnDelegationMapper.selectByNodeId("");
//		assertNotNull(list);
//	}
//
//	@Test
//	public void testSelectByNodeIdList() {
//		List<String> nodeIds = new ArrayList<String>();
//		nodeIds.add("");
//		List<CustomUnDelegation> list = customUnDelegationMapper.selectByNodeIdList(nodeIds);
//		assertNotNull(list);
//	}
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
//		Set<UnDelegation> set = new HashSet<>();
//		UnDelegation delegation = new UnDelegation();
//		delegation.setApplyAmount("applyAmount");
//		delegation.setCreateTime(new Date());
//		delegation.setDelegateAddr("delegateAddr");
//		delegation.setHash("hash");
//		delegation.setNodeId("nodeId");
//		delegation.setRealAmount("realAmount");
//		delegation.setRedeemLocked("redeemLocked");
//		delegation.setStakingBlockNum(0l);
//		delegation.setStatus(1);
//		delegation.setUpdateTime(new Date());
//		set.add(delegation);
//		int num = customUnDelegationMapper.batchInsertOrUpdateSelective(set, UnDelegation.Column.values());
//		assertTrue(num>=0);
//	}
//
//}
