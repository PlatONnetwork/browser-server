//package com.platon.browser.dao.mapper;
//
//import static org.junit.Assert.assertTrue;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.dto.CustomNode;
//
//public class CustomNodeMapperTest extends TestBase {
//
//	@Autowired
//	private CustomNodeMapper customNodeMapper;
//	@Autowired
//	private NodeMapper nodeMapper;
//
//	@Test
//	public void testSelectAll() {
//		List<CustomNode> list = customNodeMapper.selectAll();
//		assertTrue(list.size()>=0);
//	}
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
//		List<Node> list = nodeMapper.selectByExample(null);
//		Set<Node> set = new HashSet<>(list);
//		int num = customNodeMapper.batchInsertOrUpdateSelective(set, Node.Column.values());
//		assertTrue(num>=0);
//	}
//
//}
