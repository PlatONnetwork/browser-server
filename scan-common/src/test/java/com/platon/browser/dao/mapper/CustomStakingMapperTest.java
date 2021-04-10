//package com.platon.browser.dao.mapper;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.github.pagehelper.Page;
//import com.platon.browser.TestBase;
//import com.platon.browser.dto.CustomStaking;
//
//public class CustomStakingMapperTest extends TestBase {
//
//	@Autowired
//	private CustomStakingMapper customStakingMapper;
//	@Autowired
//	private StakingMapper stakingMapper;
//
//	@Test
//	public void testSelectByNodeId() {
//		List<CustomStaking> list = customStakingMapper.selectByNodeId("");
//		assertNotNull(list);
//	}
//
//	@Test
//	public void testSelectByNodeIdList() {
//		List<String> nodeIds = new ArrayList<String>();
//		nodeIds.add("");
//		List<CustomStaking> list = customStakingMapper.selectByNodeIdList(nodeIds);
//		assertNotNull(list);
//	}
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
//		List<Staking> list = stakingMapper.selectByExample(null);
//		Set<Staking> set = new HashSet<Staking>(list);
//		int num = customStakingMapper.batchInsertOrUpdateSelective(set , Staking.Column.values());
//		assertTrue(num>0);
//	}
//
//	@Test
//	public void testSelectStakingAndNodeByExample() {
//		Page<StakingNode> page = customStakingMapper.selectStakingAndNodeByExample("", "", 1, 1, 1);
//		assertTrue(page.getResult().size()==0);
//	}
//
//	@Test
//	public void testSelectStakingAndNodeByNodeId() {
//		List<StakingNode> list = customStakingMapper.selectStakingAndNodeByNodeId("");
//		assertNotNull(list);
//	}
//
//	@Test
//	public void testSelectHistoryNode() {
//		List<Integer> statusList = new ArrayList<>();
//		statusList.add(0);
//		Page<StakingNode> page = customStakingMapper.selectHistoryNode("", statusList);
//		assertTrue(page.getResult().size()==0);
//	}
//
//}
