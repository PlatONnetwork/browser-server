package com.platon.browser.data;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.StakingAmountType;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.DelegateContract;
import org.web3j.platon.contracts.NodeContract;
import org.web3j.platon.contracts.StakingContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultWasmGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 20:58
 * @Description:
 */
public class TransactionSender {
    private static Logger logger = LoggerFactory.getLogger(TransactionSender.class);
    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.112.171:6789"));
    private Credentials credentials = WalletUtils.loadCredentials("11111111","D:\\Workspace\\browser-server\\browser-agent\\src\\test\\resources\\wallet.json");
    NodeContract nodeContract = NodeContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider());
    StakingContract stakingContract = StakingContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider(),"101");
    DelegateContract delegateContract = DelegateContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider(),"101");
    public TransactionSender() throws IOException, CipherException {}

    // 发送转账交易
    @Test
    public void transfer() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.112.171:6789"));
        BigInteger blockNumber = web3j.platonBlockNumber().send().getBlockNumber();
        Transfer.sendFunds(
                web3j,
                credentials,
                "101",
                "0x8b77ac9fabb6fe247ee91ca07ea4f62c6761e79b",
                BigDecimal.valueOf(100),
                Convert.Unit.VON
        ).send();
        BigInteger balance = web3j.platonGetBalance("0x8b77ac9fabb6fe247ee91ca07ea4f62c6761e79b", DefaultBlockParameterName.LATEST).send().getBalance();
        logger.debug("balance:{}",balance);
    }

    // 发送质押交易
    @Test
    public void staking() throws Exception {
        BaseResponse res = stakingContract.staking(
                "0x00cc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba41000",
                BigInteger.valueOf(40000),
                StakingAmountType.FREE_AMOUNT_TYPE,
                "0x60ceca9c1290ee56b98d4e160ef0453f7c40d219",
                "",
                "cdm",
                "www.baidu.com",
                "baidu",
                BigInteger.ZERO
        ).send();
        logger.debug("res:{}",res);
    }

    // 修改质押信息(编辑验证人)
    @Test
    public void updateStakingInfo() throws Exception {
        StakingContract stakingContract = StakingContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider(),"101");
        BaseResponse res = stakingContract.updateStakingInfo(
                "0x00cc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba41000",
                "0x60ceca9c1290ee56b98d4e160ef0453f7c40d219",
                "PID-002","cdm-004","WWW.CCC.COM","Node of CDM"
        ).send();
        logger.debug("res:{}",res);
    }

    // 增持质押(增加自有质押)
    @Test
    public void addStaking() throws Exception {
        StakingContract stakingContract = StakingContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider(),"101");
        BaseResponse res = stakingContract.addStaking(
                "0x00cc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba41000",
                StakingAmountType.FREE_AMOUNT_TYPE,
                BigInteger.valueOf(555)
        ).send();
        logger.debug("res:{}",res);
    }

    // 撤销质押(退出验证人)
    @Test
    public void unStaking() throws Exception {
        StakingContract stakingContract = StakingContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider(),"101");
        BaseResponse res = stakingContract.unStaking(
                "0x00cc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba41000"
        ).send();
        logger.debug("res:{}",res);
    }

    // 发送委托交易
    @Test
    public void delegate() throws Exception {
        BaseResponse res = delegateContract.delegate(
                "0x00cc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba41000",
                StakingAmountType.FREE_AMOUNT_TYPE,
                BigInteger.valueOf(1000)
        ).send();
        logger.debug("res:{}",res);
    }
}
