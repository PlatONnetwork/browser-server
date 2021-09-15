package com.platon.browser.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.platon.bech32.Bech32;
import com.platon.browser.service.BlockResult;
import com.platon.parameters.NetworkParameters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.UUID;

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
     * @param salt 盐
     * @param str  要加密的字符串
     * @return java.lang.String
     * @date 2021/5/10
     */
    public static String getEncrypt(String salt, String str) {
        try {
            if (StrUtil.isEmpty(salt)) {
                salt = "my123456";
            }
            Properties properties = new Properties();
            String propertiesValue = properties.getProperty("jasypt.encryptor.password", salt);
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

    private static final BlockResult.AddressCount ADDRESS = new BlockResult.AddressCount();

    /**
     * 创建随机地址
     *
     * @param addressReusedTimes 地址可重复次数
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/24
     */
    public static String getRandomAddress(int addressReusedTimes) {
        String txHash = HexUtil.prefix(DigestUtils.sha256Hex(UUID.randomUUID().toString()));
        String hexAddr = HexUtil.prefix(DigestUtils.sha1Hex(txHash));
        return ADDRESS.get(Bech32.addressEncode(NetworkParameters.getHrp(), hexAddr), addressReusedTimes);
    }

    public static void main(String[] args) {
        long time = DateUtil.parse("2021-03-27 18:30:00", "yyyy-MM-dd HH:mm:ss").getTime();
        System.out.println("打印结果为：" + time);
        System.out.println("打印结果为：" + cn.hutool.core.util.HexUtil.toHex(time));
        int decimal = Integer.parseInt(String.valueOf(2));
        BigDecimal afterConverValue = ConvertUtil.convertByFactor(new BigDecimal("1000"), decimal);
        System.out.println("打印结果为：" + afterConverValue.toString());
    }

}
