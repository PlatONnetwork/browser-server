package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.sdk.contracts.ppos.dto.resp.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class DataGenService {

    private static final String [] NODE_IDS={
            "0x459d199acb83bfe08c26d5c484cbe36755b53b7ae2ea5f7a5f0a8f4c08e843b51c4661f3faa57b03b710b48a9e17118c2659c5307af0cc5329726c13119a6b85",
            "0x4fcc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba4150f",
            "0x53242dec8799f3f4f8882b109e1a3ebb4aa8c2082d000937d5876365414150c5337aa3d3d41ead1ac873f4e0b19cb9238d2995598207e8d571f0bd5dd843cdf3",
            "0xef97cb9caf757c70e9aca9062a9f6607ce89c3e7cac90ffee56d3fcffffa55aebd20b48c0db3924438911fd1c88c297d6532b434c56dbb5d9758f0794c6841dc"
    };

    private static final Random random = new Random();
    public static String randomNodeId(){
        return NODE_IDS[random.nextInt(NODE_IDS.length)];
    }


    private static List<Block> blockList;
    private static List<Transaction> transactionList;
    private static List<NodeOpt> nodeOptList;

    static {
        URL dirUrl = DataGenService.class.getClassLoader().getResource("data");
        String dirPath = dirUrl.getPath();
        File dir = new File(dirPath);
        Arrays.asList(dir.listFiles()).forEach(file -> {
            try {
                String content = FileUtils.readFileToString(file,"UTF-8");
                switch (file.getName()){
                    case "block.json":
                        blockList = JSON.parseArray(content,Block.class);
                        break;
                    case "transaction.json":
                        transactionList = JSON.parseArray(content,Transaction.class);
                        break;
                    case "nodeopt.json":
                        nodeOptList = JSON.parseArray(content,NodeOpt.class);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Value("${disruptor.queue.block.gen-size}")
    private int blockGenSize;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public BlockResult get(BigInteger blockNumber){
        BlockResult br = new BlockResult();
        br.setBlock(blockList.get(0));
        br.setTransactionList(transactionList);
        br.setNodeOptList(nodeOptList);

        String nodeId = randomNodeId();
        br.buildAssociation(blockNumber,nodeId);
        return br;
    }

}
