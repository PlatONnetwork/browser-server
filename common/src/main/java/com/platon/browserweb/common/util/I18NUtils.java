package com.platon.browserweb.common.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18NUtils {

    private ResourceBundle RES_BUNDLE;

    private static String b;
    private static String l;

    private I18NUtils () {
        RES_BUNDLE = ResourceBundle.getBundle(b, new Locale(l));
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
        if (RES_BUNDLE.containsKey(key)) {
            System.out.println("I18NUtils::+++++++++++::::::" + RES_BUNDLE.getLocale());
            return RES_BUNDLE.getString(key);
        } else {
            return null;
        }
    }

    public String getResource(Integer code) {
        if (RES_BUNDLE.containsKey(String.valueOf(code))) {
            return RES_BUNDLE.getString(String.valueOf(code));
        } else {
            return null;
        }
    }
}
