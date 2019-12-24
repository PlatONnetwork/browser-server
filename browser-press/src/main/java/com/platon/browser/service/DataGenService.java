package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.*;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.EpochUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
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
    private static final List<String> NODE_ID = new ArrayList<>();
    private static final List<String> ADDRESS = new ArrayList<>();

    private static final String [] NODE_IDS={
            "0x459d199acb83bfe08c26d5c484cbe36755b53b7ae2ea5f7a5f0a8f4c08e843b51c4661f3faa57b03b710b48a9e17118c2659c5307af0cc5329726c13119a6b85",
            "0x4fcc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba4150f",
            "0x53242dec8799f3f4f8882b109e1a3ebb4aa8c2082d000937d5876365414150c5337aa3d3d41ead1ac873f4e0b19cb9238d2995598207e8d571f0bd5dd843cdf3",
            "0xef97cb9caf757c70e9aca9062a9f6607ce89c3e7cac90ffee56d3fcffffa55aebd20b48c0db3924438911fd1c88c297d6532b434c56dbb5d9758f0794c6841dc"
    };

    static class Status{
        public Status(int status, int isConsensus, int isSettle, int isInit) {
            this.status = status;
            this.isConsensus = isConsensus;
            this.isSettle = isSettle;
            this.isInit = isInit;
        }
        private int status;
        private int isConsensus;
        private int isSettle;
        private int isInit;
    }

    private static final Status[] STATUSES_ARR = {
            new Status(1,1,1,2),
            new Status(1,1,1,2),
            new Status(2,1,1,2),
            new Status(2,2,2,2),
            new Status(3,2,2,2),
            new Status(3,1,2,2),
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
    private String rpPlanStr;
    private String slashStr;

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
                    case "rpplan.json":
                        rpPlanStr=content;
                        break;
                    case "slash.json":
                        slashStr=content;
                        break;
                }
            } catch (IOException e) {
                log.error("读取文件内容出错",e);
            }
        });
    }

    private long nodeOptMaxId = 0L;

    @Value("${platon.addressReusedTimes}")
    private int addressReusedTimes;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public BlockResult getBlockResult(BigInteger blockNumber){
        BlockResult br = new BlockResult();

        Block block = JSON.parseObject(blockListStr,Block.class);
        br.setBlock(block);

        List<Transaction> transactionList = JSON.parseArray(transactionListStr,Transaction.class);
        br.setTransactionList(transactionList);

        List<NodeOpt> nodeOptList = JSON.parseArray(nodeOptListStr,NodeOpt.class);
        br.setNodeOptList(nodeOptList);

        String nodeId = randomNodeId();
        br.buildAssociation(blockNumber,nodeId,++nodeOptMaxId,addressReusedTimes);

        return br;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public StakeResult getStakeResult(Transaction tx,Long totalCount) throws BlockNumberException {
        Node node = JSON.parseObject(nodeStr,Node.class);
        node.setStakingBlockNum(tx.getNum());
        node.setNodeId(DigestUtils.sha512Hex(totalCount.toString()));
        node.setNodeIcon(node.getNodeId().substring(0,6));
        node.setNodeName(node.getNodeId().substring(7,10));

        Status status = STATUSES_ARR[random.nextInt(STATUSES_ARR.length)];
        node.setStatus(status.status);
        node.setIsConsensus(status.isConsensus);
        node.setIsSettle(status.isSettle);
        node.setIsInit(status.isInit);
        node.setStakingTxIndex(tx.getIndex());
        node.setStakingAddr(DigestUtils.sha1Hex(totalCount.toString()));
        node.setStatVerifierTime(EpochUtil.getEpoch(BigInteger.valueOf(tx.getNum()),BigInteger.TEN).intValue());

        if(ADDRESS.size()<5000&&!ADDRESS.contains(tx.getFrom())){
            ADDRESS.add(tx.getFrom());
        }

        Staking staking = JSON.parseObject(stakingStr,Staking.class);
        BeanUtils.copyProperties(node,staking);

        StakeResult stakeResult = new StakeResult();
        stakeResult.setNode(node);
        stakeResult.setStaking(staking);
        return stakeResult;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Delegation getDelegation(Transaction tx,Long totalCount){
        Delegation copy = JSON.parseObject(delegationStr, Delegation.class);
        copy.setStakingBlockNum(tx.getNum());
        copy.setNodeId(DigestUtils.sha512Hex(totalCount.toString()));
        copy.setDelegateAddr(ADDRESS.get(random.nextInt(ADDRESS.size())));
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
        NODE_ID.add(copy.getNodeId());
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

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public RpPlan getRpPlan(Transaction tx,Long totalCount) throws BlockNumberException {
        RpPlan copy = JSON.parseObject(rpPlanStr, RpPlan.class);
        copy.setId(null);
        copy.setEpoch(EpochUtil.getEpoch(BigInteger.valueOf(tx.getNum()),BigInteger.TEN).longValue());
        copy.setAddress(ADDRESS.get(random.nextInt(ADDRESS.size())));
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Slash getSlash(Transaction tx,Long totalCount){
        Slash copy = JSON.parseObject(slashStr, Slash.class);
        copy.setHash(tx.getHash());
        copy.setBenefitAddr(ADDRESS.get(random.nextInt(ADDRESS.size())));
        copy.setNodeId(NODE_ID.get(random.nextInt(NODE_ID.size())));
        return copy;
    }
}
