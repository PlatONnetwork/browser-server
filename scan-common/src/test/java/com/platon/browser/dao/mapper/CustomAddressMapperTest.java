//package com.platon.browser.dao.mapper;
//
//import static org.junit.Assert.assertTrue;
//
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.dto.CustomAddress;
//
//public class CustomAddressMapperTest extends TestBase {
//
//	@Autowired
//	private CustomAddressMapper customAddressMapper;
//	@Autowired
//	private AddressMapper addressMapper;
//
//	@Test
//	public void testSelectAll() {
//		List<CustomAddress> list = customAddressMapper.selectAll();
//		assertTrue(list.size()>0);
//	}
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
//		List<Address> list = addressMapper.selectByExample(null);
//		list.forEach(address -> address.setUpdateTime(new Date()));
//		Set<Address> set = new HashSet<Address>(list);
//		int num = customAddressMapper.batchInsertOrUpdateSelective(set, Address.Column.values());
//		assertTrue(num>0);
//	}
//
//}
