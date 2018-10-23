package com.platon.browserweb.common.spring;

import com.platon.browserweb.common.util.PropertyConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component("beanLocator")
public class BeanLocator implements ApplicationContextAware {
    private static ApplicationContext ctx;     //Spring应用上下文环境

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param ctx
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        BeanLocator.ctx = ctx;
    }

    /**
     * 获取对象
     *
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
    public static  <T> T  getBean(String name) throws BeansException {
        assertContextInjected();
        boolean mock = PropertyConfigurer.getBoolean("mock");
        if (mock) {
            return (T)ctx.getBean(name + "Mock");
        }
        return  (T)ctx.getBean(name);
    }

    public static <T> Collection<T> getBeansOfType(Class<T> clazz) {
        assertContextInjected();
        Map<String, T> map = ctx.getBeansOfType(clazz);
        return map.values();
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void assertContextInjected() {
        if (ctx == null) {
            throw new IllegalStateException("Spring application context未注入,请在spring配置文件中定义BeanLocator");
        }
    }

    public static void setFactory(ApplicationContext context) {
        ctx = context;
    }
}
