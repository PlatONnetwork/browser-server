package com.platon.browser;


import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.apache.commons.io.FileUtils;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApiTestData {

    public static final String testDataDir = ApiTestData.class.getClassLoader().getResource("./").getPath()+"../../../../../testdata/";
    private static String suffix=".json",encode="UTF8";

    protected final static String[] NODE_IDS = new String[]{
        "0x37771debfe76b9335b4e6ae96d7a611cc82a57373c73f0381822467389ae2b521325b755aacc71a66f26821bb83e231e0a87ed0e92d3e1a97af5963eb87063b",
        "0x6ddd600a544ae4cfd5b4f2b0879afce79234f9755e70fb16b0385b9094a161e328a20074044dcc7b80ea50b5929ad38417c7e3e2d550686250ed40f4f98f3de",
        "0xee16e3a652fff1092c935d6b399cabfa69f4bb16a7cd06fe2d7907c50b682efcd76635dcbe5c6281943657478e897a8ed03c5577d3f817c15c8eb9eeb50bd21",
        "0xb70e884cfad9443898ba0e28a45c0e14332cf46dfbb8a9a07983b909d99cd0586af2cc5ff579b4352170e42320a254cb57b84eb97d7be86a1bd48a058683033",
        "0xdb861efe5974899e138d79c446567f7687184c0d76a20a9c4f9ed4b68f62fa15ae6a1adfd70cd57b870ef969f4fe7ef4225ecf64de3732389523dee87ed177e",
        "0x84b1daf160bcdbdce5cac0779eacd5aae2ff94dbd01ae86dd21d8307238687a78a5bcf9e446779fe3bffd206e24f43da5905a5fa4b80786beb6bea44447a975",
        "0x1d7f364569aafa1520ceb94181ff87d63c3e8354e192d17ac4535d25cd41d857fa3fc7f388648bf092cebf5839cf93ac97e2e04d886cd31b858bb274f44da50"
    };
    protected static class Org{
        public String name;
        public String website;
        public Org(String name,String website){
            this.name = name;
            this.website = website;
        }
    }
    protected final static Org[] ORGS = new Org[]{
            new Org("BAIDU"   ,"http://www.baidu.com/"),
            new Org("ALIBABA" ,"http://www.alibaba.com/"),
            new Org("TENCENT" ,"http://www.tencent.com/"),
            new Org("MEITUAN" ,"http://www.meituan.com/"),
            new Org("TOUTIAO" ,"http://www.toutiao.com/"),
            new Org("MOBIKE"  ,"http://www.mobike.com/"),
            new Org("OFO"     ,"http://www.ofo.com/"),
            new Org("DIDI"    ,"http://www.didi.com/"),
            new Org("JINGDONG","http://www.jingdong.com/"),
            new Org("XUNLEI"  ,"http://www.xunlei.com/")
    };

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

    private static String[] dataFile = {
            "transaction",
            "blockChainConfig",
            "networkstat",
            "address",
            "node",
            "block",
            "erc20Token",
            "erc20TokenDetail",
            "erc20TokenAddressRel",
            "erc20TokenTransfer"
    };
    protected List<Transaction> transactionList = Collections.emptyList();
    protected BlockChainConfig blockChainConfig = new BlockChainConfig();
    protected List<NetworkStat> networkStatList = new ArrayList<>();
    protected List<Address> addressList = Collections.emptyList();
    protected List<Node> nodeList = new ArrayList<>();
    protected List<Block> blockList = Collections.emptyList();

    @Before
    public void init() {
        Arrays.asList(dataFile).forEach(fileName -> {
            try {
                File data = new File(testDataDir + fileName + suffix);
                String content = FileUtils.readFileToString(data, encode);
                switch (fileName) {
                    case "transaction":
                        this.transactionList = JSON.parseArray(content, Transaction.class);
                        break;
                    case "blockChainConfig":
                        this.blockChainConfig = JSON.parseObject(content, BlockChainConfig.class);
                        break;
                    case "networkstat":
                        this.networkStatList = JSON.parseArray(content, NetworkStat.class);
                        break;
                    case "address":
                        this.addressList = JSON.parseArray(content, Address.class);
                        break;
                    case "node":
                        this.nodeList = JSON.parseArray(content, Node.class);
                        break;
                    case "block":
                        this.blockList = JSON.parseArray(content, Block.class);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
