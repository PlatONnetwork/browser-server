package com.platon.browser.data;

import com.platon.browser.exception.IssueEpochChangeException;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.StakingAmountType;
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
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 20:58
 * @Description:
 */
public class AccountTool {
    private static Logger logger = LoggerFactory.getLogger(AccountTool.class);
//    private String chainId = "101";
//    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.112.171:6789"));
//    private Credentials credentials = Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7");

    private String chainId = "100";
    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.120.76:6797"));
    private Credentials credentials = Credentials.create("a56f68ca7aa51c24916b9fff027708f856650f9ff36cc3c8da308040ebcc7867");
//    private Credentials credentials = WalletUtils.loadCredentials("88888888","D:\\Workspace\\browser-server\\browser-agent\\src\\test\\resources\\0127de1d120dc61b57ab51afcc0fa59022a1be94.json");

    // 充钱
    @Test
    public void charge() throws Exception {
        Transfer.sendFunds(
                currentValidWeb3j,
                credentials,
                chainId,
                "0x0127de1d120dc61b57ab51afcc0fa59022a1be94",
                BigDecimal.valueOf(500000000),
                Convert.Unit.LAT
        ).send();
        BigInteger balance = currentValidWeb3j.platonGetBalance("0x0127de1d120dc61b57ab51afcc0fa59022a1be94", DefaultBlockParameterName.LATEST).send().getBalance();
        logger.debug("balance:{}",balance);
    }

    @Test
    public void privateKey() throws UnsupportedEncodingException {
        byte[] byteArray = credentials.getEcKeyPair().getPrivateKey().toByteArray();
        String privateKey = Hex.toHexString(byteArray);

        logger.debug("Private Key:{}",privateKey);
    }

}
