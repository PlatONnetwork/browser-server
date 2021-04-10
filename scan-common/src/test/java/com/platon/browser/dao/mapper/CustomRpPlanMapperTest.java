//package com.platon.browser.dao.mapper;
//
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.dto.CustomRpPlan;
//
//public class CustomRpPlanMapperTest extends TestBase {
//
//	@Autowired
//	private CustomRpPlanMapper customRpPlanMapper;
////	@Autowired
////	private RpPlanMapper rpPlanMapper;
//
//	@Test
//	public void testSelectAll() {
//		List<CustomRpPlan> list = customRpPlanMapper.selectAll();
//		assertTrue(list.size()>=0);
//	}
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
//		Set<RpPlan> set = new HashSet<>();
//		RpPlan plan = new RpPlan();
//		plan.setAddress("address");
//		plan.setAmount("amount");
//		plan.setCreateTime(new Date());
//		plan.setEpoch(0l);
//		plan.setId(1l);
//		plan.setNumber(0l);
//		plan.setUpdateTime(new Date());
//		set.add(plan);
//		int num = customRpPlanMapper.batchInsertOrUpdateSelective(set, RpPlan.Column.values());
//		assertTrue(num>0);
//	}
//
//	@Test
//	public void testSelectSumByAddress() {
//		BigDecimal num = customRpPlanMapper.selectSumByAddress("");
//		assertNull(num);
//	}
//
//}
