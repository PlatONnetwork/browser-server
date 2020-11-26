package com.platon.browser.test;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.BrowserApiApplication;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JasyptTest {

    @Autowired
    StringEncryptor encryptor;


    @Test
    public void encry () {
        String username = encryptor.encrypt("root");//加密root
        System.out.println("username:" + username);
        String password = encryptor.encrypt("Juzhen123!");//加密123456
        System.out.println("password:" + password);
        assertTrue(true);
    }

//    @Test
    public void dect () {
        String deUserName = encryptor.decrypt("ENC(bpshgjWOgn2d+oGmSTBPNA==)");//解密
        System.out.println("deUserName:" + deUserName);
    }

    public static void main(String[] args) {
        String data = "0xc786333031303032";
        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(data));
        List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);
        System.out.println(statusCode);
    }
}
