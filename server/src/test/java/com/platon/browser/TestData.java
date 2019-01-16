package com.platon.browser;

import com.platon.browser.util.DataGenTool;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class TestData {
    private static Web3j web3j = Web3j.build(new HttpService("http://192.168.9.76:8793"));
//    private static Web3j web3j = Web3j.build(new HttpService("http://10.10.8.209:6789"));

    public static final String testDataDir = TestData.class.getClassLoader().getResource("./").getPath()+"../../../testdata/";

    static { DataGenTool.web3j = web3j; }

    public enum TestDataFileNameEnum {
        NODE("nodes-"),
        BLOCK("blocks-"),
        TRANSACTION("transactions-"),
        PENDINGTX("pendingTxes-");
        public String prefix;
        TestDataFileNameEnum(String prefix){
            this.prefix=prefix;
        }
    }
}
