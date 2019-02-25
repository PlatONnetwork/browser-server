package com.platon.FilterTest;

import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: dongqile
 * Date: 2019/2/21
 * Time: 11:43
 */
public class DataBuild {

    public static  <T> List<T> DataBuild (Class<T> clazz) throws Exception {
        if(clazz==Transaction.class){
            List<T> transactionList = new ArrayList<>();
            for (int i = 0; i <= 10; i++) {
                Transaction transaction= new Transaction();
                transaction.setBlockHash("0x" + randomHexString(64));
                transaction.setBlockNumber(String.valueOf(new Random().nextInt(1000)));
                transaction.setFrom("0x" + randomHexString(40));
                transaction.setHash("0x" + randomHexString(64));
                transaction.setInput("0x");
                transaction.setNonce(String.valueOf(new Random().nextInt(1000)));
                transaction.setPublicKey("0x" + randomHexString(128));
                transaction.setTo("0x" + randomHexString(40));
                transaction.setTransactionIndex(String.valueOf(new Random().nextInt(50)));
                transaction.setValue(String.valueOf(new Random().nextInt(30000)));
                transaction.setGas(String.valueOf(new Random().nextInt(7000000)));
                transaction.setGasPrice(String.valueOf(21000L));
                T target = (T)transaction;
                transactionList.add(target);
            }
            return transactionList;
        }
        if(clazz==EthBlock.Block.class){

        }
        if(clazz.equals(Transaction.class)){

        }
        return null;
    }


    /**
     * 随机生成十六进制随机数(不带0x)
     *
     * @param lenght
     * @return 16进制String
     */
    public static String randomHexString ( int lenght ) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < lenght; i++) {
            stringBuffer.append(Integer.toHexString(new Random().nextInt(16)));
        }
        return stringBuffer.toString().toUpperCase();
    }

    public static void main ( String[] args ) throws Exception {
        List<Transaction> list = DataBuild(Transaction.class);

        System.out.println(list);
    }
}

