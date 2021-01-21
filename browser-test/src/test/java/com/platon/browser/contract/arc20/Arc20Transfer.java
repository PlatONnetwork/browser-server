package com.platon.browser.contract.arc20;

import com.alaya.crypto.Credentials;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.platon.browser.contract.TestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * 发送Alat合约交易
 */
@Slf4j
public class Arc20Transfer extends Arc20Base {
    private Integer transferCount = 10;
    @Test
    public void openTransfer() throws Exception{
        for (String contractAddress : contractAddressList) {
            Arc20Contract contract = loadArc20Contract(contractAddress, adminWallet);
            TransactionReceipt transactionReceipt = contract.openTransfer().send();
            assertThat(transactionReceipt.isStatusOK(), is(true));
            charge();
        }
    }

    @Test
    public void transfer() throws Exception {
        for (Credentials wallet : wallets) {
            log.error("adminWallet balance:{}",getBalance(adminWallet.getAddress()));
            send(adminWallet, wallet, new BigInteger("1000000000000000000"));
            for (int i = 0; i < transferCount; i++) send(adminWallet, wallet);
        }
    }

    private void send(Credentials from,Credentials to) throws Exception {
        send(from,to,new BigInteger("100000000000000000"));
    }

    private void send(Credentials from,Credentials to,BigInteger amount) throws Exception {
        for (String contractAddress : contractAddressList) {
            Arc20Contract contract = loadArc20Contract(contractAddress, from);
            TransactionReceipt transactionReceipt = contract.transfer(to.getAddress(), amount).send();
            log.info("hash = {}", transactionReceipt.getTransactionHash());
            List<Arc20Contract.TransferEventResponse> tokenAddress = contract.getTransferEvents(transactionReceipt);
            assertThat(tokenAddress.size(), is(1));
            tokenAddress.forEach(e -> log.info("{},{},{},{}", e.log, e._from, e._to, e._value));
        }
    }
}
