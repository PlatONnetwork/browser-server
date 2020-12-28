package com.platon.browser.service.erc20;

import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.exception.BusinessException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

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
    @InjectMocks
    @Spy
    private Erc20RetryService target;

    @Test
    public void test_getErc20Token() {
        when(this.erc20TokenMapper.selectByAddress(any())).thenReturn(null);
        try {
            this.target.getErc20Token("123");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BusinessException);
        }
        Erc20Token erc20Token = new Erc20Token();
        when(this.erc20TokenMapper.selectByAddress(any())).thenReturn(erc20Token);
        Erc20Token erc20Token1 = this.target.getErc20Token("123");
        Assert.assertNotNull(erc20Token1);
    }

}
