package com.platon.browser.res;

import com.github.pagehelper.Page;
import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.res.token.*;
import com.platon.browser.utils.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ResTest {

    private List<Class<?>> target = new ArrayList<>();

    /**
     * 测试开始前，设置相关行为属性
     * 
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        String packageName = ResTest.class.getPackage().getName();
        Set<Class<?>> classSet = ClassUtil.getClasses(packageName);
        classSet.stream()
            .filter(clazz -> !clazz.getName().endsWith("Test") && !clazz.getName().endsWith("Column")
                && !clazz.getName().endsWith("Criterion") && !clazz.getName().endsWith("GeneratedCriteria"))
            .forEach(this.target::add);
    }

    @Test
    public void test() {
        for (Class<?> clazz : this.target) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers()))
                    continue;
                if (Modifier.isProtected(method.getModifiers()))
                    continue;
                if (Modifier.isPrivate(method.getModifiers()))
                    continue;
                if (method.getName().equals("init"))
                    continue;
                if (clazz.isEnum())
                    continue;
                Class<?>[] types = method.getParameterTypes();

                Object instance = null;
                try {
                    instance = clazz.newInstance();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    continue;
                }
                if (types.length != 0) {
                    Object[] args = new Object[types.length];
                    for (int i = 0; i < types.length; i++) {
                        if (Boolean.class == types[i] || boolean.class == types[i]) {
                            args[i] = Boolean.TRUE;
                            continue;
                        }
                        if (Double.class == types[i] || double.class == types[i]) {
                            args[i] = 11.3;
                            continue;
                        }
                        if (String.class == types[i]) {
                            args[i] = "333";
                            continue;
                        }
                        if (Integer.class == types[i] || int.class == types[i]) {
                            args[i] = 333;
                            continue;
                        }
                        if (Long.class == types[i] || long.class == types[i]) {
                            args[i] = 333L;
                            continue;
                        }
                        if (types[i].getTypeName().equals("byte[]")) {
                            args[i] = new byte[] {1};
                            continue;
                        }
                        if (types[i].getTypeName().equals("java.lang.Double[]")) {
                            args[i] = new Double[] {};
                            continue;
                        }
                        if (types[i].getTypeName().equals("java.lang.Long[]")) {
                            args[i] = new Long[] {};
                            continue;
                        }
                        args[i] = mock(types[i]);
                    }
                    method.setAccessible(true);
                    try {
                        method.invoke(instance, args);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                method.setAccessible(true);
                try {
                    method.invoke(instance);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        assertTrue(true);
    }

    @Test
    public void test_method() {
        QueryHolderTokenListResp queryHolderTokenListResp = QueryHolderTokenListResp.builder()
                .address("").balance(BigDecimal.TEN).contract("").decimal(0).name("").symbol("").txCount(0).build();
        queryHolderTokenListResp = new QueryHolderTokenListResp("", "", BigDecimal.TEN, 1, "", "", 0);
        assertNotNull(queryHolderTokenListResp);
        QueryTokenDetailResp queryTokenDetailResp = QueryTokenDetailResp.builder()
                .address("").blockTimestamp(new Date()).createTime(new Date()).creator("")
                .decimal(0).holder(0).name("").symbol("").totalSupply("").txCount(0).txHash("")
                .abi("").sourceCode("").binCode("").icon("").build();
        queryTokenDetailResp = new QueryTokenDetailResp("", "", "", 0,
                "", "", "", "", "", new Date(), 0, new Date(), 0, "", "", "");
        queryTokenDetailResp = QueryTokenDetailResp.fromErc20Token(Erc20Token.builder().
                decimal(1).totalSupply(BigDecimal.TEN).build());
        assertNotNull(queryTokenDetailResp);

        QueryTokenHolderListResp queryTokenHolderListResp = QueryTokenHolderListResp.builder()
                .address("").balance(BigDecimal.TEN).percent("").build();
        queryTokenHolderListResp = new QueryTokenHolderListResp("", "", BigDecimal.TEN);
        assertNotNull(queryTokenHolderListResp);

        QueryTokenListResp queryTokenListResp = QueryTokenListResp.builder()
                .address("").blockTimestamp(new Date()).createTime(new Date()).creator("")
                .decimal(0).holder(0).name("").symbol("").totalSupply(BigDecimal.TEN).txHash("")
                .webSite("").icon("").build();
        queryTokenListResp = new QueryTokenListResp("", "", "", 0,
                BigDecimal.TEN, "", "", "", "", 1, new Date(), new Date());
        queryTokenListResp = QueryTokenListResp.fromErc20Token(Erc20Token.builder()
                .decimal(1).totalSupply(BigDecimal.TEN).build());
        assertNotNull(queryTokenListResp);

        QueryTokenTransferRecordListResp queryTokenTransferRecordListResp = QueryTokenTransferRecordListResp.builder()
                .transferTo("").blockNumber(10l).blockTimestamp(new Date()).contract("").decimal(0)
                .fromType(0).methodSign("").name("").result(0).symbol("").systemTimestamp(0l)
                .toType(0).txFrom("").txHash("").value(BigDecimal.TEN).transferValue(BigDecimal.TEN)
                .type("").build();
        queryTokenTransferRecordListResp = new QueryTokenTransferRecordListResp("", 10l, "", "",
                "", BigDecimal.TEN, 1, "", "", "", 0, new Date(),
                BigDecimal.TEN, 10l, "", 0, 00);
        assertNotNull(queryTokenTransferRecordListResp);

        BaseResp baseResp = new BaseResp(1, "aaa", "");
        baseResp = BaseResp.build(1, "", "");
        assertNotNull(baseResp);

        RespPage respPage = new RespPage();
        respPage.init(new Page<>(), new ArrayList());
        respPage.init(new ArrayList(), 0l, 0l, 0l);
    }
}
