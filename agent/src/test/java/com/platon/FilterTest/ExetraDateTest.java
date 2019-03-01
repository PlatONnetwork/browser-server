package com.platon.FilterTest;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.utils.Numeric;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

/**
 * User: dongqile
 * Date: 2019/3/1
 * Time: 14:18
 */
public class ExetraDateTest {

    public static void main ( String[] args ) throws UnsupportedEncodingException {
        /*
         *
         *  16进制转byte，后32位
         */
        String exetraDateString = "0xd982040086706c61746f6e86676f312e31318777696e646f777300000000000000000000000000000000000000000000000000000000000000000000000000007bb758c1615f287cadecbe19574e3024f5ca1c3962b0f5bfce15ffbb9127533b608c191cc14d766de8956f0f9dfae3df66f5323db5496ab3ca3c5e3a4ff6140c01";
        byte[] a = Hex.decode(exetraDateString.replace("0x",""));
        System.out.println(a);
        System.out.println(a.length);
        System.out.println(a.length - 32);
        byte[] b = new byte[]{};
        b = Arrays.copyOfRange(a,32,a.length);
        System.out.println(b);
        String tickedId2 = new String(b,"utf-8");
        String tickedId = Base64.getEncoder().encodeToString(b);
        System.out.println(tickedId);
        System.out.println(tickedId2);
           //System.arraycopy(a,  32,b, 0,a.length - 32);

    }
}