package com.platon.browser;

import com.alaya.crypto.Credentials;
import com.alaya.protocol.core.DefaultBlockParameterName;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.protocol.exceptions.TransactionException;
import com.alaya.tx.Transfer;
import com.alaya.utils.Convert;
import com.platon.browser.contract.AlatContract;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * 发送Alat合约交易
 */
@Slf4j
public class AlatContractTransfer extends ContractTestBase {

    @Test
    public void openTransfer() throws Exception{
        String currContractAddress = getCurrContractAddress();
        AlatContract contract = loadContract(currContractAddress,ownerWallet);
        TransactionReceipt transactionReceipt  =  contract.openTransfer().send();
        assertThat(transactionReceipt.isStatusOK(), is(true));
        charge();
    }

    @Test
    public void transfer() throws Exception {
//        for (int i=0;i<2;i++) send(ownerWallet,ownerWallet);
//        for (int i=0;i<4;i++) send(ownerWallet,testWalletA);
//        for (int i=0;i<6;i++) send(ownerWallet,testWalletB);
//        for (int i=0;i<8;i++) send(testWalletA,testWalletA);
//        for (int i=0;i<1;i++) send(testWalletA,testWalletB);
//        for (int i=0;i<3;i++) send(testWalletA,ownerWallet);
//        for (int i=0;i<5;i++) send(testWalletB,testWalletB);
//        for (int i=0;i<7;i++) send(testWalletB,testWalletA);
    }

    private void send(Credentials from,Credentials to) throws Exception {
        String currContractAddress = getCurrContractAddress();
        AlatContract contract = loadContract(currContractAddress,from);
        TransactionReceipt transactionReceipt = contract.transfer(to.getAddress(), new BigInteger("1")).send();
        log.info("hash = {}",transactionReceipt.getTransactionHash());
        List<AlatContract.TransferEventResponse> tokenAddress = contract.getTransferEvents(transactionReceipt);
        assertThat(tokenAddress.size(), is(1));
        tokenAddress.forEach(e -> log.info("{},{},{},{}",e.log,e._from,e._to,e._value));
    }



}
