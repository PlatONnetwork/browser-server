package com.platon.FilterTest;

import com.platon.TestBase;
import com.platon.browser.client.PlatonClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: dongqile
 * Date: 2019/1/11
 * Time: 15:40
 */
@RunWith(SpringRunner.class)
public class PendingTxFilterTest extends TestBase {

    private static Logger logger = LoggerFactory.getLogger(PendingTxFilterTest.class);

    @Autowired
    private PlatonClient platon;

    @Value("${platon.chain.active}")
    private String chainId;

    @Test
    public void PengdingTxFilterTest () {
        List <Transaction> list = buildData();
        try {
            logger.info("PengdingTxFilterTest begin");
            pendingFilter.analyse(list, chainId, platon);
            logger.info("PengdingTxFilterTest end");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 构建随机入参
     *
     * @return List<Transaction>
     */
    private List <Transaction> buildData () {
        List <Transaction> transactionList = new ArrayList <>();
        for (int i = 0; i <= 10; i++) {
            Transaction transaction = new Transaction();
            transaction.setBlockHash("0x" + randomHexString(64));
            transaction.setBlockNumber(new Random(1000L).toString());
            transaction.setFrom("0x" + randomHexString(40));
            transaction.setHash("0x" + randomHexString(64));
            transaction.setInput("0x");
            transaction.setNonce(new Random(500000L).toString());
            transaction.setPublicKey("0x" + randomHexString(128));
            transaction.setTo("0x" + randomHexString(40));
            transaction.setTransactionIndex(new Random(1500L).toString());
            transaction.setValue(new Random(9000000L).toString());
            transaction.setGas(new Random(9900000L).toString());
            transaction.setGasPrice(String.valueOf(21000L));
            transaction.setR(new Random(200000L).toString());
            transaction.setV(new Random(200000L).toString());
            transaction.setS(new Random(200000L).toString());
            transaction.setCreates(new Random(200000L).toString());
            transaction.setRaw(new Random(200000L).toString());
            transactionList.add(transaction);
        }
        return transactionList;
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


}