package com.platon.browser.utils;

import cn.hutool.core.date.DateUtil;
import com.platon.browser.bean.NodeSettleStatis;
import com.platon.browser.bean.NodeSettleStatisBase;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class CommonUtilTest {

    @Test
    public void test() {
        long time = DateUtil.parse("2021-03-27 18:30:00", "yyyy-MM-dd HH:mm:ss").getTime();
        System.out.println("打印结果为：" + time);
        System.out.println("打印结果为：" + cn.hutool.core.util.HexUtil.toHex(time));
        int decimal = Integer.parseInt(String.valueOf(2));
        BigDecimal afterConverValue = ConvertUtil.convertByFactor(new BigDecimal("1000"), decimal);
        System.out.println("打印结果为：" + afterConverValue.toString());
        List<Transaction> lists = new ArrayList<Transaction>();
        Transaction t1 = new Transaction();
        List<ErcTx> erc20TxList1 = new ArrayList<>();
        ErcTx ercTx1 = new ErcTx();
        ErcTx ercTx2 = new ErcTx();
        erc20TxList1.add(ercTx1);
        erc20TxList1.add(ercTx2);
        t1.setErc20TxList(erc20TxList1);
        //lists.add(t1);

        Transaction t2 = new Transaction();
        List<ErcTx> erc20TxList2 = new ArrayList<>();
        ErcTx ercTx3 = new ErcTx();
        ErcTx ercTx4 = new ErcTx();
        ErcTx ercTx5 = new ErcTx();
        erc20TxList2.add(ercTx3);
        erc20TxList2.add(ercTx4);
        erc20TxList2.add(ercTx5);
        t2.setErc20TxList(erc20TxList2);
        //lists.add(t2);
        IntSummaryStatistics erc20Size = lists.stream().collect(Collectors.summarizingInt(transaction -> transaction.getErc20TxList().size()));
        IntSummaryStatistics virtualTransactionSize = lists.stream().collect(Collectors.summarizingInt(transaction -> transaction.getVirtualTransactions().size()));

        log.info("===={}", erc20Size);
        log.info("===={}", virtualTransactionSize);
    }

    @Test
    public void unit256MaxNumTest1() {
        // unit8的最大值就是2的8次方，然后取值范围[0-2^8-1]
        log.info("unit256的最大值为======{}", new BigDecimal("2").pow(256).subtract(BigDecimal.ONE));
        log.info("unit8的最大值为======{}", new BigDecimal("2").pow(31).subtract(BigDecimal.ONE));
    }

    @Test
    public void Test1() {
        NodeSettleStatis nodeSettleStatis = new NodeSettleStatis();
        nodeSettleStatis.setNodeId("aaaaaaaa");

        NodeSettleStatisBase nodeSettleStatisBase1 = new NodeSettleStatisBase();
        nodeSettleStatisBase1.setSettleEpochRound(new BigInteger("1"));
        nodeSettleStatisBase1.setBlockNumGrandTotal(new BigInteger("1"));
        nodeSettleStatisBase1.setBlockNumElected(new BigInteger("10"));

        NodeSettleStatisBase nodeSettleStatisBase2 = new NodeSettleStatisBase();
        nodeSettleStatisBase2.setSettleEpochRound(new BigInteger("2"));
        nodeSettleStatisBase2.setBlockNumGrandTotal(new BigInteger("13"));
        nodeSettleStatisBase2.setBlockNumElected(new BigInteger("1"));

        NodeSettleStatisBase nodeSettleStatisBase3 = new NodeSettleStatisBase();
        nodeSettleStatisBase3.setSettleEpochRound(new BigInteger("3"));
        nodeSettleStatisBase3.setBlockNumGrandTotal(new BigInteger("1"));
        nodeSettleStatisBase3.setBlockNumElected(new BigInteger("14"));

        NodeSettleStatisBase nodeSettleStatisBase4 = new NodeSettleStatisBase();
        nodeSettleStatisBase4.setSettleEpochRound(new BigInteger("4"));
        nodeSettleStatisBase4.setBlockNumGrandTotal(new BigInteger("1"));
        nodeSettleStatisBase4.setBlockNumElected(new BigInteger("17"));

        NodeSettleStatisBase nodeSettleStatisBase5 = new NodeSettleStatisBase();
        nodeSettleStatisBase5.setSettleEpochRound(new BigInteger("5"));
        nodeSettleStatisBase5.setBlockNumGrandTotal(new BigInteger("1"));
        nodeSettleStatisBase5.setBlockNumElected(new BigInteger("21"));

        NodeSettleStatisBase nodeSettleStatisBase6 = new NodeSettleStatisBase();
        nodeSettleStatisBase6.setSettleEpochRound(new BigInteger("6"));
        nodeSettleStatisBase6.setBlockNumGrandTotal(new BigInteger("1"));
        nodeSettleStatisBase6.setBlockNumElected(new BigInteger("21"));

        NodeSettleStatisBase nodeSettleStatisBase7 = new NodeSettleStatisBase();
        nodeSettleStatisBase7.setSettleEpochRound(new BigInteger("7"));
        nodeSettleStatisBase7.setBlockNumGrandTotal(new BigInteger("1"));
        nodeSettleStatisBase7.setBlockNumElected(new BigInteger("21"));

        NodeSettleStatisBase nodeSettleStatisBase8 = new NodeSettleStatisBase();
        nodeSettleStatisBase8.setSettleEpochRound(new BigInteger("8"));
        nodeSettleStatisBase8.setBlockNumGrandTotal(new BigInteger("1"));
        nodeSettleStatisBase8.setBlockNumElected(new BigInteger("21"));


        nodeSettleStatis.getNodeSettleStatisQueue().offer(nodeSettleStatisBase1);
        nodeSettleStatis.getNodeSettleStatisQueue().offer(nodeSettleStatisBase2);
        nodeSettleStatis.getNodeSettleStatisQueue().offer(nodeSettleStatisBase3);
        nodeSettleStatis.getNodeSettleStatisQueue().offer(nodeSettleStatisBase4);
        nodeSettleStatis.getNodeSettleStatisQueue().offer(nodeSettleStatisBase5);
        nodeSettleStatis.getNodeSettleStatisQueue().offer(nodeSettleStatisBase6);
        nodeSettleStatis.getNodeSettleStatisQueue().offer(nodeSettleStatisBase7);
        nodeSettleStatis.getNodeSettleStatisQueue().offer(nodeSettleStatisBase8);

        log.error("============{}", nodeSettleStatis.computeGenBlocksRate(BigInteger.valueOf(8)));
    }

    @Test
    public void test3() {
        BigDecimal percent = new BigDecimal("280")
                .divide(new BigDecimal("2800"), 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(6, RoundingMode.HALF_UP)
                .stripTrailingZeros();

        log.info("============{}", percent.toPlainString());
    }

    @Test
    public void testCompress() {
        String text = "{\"nodeAprQueue\":[{\"epochNum\":647,\"deleAnnualizedRate\":\"0\"},{\"epochNum\":646,\"deleAnnualizedRate\":\"0\"},{\"epochNum\":645,\"deleAnnualizedRate\":\"0\"},{\"epochNum\":644,\"deleAnnualizedRate\":\"0\"},{\"epochNum\":643,\"deleAnnualizedRate\":\"0\"},{\"epochNum\":642,\"deleAnnualizedRate\":\"0\"},{\"epochNum\":641,\"deleAnnualizedRate\":\"0\"},{\"epochNum\":640,\"deleAnnualizedRate\":\"0\"},{\"epochNum\":639,\"deleAnnualizedRate\":\"0\"}],\"nodeId\":\"0x00ec943a40e69df4bb7bf2b516360af9e26e0b4072c4ef08527a4bd9fa9f83fe304ea3751ac81cc88e8a70374d2773fc2fc525c4173a8798cb81c153bc2e7f92\"}";
        String compressed = GzipUtils.compress(text);
        log.info("============{}, length:{}", compressed, compressed.length());
    }
}
