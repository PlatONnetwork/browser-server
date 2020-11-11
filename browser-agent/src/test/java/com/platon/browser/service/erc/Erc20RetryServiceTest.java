package com.platon.browser.service.erc;

import com.platon.browser.common.service.erc.Erc20RetryService;
import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.exception.BusinessException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-10-28 14:37
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class Erc20RetryServiceTest {

    @Mock
    private Erc20TokenMapper erc20TokenMapper;

    @Spy
    private Erc20RetryService targe;

    @Test
    public void test_getErc20Token() {
        ReflectionTestUtils.setField(this.targe, "erc20TokenMapper", this.erc20TokenMapper);
        when(this.erc20TokenMapper.selectByAddress(any())).thenReturn(null);
        try {
            this.targe.getErc20Token("123");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BusinessException);
        }
        Erc20Token erc20Token = new Erc20Token();
        when(this.erc20TokenMapper.selectByAddress(any())).thenReturn(erc20Token);
        Erc20Token erc20Token1 = this.targe.getErc20Token("123");
        Assert.assertNotNull(erc20Token1);
    }

}
