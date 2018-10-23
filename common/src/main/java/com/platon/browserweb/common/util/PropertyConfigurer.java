/*
 * Copyright (c) 2017. juzhen.io. All rights reserved.
 */

package com.platon.browserweb.common.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component("exchangePropertyConfigurer")
public class PropertyConfigurer extends PropertySourcesPlaceholderConfigurer {

	public static final String EMAIL_URL = "email.url";

	public static final String SMS_URL = "sms.url";

	public static final String IS_SEND_SMS = "isSendSms";
	
	public static final String VPN_IP = "vpn.ip";

	public static final String VPN_PORT = "vpn.port";

	private static ConfigurablePropertyResolver propertyResolver;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, ConfigurablePropertyResolver propertyResolver) throws BeansException {
		super.processProperties(beanFactoryToProcess, propertyResolver);
		this.propertyResolver = propertyResolver;
	}



	public static String getStringValue(String key) {
		return propertyResolver.getProperty(key);
	}

	public static String getEmailUrl() {
		return getStringValue(EMAIL_URL);
	}

	public static String getSmsUrl() {
		return getStringValue(SMS_URL);
	}

	public static String getIsSendSms() {
		return getStringValue(IS_SEND_SMS);
	}
	
	public static String getVpnIp() {
		return getStringValue(VPN_IP);
	}

	public static Integer getVpnPort() {
		return Integer.valueOf(getStringValue(VPN_PORT));
	}

	public static boolean getBoolean(String key) {
		if (propertyResolver != null) {
			String flag = propertyResolver.getProperty(key);
			return Boolean.valueOf(flag);
		}
		return false;
	}
}
