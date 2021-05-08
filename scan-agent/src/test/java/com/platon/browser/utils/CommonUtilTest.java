package com.platon.browser.utils;

import cn.hutool.core.date.DateUtil;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
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

}
