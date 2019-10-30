package com.platon.browser.old.engine.cache;

import com.platon.browser.TestBase;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.old.engine.stage.BlockChainStage;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.old.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 20:29
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressCacheUpdaterTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(AddressCacheUpdaterTest.class);
    @Spy
    private AddressCacheUpdater target;
    @Mock
    private CacheHolder cacheHolder;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "cacheHolder", cacheHolder);
    }

    @Test
    public void testUpdateAddressStatistics() throws NoSuchBeanException, BusinessException {
        NodeCache nodeCache = mock(NodeCache.class);
        when(cacheHolder.getNodeCache()).thenReturn(nodeCache);
        AddressCache addressCache = mock(AddressCache.class);
        when(cacheHolder.getAddressCache()).thenReturn(addressCache);
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        when(nodeCache.getAllStaking()).thenReturn(new HashSet<>(stakings));
        when(nodeCache.getDelegationByIsHistory(any(CustomDelegation.YesNoEnum.class))).thenReturn(delegations);
        CustomStaking staking = stakings.get(0);
        CustomAddress address = addresses.get(0);
        staking.setStakingAddr(address.getAddress());
        when(nodeCache.getStaking(anyString(),anyLong())).thenReturn(staking);
        when(addressCache.getAllAddress()).thenReturn(addresses);
        target.updateAddressStatistics();
        verify(target, times(1)).updateAddressStatistics();
    }

}
