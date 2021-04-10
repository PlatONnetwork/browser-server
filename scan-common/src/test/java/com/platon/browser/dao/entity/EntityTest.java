package com.platon.browser.dao.entity;

import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.utils.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

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
public class EntityTest {

    private List<Class<?>> target = new ArrayList<>();
    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        String packageName= EntityTest.class.getPackage().getName();
        Set<Class<?>> classSet = ClassUtil.getClasses(packageName);
        classSet.stream().filter(clazz->!clazz.getName().endsWith("Test")
                &&!clazz.getName().endsWith("Column")
                &&!clazz.getName().endsWith("Criterion")
                &&!clazz.getName().endsWith("GeneratedCriteria")
                &&!clazz.getName().endsWith("Example")
        ).forEach(target::add);
    }
    @Test
    public void test() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        for (Class<?> clazz:target){
            Method[] methods = clazz.getDeclaredMethods();
            for(Method method:methods){
//                if(Modifier.isStatic(method.getModifiers())) continue;
//                if(Modifier.isProtected(method.getModifiers())) continue;
                if(Modifier.isPrivate(method.getModifiers())) continue;
                if(method.getName().equals("init")) continue;
                Class<?>[] types = method.getParameterTypes();
                Object instance = clazz.newInstance();
                if(types.length!=0){
                    Object[] args = new Object[types.length];
                    for (int i=0;i<types.length;i++){
                        if(Boolean.class==types[i]||boolean.class==types[i]){
                            args[i]=Boolean.TRUE;
                            continue;
                        }
                        if(Double.class==types[i]||double.class==types[i]){
                            args[i]=11.3;
                            continue;
                        }
                        if(String.class==types[i]){
                            args[i]="333";
                            continue;
                        }
                        if(Integer.class==types[i]||int.class==types[i]){
                            args[i]=333;
                            continue;
                        }
                        if(Long.class==types[i]||long.class==types[i]){
                            args[i]=333L;
                            continue;
                        }
                        args[i]=mock(types[i]);
                    }
                    method.setAccessible(true);
                    method.invoke(instance,args);
                    continue;
                }
                method.setAccessible(true);
                method.invoke(instance);
            }
        }
        assertTrue(true);
    }
}
