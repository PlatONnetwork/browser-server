package com.platon.browser.proxyppos;

import com.platon.sdk.utlis.NetworkParameters;
import org.junit.Before;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.GasProvider;

import java.math.BigInteger;
import java.util.List;

public abstract class TestBase {
	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(999999L);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(1000000000000L);

	protected static final long chainId = 103;
	protected static final String nodeUrl = "http://192.168.120.145:6790";

	protected Credentials credentials;
	protected String credentialsAddress;
	protected Credentials benefitCredentials;

	protected Web3j web3j;
	protected TransactionManager transactionManager;
	protected GasProvider gasProvider;

	protected String proxyContractAddress = "lax1ufjvfyxxxy6q3j5ayth97pcrn9pn475swqed9h";
	protected ProxyContract proxyContract;

	@Before
	public void init() {
		credentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
		benefitCredentials =  Credentials.create("0x3581985348bffd03b286b37712165f7addf3a8d907b25efc44addf54117e9b91");
		credentialsAddress = credentials.getAddress(NetworkParameters.TestNetParams);
		web3j = Web3j.build(new HttpService(nodeUrl));
		transactionManager = new RawTransactionManager(web3j, credentials, chainId);
		gasProvider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);

		proxyContract = ProxyContract.load(proxyContractAddress, web3j, transactionManager, gasProvider, chainId);
	}

	protected void invokeProxyContract(byte[] data1,String pposContractAddress1,byte[] data2,String pposContractAddress2) throws Exception {
		TransactionReceipt transactionReceipt = proxyContract.proxySend(data1, pposContractAddress1, data2,pposContractAddress2).send();
		System.out.println("send hash = " + transactionReceipt.getTransactionHash());
		List<ProxyContract.ProxyEventEventResponse> eventEvents = proxyContract.getProxyEventEvents(transactionReceipt);
		eventEvents.forEach(event -> System.out.println(event.log + " one"  + new String(event.oneEvent) + "  two" +  new String(event.twoEvent)));
	}
}
