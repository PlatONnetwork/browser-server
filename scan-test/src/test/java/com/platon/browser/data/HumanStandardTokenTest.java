//package com.platon.browser.data;
//
//import java.io.IOException;
//import java.math.BigInteger;
//import java.util.List;
//
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.alaya.protocol.core.DefaultBlockParameter;
//import com.alaya.protocol.core.DefaultBlockParameterName;
//import com.alaya.protocol.core.methods.response.TransactionReceipt;
//import com.alaya.protocol.core.methods.response.Log;
//import com.alaya.protocol.core.methods.response.PlatonBlock;
//import com.alaya.protocol.core.methods.response.PlatonBlock.Block;
//import com.alaya.protocol.core.methods.response.PlatonBlock.TransactionHash;
//import com.alaya.protocol.core.methods.response.PlatonGetCode;
//
//import com.platon.browser.data.HumanStandardToken.TransferEventResponse;
//
//import rx.Observable;
//import rx.Observer;
//
///**
// * Test the smart contract wrapper class
// *
// * @author lhdeng
// *
// */
//public class HumanStandardTokenTest extends BaseContractTest {
//	private Logger logger = LoggerFactory.getLogger(HumanStandardTokenTest.class);
//
//	// The solidity smart contract 'HumanStandardToken' address
//	String contractAddress = "0x3b1546bd4274d170baef32f0fe92bb59783550f3";
//	String toAddress = "0x31ac3dad7fa96b62d58b2be229575db40aa28b2c";
//	String transactionHash = "0x14b2c0fa9c7efa6952a323ceebff6d73e721c32960c03e9c589bb28fa0333120";
//	BigInteger blockNum = BigInteger.valueOf(4379l);
//
//	@Test
//	public void deploy() {
//		BigInteger initialAmount = BigInteger.valueOf(10000000000000L);
//		String tokenName = "My Token";
//		BigInteger decimal = BigInteger.valueOf(2L);
//		String tokenSymbol = "MT";
//
//		HumanStandardToken humanStandardToken = null;
//		try {
//			humanStandardToken = HumanStandardToken.deploy(web3j, transactionManager, gasProvider, initialAmount, tokenName, decimal, tokenSymbol)
//					.send();
//			contractAddress = humanStandardToken.getContractAddress();
//			transactionHash = humanStandardToken.getTransactionReceipt().get().getTransactionHash();
//			blockNum = humanStandardToken.getTransactionReceipt().get().getBlockNumber();
//			logger.info("Deploy smart contract [HumanStandardToken] success.contract address >>> " + contractAddress + " transactionHash >>>>" + transactionHash
//					+ " block >>>>" + blockNum);
//		} catch (Exception e) {
//			logger.error("Deploy smart contract [HumanStandardToken] error: " + e.getMessage(), e);
//		}
//	}
//
//	@Test
//	public void getBalance() {
//		HumanStandardToken humanStandardToken = HumanStandardToken.load(contractAddress, web3j, transactionManager, gasProvider);
//
//		BigInteger balance = null;
//		try {
//			balance = humanStandardToken.balanceOf(address).send();
//			logger.info("The address[{}] balance is:{}", address, balance.toString());
//		} catch (Exception e) {
//			logger.error("Get balance error,address:{}", address, e);
//		}
//	}
//
//	@Test
//	public void transfer() {
//		HumanStandardToken humanStandardToken = HumanStandardToken.load(contractAddress, web3j, transactionManager, gasProvider);
//
//		// get balance
//		BigInteger balance = null;
//		try {
//			balance = humanStandardToken.balanceOf(address).send();
//			logger.info("Before The address[{}] balance is:{}", address, balance.toString());
//		} catch (Exception e) {
//			logger.error("Get balance error,address:{}", address, e);
//		}
//
//		// transfer
//		BigInteger value = BigInteger.valueOf(200000L);
//		try {
//			TransactionReceipt receipt = humanStandardToken.transfer(toAddress, value).send();
//
//			logger.info("transfer success,transaction hash:{}", receipt.getTransactionHash());
//
//			List<TransferEventResponse> list = humanStandardToken.getTransferEvents(receipt);
//			logger.info("from:{}", list.get(0)._from);
//			logger.info("to:{}", list.get(0)._to);
//			logger.info("value:{}", list.get(0)._value.toString());
//		} catch (Exception e) {
//			logger.info("transfer error,from:{},to:{},value:{}", address, toAddress, value, e);
//		}
//
//		// get balance
//		BigInteger balance_2 = null;
//		try {
//			balance_2 = humanStandardToken.balanceOf(address).send();
//			logger.info("After The address[{}] balance is:{}", address, balance_2.toString());
//		} catch (Exception e) {
//			logger.error("Get balance error,address:{}", address, e);
//		}
//	}
//
//	@Test
//	public void eventListening() {
//		HumanStandardToken humanStandardToken = HumanStandardToken.load(contractAddress, web3j, transactionManager, gasProvider);
//		Observable<TransferEventResponse> observable = humanStandardToken.transferEventObservable(DefaultBlockParameterName.EARLIEST,
//				DefaultBlockParameterName.LATEST);
//
//		Observer<TransferEventResponse> observer = new Observer<HumanStandardToken.TransferEventResponse>() {
//			@Override
//			public void onNext(TransferEventResponse t) {
//				logger.info("from:{}", t._from);
//				logger.info("to:{}", t._to);
//				logger.info("value:{}", t._value);
//			}
//
//			@Override
//			public void onError(Throwable e) {
//
//			}
//
//			@Override
//			public void onCompleted() {
//
//			}
//		};
//		observable.subscribe(observer);
//	}
//
//	@Test
//	public void getTransRecipt() throws IOException {
//		PlatonBlock block = web3j.platonGetBlockByNumber(DefaultBlockParameter.valueOf(blockNum), true).send();
//		PlatonGetCode platonGetCode = web3j.platonGetCode(contractAddress, DefaultBlockParameterName.LATEST).send();
//		TransactionReceipt transactionReceipt = web3j.platonGetTransactionReceipt(transactionHash).send().getResult();
//		List<Log> logs = transactionReceipt.getLogs();
//		for(Log log:logs) {
//			logger.info(log.getData());
//		}
//	}
//}
