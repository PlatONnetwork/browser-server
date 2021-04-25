//package com.platon.browser.dao.mapper;
//
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
//import com.platon.browser.dto.CustomSlash;
//
//public class CustomSlashMapperTest extends TestBase {
//
//	@Autowired
//	private CustomSlashMapper customSlashMapper;
//
//	@Test
//	public void testSelectByNodeId() {
//		List<CustomSlash> list = customSlashMapper.selectByNodeId("");
//		assertTrue(list.size()==0);
//	}
//
//	@Test
//	public void testSelectByNodeIdList() {
//		List<String> nodeIds = new ArrayList<>();
//		nodeIds.add("");
//		List<CustomSlash> list = customSlashMapper.selectByNodeIdList(nodeIds);
//		assertTrue(list.size()==0);
//	}
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
//		Set<Slash> set = new HashSet<>();
//		Slash slash = new Slash();
//		slash.setCreateTime(new Date());
//		slash.setData("data");
//		slash.setDenefitAddr("denefitAddr");
//		slash.setHash("hash");
//		slash.setIsQuit(1);
//		slash.setNodeId("nodeId");
//		slash.setReward("reward");
//		slash.setSlashRate("slashRate");
//		slash.setStatus(1);
//		slash.setUpdateTime(new Date());
//		set.add(slash);
//		int num = customSlashMapper.batchInsertOrUpdateSelective(set, Slash.Column.values());
//		assertTrue(num>=0);
//	}
//
//}
