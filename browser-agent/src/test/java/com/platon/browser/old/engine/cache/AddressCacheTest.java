package com.platon.browser.old.engine.cache;

import com.platon.browser.TestBase;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.exception.BeanCreateOrUpdateException;
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

import static org.junit.Assert.assertEquals;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 20:29
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressCacheTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(AddressCacheTest.class);
    @Spy
    private AddressCache addressCache;

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
    public void test() throws NoSuchBeanException {
        CustomAddress ca = new CustomAddress();
        ca.setAddress("address");
        addressCache.add(ca);
        CustomAddress result = addressCache.getAddress("address");
        assertEquals(ca,result);

        Collection<CustomAddress> all = addressCache.getAllAddress();
        assertEquals(1,all.size());

        thrown.expect(NoSuchBeanException.class);
        addressCache.getAddress("xxx");
    }

}
