package com.platon.browser;

import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.http.HttpService;
import com.platon.tx.RawTransactionManager;
import com.platon.tx.TransactionManager;
import com.platon.tx.Transfer;
import com.platon.tx.gas.ContractGasProvider;
import com.platon.tx.gas.GasProvider;
import com.platon.utils.Convert;
import com.platon.browser.contract.AlatContract;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Slf4j
public abstract class ContractTestBase {
	protected File contractList = new File( "E:\\Java\\browser-server\\browser-test\\src\\test\\resources\\contract.list");
	protected File contractCurr = new File( "E:\\Java\\browser-server\\browser-test\\src\\test\\resources\\contract.curr");

	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(4700000);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(1000000L);
	protected static final long chainId = 201018;
	protected static final String nodeUrl = "http://192.168.112.172:6778";
	protected static final String privateKey = "0xa689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b";
	protected Credentials ownerWallet;
	protected Web3j web3j;
	protected TransactionManager transactionManager;
	protected GasProvider gasProvider;

	// 测试钱包
	protected Credentials testWalletA = Credentials.create("0x690a32ceb7eab4131f7be318c1672d3b9b2dadeacba20b99432a7847c1e926e1");
	protected Credentials testWalletB = Credentials.create("0xf51ca759562e1daf9e5302d121f933a8152915d34fcbc27e542baf256b5e4b74");
	protected Credentials testWalletC = Credentials.create("0x3a4130e4abb887a296eb38c15bbd83253ab09492a505b10a54b008b7dcc1668");

	@Before
	public void init() {
		ownerWallet = Credentials.create(privateKey);
		web3j = Web3j.build(new HttpService(nodeUrl));
		transactionManager = new RawTransactionManager(web3j, ownerWallet);
		gasProvider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);
	}

	/**
	 * 对指定钱包地址充钱
	 * @throws Exception
	 */
	protected void charge() throws Exception {
		BigInteger ownerWalletBalance = web3j.platonGetBalance(ownerWallet.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
		log.info("ownerWalletBalance:{}",ownerWalletBalance);

		Transfer.sendFunds(
				web3j,
				ownerWallet,
				testWalletA.getAddress(),
				BigDecimal.valueOf(10000000),
				Convert.Unit.KPVON
		).send();
		BigInteger testWalletABalance = web3j.platonGetBalance(testWalletA.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
		log.info("testWalletABalance:{}",testWalletABalance);

		Transfer.sendFunds(
				web3j,
				ownerWallet,
				testWalletB.getAddress(),
				BigDecimal.valueOf(10000000),
				Convert.Unit.KPVON
		).send();
		BigInteger testWalletBBalance = web3j.platonGetBalance(testWalletB.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
		log.info("testWalletBBalance:{}",testWalletBBalance);

		Transfer.sendFunds(
				web3j,
				ownerWallet,
				testWalletC.getAddress(),
				BigDecimal.valueOf(10000000),
				Convert.Unit.KPVON
		).send();
		BigInteger testWalletCBalance = web3j.platonGetBalance(testWalletC.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
		log.info("testWalletCBalance:{}",testWalletCBalance);
	}

	protected AlatContract loadContract(String address, Credentials credentials){
//		AlatContract.load()
		return AlatContract.load(address, web3j, credentials, gasProvider);
	}

	protected String getCurrContractAddress(){
		try {
			List<String> lines = FileUtils.readLines(contractCurr,"UTF-8");
			if(lines.size()>0) return lines.get(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
