package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.*;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Data
@Service
public class DataGenService {

    private static final List<String> PROPOSAL_HASH = new ArrayList<>();

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

    private String blockListStr;
    private String transactionListStr;
    private String nodeOptListStr;
    private String nodeStr;
    private String stakingStr;
    private String delegationStr;
    private String proposalStr;
    private String voteStr;

    private NetworkStat networkStat;
    private List<Config> configList;

    @PostConstruct
    private void init() {
        URL dirUrl = DataGenService.class.getClassLoader().getResource("data");
        String dirPath = dirUrl.getPath();
        File dir = new File(dirPath);
        Arrays.asList(dir.listFiles()).forEach(file -> {
            try {
                String content = FileUtils.readFileToString(file,"UTF-8");
                switch (file.getName()){
                    case "block.json":
                        blockListStr = content;
                        break;
                    case "transaction.json":
                        transactionListStr = content;
                        break;
                    case "nodeopt.json":
                        nodeOptListStr = content;
                        break;
                    case "node.json":
                        nodeStr=content;
                        break;
                    case "staking.json":
                        stakingStr=content;
                        break;
                    case "networkstat.json":
                        networkStat=JSON.parseObject(content, NetworkStat.class);
                        break;
                    case "delegation.json":
                        delegationStr=content;
                        break;
                    case "config.json":
                        configList=JSON.parseArray(content,Config.class);
                        break;
                    case "proposal.json":
                        proposalStr=content;
                        break;
                    case "vote.json":
                        voteStr=content;
                        break;
                }
            } catch (IOException e) {
                log.error("读取文件内容出错",e);
            }
        });
    }


    @Value("${disruptor.queue.block.gen-size}")
    private int blockGenSize;

    private long nodeOptMaxId = 0L;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public BlockResult getBlock(BigInteger blockNumber){
        BlockResult br = new BlockResult();

        Block block = JSON.parseObject(blockListStr,Block.class);
        br.setBlock(block);

        List<Transaction> transactionList = JSON.parseArray(transactionListStr,Transaction.class);
        br.setTransactionList(transactionList);

        List<NodeOpt> nodeOptList = JSON.parseArray(nodeOptListStr,NodeOpt.class);
        br.setNodeOptList(nodeOptList);

        String nodeId = randomNodeId();
        br.buildAssociation(blockNumber,nodeId,++nodeOptMaxId);

        return br;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Node getNode(Transaction tx,Long totalCount){
        Node copy = JSON.parseObject(nodeStr,Node.class);
        copy.setStakingBlockNum(tx.getNum());
        copy.setNodeId(DigestUtils.sha512Hex(totalCount.toString()));
        copy.setNodeIcon(copy.getNodeId().substring(0,6));
        copy.setNodeName(copy.getNodeId().substring(7,10));
        copy.setStakingTxIndex(tx.getIndex());
        copy.setStakingAddr(DigestUtils.sha1Hex(totalCount.toString()));
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Staking getStaking(Transaction tx,Long totalCount){
        Staking copy = JSON.parseObject(stakingStr,Staking.class);
        copy.setStakingBlockNum(tx.getNum());
        copy.setNodeId(DigestUtils.sha512Hex(totalCount.toString()));
        copy.setNodeIcon(copy.getNodeId().substring(0,6));
        copy.setNodeName(copy.getNodeId().substring(7,10));
        copy.setStakingTxIndex(tx.getIndex());
        copy.setStakingAddr(DigestUtils.sha1Hex(totalCount.toString()));
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Delegation getDelegation(Transaction tx,Long totalCount){
        Delegation copy = JSON.parseObject(delegationStr, Delegation.class);
        copy.setStakingBlockNum(tx.getNum());
        copy.setNodeId(DigestUtils.sha512Hex(totalCount.toString()));
        copy.setDelegateAddr(tx.getTo());
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Proposal getProposal(Transaction tx,Long totalCount){
        Proposal copy = JSON.parseObject(proposalStr, Proposal.class);
        copy.setHash(tx.getHash());
        copy.setNodeId(DigestUtils.sha512Hex(totalCount.toString()));
        copy.setNodeName(copy.getNodeId().substring(7,10));
        copy.setName(copy.getNodeId().substring(0,6));
        // 1文本提案,2升级提案,3参数提案,4取消提案
        switch (tx.getTypeEnum()){
            case PROPOSAL_TEXT:
                copy.setType(1);
                break;
            case PROPOSAL_CANCEL:
                copy.setType(4);
                break;
            case PROPOSAL_PARAMETER:
                copy.setType(3);
                break;
            case PROPOSAL_UPGRADE:
                copy.setType(2);
                break;
        }
        copy.setBlockNumber(tx.getNum());
        PROPOSAL_HASH.add(copy.getHash());
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Vote getVote(Transaction tx,Long totalCount){
        Vote copy = JSON.parseObject(voteStr, Vote.class);
        copy.setNodeId(DigestUtils.sha512Hex(totalCount.toString()));
        copy.setNodeName(copy.getNodeId().substring(7,10));
        copy.setHash(tx.getHash());
        copy.setProposalHash(PROPOSAL_HASH.get(random.nextInt(PROPOSAL_HASH.size())));
        return copy;
    }
}
