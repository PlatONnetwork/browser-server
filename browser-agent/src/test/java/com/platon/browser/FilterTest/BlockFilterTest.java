package com.platon.browser.FilterTest;

import com.alibaba.fastjson.JSON;
import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dto.AnalysisResult;
import com.platon.browser.dto.EventRes;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.filter.BlockFilter;
import com.platon.browser.util.TransactionAnalysis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User: dongqile
 * Date: 2019/1/10
 * Time: 10:44
 */
@RunWith(SpringRunner.class)
public class BlockFilterTest extends TestBase {

    protected static Logger logger = LoggerFactory.getLogger(BlockFilterTest.class);

    @Autowired
    private BlockFilter blockFilter;
    @Autowired
    private PlatonClient platon;

    @Test
    public void  BlockFilterBuildTest(){
        try{
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(String.valueOf(779L)));
            EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();
            List<EthBlock.TransactionResult> list = ethBlock.getBlock().getTransactions();
            List<Transaction> list1 = new ArrayList <>();
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
                TicketContract ticketContract = platon.getTicketContract(chainId);
                AnalysisResult analysisResult = TransactionAnalysis.analysis(transaction.getInput(), false);
                String type = TransactionAnalysis.getTypeName(analysisResult.getType());
                if (TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code.equals(type)) {
                    if (receipt.getStatus().equals("0x1")) {
                        //get tickVoteContract vote event
                        List <TicketContract.VoteTicketEventEventResponse> eventEventResponses = ticketContract.getVoteTicketEventEvents(receipt);
                        String event = eventEventResponses.get(0).param1;
                        EventRes eventRes = JSON.parseObject(event, EventRes.class);
                        //event objcet is jsonString , transform jsonObject <EventRes>
                        //EventRes get Data
                        String res = eventRes.getData();
                        String[] strs = res.split(":");
                    }
                }
            }
            //Block block = blockFilter.analysis(ethBlock,list2,list1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void BlockFilterTest(){
        try {
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(String.valueOf(1073L)));
            EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();
            List<EthBlock.TransactionResult> list = ethBlock.getBlock().getTransactions();
            List<Transaction> list1 = new ArrayList <>();
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
            //Block block = blockFilter.blockAnalysis(ethBlock,list2,list1);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void BlockFilterValueConversion(){
        try {
            BigInteger bigInteger = new BigInteger("234550000");
           // String res = blockFilter.valueConversion(bigInteger);
            //System.out.println(res);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}