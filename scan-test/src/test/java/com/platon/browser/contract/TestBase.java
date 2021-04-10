package com.platon.browser.contract;

import com.platon.bech32.Bech32;
import com.platon.crypto.Credentials;
import com.platon.parameters.NetworkParameters;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.http.HttpService;
import com.platon.tx.RawTransactionManager;
import com.platon.tx.TransactionManager;
import com.platon.tx.Transfer;
import com.platon.tx.gas.ContractGasProvider;
import com.platon.tx.gas.GasProvider;
import com.platon.utils.Convert;
import com.platon.browser.v0152.contract.Erc20Contract;
import com.platon.browser.v0152.contract.Erc721Contract;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public abstract class TestBase {
	protected static final String HRP = "atp";
	protected static final long CHAIN_ID = 201018;
	protected static final Web3j WEB3J = Web3j.build(new HttpService("http://192.168.120.151:6789"));
	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(4700000);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(1000000L);

	static {
		NetworkParameters.init(CHAIN_ID, HRP);
	}

	protected GasProvider gasProvider;
	protected String contractAddressFilePath;
	protected TransactionManager transactionManager;
	protected List<Credentials> wallets = new ArrayList<>();
	protected List<String> walletAddressList=new ArrayList<>();
	protected List<String> contractAddressList=Collections.emptyList();
	protected Credentials adminWallet = Credentials.create("0xa689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");

	protected abstract String getContractAddressFileName();

	protected static String randomStr(){
		return RandomStringUtils.randomAlphanumeric(5);
	}

	@Before
	public void init() {
		wallets.addAll(Arrays.asList(
			Credentials.create("0x690a32ceb7eab4131f7be318c1672d3b9b2dadeacba20b99432a7847c1e926e1"),
			Credentials.create("0xf51ca759562e1daf9e5302d121f933a8152915d34fcbc27e542baf256b5e4b74"),
			Credentials.create("0x3a4130e4abb887a296eb38c15bbd83253ab09492a505b10a54b008b7dcc1668")
		));

		transactionManager = new RawTransactionManager(WEB3J, wallets.get(0));
		gasProvider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);
		contractAddressList = Collections.emptyList();
		try {
			String contractAddressFileName = getContractAddressFileName();
			URL url = TestBase.class.getResource(contractAddressFileName);
			String path = url.getPath().replace("build/resources/test","src/test/resources");
			contractAddressFilePath = path;
			File addressListFile = new File(contractAddressFilePath);
			contractAddressList = FileUtils.readLines(addressListFile,"UTF-8");

			String walletAddressFileName = "/wallet.address.list";
			url = TestBase.class.getResource(walletAddressFileName);
			path = url.getPath().replace("build/resources/test","src/test/resources");
			File walletListFile = new File(path);
			List<String> addressList = FileUtils.readLines(walletListFile,"UTF-8");
			for (String address : addressList) {
				String hrpAddress = Bech32.addressEncode(HRP,address);
				walletAddressList.add(hrpAddress);
			}
			log.info("walletAddressList:{}",walletAddressList);
		} catch (IOException e) {
			log.error("",e);
		}
	}

	/**
	 * 对指定钱包地址充钱
	 * @throws Exception
	 */
	protected void charge() throws Exception {
		BigInteger adminWalletBalance = WEB3J.platonGetBalance(adminWallet.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
		log.info("adminWallet:{},balance:{}",adminWallet.getAddress(),adminWalletBalance);
		for (Credentials wallet : wallets) {
			Transfer.sendFunds(
					WEB3J,
					adminWallet,
					wallet.getAddress(),
					BigDecimal.valueOf(10000),
					Convert.Unit.KPVON
			).send();
			BigInteger balance = WEB3J.platonGetBalance(wallet.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
			log.info("address:{},balance:{}", wallet.getAddress(),balance);
		}
	}

	protected Erc20Contract loadArc20Contract(String address, Credentials credentials){
		return Erc20Contract.load(address, WEB3J, credentials, gasProvider);
	}

	protected Erc721Contract loadArc721Contract(String address, Credentials credentials){
		ContractGasProvider gasProvider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);
		Erc721Contract contract = Erc721Contract.load(address,
				WEB3J,
				credentials,
				gasProvider);
		return contract;
	}

	protected BigInteger getBalance(String address) throws IOException {
		BigInteger balance = WEB3J.platonGetBalance(adminWallet.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
		return balance;
	}
}
