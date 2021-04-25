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
//import com.platon.browser.TestBase;
//import com.platon.browser.dto.CustomNodeOpt;
//
//public class CustomNodeOptMapperTest extends TestBase {
//
//	@Autowired
//	private CustomNodeOptMapper customNodeOptMapper;
//	@Autowired
//	private NodeOptMapper nodeOptMapper;
//
//	@Test
//	public void testSelectByNodeId() {
//		List<CustomNodeOpt> list = customNodeOptMapper.selectByNodeId("");
//		assertNotNull(list);
//	}
//
//	@Test
//	public void testSelectByNodeIdList() {
//		List<String> nodeIds = new ArrayList<>();
//		nodeIds.add("");
//		List<CustomNodeOpt> list = customNodeOptMapper.selectByNodeIdList(nodeIds);
//		assertNotNull(list);
//	}
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
//		List<NodeOpt> list =nodeOptMapper.selectByExample(null);
//		Set<NodeOpt> set = new HashSet<>(list);
//		int num = customNodeOptMapper.batchInsertOrUpdateSelective(set, NodeOpt.Column.values());
//		assertTrue(num>=0);
//	}
//
//}
