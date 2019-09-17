package com.platon.browser;

import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.NodeBean;
import com.platon.browser.bean.TransactionBean;
import com.platon.browser.dto.*;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.exception.CacheConstructException;
import com.platon.browser.exception.NoSuchBeanException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.web3j.platon.bean.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/7 16:35
 * @Description:
 */
public class TestBase {
    private static Logger logger = LoggerFactory.getLogger(TestBase.class);
    private static String prefix = "data/",suffix=".json",encode="UTF8";
    private static String[] dataFile = {
            "node",
            "block",
            "transaction",
            "staking",
            "delegation",
            "unDelegation",
            "verifier",
            "validator",
            "candidate",
            "address",
            "proposal"
    };

    public static NodeCache NODE_CACHE = new NodeCache();
    public static List<CustomNode> nodes= Collections.emptyList();
    public static List<CustomBlock> blocks= Collections.emptyList();
    public static List<TransactionBean> transactions= Collections.emptyList();
    public static List<CustomStaking> stakings= Collections.emptyList();
    public static List<CustomDelegation> delegations= Collections.emptyList();
    public static List<CustomUnDelegation> unDelegations= Collections.emptyList();
    public static List<CustomProposal> proposals = Collections.emptyList();
    public static List<Node> verifiers= new ArrayList<>();
    public static List<Node> validators= new ArrayList<>();
    public static List<Node> candidates= new ArrayList<>();
    public static List<CustomAddress> addresses= Collections.emptyList();

    static {
        Arrays.asList(dataFile).forEach(fileName->{
            try {
                URL url = TestBase.class.getClassLoader().getResource(prefix+fileName+suffix);
                String path = url.getPath();
                String content = FileUtils.readFileToString(new File(path),encode);

                switch (fileName){
                    case "node":
                        nodes = JSON.parseArray(content,CustomNode.class);
                        break;
                    case "block":
                        blocks = JSON.parseArray(content,CustomBlock.class);
                        break;
                    case "transaction":
                        transactions = JSON.parseArray(content,TransactionBean.class);
                        break;
                    case "staking":
                        stakings = JSON.parseArray(content,CustomStaking.class);
                        break;
                    case "delegation":
                        delegations = JSON.parseArray(content,CustomDelegation.class);
                        break;
                    case "unDelegation":
                        unDelegations = JSON.parseArray(content,CustomUnDelegation.class);
                        break;
                    case "verifier":
                        List<NodeBean> verList = JSON.parseArray(content,NodeBean.class);
                        verifiers.addAll(verList);
                        break;
                    case "validator":
                        List<NodeBean> valList = JSON.parseArray(content,NodeBean.class);
                        validators.addAll(valList);
                        break;
                    case "candidate":
                        List<NodeBean> canList = JSON.parseArray(content,NodeBean.class);
                        candidates.addAll(canList);
                        break;
                    case "address":
                        addresses = JSON.parseArray(content,CustomAddress.class);
                        break;
                    case "proposal":
                        proposals = JSON.parseArray(content,CustomProposal.class);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Map<Long,List<CustomTransaction>> txMap = new HashMap<>();
        transactions.forEach(tx->{
            // 交易类型置空，tx_info置空
            tx.setTxType(null);
            tx.setTxInfo(null);
            List<CustomTransaction> txes = txMap.computeIfAbsent(tx.getBlockNumber(), k -> new ArrayList<>());
            txes.add(tx);
        });
        blocks.forEach(block -> {
            List<CustomTransaction> txes = txMap.get(block.getNumber());
            if(txes!=null) block.setTransactionList(txes);
        });

        try {
            NODE_CACHE.init(nodes,stakings,delegations,unDelegations);
        } catch (CacheConstructException e) {
            e.printStackTrace();
        }
        logger.info("测试数据加载完成！");
    }


}
