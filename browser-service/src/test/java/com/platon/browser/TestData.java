package com.platon.browser;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.*;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.apache.commons.io.FileUtils;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestData {
	public static final String testDataDir = TestData.class.getClassLoader().getResource("./").getPath()+"../../../testdata/";
	private static String prefix = "data/",suffix=".json",encode="UTF8";
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
    protected List<Erc20Token> erc20Tokens = Collections.emptyList();
    protected List<Erc20TokenDetailWithBLOBs> erc20TokenDetails = Collections.emptyList();
    protected List<ESTokenTransferRecord> esTokenTransferRecords = Collections.emptyList();
    protected List<Erc20TokenAddressRel> erc20TokenAddressRels = Collections.emptyList();

    @Before
    public void init() {
        Arrays.asList(dataFile).forEach(fileName -> {
            try {
                URL url = TestData.class.getClassLoader().getResource(prefix + fileName + suffix);
                String path = url.getPath();
                String content = FileUtils.readFileToString(new File(path), encode);

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
                    case "erc20Token":
                        this.erc20Tokens = JSON.parseArray(content, Erc20Token.class);
                        break;
                    case "erc20TokenDetail":
                        this.erc20TokenDetails = JSON.parseArray(content, Erc20TokenDetailWithBLOBs.class);
                        break;
                    case "erc20TokenAddressRel":
                        this.erc20TokenAddressRels = JSON.parseArray(content, Erc20TokenAddressRel.class);
                        break;
                    case "erc20TokenTransfer":
                        this.esTokenTransferRecords = JSON.parseArray(content, ESTokenTransferRecord.class);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
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
