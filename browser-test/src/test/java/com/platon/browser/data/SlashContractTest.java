package com.platon.browser.data;//package com.platon.browser.data;

import com.platon.contracts.ppos.SlashContract;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.TransactionResponse;
import com.platon.contracts.ppos.dto.common.DuplicateSignType;
import org.junit.Before;
import org.junit.Test;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.PlatonSendTransaction;
import com.platon.protocol.http.HttpService;

import java.math.BigInteger;

public class SlashContractTest {
	// private Web3j web3j = Web3j.build(new
	// HttpService("http://192.168.120.76:6794"));
	private Web3j web3j = Web3j.build(new HttpService("http://192.168.112.172:8789"));

	private String evidence = "{\"prepareA\":{\"epoch\":0,\"viewNumber\":0,\"blockHash\":\"0x6cb6b43b5ef4c9dafb15490a859e02762971e5e67d81affded0bb83fc55bd9c7\",\"blockNumber\":148631,\"blockIndex\":0,\"blockData\":\"0x4016e722f29004bb4e159ef07fd8b09e14e4512b01f0ac81c688ed66422f92bc\",\"validateNode\":{\"index\":0,\"address\":\"0xcfe51d85f9965f6d031e4e3cce688eab7c95e940\",\"nodeId\":\"bfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf\",\"blsPubKey\":\"b4713797d296c9fe1749d22eb59b03d9694ab896b71449b0e6daf2d1ecb3a9d3d6e9c258b37acb2d07fa82bcb55ced144fb4b056d6cd192a509859615b090128d6e5686e84df47951e1781625627907054975f76e427da8d32d3f30b9a53e60f\"},\"signature\":\"0x2a7f1282177c4a84a5d53c7595956b255042be1efeac5e142b9c6a2d673f04e46b1af2665294a3994c14aa1ee6dd211000000000000000000000000000000000\"},\"prepareB\":{\"epoch\":0,\"viewNumber\":0,\"blockHash\":\"0xc410e26bd8a8d8a0e5c57560b0b2561fd2449992d9616fc42f9d3dc2d9235e73\",\"blockNumber\":148631,\"blockIndex\":0,\"blockData\":\"0xd46bd95d65689b8c53c643e07c5deb24e757b3786c49451118830bce0b7056a2\",\"validateNode\":{\"index\":0,\"address\":\"0xcfe51d85f9965f6d031e4e3cce688eab7c95e940\",\"nodeId\":\"bfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf\",\"blsPubKey\":\"b4713797d296c9fe1749d22eb59b03d9694ab896b71449b0e6daf2d1ecb3a9d3d6e9c258b37acb2d07fa82bcb55ced144fb4b056d6cd192a509859615b090128d6e5686e84df47951e1781625627907054975f76e427da8d32d3f30b9a53e60f\"},\"signature\":\"0x6fa3eaeefb494b65f68c8740f5dcd29e000bb003e5672d71e95dadfd19811c88075b6668bf16d35dda5857830812421600000000000000000000000000000000\"}}";

	private SlashContract slashContract;

	private Credentials credentials;

	@Before
	public void init() {

		credentials = Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7");

		slashContract = SlashContract.load(web3j, credentials);
	}

	/**
	 * 举报双签 data 证据的json值，格式为RPC接口Evidences的返回值
	 */
	@Test
	public void reportDuplicateSign() {
		try {
			PlatonSendTransaction platonSendTransaction = slashContract
					.reportDoubleSignReturnTransaction(DuplicateSignType.PREPARE_BLOCK, evidence).send();
			TransactionResponse baseResponse = slashContract.getTransactionResponse(platonSendTransaction).send();
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
			CallResponse<?> baseResponse = slashContract.checkDoubleSign(DuplicateSignType.PREPARE_BLOCK,
					"0xfa0974d4f6ec349206c1f7cea8dda465cbd17757", BigInteger.valueOf(500)).send();

			System.out.println(baseResponse.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
