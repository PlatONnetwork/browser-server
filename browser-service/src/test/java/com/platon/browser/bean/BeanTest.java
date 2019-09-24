package com.platon.browser.bean;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.platon.browser.client.AccuVerifiersCount;
import com.platon.browser.config.bean.Common;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.req.PageReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.resp.staking.AliveStakingListResp;
import com.platon.browser.utils.ClassUtil;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BeanTest {
	
//	private static Logger logger = LoggerFactory.getLogger(BeanTest.class);

//	private List<Class<?>> target = new ArrayList<>();
	private List<List<Class<?>>> targetList = new ArrayList<>();

	/**
	 * 测试开始前，设置相关行为属性
	 * 
	 * @throws IOException
	 * @throws BeanCreateOrUpdateException
	 */
	@Before
//	@Test
	public void setup() {
		List<Class<?>> target = new ArrayList<>();
		Set<Class<?>> classSet;
		String packageName;
		
		packageName = AccuVerifiersCount.class.getPackage().getName();
		classSet = ClassUtil.getClasses(packageName);
		classSet.stream()
			.filter(clazz -> 
					!(clazz.getName().endsWith("Test") || 
					clazz.getAnnotations().length>0)
					).forEach(target::add);
		targetList.add(target);
		
		target = new ArrayList<>();
		packageName = Common.class.getPackage().getName();
		classSet = ClassUtil.getClasses(packageName);
		classSet.stream()
			.filter(clazz -> 
					!(clazz.getName().endsWith("Test") ||
					clazz.getName().endsWith("EconomicConfigParam") ||
					clazz.getAnnotations().length>0)
				).forEach(target::add);
		targetList.add(target);
		
		target = new ArrayList<>();
		packageName = AccountDownload.class.getPackage().getName();
		classSet = ClassUtil.getClasses(packageName);
		classSet.stream().forEach(target::add);
		targetList.add(target);
		
		target = new ArrayList<>();
		packageName = PageReq.class.getPackage().getName();
		classSet = ClassUtil.getClasses(packageName);
		classSet.stream().forEach(target::add);
		targetList.add(target);
		
		target = new ArrayList<>();
		packageName = RespPage.class.getPackage().getName();
		classSet = ClassUtil.getClasses(packageName);
		classSet.stream().filter(clazz->!clazz.getName().endsWith("BaseResp")).forEach(target::add);
		targetList.add(target);
		
		target = new ArrayList<>();
		packageName = AliveStakingListResp.class.getPackage().getName();
		classSet = ClassUtil.getClasses(packageName);
		classSet.stream().forEach(target::add);
		targetList.add(target);
	}

	/*
	 */
	@Test
	public void test() throws InvocationTargetException, IllegalAccessException, InstantiationException {
		for (List<Class<?>> targetPkgList : targetList) {
			for (Class<?> clazz : targetPkgList) {
//				System.out.println(clazz);
				Method[] methods = clazz.getDeclaredMethods();
				for (Method method : methods) {
//					System.out.println("test methos--->"+method.getName());
					if (Modifier.isStatic(method.getModifiers())) continue;
					if (Modifier.isProtected(method.getModifiers())) continue;
					Class<?>[] types = method.getParameterTypes();
					Object instance = clazz.newInstance();
					if (types.length != 0) {
						Object[] args = new Object[types.length];
						for (int i = 0; i < types.length; i++) {
//							System.out.println(types[i].getTypeName());
							if (Boolean.class == types[i] || types[i].getName().equals("boolean")) {
								args[i] = Boolean.TRUE;
								continue;
							}
							if (Double.class == types[i]) {
								args[i] = 11.3;
								continue;
							}
							if (String.class == types[i] && clazz.getName().endsWith(".RestrictingBalance")) {
								args[i] = "0x7b";
								continue;
							}
							if (String.class == types[i]) {
								args[i] = "333";
								continue;
							}
							if (Integer.class == types[i] || types[i].getName().equals("int")) {
								args[i] = 333;
								continue;
							}
							if (Long.class == types[i] || types[i].getName().equals("long")) {
								args[i] = 333l;
								continue;
							}
							if (BigDecimal.class == types[i]) {
								args[i] = new BigDecimal(333);
								continue;
							}
							if (types[i].getTypeName().equals("byte[]")) {
								args[i] = "string".getBytes();
								continue;
							}
							if (types[i].getTypeName().equals("java.lang.Long[]")) {
								args[i] = new Long[2];
								continue;
							}
							if (types[i].getTypeName().equals("java.lang.Double[]")) {
								args[i] = new Double[2];
								continue;
							}
							args[i] = mock(types[i]);
						}
						method.invoke(instance, args);
						continue;
					}
					method.invoke(instance);
				}
			}
			assertTrue(true);
		}
	}
	
}
