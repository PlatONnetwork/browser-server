package com.platon.browser.FilterTest;

import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.abi.datatypes.Event;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User: dongqile
 * Date: 2019/1/11
 * Time: 10:57
 */
@RunWith(SpringRunner.class)
public class TransactionFilterTest extends TestBase {

    @Autowired
    private PlatonClient platonClient;

    private static Logger logger = LoggerFactory.getLogger(TransactionFilterTest.class);

    @Test
    public void TransactionFilter(){
        try{
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(String.valueOf(65312L)));
            EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();
            List<EthBlock.TransactionResult> list = ethBlock.getBlock().getTransactions();
            List<Transaction> list1 = new ArrayList<>();
            List<TransactionReceipt> list2 = new ArrayList <>();
            for (EthBlock.TransactionResult transactionResult : list) {
                Transaction txList = (Transaction) transactionResult.get();
                EthTransaction ethTransaction = web3j.ethGetTransactionByHash(txList.getHash()).send();
                Optional<Transaction> value = ethTransaction.getTransaction();
                Transaction transaction = value.get();
                list1.add(transaction);
                EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(transaction.getHash()).send();
                Optional <TransactionReceipt> transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt();
                TransactionReceipt receipt = transactionReceipt.get();
                list2.add(receipt);
            }
            long time = ethBlock.getBlock().getTimestamp().longValue();
            //boolean res = transactionFilter.transactionAnalysis(list2,list1,time);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Test
    public void transationAsly(){
        String hash = "0x16bfff398601bfff76a25a80e02ea580a34d8dd471bd9a1be28e6606cdb5c6ad";
        web3j = platonClient.getWeb3j("203");
        try {
            EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(hash).send();
            Optional <TransactionReceipt> transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt();
            TransactionReceipt receipt = transactionReceipt.get();
            logger.error(receipt.getContractAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}