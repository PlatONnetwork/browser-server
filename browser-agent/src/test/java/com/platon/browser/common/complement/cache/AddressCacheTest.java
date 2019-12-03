package com.platon.browser.common.complement.cache;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressCacheTest extends AgentTestBase {

    @Spy
    private AddressCache addressCache;
    @Test
    public void test(){
        addressCache.init(new ArrayList<>(addressList));
        addressCache.initOnFrist();
        addressCache.update(transactionList.get(0));
        addressCache.getAll();
        addressCache.cleanAll();
    }
}
