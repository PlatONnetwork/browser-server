package com.platon.browser.proxyppos;

import com.alibaba.fastjson.JSON;
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
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

public abstract class TestBase {
	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(999999L);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(1000000000000L);

	protected static final long chainId = 103;
	protected static final String nodeUrl = "http://192.168.120.145:6790";

	protected Credentials defaultCredentials;
	protected String defaultCredentialsAddress;
	protected Credentials benefitCredentials;

	protected static final Properties ERRORS = new Properties();

	static {
		InputStream inputStream = TestBase.class.getClassLoader().getResourceAsStream("error.properties");
		try {
			ERRORS.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected Web3j web3j;
	protected TransactionManager defaultTransactionManager;
	protected GasProvider gasProvider;

	protected String proxyContractAddress = CommonTest.PROXY_CONTRACT_ADDRESS;
	protected ProxyContract defaultProxyContract;

	@Before
	public void before() throws IOException {
		defaultCredentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
		benefitCredentials =  Credentials.create("0x3581985348bffd03b286b37712165f7addf3a8d907b25efc44addf54117e9b91");
		defaultCredentialsAddress = defaultCredentials.getAddress(NetworkParameters.TestNetParams);
		web3j = Web3j.build(new HttpService(nodeUrl));
		defaultTransactionManager = new RawTransactionManager(web3j, defaultCredentials, chainId);
		gasProvider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);
		defaultProxyContract = ProxyContract.load(proxyContractAddress, web3j, defaultTransactionManager, gasProvider, chainId);

		BigInteger contractBalance = web3j.platonGetBalance(proxyContractAddress, DefaultBlockParameterName.LATEST).send().getBalance();
		System.out.println("【Before】 contract balance of ["+proxyContractAddress+"]:"+contractBalance+"(VON),"+ Convert.fromVon(contractBalance.toString(), Convert.Unit.LAT)+"(LAT)");
	}

	@After
	public void after() throws IOException {
		BigInteger contractBalance = web3j.platonGetBalance(proxyContractAddress, DefaultBlockParameterName.LATEST).send().getBalance();
		System.out.println("【After】 contract balance of ["+proxyContractAddress+"]:"+contractBalance+"(VON),"+ Convert.fromVon(contractBalance.toString(), Convert.Unit.LAT)+"(LAT)");
	}

	protected String deployProxyContract() throws Exception {
		String contractAddress = ProxyContract.deploy(web3j,defaultCredentials,gasProvider,chainId).send().getContractAddress();
		System.out.println(contractAddress);
		return contractAddress;
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
