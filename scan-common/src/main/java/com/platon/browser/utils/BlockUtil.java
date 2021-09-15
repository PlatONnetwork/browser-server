package com.platon.browser.utils;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.LoggerFactory;

/**
 * 区块工具类
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/3/10
 */
public class BlockUtil {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BlockUtil.class);

    /**
     * 16进制前缀
     */
    public static final String HEX_PRE = "0x";

    /**
     * 从extraData获取postscript
     *
     * @param extraData
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/10
     */
    public static String getPostscriptFromExtraData(String extraData) {
        try {
            if (StringUtils.isNotBlank(extraData)) {
                byte[] byteExtraData = Hex.decode(delHEXPre(extraData));
                return new String(byteExtraData);
            } else {
                return extraData;
            }
        } catch (Exception e) {
            log.error("从extraData获取postscript异常", e);
            return extraData;
        }
    }

    /**
     * 删除16进制前缀0x
     *
     * @param hexString
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/10
     */
    public static String delHEXPre(String hexString) {
        if (StringUtils.isNotBlank(hexString) && hexString.startsWith(HEX_PRE)) {
            return StringUtils.removeStart(hexString, HEX_PRE);
        } else {
            return hexString;
        }
    }

    public static void main(String[] args) {
        String desc1 = "τῆς ἄνω ὁδοῦ ἀεὶ ἑξόμεθα καὶ δικαιοσύνην μετὰ φρονήσεως παντὶ τρόπῳ ἐπιτηδεύσομεν.";
        String desc2 = "We shall always keep to the upper road and practice justice with prudence in every way.";
        String desc3 = "让我们永远坚持走向上的路，全力以审慎践行正义。";
        desc1 = "0x" + Hex.toHexString(desc1.getBytes());
        System.out.println("打印结果为：" + desc1);
        System.out.println("打印结果为：" + BlockUtil.getPostscriptFromExtraData("0x"));
    }

}
