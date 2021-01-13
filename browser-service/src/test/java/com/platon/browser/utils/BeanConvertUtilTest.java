//package com.platon.browser.util;
//
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//
//import com.platon.browser.res.proposal.ProposalDetailsResp;
//
//public class BeanConvertUtilTest {
//
//	@Test
//	public void testBeanConvert() {
//		Proposal proposal = new Proposal();
//		proposal.setPipNum("0xefsdfefa5fse");
//		ProposalDetailsResp resp = BeanConvertUtil.beanConvert(proposal, ProposalDetailsResp.class);
//		assertTrue(proposal.getPipNum().equals(resp.getPipNum()));
//		resp = BeanConvertUtil.beanConvert(null, ProposalDetailsResp.class);
//		assertNull(resp);
//	}
//
//	@Test
//	public void testListConvert() {
//		Proposal proposal = new Proposal();
//		proposal.setPipNum("0xefsdfefa5fse");
//		List<Proposal> proposals = new ArrayList<>();
//		proposals.add(proposal);
//		List<ProposalDetailsResp> resps = BeanConvertUtil.listConvert(proposals, ProposalDetailsResp.class);
//		assertTrue(proposal.getPipNum().equals(resps.get(0).getPipNum()));
//	}
//
//	@Test
//	public void testObjectToMap() {
//		Proposal proposal = new Proposal();
//		proposal.setPipNum("0xefsdfefa5fse");
//		Map<?, ?> map = BeanConvertUtil.objectToMap(proposal);
//		assertTrue(proposal.getPipNum().equals(map.get("pipNum")));
//	}
//
//	@Test
//	public void testMapToObject() {
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("pipNum","0xefsdfefa5fse");
//		Proposal proposal = BeanConvertUtil.mapToObject(map,Proposal.class);
//		assertTrue(proposal.getPipNum().equals(map.get("pipNum")));
//	}
//
//}
