//package com.platon.browser.dao.mapper;
//
//import java.util.List;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.platon.browser.TestBase;
//
//import static org.junit.Assert.assertTrue;
//
//public class CustomTransactionMapperTest extends TestBase {
//
//	@Autowired
//	private TransactionMapper transactionMapper;
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
//		TransactionExample example = new TransactionExample();
//		TransactionExample.Criteria criteria = example.createCriteria();
//		criteria.andHashEqualTo("0x17974c832860b52e6ac099503bb781e31d01d8c515e6f520487b848f8b3763f6");
//		criteria.andBlockNumberEqualTo(28150l);
//		List<TransactionWithBLOBs> list = transactionMapper.selectByExampleWithBLOBs(example);
//		System.out.println(list.size());
////		Set<TransactionWithBLOBs> set = new HashSet<>(list);
////		int num = customTransactionMapper.batchInsertOrUpdateSelective(set, TransactionWithBLOBs.Column.values());
//		assertTrue(true);
//	}
//
//}
