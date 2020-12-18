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
        //*********contract: atp1anqfd4c0j09dy2lqtcapgyvrpa8yf23vhy7p9t **********
        send(ownerWallet,testWalletA,new BigInteger("1000000000000000000000"));
        send(ownerWallet,testWalletB,new BigInteger("1000000000000000000000"));
        send(ownerWallet,testWalletC,new BigInteger("1000000000000000000000"));

        // ownerWallet: atp196278ns22j23awdfj9f2d4vz0pedld8a2fzwwj
        for (int i=0;i<8;i++) send(ownerWallet,ownerWallet);
        for (int i=0;i<8;i++) send(ownerWallet,testWalletA);
        for (int i=0;i<8;i++) send(ownerWallet,testWalletB);
        // testWalletA: atp1gqsdewmd0w4ct8xwnl6xxlu0yx46p8ssjtys38
        for (int i=0;i<8;i++) send(testWalletA,testWalletA);
        for (int i=0;i<8;i++) send(testWalletA,testWalletB);
        for (int i=0;i<8;i++) send(testWalletA,ownerWallet);
        // testWalletB: atp1zkrxx6rf358jcvr7nruhyvr9hxpwv9uncjmns0
        for (int i=0;i<8;i++) send(testWalletB,testWalletB);
        for (int i=0;i<8;i++) send(testWalletB,testWalletA);
        // testWalletC: atp1cy2uat0eukfrxv897s5s8lnljfka5ewjj943gf
        for (int i=0;i<17;i++) send(testWalletC,testWalletC);
        for (int i=0;i<12;i++) send(testWalletC,testWalletA);
        for (int i=0;i<12;i++) send(testWalletC,testWalletB);

//         ownerWallet: atp196278ns22j23awdfj9f2d4vz0pedld8a2fzwwj
//        for (int i=0;i<8;i++) send(ownerWallet,ownerWallet);
//        for (int i=0;i<4;i++) send(ownerWallet,testWalletA);
//        for (int i=0;i<6;i++) send(ownerWallet,testWalletB);
//        for (int i=0;i<8;i++) send(testWalletA,testWalletA);
//        for (int i=0;i<1;i++) send(testWalletA,testWalletB);
//        for (int i=0;i<3;i++) send(testWalletA,ownerWallet);
//        for (int i=0;i<5;i++) send(testWalletB,testWalletB);
//        for (int i=0;i<7;i++) send(testWalletB,testWalletA);
    }

    private void send(Credentials from,Credentials to) throws Exception {
        send(from,to,new BigInteger("100000000000000000"));
    }

    private void send(Credentials from,Credentials to,BigInteger amount) throws Exception {
        String currContractAddress = getCurrContractAddress();
        AlatContract contract = loadContract(currContractAddress,from);
        TransactionReceipt transactionReceipt = contract.transfer(to.getAddress(), amount).send();
        log.info("hash = {}",transactionReceipt.getTransactionHash());
        List<AlatContract.TransferEventResponse> tokenAddress = contract.getTransferEvents(transactionReceipt);
        assertThat(tokenAddress.size(), is(1));
        tokenAddress.forEach(e -> log.info("{},{},{},{}",e.log,e._from,e._to,e._value));
    }



}
