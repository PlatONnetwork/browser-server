package com.platon.browser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;

public class TestData {
	public static final String testDataDir = TestData.class.getClassLoader().getResource("./").getPath()+"../../../testdata/";
	private static String prefix = "data/",suffix=".json",encode="UTF8";
    private static String[] dataFile = {
            "transaction",
            "blockChainConfig",
            "networkstat",
            "address",
            "node",
            "block"
    };
    protected List<Transaction> transactionList= Collections.emptyList();
    protected BlockChainConfig blockChainConfig = new BlockChainConfig();
    protected List<NetworkStat> networkStatList= new ArrayList<>();
    protected List<Address> addressList= Collections.emptyList();
    protected List<Node> nodeList = new ArrayList<>();
    protected List<Block> blockList= Collections.emptyList();
    @Before
    public void init(){
        Arrays.asList(dataFile).forEach(fileName->{
            try {
                URL url = TestData.class.getClassLoader().getResource(prefix+fileName+suffix);
                String path = url.getPath();
                String content = FileUtils.readFileToString(new File(path),encode);

                switch (fileName){
                    case "transaction":
                        transactionList = JSON.parseArray(content,Transaction.class);
                        break;
                    case "blockChainConfig":
                        blockChainConfig = JSON.parseObject(content,BlockChainConfig.class);
                        break;
                    case "networkstat":
                        networkStatList = JSON.parseArray(content,NetworkStat.class);
                        break;
                    case "address":
                        addressList = JSON.parseArray(content, Address.class);
                        break;
                    case "node":
                        nodeList = JSON.parseArray(content,Node.class);
                        break;
                    case "block":
                    	blockList = JSON.parseArray(content,Block.class);
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
