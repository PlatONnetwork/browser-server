package com.platon.browser.task;

import com.platon.browser.TestBase;
import com.platon.browser.client.RestrictingBalance;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.stage.BlockChainStage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
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
    private AddressUpdateTask addressUpdateTask;
    @Mock
    private SpecialContractApi sca;
    @Mock
    private CacheHolder cacheHolder;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(addressUpdateTask, "cacheHolder", cacheHolder);
        ReflectionTestUtils.setField(addressUpdateTask, "sca", sca);
    }

    @Test
    public void testStart() throws Exception {
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        doReturn(addresses).when(addressUpdateTask).getAllAddress();
        List<RestrictingBalance> data = new ArrayList<>();
        addresses.forEach(ca->{
            RestrictingBalance rb = new RestrictingBalance();
            rb.setAccount(ca.getAddress());
            rb.setFreeBalance(Numeric.toHexStringWithPrefix(new BigInteger(ca.getBalance())));
            rb.setLockBalance(Numeric.toHexStringWithPrefix(new BigInteger(ca.getRestrictingBalance())));
            rb.setPledgeBalance(Numeric.toHexStringWithPrefix(new BigInteger(ca.getRestrictingBalance())));
            data.add(rb);
        });

        doReturn(data).when(addressUpdateTask).getRestrictingBalance(anyString());
        addressUpdateTask.start();

        verify(addressUpdateTask, times(1)).start();
    }
}
