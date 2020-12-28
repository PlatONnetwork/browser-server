//package com.platon.browser.service.impl;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import java.util.Date;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.service.BlockService;
//import com.platon.browser.req.PageReq;
//import com.platon.browser.req.newblock.BlockDetailNavigateReq;
//import com.platon.browser.req.newblock.BlockDetailsReq;
//import com.platon.browser.req.newblock.BlockDownload;
//import com.platon.browser.req.newblock.BlockListByNodeIdReq;
//import com.platon.browser.res.RespPage;
//import com.platon.browser.res.block.BlockDetailResp;
//import com.platon.browser.res.block.BlockListResp;
//
//public class BlockServiceImplTest extends TestBase{
//
//	@Autowired
//    private BlockService blockService;
//	
////	@Test
////	public void blockList() {
////		PageReq pageReq = new PageReq();
////        RespPage<BlockListResp> pages = blockService.blockList(pageReq);
////        assertTrue(pages.getData().size()>=0);
////	}
//	
//	@Test
//	public void blockListByNodeId() {
//		BlockListByNodeIdReq req = new BlockListByNodeIdReq();
//		req.setNodeId("0xef97cb9caf757c70e9aca9062a9f6607ce89c3e7cac90ffee56d3fcffffa55aebd20b48c0db3924438911fd1c88c297d6532b434c56dbb5d9758f0794c6841dc");
//		RespPage<BlockListResp> pages = blockService.blockListByNodeId(req);
//		assertTrue(pages.getData().size()>=0);
//	}
//	
////	@Test
////	public void blockDetails() {
////		BlockDetailsReq req = new BlockDetailsReq();
////		req.setNumber(0);
////		BlockDetailResp blockDetailResp = blockService.blockDetails(req);
////		assertNotNull(blockDetailResp);
////	}
//	
////	@Test
////	public void blockDetailNavigate() {
////		BlockDetailNavigateReq req = new BlockDetailNavigateReq();
////		req.setNumber(0l);
////		req.setDirection("next");
////		BlockDetailResp blockDetailResp = blockService.blockDetailNavigate(req);
////		assertNotNull(blockDetailResp);
////	}
//	
//	@Test
//	public void blockListByNodeIdDownload() {
//		String nodeId = "0xef97cb9caf757c70e9aca9062a9f6607ce89c3e7cac90ffee56d3fcffffa55aebd20b48c0db3924438911fd1c88c297d6532b434c56dbb5d9758f0794c6841dc";
//		BlockDownload download = blockService.blockListByNodeIdDownload(nodeId, new Date().getTime(), "en", "+8");
//		assertNotNull(download.getData());
//	}
//	
//}
