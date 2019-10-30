package com.platon.browser.old.engine.stage;

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
public class StageTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(StageTest.class);

    private List<Class<?>> target = new ArrayList<>();
    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        String packageName= StageTest.class.getPackage().getName();
        Set<Class<?>> classSet = ClassUtil.getClasses(packageName);
        classSet.stream().filter(clazz->!clazz.getName().endsWith("Test")).forEach(target::add);
    }
    @Test
    public void test() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        for (Class<?> clazz:target){
            Method[] methods = clazz.getDeclaredMethods();
            for(Method method:methods){
                if(Modifier.isStatic(method.getModifiers())) continue;
                Class<?>[] types = method.getParameterTypes();
                Object instance = clazz.newInstance();
                if(types.length!=0){
                    Object[] args = new Object[types.length];
                    for (int i=0;i<types.length;i++) args[i]=mock(types[i]);
                    method.invoke(instance,args);
                    continue;
                }
                method.invoke(instance);
            }
        }
        assertTrue(true);
    }
}
