package com.platon.browser.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.platon.crypto.Credentials;
import com.platon.crypto.Sign;
import com.platon.crypto.Sign.SignatureData;
import com.platon.protocol.core.methods.response.PlatonBlock;
import com.platon.utils.Numeric;

public class NodeUtilTest {

	@Test
	public void testGetPublickey() {
//		Credentials credentials = Credentials.create("35d54365a056d9afacf5f5ab709b422a869879604f3117010d23c865d2731ae6");
		Credentials credentials = Credentials.create("fb054d54312a6de6fc0a5a3049e63525d9dccd0cd94b55ebe535ec25acd54c8a");
		PlatonBlock.Block block = new PlatonBlock.Block();
		block.setNumber("0x123");
		block.setParentHash("0x40dcaa2aabfd7de17a9dd21094db7190c3d16af3ac164bc5462dfff73c764cf2");
		block.setMiner("lax10np85x9huvu8rt85ekpws7wr4vjx9j4fty2f2l");
		block.setExtraData("000000000000000000000000000000000000000000000000000000000000000000");
		block.setStateRoot("0x40dcaa2aabfd7de17a9dd21094db7190c3d16af3ac164bc5462dfff73c764cf2");
		block.setTransactionsRoot("0x40dcaa2aabfd7de17a9dd21094db7190c3d16af3ac164bc5462dfff73c764cf2");
		block.setReceiptsRoot("0x40dcaa2aabfd7de17a9dd21094db7190c3d16af3ac164bc5462dfff73c764cf2");
		block.setLogsBloom("0xd782070186706c61746f6e86676f312e3131856c696e757800000000000000008e9526e312a8478b118fe9ae29567558e936c033cd0504edb7b33a082fb8d15e77caeedfc3aa42369e068211a15777e174cfa92229ddca1257e9618e8ec84c4201");
		block.setNonce("0x40dcaa2aabfd7de17a9dd21094db7190c3d16af3ac164bc5462dfff73c764cf2");
		block.setGasLimit("0x123");
		block.setGasUsed("0x1");
		block.setTimestamp("0x1");
		byte[] msg = NodeUtil.encode(block);
		SignatureData signatureData = Sign.signMessage(msg, credentials.getEcKeyPair());
		byte[] sigData = new byte[65];
		sigData[64] = 0;
		System.arraycopy(signatureData.getR(), 0, sigData, 0, 32);
		System.arraycopy(signatureData.getS(), 0, sigData, 32, 32);
		String signMsg = Numeric.toHexString(sigData);
		block.setExtraData("000000000000000000000000000000000000000000000000000000000000000000" + signMsg.substring(2));
		String hexStr = NodeUtil.getPublicKey(block);
		System.out.println(hexStr);
		assertTrue(hexStr.equals("ef662a76e8d5aa41c5e08b613806e8dc7d8a8559be2a2c19a334e3099cab205bcc074c5af9633ebe84ac590b2967acc2b8cfe25eeb3dbc2b038ec35717edd180"));
	}
}
