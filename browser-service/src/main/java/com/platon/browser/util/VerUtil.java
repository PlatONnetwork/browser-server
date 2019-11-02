package com.platon.browser.util;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版本号转换
 *  @file VerUtil.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年9月9日
 */
public class VerUtil {
  private VerUtil(){}

  private static Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)");
  /**
   * 版本号转biginteger
   * @method toInteger
   * @param version
   * @return
   */
  public static BigInteger toInteger(String version) {
    Matcher matcher = pattern.matcher(version);
    if (matcher.find()) {
      int ver = Byte.parseByte(matcher.group(1)) << 16 & 0x7fffffff;
      int lite = Byte.parseByte(matcher.group(2)) << 8 & 0x7fffffff;
      int patch = Byte.parseByte(matcher.group(3)) & 0x7fffffff;
      int id = ver | lite | patch;
      return BigInteger.valueOf(id);
    } else {
      throw new NumberFormatException("version is invalid");
    }
  }

  /**
   * BigInteger转版本号
   * @method toVersion
   * @param version
   * @return
   */
  public static String toVersion(BigInteger version) {
    int v = version.intValue();
    int ver = v >> 16 & 0x0000ffff;
    int lite = v >> 8 & 0x000000ff;
    int patch = v & 0x000000ff;
    return String.format("%s.%s.%s", ver, lite, patch);
  }
  
  /**
   * BigInteger转大版本号
   * @method toVersion
   * @param version
   * @return
   */
  public static BigInteger transferBigVersion(BigInteger version) {
    int v = version.intValue();
    int ver = v >> 16 & 0x0000ffff;
    int lite = v >> 8 & 0x000000ff;
    String bigVersion = String.format("%s.%s.%s", ver, lite, 0);
    return toInteger(bigVersion);
  }
}
