package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.BrowserConst;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.dao.mapper.CustomErc20TokenAddressRelMapper;
import com.platon.browser.erc.ErcService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @program: ErcBalanceUpdateTest.java
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020/10/16
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ErcBalanceUpdateTest extends AgentTestBase {
    @Mock
    protected RedisTemplate<String, String> redisTemplate;
    @Mock
    private CustomErc20TokenAddressRelMapper customErc20TokenAddressRelMapper;
    @Mock
    private ErcService ercService;
    @Mock
    private SetOperations setOperations;

    @Spy
    private ErcBalanceUpdateTask target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(this.target, "redisTemplate", this.redisTemplate);
        ReflectionTestUtils.setField(this.target, "customErc20TokenAddressRelMapper", this.customErc20TokenAddressRelMapper);
        ReflectionTestUtils.setField(this.target, "ercService", this.ercService);
        ReflectionTestUtils.setField(this.target, "batchSize", 10);

        Set<String> data = new HashSet<>();
        data.add("lax1jzcc0xqvkglwmr3txeaf2c9jxp6pzmse3gxk9n#lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e");

        when(this.redisTemplate.opsForSet()).thenReturn(this.setOperations);
        when(this.setOperations.members(BrowserConst.ERC_BALANCE_KEY)).thenReturn(data);
        when(this.ercService.getBalance(any(), any())).thenReturn(BigInteger.TEN);
    }

    @Test
    public void test() {
        AppStatusUtil.setStatus(AppStatus.RUNNING);
        this.target.cron();
    }
}
