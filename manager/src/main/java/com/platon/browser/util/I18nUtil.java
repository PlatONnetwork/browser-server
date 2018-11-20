package com.platon.browser.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class I18nUtil {
    private MessageSource messageSource;
    public I18nUtil(MessageSource messageSource){
        this.messageSource=messageSource;
    }
    public String i(I18nEnum key, Object... param){
        Locale locale = LocaleContextHolder.getLocale();
        String msg = messageSource.getMessage(key.name().toLowerCase(),param, locale);
        return msg;
    }
}
