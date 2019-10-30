package com.platon.browser.old.task;

import com.platon.browser.TestBase;
import com.platon.browser.client.RestrictingBalance;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.old.engine.cache.CacheHolder;
import com.platon.browser.old.engine.stage.BlockChainStage;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.old.task.cache.AddressTaskCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 20:29
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressUpdateTaskTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(AddressUpdateTaskTest.class);

    @Spy
    private AddressUpdateTask target;
    @Mock
    private SpecialContractApi sca;
    @Mock
    private CacheHolder cacheHolder;
    @Mock
    private AddressTaskCache taskCache;

    @Before
    public void setup() throws ContractInvokeException {
        ReflectionTestUtils.setField(target, "cacheHolder", cacheHolder);
        ReflectionTestUtils.setField(target, "sca", sca);
        ReflectionTestUtils.setField(target, "taskCache", taskCache);

        List<RestrictingBalance> data = new ArrayList<>();
        addresses.forEach(ca->{
            RestrictingBalance rb = new RestrictingBalance();
            rb.setAccount(ca.getAddress());
            rb.setFreeBalance(Numeric.toHexStringWithPrefix(new BigInteger(ca.getBalance())));
            rb.setLockBalance(Numeric.toHexStringWithPrefix(new BigInteger(ca.getRestrictingBalance())));
            rb.setPledgeBalance(Numeric.toHexStringWithPrefix(new BigInteger(ca.getRestrictingBalance())));
            data.add(rb);
        });

        doReturn(data).when(target).getRestrictingBalance(anyString());
    }

    @Test
    public void testStart() throws Exception {
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        doReturn(addresses).when(target).getAllAddress();

        target.start();

        verify(target, times(1)).start();
    }

    @Test
    public void testBatchQueryBalance() throws Exception {

        Collection<CustomAddress> addresses = new ArrayList<>();

        for (int i=0;i<1000;i++){
            CustomAddress ca = new CustomAddress();
            ca.setAddress(String.valueOf(i));
            addresses.add(ca);
        }

        target.batchQueryBalance(addresses);

        verify(target, times(1)).batchQueryBalance(anyCollection());
    }
}
