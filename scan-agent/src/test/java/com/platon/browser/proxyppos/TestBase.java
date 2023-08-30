package com.platon.browser.proxyppos;

import com.alibaba.fastjson.JSON;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.protocol.http.HttpService;
import com.platon.tx.gas.ContractGasProvider;
import com.platon.tx.gas.GasProvider;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

public abstract class TestBase {
	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(4700000L);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(1000000000000L);
	protected static final GasProvider gasProvider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);
	protected static final long chainId = 103;
	protected static Web3j defaultWeb3j = Web3j.build(new HttpService("http://192.168.120.145:6790"));

	protected static Credentials benefitCredentials =
			Credentials.create("3581985348bffd03b286b37712165f7addf3a8d907b25efc44addf54117e9b91");
	protected static Credentials defaultCredentials =
			Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");

	protected Credentials delegateCredentials = Credentials.create("5d7f539ac15de26de6abbb664291e613882842d3dbe4ec79b57af8bc6bb834aa");

	protected String proxyStakingContractAddress = "lax1d33nfd4djqyzmp45jq6zkwgac4e5q3jgfznp46";
	protected String proxyDelegateContractAddress = "lax1ufjvfyxxxy6q3j5ayth97pcrn9pn475swqed9h";
	protected String proxySlashContractAddress = "lax16wnrpdpn0kg4c5k0pesax5letmzwpmv7j2dlnn";

	protected static final Properties ERRORS = new Properties();
	static {
		InputStream inputStream = TestBase.class.getClassLoader().getResourceAsStream("error.properties");
		try {
			ERRORS.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String deployProxyContract() throws Exception {
		String contractAddress = ProxyContract.deploy(defaultWeb3j,defaultCredentials,gasProvider,chainId).send().getContractAddress();
		return contractAddress;
	}

	/**
	 * TODO: INIT-STEP-01 当链重新部署时，生成两个代理合约，
	 * 并用生成的合约地址替换本类中的proxyStakingContractAddress和proxyDelegateContractAddress
	 * 部署PPOS代理合约
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String address1 = deployProxyContract();
		System.out.println(address1);
		///String address2 = deployProxyContract();
		//System.out.println(address2);
	}

	protected void invokeProxyContract(ProxyContract targetContract,byte[] data1,String pposContractAddress1,byte[] data2,String pposContractAddress2) throws Exception {
		TransactionReceipt transactionReceipt = targetContract.proxySend(data1, pposContractAddress1, data2,pposContractAddress2).send();
		List<ProxyContract.ProxyEventEventResponse> eventEvents = targetContract.getProxyEventEvents(transactionReceipt);
		System.out.println("===============================================");
		System.out.println("===============================================");
		System.out.println("BlockNum="+transactionReceipt.getBlockNumber()+",TxHash = " + transactionReceipt.getTransactionHash());
		eventEvents.forEach(event -> System.out.println(
			"PPOS RESULT:"
			+"\nOne:"+new String(event.oneEvent)+"("+ERRORS.getProperty(new String(event.oneEvent))+")"
			+"\nTwo:"+new String(event.twoEvent)+"("+ERRORS.getProperty(new String(event.twoEvent))+")"
			+"\nLog:"+JSON.toJSONString(event.log)
		));
		System.out.println("===============================================");
		System.out.println("===============================================");
	}
}
