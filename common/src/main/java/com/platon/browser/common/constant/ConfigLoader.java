package com.platon.browser.common.constant;


import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:32
 */
public abstract class ConfigLoader {

    private  static Properties properties;


    static void loadConfigPath(){
        properties = new Properties();
        InputStream in = null;
        try {
            in = ConfigLoader.class.getClassLoader().getResourceAsStream("web3j.properties");
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