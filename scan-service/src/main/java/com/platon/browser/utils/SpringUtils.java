package com.platon.browser.utils;

import com.platon.browser.config.EsClusterConfig;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringUtils implements BeanPostProcessor  {

	@Autowired
	private EsClusterConfig esClusterConfig;

	@Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

	public Object resetSpring(String targetName) {
		defaultListableBeanFactory.setAllowBeanDefinitionOverriding(true);
		boolean containsBean = defaultListableBeanFactory.containsBean(targetName);
		log.error("start replace, containsBean:{}" ,containsBean);
		if (containsBean) {
            //移除bean的定义和实例
            defaultListableBeanFactory.removeBeanDefinition(targetName);
        }
		EsClusterConfig es = new EsClusterConfig();
		BeanUtils.copyProperties(esClusterConfig, es);
		RestHighLevelClient restHighLevelClient = es.client();
		log.error(restHighLevelClient.toString());
		//注册新的bean定义和实例
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(restHighLevelClient.getClass());
		defaultListableBeanFactory.registerBeanDefinition(targetName, beanDefinitionBuilder.getBeanDefinition());
        defaultListableBeanFactory.registerSingleton(targetName, restHighLevelClient);
        log.error("replace success!");
        return restHighLevelClient;
	}
}
