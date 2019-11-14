package complement.converter;

import com.platon.browser.TestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.delegate.DelegateCreateConverter;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @Auther: dongqile
 * @Date: 2019/11/13
 * @Description: 委托相关业务转换器测试类
 */

@RunWith(MockitoJUnitRunner.Silent.class)
public class DelegateConverterTest extends TestBase {

    @Mock
    private DelegateBusinessMapper delegateBusinessMapper;
    @Mock
    private CollectionEvent collectionEvent;
    @Mock
    private Transaction transaction;
    @Mock
    private NodeCache nodeCache;
    @Spy
    private DelegateCreateConverter target;

    @Before
    public void delegateCretaeConvert() throws Exception{
        ReflectionTestUtils.setField(target,"delegateBusinessMapper",delegateBusinessMapper);
        ReflectionTestUtils.setField(target,"collectionEvent",collectionEvent);
        ReflectionTestUtils.setField(target,"transaction",transaction);
        ReflectionTestUtils.setField(target,"nodeCache",nodeCache);
        //TestDate中加载.json文件列表
        Block block = blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();

        List <Transaction> transactions = new ArrayList <>(transactionList);
        Transaction tx = transactionList.get(0);
        when(collectionEvent.getEpochMessage()).thenReturn(epochMessage);
        when(collectionEvent.getBlock()).thenReturn(block);
        when(collectionEvent.getTransactions()).thenReturn(transactions);

    }
}