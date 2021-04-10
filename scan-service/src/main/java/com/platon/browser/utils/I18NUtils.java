package com.platon.browser.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18NUtils {

    private ResourceBundle resourceBundle;

    private static String b;
    private static String l;

    private I18NUtils() {
        resourceBundle = ResourceBundle.getBundle(b, new Locale(l));
    }


    /**
     * 获取配置对象.
     *
     * @return 配置对象
     */
    public static I18NUtils getInstance() {
        return SingletonContainer.instance;
    }

    public static void init(String baseName, String locale) {
        b = baseName;
        l = locale;
    }

    private static class SingletonContainer{
        private static I18NUtils instance = new I18NUtils();
    }

    public String getResource(String key) {
        if (resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        } else {
            return null;
        }
    }

    public String getResource(Integer code) {
        if (resourceBundle.containsKey(String.valueOf(code))) {
            return resourceBundle.getString(String.valueOf(code));
        } else {
            return null;
        }
    }
}
