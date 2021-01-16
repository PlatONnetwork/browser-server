package com.platon.browser.utils;

import com.platon.browser.enums.I18nEnum;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class I18nUtil {
    @Autowired
    private MessageSource messageSource;
    public String i( I18nEnum key, Object... param){
    	/** 获取默认locale */
        Locale locale = LocaleContextHolder.getLocale();
        /** 加载对应key的中英文 */
        return messageSource.getMessage(key.name().toLowerCase(),param, locale);
    }
    
    public String i( I18nEnum key, String localStr, Object... param){
    	/** 获取locale */
    	Locale locale = Locale.forLanguageTag(localStr);
        /** 加载对应key的中英文 */
        return messageSource.getMessage(key.name().toLowerCase(),param, locale);
    }
    
    public String getMessageForStr( String key, String localStr, Object... param){
    	I18nEnum keyI18 = I18nEnum.valueOf(key);
    	/** 获取locale */
        Locale locale = Locale.forLanguageTag(localStr);
        /** 加载对应key的中英文 */
        return messageSource.getMessage(keyI18.name().toLowerCase(),param, locale);
    }
}
