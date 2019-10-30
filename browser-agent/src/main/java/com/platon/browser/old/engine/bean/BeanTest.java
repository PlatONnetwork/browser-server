package com.platon.browser.old.engine.bean;

import com.platon.browser.TestBase;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.utils.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 20:29
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class BeanTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(BeanTest.class);

    private List<Class<?>> target = new ArrayList<>();
    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        String packageName= BeanTest.class.getPackage().getName();
        Set<Class<?>> classSet = ClassUtil.getClasses(packageName);
        classSet.stream().filter(clazz->!clazz.getName().endsWith("Test")).forEach(target::add);
    }
    @Test
    public void test() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        for (Class<?> clazz:target){
            Method[] methods = clazz.getDeclaredMethods();
            for(Method method:methods){
                if(Modifier.isStatic(method.getModifiers())) continue;
                if(Modifier.isProtected(method.getModifiers())) continue;
                Class<?>[] types = method.getParameterTypes();
                Object instance = clazz.newInstance();
                if(types.length!=0){
                    Object[] args = new Object[types.length];
                    for (int i=0;i<types.length;i++){
                        if(Boolean.class==types[i]){
                            args[i]=Boolean.TRUE;
                            continue;
                        }
                        if(Double.class==types[i]){
                            args[i]=11.3;
                            continue;
                        }
                        if(String.class==types[i]){
                            args[i]="333";
                            continue;
                        }
                        if(Integer.class==types[i]){
                            args[i]=333;
                            continue;
                        }
                        args[i]=mock(types[i]);
                    }
                    method.invoke(instance,args);
                    continue;
                }
                method.invoke(instance);
            }
        }
        assertTrue(true);
    }
}
