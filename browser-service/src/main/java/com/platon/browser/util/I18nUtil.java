package com.platon.browser.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18nUtil {
    @Autowired
    private MessageSource messageSource;
    public String i(I18nEnum key, Object... param){
        Locale locale = LocaleContextHolder.getLocale();
        String msg = messageSource.getMessage(key.name().toLowerCase(),param, locale);
        return msg;
    }
}
