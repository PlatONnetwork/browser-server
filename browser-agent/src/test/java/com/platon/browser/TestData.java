package com.platon.browser;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.web3j.platon.bean.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @description: 测试数据
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 15:11:37
 **/
@Slf4j
public class TestData {
    private static String prefix = "data/",suffix=".json",encode="UTF8";
    private static String[] dataFile = {
            "node",
            "block",
            "transaction",
            "staking",
            "delegation",
            "verifier",
            "validator",
            "candidate",
            "address",
            "proposal",
            "blockChainConfig"
    };

    protected List<CustomNode> nodeList= Collections.emptyList();
    protected List<CollectionBlock> blockList= Collections.emptyList();
    protected List<CollectionTransaction> transactionList= Collections.emptyList();
    protected List<CustomStaking> stakingList= Collections.emptyList();
    protected List<CustomDelegation> delegationList= Collections.emptyList();
    protected List<CustomProposal> proposalList = Collections.emptyList();
    protected List<Node> verifierList = new ArrayList<>();
    protected List<Node> validatorList = new ArrayList<>();
    protected List<Node> candidateList = new ArrayList<>();
    protected List<CustomAddress> addressList= Collections.emptyList();
    protected BlockChainConfig blockChainConfig = new BlockChainConfig();

    @Before
    public void init(){
        Arrays.asList(dataFile).forEach(fileName->{
            try {
                URL url = TestBase.class.getClassLoader().getResource(prefix+fileName+suffix);
                String path = url.getPath();
                String content = FileUtils.readFileToString(new File(path),encode);

                switch (fileName){
                    case "node":
                        nodeList = JSON.parseArray(content,CustomNode.class);
                        break;
                    case "block":
                        blockList = JSON.parseArray(content,CollectionBlock.class);
                        break;
                    case "transaction":
                        transactionList = JSON.parseArray(content,CollectionTransaction.class);
                        break;
                    case "staking":
                        stakingList = JSON.parseArray(content, CustomStaking.class);
                        break;
                    case "delegation":
                        delegationList = JSON.parseArray(content, CustomDelegation.class);
                        break;
                    case "verifier":
                        List<Node> verList = JSON.parseArray(content,Node.class);
                        verifierList.addAll(verList);
                        break;
                    case "validator":
                        List<Node> valList = JSON.parseArray(content,Node.class);
                        validatorList.addAll(valList);
                        break;
                    case "candidate":
                        List<Node> canList = JSON.parseArray(content,Node.class);
                        candidateList.addAll(canList);
                        break;
                    case "address":
                        addressList = JSON.parseArray(content, CustomAddress.class);
                        break;
                    case "proposal":
                        proposalList = JSON.parseArray(content,CustomProposal.class);
                        break;
                    case "blockChainConfig":
                        blockChainConfig = JSON.parseObject(content,BlockChainConfig.class);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
