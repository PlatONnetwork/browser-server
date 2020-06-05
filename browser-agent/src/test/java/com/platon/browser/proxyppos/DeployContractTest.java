package com.platon.browser.proxyppos;

import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

public class DeployContractTest extends TestBase {
    public static final String proxyContractAddress = "lax18v25d02zwnghpwh0xtc0ay4mt9ur258nfmecxy";

    @Test
    public void deploy() throws Exception {
        String contractAddress = deployProxyContract();
    }

    @Test
    public void transfer() throws Exception {
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, chainId);
        new Transfer(web3j,transactionManager).sendFunds(proxyContractAddress,new BigDecimal("50000000"), Convert.Unit.LAT,GAS_PRICE,GAS_LIMIT).send();
        System.out.println("balance="+ web3j.platonGetBalance(proxyContractAddress, DefaultBlockParameterName.LATEST).send().getBalance());
    }
}
