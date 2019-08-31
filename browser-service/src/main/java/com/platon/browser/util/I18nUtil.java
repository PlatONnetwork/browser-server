package com.platon.browser.util;

import com.platon.browser.enums.I18nEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化工具类
 *  @file I18nUtil.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Component
public class I18nUtil {
    @Autowired
    private MessageSource messageSource;
    public String i( I18nEnum key, Object... param){
    	/** 获取默认locale */
        Locale locale = LocaleContextHolder.getLocale();
        /** 加载对应key的中英文 */
        String msg = messageSource.getMessage(key.name().toLowerCase(),param, locale);
        return msg;
    }
}
