package com.platon.browser.engine.cache;

import com.platon.browser.TestBase;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomUnDelegation;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.CacheConstructException;
import com.platon.browser.exception.NoSuchBeanException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 20:29
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class NodeCacheTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(NodeCacheTest.class);
    @Spy
    private NodeCache target;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {

    }

    @Test
    public void testInit() {
        try {
            target.init(nodes,stakings,delegations,unDelegations);
        } catch (CacheConstructException e) {
            fail();
        }

        CustomStaking errStaking = new CustomStaking();
        errStaking.setNodeId("unknown");
        stakings.add(errStaking);
        try {
            target.init(nodes,stakings,delegations,unDelegations);
            fail();
        } catch (CacheConstructException e) {
            assertTrue(e.getMessage().contains("无法向其关联质押"));
        }

        stakings.remove(errStaking);
        CustomDelegation errDelegation = new CustomDelegation();
        errDelegation.setNodeId("unknown");
        delegations.add(errDelegation);
        try {
            target.init(nodes,stakings,delegations,unDelegations);
            fail();
        } catch (CacheConstructException e) {
            assertTrue(e.getMessage().contains("无法向其关联委托"));
        }


        delegations.remove(errDelegation);
        CustomUnDelegation errUnDelegation = new CustomUnDelegation();
        errUnDelegation.setNodeId("unknown");
        errUnDelegation.setStakingBlockNum(888888888888L);
        unDelegations.add(errUnDelegation);
        try {
            target.init(nodes,stakings,delegations,unDelegations);
            fail();
        } catch (CacheConstructException e) {
            assertTrue(e.getMessage().contains("无法向其关联解委托"));
        }

    }

}
