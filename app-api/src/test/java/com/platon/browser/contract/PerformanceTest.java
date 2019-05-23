package com.platon.browser.contract;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultWasmGasProvider;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/23 15:30
 * @Description:
 */
public class PerformanceTest {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceTest.class);

    private static final int clientNum = 100;
    private static final int timesNum = 10;

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(clientNum*timesNum);

    private static final Web3j web3j = Web3j.build(new HttpService("http://192.168.112.183:6789"));

    private static final TicketContract ticketContract = TicketContract.load(web3j,new ReadonlyTransactionManager(web3j, TicketContract.CONTRACT_ADDRESS),new DefaultWasmGasProvider());
    private static final CandidateContract candidateContract = CandidateContract.load(web3j,new ReadonlyTransactionManager(web3j, CandidateContract.CONTRACT_ADDRESS), new DefaultWasmGasProvider());

    class Calculator{
        public long totalMilliseconds=0;
        public long totalCount=0;
        public BigDecimal getTps(){
            return BigDecimal.valueOf(totalCount).divide(BigDecimal.valueOf(totalMilliseconds),4,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(1000));
        }
    }

    @Test
    public void GetTicketCountByTxHash(){
        StringBuilder hashes = new StringBuilder();
        hashes.append("0x000788f9cabaa610aa680e00597aee9cf35489f8dac3ea407cf6150e73dc24bf").append(":");
        hashes.append("0x001b1567bc44ca091d6e5a3e2b30366ff44cc00c6e32ce021d2f9618fa045371").append(":");
        hashes.append("0x001bb7b069b7701ae79d19012ab8b9b2e817b73476ed8523d45a11571f0da7f6").append(":");
        hashes.append("0x0075091d6a25e24ff6336fadf5330898d7d9fb4ddcf0417f86c65e344b3b4250");

        Calculator calculator = new Calculator();
        CountDownLatch timesLatch = new CountDownLatch(timesNum);
        for (int i=0;i<timesNum;i++){
            try{
                CountDownLatch clientLatch = new CountDownLatch(clientNum);
                for (int j=0;j<clientNum;j++){
                    THREAD_POOL.submit(()->{
                        try {
                            long beginTime = System.currentTimeMillis();
                            ticketContract.GetTicketCountByTxHash(hashes.toString()).send();
                            long diff = System.currentTimeMillis()-beginTime;
                            calculator.totalMilliseconds+=diff;
                            calculator.totalCount++;
                            logger.error("{}, GetTicketCountByTxHash() 耗时：{}ms",Thread.currentThread().getName(),diff);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            clientLatch.countDown();
                        }
                    });
                }
                try {
                    clientLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }finally {
                timesLatch.countDown();
            }
        }
        try {
            timesLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 计算TPS
        logger.error("GetTicketCountByTxHash() TPS：{}",calculator.getTps());
    }

    @Test
    public void GetTicketPrice(){
        Calculator calculator = new Calculator();
        CountDownLatch timesLatch = new CountDownLatch(timesNum);
        for (int i=0;i<timesNum;i++){
            try{
                CountDownLatch clientLatch = new CountDownLatch(clientNum);
                for (int j=0;j<clientNum;j++){
                    THREAD_POOL.submit(()->{
                        try {
                            long beginTime = System.currentTimeMillis();
                            ticketContract.GetTicketPrice().send();
                            long diff = System.currentTimeMillis()-beginTime;
                            calculator.totalMilliseconds+=diff;
                            calculator.totalCount++;
                            logger.error("{}, GetTicketPrice() 耗时：{}ms",Thread.currentThread().getName(),diff);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            clientLatch.countDown();
                        }
                    });
                }
                try {
                    clientLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }finally {
                timesLatch.countDown();
            }
        }
        try {
            timesLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 计算TPS
        logger.error("GetTicketPrice() TPS：{}",calculator.getTps());
    }


    @Test
    public void GetCandidateTicketCount(){
        StringBuilder hashes = new StringBuilder();
        hashes.append("0x24b0c456ae5cad46c4fb9bc02c867b997e22f30696e6e330926f785ca2e7410baf1eb34ffd9b5b07b5ba6e02b693faf57afb33f7c66cfbcf4c9186b4bfac737d").append(":");
        hashes.append("0xa188edb6776931b5f18e228028aaab0d57217772753ac8d5bdaae585a4440cc94520c3b6f617c5cf60725893bc04326c87b5211d4b1d6c100dfc09f2c70917d8").append(":");
        hashes.append("0x7df86962416063d3f90bd629a850951811858cd9aeb78c6737a329e28cc2d555ec91cc46f1de534d359a15989d36d08d254ff74be3e0668547364fb6d004a00c").append(":");
        hashes.append("0xc7fc34d6d8b3d894a35895aaf2f788ed445e03b7673f7ce820aa6fdc02908eeab6982b7eb97e983cc708bcec093b3bc512b0b1fbf668e6ab94cd91f2d642e591");

        Calculator calculator = new Calculator();
        CountDownLatch timesLatch = new CountDownLatch(timesNum);
        for (int i=0;i<timesNum;i++){
            try{
                CountDownLatch clientLatch = new CountDownLatch(clientNum);
                for (int j=0;j<clientNum;j++){
                    THREAD_POOL.submit(()->{
                        try {
                            long beginTime = System.currentTimeMillis();
                            ticketContract.GetCandidateTicketCount(hashes.toString()).send();
                            long diff = System.currentTimeMillis()-beginTime;
                            calculator.totalMilliseconds+=diff;
                            calculator.totalCount++;
                            logger.error("{}, GetCandidateTicketCount() 耗时：{}ms",Thread.currentThread().getName(),diff);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            clientLatch.countDown();
                        }
                    });
                }
                try {
                    clientLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }finally {
                timesLatch.countDown();
            }
        }
        try {
            timesLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 计算TPS
        logger.error("GetCandidateTicketCount() TPS：{}",calculator.getTps());
    }
}
