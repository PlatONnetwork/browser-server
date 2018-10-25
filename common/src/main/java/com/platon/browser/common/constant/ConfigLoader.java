package com.platon.browser.common.constant;

import com.platon.browser.common.util.PropertyConfigurer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:32
 */
public abstract class ConfigLoader {

    private  static Properties properties;

    public static void loadConfigPath(String configPath){
        properties = new Properties();
        InputStream in = null;
        try {
            File file = new File(configPath);
            in = new FileInputStream(file);
            properties.load(in);
        } catch (Exception e) {
            throw new IllegalArgumentException("load config failed, please check config path", e);
        }finally {
            try {
                in.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    public static Properties getProperties() {
        return properties;
    }
}