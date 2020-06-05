package com.platon.browser.proxyppos;

import com.alibaba.fastjson.JSON;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.GasProvider;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

public abstract class TestBase {
	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(999999L);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(1000000000000L);
	protected static final GasProvider gasProvider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);
	protected static final long chainId = 103;
	protected static Web3j defaultWeb3j = Web3j.build(new HttpService("http://192.168.120.145:6790"));

	protected static Credentials benefitCredentials =
			Credentials.create("3581985348bffd03b286b37712165f7addf3a8d907b25efc44addf54117e9b91");
	protected static Credentials defaultCredentials =
			Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");

	protected String proxyContractAddress = "lax1ufjvfyxxxy6q3j5ayth97pcrn9pn475swqed9h";

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
	 * 部署PPOS代理合约
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String address = deployProxyContract();
		System.out.println(address);
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
			+"\nLog:"+JSON.toJSONString(event.log,true)
		));
		System.out.println("===============================================");
		System.out.println("===============================================");
	}
}
