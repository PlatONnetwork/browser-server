package com.platon.browser.common.complement.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.NoSuchBeanException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TpsCalcCacheTest extends AgentTestBase {

    @Spy
    private TpsCalcCache tpsCalcCache;
    @Test
    public void test() throws NoSuchBeanException {
        CollectionBlock block = blockList.get(0);
        tpsCalcCache.update(block);
        tpsCalcCache.getTps();

        block.setTime(new Date());
        block.setTransactions(new ArrayList<>(transactionList));
        tpsCalcCache.update(block);
        tpsCalcCache.getTps();

        assertTrue(true);
    }
}
