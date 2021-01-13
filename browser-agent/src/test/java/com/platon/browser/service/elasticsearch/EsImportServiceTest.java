package com.platon.browser.service.elasticsearch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.elasticsearch.dto.OldErcTx;
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
public class EsImportServiceTest extends AgentTestBase {
    @Mock
    private EsBlockService blockService;
    @Mock
    private EsTransactionService transactionService;
    @Mock
    private EsNodeOptService nodeOptService;
    @Mock
    private EsDelegateRewardService delegateRewardService;
    @Mock
    private OldEsErc20TxService oldEsErc20TxService;
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
    public void getOldErc20TxList() {
        Set<Transaction> transactions = new HashSet<>();
        Transaction transaction = new Transaction();
        List<OldErcTx> oldErcTxList = new ArrayList<>();
        OldErcTx oldErcTx = new OldErcTx();
        oldErcTxList.add(oldErcTx);
        transaction.setOldErcTxes(oldErcTxList);
        transactions.add(transaction);
        target.getOldErc20TxList(transactions);
        Assert.assertTrue(true);
    }
}
