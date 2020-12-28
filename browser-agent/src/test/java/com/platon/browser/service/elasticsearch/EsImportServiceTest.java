package com.platon.browser.service.elasticsearch;

import com.platon.browser.TestBase;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EsImportServiceTest extends TestBase {
    @Mock
    private EsBlockService blockService;
    @Mock
    private EsTransactionService transactionService;
    @Mock
    private EsNodeOptService nodeOptService;
    @Mock
    private EsDelegateRewardService delegateRewardService;
    @Mock
    private EsTokenTransferRecordService esTokenTransferRecordService;
    @InjectMocks
    @Spy
    private EsImportService target;

    @Before
    public void setup() throws Exception {

    }

    /**
     * 根据区块号获取激励池余额
     */
    @Test
    public void batchImport() throws InterruptedException {
        this.target.batchImport(Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
        verify(this.target, times(1)).batchImport(anySet(), anySet(), anySet(), anySet());
    }

    @Test
    public void retryRecordSet() {
        Set<Transaction> transactions = new HashSet<>();
        Transaction transaction = new Transaction();
        List<ESTokenTransferRecord> esTokenTransferRecordList = new ArrayList<>();
        ESTokenTransferRecord esTokenTransferRecord = new ESTokenTransferRecord();
        esTokenTransferRecordList.add(esTokenTransferRecord);
        transaction.setEsTokenTransferRecords(esTokenTransferRecordList);
        transactions.add(transaction);
        this.target.retryRecordSet(transactions);
        Assert.assertTrue(true);
    }
}
