package com.platon.browser.job;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestC {

    private final static ExecutorService POOL = Executors.newFixedThreadPool(5);

    private final static Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:7793"));
    public static void main(String[] args) {
        List<EthBlock.Block> data = new ArrayList<>();
        for(int j=1;j<=5;j++){
            try {
                data.add(web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(j)),true).send().getBlock());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        CountDownLatch latch = new CountDownLatch(data.size());

        Map<String, String> res = new HashMap<>();
        Map<String, String> err = new HashMap<>();

        int count = 0;
        for(EthBlock.Block block : data){
            int i = count;
            POOL.submit(()->{
                try{


                }finally {
                    latch.countDown();
                }
                try {
                    TimeUnit.SECONDS.sleep(i+3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                res.put("thread-"+i,data.get(i).getNumber().toString());

                System.out.println("线程"+i+"完成！");

                //data.set(i,data.get(i).getNumber().toString()+" done!");
            });
            count++;
        }

        try {
            // 等待所有线程完成
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("所有线程完成，执行数据入库操作！");
        data.forEach(d-> System.out.println(d));
    }
}
