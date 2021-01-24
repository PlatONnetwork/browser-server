package com.platon.browser.service;

import com.alaya.bech32.Bech32;
import com.alaya.crypto.Keys;
import com.alaya.parameters.NetworkParameters;
import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.elasticsearch.dto.*;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.utils.HexUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Slf4j
@Data
@Service
public class DataGenService {
    private static final Random random = new Random();

    private static final List<String> PROPOSAL_HASH = new ArrayList<>();
    private static final List<String> NODE_IDS = new ArrayList<>();
    private static final List<String> ADDRESS = new ArrayList<>();

    @Autowired
    private NodeMapper nodeMapper;

    static {
        PROPOSAL_HASH.add(HexUtil.prefix(DigestUtils.sha1Hex(UUID.randomUUID().toString())));
        NODE_IDS.add(HexUtil.prefix(DigestUtils.sha512Hex(UUID.randomUUID().toString())));
        ADDRESS.add(HexUtil.prefix(DigestUtils.sha1Hex(UUID.randomUUID().toString())));
    }

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
    private Status randomStatus(){
        Status status = STATUSES_ARR[random.nextInt(STATUSES_ARR.length)];
        return status;
    }

    private String randomNodeId(){
        String nodeId = NODE_IDS.get(random.nextInt(NODE_IDS.size()));
        return nodeId;
    }

    private String randomAddress(){
        String address = ADDRESS.get(random.nextInt(ADDRESS.size()));
        return address;
    }

    private String rAddress() {
    	try {
			return Bech32.addressEncode(NetworkParameters.Hrp.ATX.getHrp(), Keys.getAddress(Keys.createEcKeyPair()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }

    private String rMockAddress() {
        try {
            return NetworkParameters.Hrp.ATX.getHrp() + "0000000"
                    + UUID.randomUUID().toString().replace("-","");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String randomProposalHash(){
        String hash = PROPOSAL_HASH.get(random.nextInt(PROPOSAL_HASH.size()));
        return hash;
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
    private String rewardStr;
    private String estimateStr;
    private String slashStr;
    private String erc20TokenStr;
    private String esTokenTransferRecordStr;

    private NetworkStat networkStat;
    private List<Config> configList;
    private String transferTxStr;
    private String tokenTransferTxStr;


    @Value("${platon.txCountPerBlock}")
    private long txCountPerBlock;

    @PostConstruct
    private void init() {
        String dirPath = System.getProperty("user.dir")+File.separator+"template-data";
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
                        List<Transaction> transactionList = JSON.parseArray(content,Transaction.class);
                        transactionList.forEach(tx->{
                            if(tx.getTypeEnum()==Transaction.TypeEnum.TRANSFER){
                                transferTxStr=JSON.toJSONString(tx);
                            }
                            if (tx.getTypeEnum() == Transaction.TypeEnum.ERC20_CONTRACT_EXEC) {
                                tokenTransferTxStr = JSON.toJSONString(tx);
                            }
                        });
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
                    case "delegatinReward.json":
                        rewardStr=content;
                        break;
                    case "slash.json":
                        slashStr=content;
                        break;
                    case "gasEstimate.json":
                    	estimateStr=content;
                        break;
                    case "erc20Token.json":
                        erc20TokenStr = content;
                        break;
                    case "tokenTransfer.json":
                        esTokenTransferRecordStr = content;
                        break;

                }
            } catch (IOException e) {
                log.error("读取文件内容出错",e);
            }
        });

        List<Node> nodeList = nodeMapper.selectByExample(null);
        nodeList.forEach(node->NODE_IDS.add(node.getNodeId()));
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

        for(int i=transactionList.size();i<txCountPerBlock;i++){
            //Transaction tx = JSON.parseObject(transferTxStr,Transaction.class);
            Transaction tx = JSON.parseObject(tokenTransferTxStr, Transaction.class);
            transactionList.add(tx);
        }

        br.setTransactionList(transactionList);

        List<NodeOpt> nodeOptList = JSON.parseArray(nodeOptListStr,NodeOpt.class);
        br.setNodeOptList(nodeOptList);

        String nodeId = randomNodeId();
        br.buildAssociation(blockNumber,nodeId,++nodeOptMaxId,addressReusedTimes);

        return br;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public StakeResult getStakeResult(Transaction tx) throws BlockNumberException {
        Node node = JSON.parseObject(nodeStr,Node.class);
        node.setStakingBlockNum(tx.getNum());
        node.setNodeId(HexUtil.prefix(DigestUtils.sha512Hex(UUID.randomUUID().toString())));
        node.setNodeIcon(node.getNodeId().substring(0,6));
        node.setNodeName(node.getNodeId().substring(7,10));

        Status status = randomStatus();
        node.setStatus(status.status);
        node.setIsConsensus(status.isConsensus);
        node.setIsSettle(status.isSettle);
        node.setIsInit(status.isInit);
        node.setStakingTxIndex(tx.getIndex());
        node.setStakingAddr(randomAddress());
        node.setStatVerifierTime(EpochUtil.getEpoch(BigInteger.valueOf(tx.getNum()),BigInteger.TEN).intValue());

        if(ADDRESS.size()<5000&&!ADDRESS.contains(tx.getFrom())){
            ADDRESS.add(tx.getFrom());
        }

        Staking staking = JSON.parseObject(stakingStr,Staking.class);
        BeanUtils.copyProperties(node,staking);

        StakeResult stakeResult = new StakeResult();
        stakeResult.setNode(node);
        stakeResult.setStaking(staking);

        NODE_IDS.add(node.getNodeId());
        return stakeResult;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Delegation getDelegation(Transaction tx){
        Delegation copy = JSON.parseObject(delegationStr, Delegation.class);
        copy.setStakingBlockNum(tx.getNum());
        copy.setNodeId(randomNodeId());
        copy.setDelegateAddr(rAddress());
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Proposal getProposal(Transaction tx){
        Proposal copy = JSON.parseObject(proposalStr, Proposal.class);
        copy.setHash(tx.getHash());
        copy.setNodeId(randomNodeId());
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
    public Vote getVote(Transaction tx){
        Vote copy = JSON.parseObject(voteStr, Vote.class);
        copy.setNodeId(randomNodeId());
        copy.setNodeName(copy.getNodeId().substring(7,10));
        copy.setHash(tx.getHash());
        copy.setProposalHash(randomProposalHash());
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public RpPlan getRpPlan(Transaction tx) throws BlockNumberException {
        RpPlan copy = JSON.parseObject(rpPlanStr, RpPlan.class);
        copy.setId(null);
        copy.setEpoch(EpochUtil.getEpoch(BigInteger.valueOf(tx.getNum()),BigInteger.TEN));
        copy.setAddress(randomAddress());
        return copy;
    }
    
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public DelegationReward getReward(Transaction tx) throws BlockNumberException {
    	DelegationReward copy = JSON.parseObject(rewardStr, DelegationReward.class);
    	copy.setHash(tx.getHash());
        copy.setAddr(randomAddress());
        return copy;
    }
    
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public GasEstimate getEstimate(Transaction tx) throws BlockNumberException {
    	GasEstimate copy = JSON.parseObject(estimateStr, GasEstimate.class);
    	try {
			copy.setAddr(rAddress());
		} catch (Exception e) {
			e.printStackTrace();
		} 
        
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Slash getSlash(Transaction tx){
        Slash copy = JSON.parseObject(slashStr, Slash.class);
        copy.setHash(tx.getHash());
        copy.setBenefitAddr(randomAddress());
        copy.setNodeId(randomNodeId());
        return copy;
    }
//
//    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
//    public Erc20Token getErc20Token(Transaction tx){
//        Erc20Token copy = JSON.parseObject(erc20TokenStr, Erc20Token.class);
//        copy.setTxHash(tx.getHash());
//        copy.setAddress(rAddress());
//        copy.setName("Token-" + tx.getSeq());
//        copy.setSymbol(tx.getSeq() + "");
//        copy.setDecimal(18);
//        copy.setTotalSupply(new BigDecimal("10000000000000000000000000"));
//        copy.setCreator(tx.getFrom());
//        copy.setType("E");
//        copy.setStatus(1);
//        copy.setTxCount(100);
//        copy.setBlockTimestamp(tx.getTime());
//        copy.setCreateTime(new Date());
//        return copy;
//    }
//
//    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
//    public OldErcTx getESTokenTransferRecord(Transaction tx){
//        OldErcTx transferRecord = JSON.parseObject(esTokenTransferRecordStr, OldErcTx.class);
//        transferRecord.setSeq(tx.getSeq());
//        transferRecord.setBTime(tx.getTime());
//        transferRecord.setBn(tx.getNum());
//        transferRecord.setContract(tx.getContractAddress());
//        transferRecord.setCtime(new Date());
//        transferRecord.setDecimal(17);
//        transferRecord.setFrom(tx.getFrom());
//        transferRecord.setHash(tx.getHash());
//        transferRecord.setName("Token-" + tx.getSeq());
//        transferRecord.setSymbol("" + tx.getSeq());
//        transferRecord.setTValue("100000000000000000000");
//        transferRecord.setResult(1);
//        transferRecord.setTto(tx.getTo());
//        transferRecord.setTxFee(tx.getCost());
//        return transferRecord;
//    }
//
//    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
//    public List<Erc20TokenAddressRel> getErc20TokenAddressRel(Erc20Token token, int addressCountPerBlock){
//        List<Erc20TokenAddressRel> erc20TokenAddressRelList = new ArrayList<>();
//        for (int i = 0; i < addressCountPerBlock; i++) {
//            Erc20TokenAddressRel rel = Erc20TokenAddressRel.builder()
//                    .contract(token.getAddress())
//                    .address(rMockAddress())
//                    .balance(new BigDecimal("10000000000000000000"))
//                    .name(token.getName()).symbol(token.getSymbol())
//                    .decimal(token.getDecimal()).txCount(i)
//                    .totalSupply(token.getTotalSupply()).updateTime(new Date())
//                    .build();
//            erc20TokenAddressRelList.add(rel);
//        }
//        return erc20TokenAddressRelList;
//    }
}
