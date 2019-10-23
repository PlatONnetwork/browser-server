package com.platon.browser.util;

import com.platon.browser.enums.I18nEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static final Map<Locale, TimeZone> LOCAL_TIME_ZONES;
    static {
        Map<Locale, TimeZone> map = new HashMap<>(64);
        map.put(Locale.US, TimeZone.getTimeZone("America/Chicago"));
        map.put(Locale.CANADA, TimeZone.getTimeZone("America/Chicago"));
        map.put(Locale.CHINA, TimeZone.getTimeZone("Asia/Shanghai"));
        map.put(Locale.SIMPLIFIED_CHINESE, TimeZone.getTimeZone("Asia/Shanghai"));
        map.put(Locale.TAIWAN, TimeZone.getTimeZone("Asia/Shanghai"));
        map.put(Locale.TRADITIONAL_CHINESE, TimeZone.getTimeZone("Asia/Shanghai"));
        map.put(Locale.FRANCE, TimeZone.getTimeZone("Europe/Paris"));
        map.put(Locale.FRENCH, TimeZone.getTimeZone("Europe/Paris"));
        map.put(Locale.JAPAN, TimeZone.getTimeZone("Asia/Tokyo"));
        map.put(Locale.ENGLISH, TimeZone.getTimeZone("Europe/London"));
        map.put(Locale.GERMAN, TimeZone.getTimeZone("Europe/Paris"));
        LOCAL_TIME_ZONES = Collections.unmodifiableMap(map);
    }

    private static TimeZone getTimeZone(){
        Locale locale = LocaleContextHolder.getLocale();
        return LOCAL_TIME_ZONES.get(locale);
    }

    public static String getDateTimeString(Date date,TimeZone timeZone,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    public static void main(String[] args) throws ParseException {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = sdf.parse("2018-10-11 09:33:24"); // 格林威治日期时间

        log.info("格林威治时间：{}",getDateTimeString(date,TimeZone.getTimeZone("GMT"),format));

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        log.info("英国日期时间：{}",getDateTimeString(date,getTimeZone(),format));

        LocaleContextHolder.setLocale(Locale.US);
        log.info("美国日期时间：{}",getDateTimeString(date,getTimeZone(),format));

        LocaleContextHolder.setLocale(Locale.CHINA);
        log.info("中国日期时间：{}",getDateTimeString(date,getTimeZone(),format));

        LocaleContextHolder.setLocale(Locale.JAPAN);
        log.info("日本日期时间：{}",getDateTimeString(date,getTimeZone(),format));

        LocaleContextHolder.setLocale(Locale.FRANCE);
        log.info("法国日期时间：{}",getDateTimeString(date,getTimeZone(),format));

    }
}
