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
    public TransactionSender() throws IOException, CipherException {}

    // 发送转账交易
    @Test
    public void transfer() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.112.171:6789"));

        BigInteger bl = web3j.platonBlockNumber().send().getBlockNumber();

        Transfer.sendFunds(web3j,credentials,"101","0x8b77ac9fabb6fe247ee91ca07ea4f62c6761e79b", BigDecimal.valueOf(100), Convert.Unit.VON).send();
        BigInteger balance = web3j.platonGetBalance("0x8b77ac9fabb6fe247ee91ca07ea4f62c6761e79b", DefaultBlockParameterName.LATEST).send().getBalance();
        logger.debug("balance:{}",balance);
    }

    // 发送质押交易
    @Test
    public void staking() throws Exception {
        NodeContract nodeContract = NodeContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider());
        BaseResponse<List<Node>> response = nodeContract.getVerifierList().send();
        if(response.isStatusOk()){
            StakingContract stakingContract = StakingContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider(),"101");
            response.data.forEach(node->{
                try {
                    BaseResponse res = stakingContract.staking(node.getNodeId(), BigInteger.valueOf(10000), StakingAmountType.FREE_AMOUNT_TYPE,node.getBenifitAddress(),node.getExternalId(),node.getNodeName(),node.getWebsite(),node.getDetails(),node.getProgramVersion()).send();
                    logger.debug("res:{}",res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // 发送委托交易
    @Test
    public void delegate() throws Exception {
        NodeContract nodeContract = NodeContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider());
        BaseResponse<List<Node>> response = nodeContract.getVerifierList().send();
        if(response.isStatusOk()){
            DelegateContract delegateContract = DelegateContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider(),"101");
            response.data.forEach(node->{
                try {
                    BaseResponse res = delegateContract.delegate(node.getNodeId(),StakingAmountType.FREE_AMOUNT_TYPE,BigInteger.valueOf(1000)).send();
                    logger.debug("res:{}",res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
