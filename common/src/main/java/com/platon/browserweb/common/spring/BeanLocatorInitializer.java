/*
 * Copyright (c) 2018. juzhen.io. All rights reserved.
 */

package com.platon.browserweb.common.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/7/16
 */
public class BeanLocatorInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private final static Logger logger = LoggerFactory.getLogger(BeanLocatorInitializer.class);
    public void initialize(ConfigurableApplicationContext applicationContext) {
        logger.info("ServiceLocatorInitializer ServiceLocator Context Aware");
        BeanLocator.setFactory(applicationContext);
    }
}
