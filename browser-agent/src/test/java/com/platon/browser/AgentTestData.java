package com.platon.browser;

import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.BlockBean;
import com.platon.browser.bean.NodeBean;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.ReceiptResult;
import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.web3j.platon.bean.Node;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * @description: 测试数据
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 15:11:37
 **/
@Slf4j
public class AgentTestData {
    private static String prefix = "data/",suffix=".json",encode="UTF8";
    private static String[] dataFile = {
            "node",
            "block",
            "raw-block",
            "transaction",
            "receipts",
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
    protected List<PlatonBlock.Block> rawBlockList= new ArrayList<>();
    protected List<CollectionTransaction> transactionList= Collections.emptyList();
    protected List<ReceiptResult> receiptResultList= Collections.emptyList();
    protected List<CustomStaking> stakingList= Collections.emptyList();
    protected List<CustomDelegation> delegationList= Collections.emptyList();
    protected List<CustomProposal> proposalList = Collections.emptyList();
    protected List<Node> verifierList = new ArrayList<>();
    protected List<Node> validatorList = new ArrayList<>();
    protected List<Node> candidateList = new ArrayList<>();
    protected List<CustomAddress> addressList= Collections.emptyList();
    protected BlockChainConfig blockChainConfig = new BlockChainConfig();

    protected Map<Long,PlatonBlock.Block> rawBlockMap = new HashMap<>();
    protected Map<Long, ReceiptResult> receiptResultMap = new HashMap<>();

    @Before
    public void init(){
        PlatOnClient.LOG_DECODE_EXECUTOR = Executors.newFixedThreadPool(100);
        Arrays.asList(dataFile).forEach(fileName->{
            try {
                URL url = AgentTestBase.class.getClassLoader().getResource(prefix+fileName+suffix);
                String path = url.getPath();
                String content = FileUtils.readFileToString(new File(path),encode);

                switch (fileName){
                    case "node":
                        nodeList = JSON.parseArray(content,CustomNode.class);
                        break;
                    case "block":
                        blockList = JSON.parseArray(content,CollectionBlock.class);
                        break;
                    case "raw-block":
                        List<BlockBean> blockBeans = JSON.parseArray(content, BlockBean.class);
                        blockBeans.forEach(b->{
                            PlatonBlock.Block block = new PlatonBlock.Block();
                            BeanUtils.copyProperties(b,block);
                            rawBlockList.add(block);
                            rawBlockMap.put(block.getNumber().longValue(),block);
                        });
                        break;
                    case "transaction":
                        transactionList = JSON.parseArray(content,CollectionTransaction.class);
                        break;
                    case "receipts":
                        receiptResultList = JSON.parseArray(content,ReceiptResult.class);
                        receiptResultList.forEach(rr->{
                            try {
                                rr.resolve(rr.getResult().get(0).getBlockNumber());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            receiptResultMap.put(rr.getResult().get(0).getBlockNumber(),rr);
                        });
                        break;
                    case "staking":
                        stakingList = JSON.parseArray(content, CustomStaking.class);
                        break;
                    case "delegation":
                        delegationList = JSON.parseArray(content, CustomDelegation.class);
                        break;
                    case "verifier":
                        List<NodeBean> verList = JSON.parseArray(content,NodeBean.class);
                        verifierList.addAll(verList);
                        break;
                    case "validator":
                        List<NodeBean> valList = JSON.parseArray(content,NodeBean.class);
                        validatorList.addAll(valList);
                        break;
                    case "candidate":
                        List<NodeBean> canList = JSON.parseArray(content,NodeBean.class);
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
