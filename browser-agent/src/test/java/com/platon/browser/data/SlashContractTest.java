package com.platon.browser.data;

import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.DuplicateSignType;
import org.web3j.platon.contracts.SlashContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.PlatonSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

public class SlashContractTest {
	// private Web3j web3j = Web3j.build(new
	// HttpService("http://192.168.120.76:6794"));
	private Web3j web3j = Web3j.build(new HttpService("http://192.168.112.172:8789"));

	private String evidence = "{\"prepare_a\":{\"epoch\":0,\"view_number\":0,\"block_hash\":\"0xdd563dd8edd5fd584027668abeec3606e786847b1e959c78961994a140f90be7\",\"block_number\":500,\"block_index\":0,\"validate_node\":{\"index\":0,\"address\":\"0xcfe51d85f9965f6d031e4e3cce688eab7c95e940\",\"NodeID\":\"bfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf\",\"blsPubKey\":\"b4713797d296c9fe1749d22eb59b03d9694ab896b71449b0e6daf2d1ecb3a9d3d6e9c258b37acb2d07fa82bcb55ced144fb4b056d6cd192a509859615b090128d6e5686e84df47951e1781625627907054975f76e427da8d32d3f30b9a53e60f\"},\"signature\":\"0x3e66e9c58ce70cdd095d22dbb0b77c3115dde484e0ed0ffc8d44e5c6faeb4e1946298299b9781060f046b7ca3a5c8c1000000000000000000000000000000000\"},\"prepare_b\":{\"epoch\":0,\"view_number\":0,\"block_hash\":\"0xb2f002c8bdb243570c912b86d60e8b39baa638cf5d6c977135bf8839b4ebfb2e\",\"block_number\":500,\"block_index\":0,\"validate_node\":{\"index\":0,\"address\":\"0xcfe51d85f9965f6d031e4e3cce688eab7c95e940\",\"NodeID\":\"bfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf\",\"blsPubKey\":\"b4713797d296c9fe1749d22eb59b03d9694ab896b71449b0e6daf2d1ecb3a9d3d6e9c258b37acb2d07fa82bcb55ced144fb4b056d6cd192a509859615b090128d6e5686e84df47951e1781625627907054975f76e427da8d32d3f30b9a53e60f\"},\"signature\":\"0xbc4fcc18f08fde3cb8e965bdc6feece015b304c4a263c616666ad645bf6ec85568d1ac4283a846dd02ed9170766f211100000000000000000000000000000000\"}}";

	private SlashContract slashContract;

	private Credentials credentials;

	@Before
	public void init() {

		credentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");

		slashContract = SlashContract.load(web3j, credentials, "100");
	}

	/**
	 * 举报双签 data 证据的json值，格式为RPC接口Evidences的返回值
	 */
	@Test
	public void reportDuplicateSign() {
		try {
			PlatonSendTransaction platonSendTransaction = slashContract
					.reportDoubleSignReturnTransaction(DuplicateSignType.PREPARE_BLOCK, evidence).send();
			BaseResponse<?> baseResponse = slashContract.getReportDoubleSignResult(platonSendTransaction).send();
			System.out.println(baseResponse.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询节点是否已被举报过多签 typ 代表双签类型，1：prepare，2：viewChange，3：TimestampViewChange addr
	 * 举报的节点地址 blockNumber 多签的块高
	 */
	@Test
	public void checkDuplicateSign() {
		try {
			BaseResponse<?> baseResponse = slashContract.checkDoubleSign(DuplicateSignType.PREPARE_BLOCK,
					"0xfa0974d4f6ec349206c1f7cea8dda465cbd17757", BigInteger.valueOf(500)).send();

			System.out.println(baseResponse.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
