package com.platon.browser.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.CollectionBlock;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TpsCalcCacheTest extends AgentTestBase {

    @Spy
    private TpsCalcCache tpsCalcCache;
    @Test
    public void test1() {
    	TpsCalcCache tpsCalcCache = new TpsCalcCache();
        CollectionBlock block1 = blockList.get(0);
        CollectionBlock block2 = blockList.get(1);
        CollectionBlock block3 = blockList.get(2);
//        CollectionBlock block4 = blockList.get(3);
        for (int i=0;i<1500;i++){
            block1.getDtoTransactions().add(new Transaction());
            block2.getDtoTransactions().add(new Transaction());
            block3.getDtoTransactions().add(new Transaction());
        }
        Date date = new Date();
        block1.setTime(date);
        Date date1 = new Date(date.getTime() + 1100);
        block2.setTime(date);
        Date date2 = new Date(date.getTime() + 11000);
        block3.setTime(date2);
//        Date date3 = new Date(date.getTime() + 13000);
//        block4.setTime(date3);

        tpsCalcCache.update(block1);
        tpsCalcCache.update(block2);
        tpsCalcCache.update(block3);
//        tpsCalcCache.update(block4);
        int tps = tpsCalcCache.getTps();
        // 3000 / (0-0+1) = 3000
        assertEquals(300,tps);

        int maxtps = tpsCalcCache.getMaxTps();
        assertEquals(3000,maxtps);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    @Test
//    public void test2() throws ParseException {
//        CollectionBlock block1 = blockList.get(0);
//        CollectionBlock block2 = blockList.get(1);
//        CollectionBlock block3 = blockList.get(2);
//
//        for (int i=0;i<1500;i++){
//            block1.getTransactions().add(new Transaction());
//            block2.getTransactions().add(new Transaction());
//            block3.getTransactions().add(new Transaction());
//        }
//
//        String dateStr1 = "2020-05-29 00:00:00";
//        String dateStr2 = "2020-05-29 00:00:01";
//        Date date1 = sdf.parse(dateStr1);
//        Date date2 = sdf.parse(dateStr2);
//        block1.setTime(date1);
//        block2.setTime(date2);
//
//        tpsCalcCache.update(block1);
//        tpsCalcCache.update(block2);
//        int tps = tpsCalcCache.getTps();
//        // 3000 / (1-0+1) = 1500
//        assertEquals(1500,tps);
//    }
//
//    @Test
//    public void test3() throws ParseException {
//        CollectionBlock block1 = blockList.get(0);
//        CollectionBlock block2 = blockList.get(1);
//
//        for (int i=0;i<1500;i++){
//            block1.getTransactions().add(new Transaction());
//            block2.getTransactions().add(new Transaction());
//        }
//
//        String dateStr1 = "2020-05-29 00:00:00";
//        String dateStr2 = "2020-05-29 00:00:08";
//        Date date1 = sdf.parse(dateStr1);
//        Date date2 = sdf.parse(dateStr2);
//        block1.setTime(date1);
//        block2.setTime(date2);
//
//        tpsCalcCache.update(block1);
//        tpsCalcCache.update(block2);
//        int tps = tpsCalcCache.getTps();
//        // 3000 / (8-0+1) = 334
//        assertEquals(334,tps);
//    }
//
//    @Test
//    public void test4() throws ParseException {
//        CollectionBlock block1 = blockList.get(0);
//        CollectionBlock block2 = blockList.get(1);
//        CollectionBlock block3 = blockList.get(2);
//
//        for (int i=0;i<1500;i++){
//            block1.getTransactions().add(new Transaction());
//            block2.getTransactions().add(new Transaction());
//            block3.getTransactions().add(new Transaction());
//        }
//
//        String dateStr1 = "2020-05-29 00:00:00";
//        String dateStr2 = "2020-05-29 00:00:08";
//        String dateStr3 = "2020-05-29 00:00:13";
//        Date date1 = sdf.parse(dateStr1);
//        Date date2 = sdf.parse(dateStr2);
//        Date date3 = sdf.parse(dateStr3);
//        block1.setTime(date1);
//        block2.setTime(date2);
//        block3.setTime(date3);
//
//        tpsCalcCache.update(block1);
//        tpsCalcCache.update(block2);
//        tpsCalcCache.update(block3);
//        int tps = tpsCalcCache.getTps();
//        // 3000 / (13-8+1) = 500
//        assertEquals(500,tps);
//    }
}
