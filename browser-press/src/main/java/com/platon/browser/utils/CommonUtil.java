package com.platon.browser.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

@Slf4j
public class CommonUtil {

    public final static String WIN = "win";

    /**
     * 获取耗时
     *
     * @param lastTime  上次总耗时（毫秒）
     * @param startTime 开始时间（毫秒）
     * @param endTime   结束时间（毫秒）
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/13
     */
    public static String getTime(long lastTime, long startTime, long endTime) {
        long h = (endTime - startTime + lastTime) / 1000 / 60 / 60;
        long m = ((endTime - startTime + lastTime) / 1000 / 60) % 60;
        return h + "h:" + m + "m";
    }

    /**
     * 解析时间，字符串转long
     *
     * @param time 耗时，格式例如：1h:23m
     * @return long 耗时毫秒
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/13
     */
    public static long resolving(String time) {
        if (StringUtils.isNotBlank(time)) {
            try {
                String[] times = time.split(":");
                String h = times[0].substring(0, times[0].length() - 1);
                String m = times[1].substring(0, times[1].length() - 1);
                long hh = new Long(h) * 60 * 60 * 1000;
                long mm = new Long(m) * 60 * 1000;
                return hh + mm;
            } catch (Exception e) {
                log.error("解析耗时异常", e);
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 判断当前系统是win(true)还是linux(false)
     *
     * @param
     * @return boolean
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/13
     */
    public static boolean isWin() {
        String osname = System.getProperties().getProperty("os.name");
        if (osname.toLowerCase().contains(WIN)) {
            return true;
        }
        return false;
    }

    /**
     * jasypt加密
     *
     * @param str 要加密的字符串
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/15
     */
    public static String getEncrypt(String str) {
        try {
            String path = CommonUtil.isWin() ? "/browser-press/jasypt.properties" : "jasypt.properties";
            File jasyptFile = FileUtils.getFile(System.getProperty("user.dir"), path);
            Properties properties = new Properties();
            properties.load(new FileInputStream(jasyptFile));
            String propertiesValue = properties.getProperty("jasypt.encryptor.password", "my123456");
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            //加密所需的salt(盐)
            textEncryptor.setPassword(propertiesValue);
            //要加密的数据（数据库的用户名或密码）
            return textEncryptor.encrypt(str);
        } catch (Exception e) {
            log.error("加密异常", e);
            return "";
        }
    }

    public static void main(String[] args) {
        System.out.println("打印结果为：" + System.getProperties().getProperty("os.name").toLowerCase());
        System.out.println("打印结果为：" + getEncrypt("root"));
        System.out.println("打印结果为：" + getEncrypt("Juzix123!"));
    }

}
