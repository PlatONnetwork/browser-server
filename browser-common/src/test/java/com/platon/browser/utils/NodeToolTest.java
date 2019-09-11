package com.platon.browser.utils;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.web3j.protocol.core.methods.response.PlatonBlock;

public class NodeToolTest {
	
	private PlatonBlock.Block block;
	
	@Before
	public void setUp() throws Exception {
/*
Block 
[
number=1, hash=0x3b84cbfb116fd513346bb204df1140600ab0d6effe1529a2ef32f54b6bcea8dc, 
parentHash=0x6aabd28f47505c87a158ffe8841e3438a412d2ca11f8c23d4d6ab13011916d0f, 
timestamp=Wed Sep 11 11:24:53 CST 2019, size=728, gasLimit=41065279402, 
gasUsed=0, statTxQty=0, statTransferQty=0, statStakingQty=0, statProposalQty=0, 
statDelegateQty=0, statTxGasLimit=0, statTxFee=0, nodeName=platon.node.1, 
nodeId=0x4fcc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba4150f, 
blockReward=81942419527161406250000, miner=0x1000000000000000000000000000000000000003, 
createTime=Wed Sep 11 11:25:29 CST 2019, updateTime=Wed Sep 11 11:25:29 CST 2019, 
extraData=0xd782070186706c61746f6e86676f312e3131856c696e7578000000000000000015825c1538d89090737d274801fa977041fd5d46f94864c7e034852b296040f9480744021fd25030250da5e538a6e6009c2b089709009a4c643771e4d39cde1601]
 */
		block = new PlatonBlock.Block();
		block.setNumber("0x1");
		block.setHash("0x3b84cbfb116fd513346bb204df1140600ab0d6effe1529a2ef32f54b6bcea8dc");
		block.setParentHash("0x6aabd28f47505c87a158ffe8841e3438a412d2ca11f8c23d4d6ab13011916d0f");
		block.setTimestamp("1568172293");
		block.setSize("728");
		block.setGasLimit("41065279402");
		block.setGasUsed("0");
		block.setTransactions(new ArrayList<>());
		block.setMiner("0x1000000000000000000000000000000000000003");
		block.setExtraData("0xd782070186706c61746f6e86676f312e3131856c696e7578000000000000000015825c1538d89090737d274801fa977041fd5d46f94864c7e034852b296040f9480744021fd25030250da5e538a6e6009c2b089709009a4c643771e4d39cde1601");
	}


	@Test
	public void testCalculateNodePublicKey() {
		String publicKey = NodeTool.calculateNodePublicKey(block);
		System.out.println(publicKey);
	}

	@Test
	public void testTestBlock() {
		// TODO
	}

	@Test
	public void testAsRlpValues() {
		// TODO
	}

	@Test
	public void testDecodeHash() {
		// TODO
	}

}
