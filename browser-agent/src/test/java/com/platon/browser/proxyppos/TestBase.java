package com.platon.browser.proxyppos;

import com.platon.sdk.utlis.NetworkParameters;
import org.junit.After;
import org.junit.Before;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.GasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
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

	protected String proxyContractAddress = CommonTest.PROXY_CONTRACT_ADDRESS;
	protected ProxyContract proxyContract;

	@Before
	public void before() throws IOException {
		credentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
		benefitCredentials =  Credentials.create("0x3581985348bffd03b286b37712165f7addf3a8d907b25efc44addf54117e9b91");
		credentialsAddress = credentials.getAddress(NetworkParameters.TestNetParams);
		web3j = Web3j.build(new HttpService(nodeUrl));
		transactionManager = new RawTransactionManager(web3j, credentials, chainId);
		gasProvider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);
		proxyContract = ProxyContract.load(proxyContractAddress, web3j, transactionManager, gasProvider, chainId);

		BigInteger balance = web3j.platonGetBalance(credentials.getAddress(chainId), DefaultBlockParameterName.LATEST).send().getBalance();
		System.out.println("【Before】 balance of ["+credentials.getAddress(chainId)+"]:"+balance+"(VON),"+ Convert.fromVon(balance.toString(), Convert.Unit.LAT)+"(LAT)");

		BigInteger contractBalance = web3j.platonGetBalance(proxyContractAddress, DefaultBlockParameterName.LATEST).send().getBalance();
		System.out.println("【Before】 contract balance of ["+proxyContractAddress+"]:"+contractBalance+"(VON),"+ Convert.fromVon(contractBalance.toString(), Convert.Unit.LAT)+"(LAT)");
	}

	@After
	public void after() throws IOException {
		BigInteger balance = web3j.platonGetBalance(credentials.getAddress(chainId), DefaultBlockParameterName.LATEST).send().getBalance();
		System.out.println("【After】 balance of ["+credentials.getAddress(chainId)+"]:"+balance+"(VON),"+ Convert.fromVon(balance.toString(), Convert.Unit.LAT)+"(LAT)");

		BigInteger contractBalance = web3j.platonGetBalance(proxyContractAddress, DefaultBlockParameterName.LATEST).send().getBalance();
		System.out.println("【After】 contract balance of ["+proxyContractAddress+"]:"+contractBalance+"(VON),"+ Convert.fromVon(contractBalance.toString(), Convert.Unit.LAT)+"(LAT)");
	}

	protected String deployProxyContract() throws Exception {
		String contractAddress = ProxyContract.deploy(web3j,credentials,gasProvider,chainId).send().getContractAddress();
		System.out.println(contractAddress);
		return contractAddress;
	}

	protected void invokeProxyContract(byte[] data1,String pposContractAddress1,byte[] data2,String pposContractAddress2) throws Exception {
		TransactionReceipt transactionReceipt = proxyContract.proxySend(data1, pposContractAddress1, data2,pposContractAddress2).send();
		System.out.println("BlockNum="+transactionReceipt.getBlockNumber()+",TxHash = " + transactionReceipt.getTransactionHash());
		List<ProxyContract.ProxyEventEventResponse> eventEvents = proxyContract.getProxyEventEvents(transactionReceipt);
		eventEvents.forEach(event -> System.out.println(event.log + " one"  + new String(event.oneEvent) + "  two" +  new String(event.twoEvent)));
	}
}
