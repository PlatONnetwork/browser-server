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
//
//public class CustomNetworkStatMapperTest extends TestBase {
//
//	@Autowired
//	private CustomNetworkStatMapper networkStatMapper;
//	@Autowired
//	private NetworkStatMapper mapper;
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
//		List<NetworkStat> list = mapper.selectByExample(null);
//		Set<NetworkStat> set = new HashSet<>(list);
//		int num = networkStatMapper.batchInsertOrUpdateSelective(set, NetworkStat.Column.values());
//		assertTrue(num>0);
//	}
//
//}
