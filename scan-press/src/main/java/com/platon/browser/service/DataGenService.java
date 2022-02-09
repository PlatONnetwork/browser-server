package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.bech32.Bech32;
import com.platon.browser.PressApplication;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.elasticsearch.dto.*;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.CommonUtil;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.utils.HexUtil;
import com.platon.crypto.Keys;
import com.platon.parameters.NetworkParameters;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
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

    @Resource
    private NodeMapper nodeMapper;

    static {
        PROPOSAL_HASH.add(HexUtil.prefix(DigestUtils.sha1Hex(UUID.randomUUID().toString())));
        NODE_IDS.add(HexUtil.prefix(DigestUtils.sha512Hex(UUID.randomUUID().toString())));
        ADDRESS.add(HexUtil.prefix(DigestUtils.sha1Hex(UUID.randomUUID().toString())));
    }

    static class Status {

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

    private static final Status[] STATUSES_ARR = {new Status(1, 1, 1, 2), new Status(1, 1, 1, 2), new Status(2, 1, 1, 2), new Status(2, 2, 2, 2), new Status(3, 2, 2, 2), new Status(3, 1, 2, 2),};

    private Status randomStatus() {
        Status status = STATUSES_ARR[random.nextInt(STATUSES_ARR.length)];
        return status;
    }

    private String randomNodeId() {
        String nodeId = NODE_IDS.get(random.nextInt(NODE_IDS.size()));
        return nodeId;
    }

    private String randomAddress() {
        String address = ADDRESS.get(random.nextInt(ADDRESS.size()));
        return address;
    }

    private String rAddress() {
        try {
            return Bech32.addressEncode(NetworkParameters.getHrp(), Keys.getAddress(Keys.createEcKeyPair()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String rMockAddress() {
        try {
            return NetworkParameters.getHrp() + "0000000" + UUID.randomUUID().toString().replace("-", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String randomProposalHash() {
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

    private String erc721TokenStr;

    private String esTokenTransferRecordStr;

    private NetworkStat networkStat;

    private List<Config> configList;

    private String transferTxStr;

    private String tokenTransferTxStr;

    @Value("${platon.txCountPerBlock}")
    private long txCountPerBlock;

    @PostConstruct
    private void init() {
        String path = CommonUtil.isWin() ? "browser-press/template-data" : "template-data";
        String dirPath = System.getProperty("user.dir") + File.separator + path;
        File dir = new File(dirPath);
        Arrays.asList(dir.listFiles()).forEach(file -> {
            try {
                String content = FileUtils.readFileToString(file, "UTF-8");
                switch (file.getName()) {
                    case "block.json":
                        blockListStr = content;
                        break;
                    case "transaction.json":
                        transactionListStr = content;
                        List<Transaction> transactionList = JSON.parseArray(content, Transaction.class);
                        transactionList.forEach(tx -> {
                            if (tx.getTypeEnum() == Transaction.TypeEnum.TRANSFER) {
                                transferTxStr = JSON.toJSONString(tx);
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
                        nodeStr = content;
                        break;
                    case "staking.json":
                        stakingStr = content;
                        break;
                    case "networkstat.json":
                        networkStat = JSON.parseObject(content, NetworkStat.class);
                        break;
                    case "delegation.json":
                        delegationStr = content;
                        break;
                    case "config.json":
                        configList = JSON.parseArray(content, Config.class);
                        break;
                    case "proposal.json":
                        proposalStr = content;
                        break;
                    case "vote.json":
                        voteStr = content;
                        break;
                    case "rpplan.json":
                        rpPlanStr = content;
                        break;
                    case "delegatinReward.json":
                        rewardStr = content;
                        break;
                    case "slash.json":
                        slashStr = content;
                        break;
                    case "gasEstimate.json":
                        estimateStr = content;
                        break;
                    case "erc20Token.json":
                        erc20TokenStr = content;
                        break;
                    case "erc721Token.json":
                        erc721TokenStr = content;
                        break;
                    case "tokenTransfer.json":
                        esTokenTransferRecordStr = content;
                        break;

                }
            } catch (IOException e) {
                log.error("读取文件内容出错", e);
            }
        });

        List<Node> nodeList = nodeMapper.selectByExample(null);
        nodeList.forEach(node -> NODE_IDS.add(node.getNodeId()));
    }

    private long nodeOptMaxId = 0L;

    @Value("${platon.addressReusedTimes}")
    private int addressReusedTimes;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public BlockResult getBlockResult(BigInteger blockNumber, long nodeMaxCount) {
        BlockResult br = new BlockResult();

        Block block = JSON.parseObject(blockListStr, Block.class);
        br.setBlock(block);
        List<Transaction> transactionList = new ArrayList<>();
        for (int i = 1; i <= txCountPerBlock; i++) {
            Transaction tx = JSON.parseObject(tokenTransferTxStr, Transaction.class);
            tx.setHash(BlockResult.createNum("0x", 64, i));
            if (i <= 10) {
                tx.setType(Transaction.TypeEnum.ERC20_CONTRACT_CREATE.getCode());
                tx.setToType(Transaction.ToTypeEnum.ERC20_CONTRACT.getCode());
                tx.setContractType(ContractTypeEnum.ERC20_EVM.getCode());
            } else if (10 < i && i <= 20) {
                tx.setType(Transaction.TypeEnum.ERC20_CONTRACT_EXEC.getCode());
                tx.setToType(Transaction.ToTypeEnum.ERC20_CONTRACT.getCode());
            } else if (20 < i && i <= 30) {
                tx.setType(Transaction.TypeEnum.ERC721_CONTRACT_CREATE.getCode());
                tx.setToType(Transaction.ToTypeEnum.ERC721_CONTRACT.getCode());
                tx.setContractType(ContractTypeEnum.ERC721_EVM.getCode());
            } else if (30 < i && i <= 40) {
                tx.setType(Transaction.TypeEnum.ERC721_CONTRACT_EXEC.getCode());
                tx.setToType(Transaction.ToTypeEnum.ERC721_CONTRACT.getCode());
            } else if (40 < i && i <= 180) {
                //转账
                tx.setType(0);
            } else if (180 < i && i <= 182) {
                //发起质押
                tx.setType(BlockResult.getRandom(1000, 1003));
            } else if (182 < i && i <= 190) {
                //委托
                tx.setType(1004);
            } else if (190 < i && i <= 200) {
                tx.setType(1005);
            }
            tx.setContractAddress(CommonUtil.getRandomAddress(1));
            tx.setSeq(++PressApplication.currentTransferCount);
            transactionList.add(tx);
        }
        br.setTransactionList(transactionList);
        List<NodeOpt> nodeOptList = JSON.parseArray(nodeOptListStr, NodeOpt.class);
        br.setNodeOptList(nodeOptList);

        // String nodeId = randomNodeId();
        String nodeId = BlockResult.createNodeId(BlockResult.getRandom(1, (int) nodeMaxCount));
        br.buildAssociation(blockNumber, nodeId, ++nodeOptMaxId, addressReusedTimes);
        return br;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public StakeResult getStakeResult(Transaction tx, long currentNodeSum, int multiple) throws BlockNumberException {
        Node node = JSON.parseObject(nodeStr, Node.class);
        node.setStakingBlockNum(tx.getNum());
        long num = currentNodeSum;
        String nodeId = BlockResult.createNodeId(++num);
        node.setNodeId(nodeId);
        String hex = HexUtil.prefix(DigestUtils.sha512Hex(UUID.randomUUID().toString()));
        node.setNodeIcon(hex.substring(0, 6));
        node.setNodeName(hex.substring(7, 10));

        Status status = randomStatus();
        node.setStatus(status.status);
        node.setIsConsensus(status.isConsensus);
        BigInteger[] bigInteger = BigInteger.valueOf(num).divideAndRemainder(BigInteger.valueOf(2L));
        int isSettle = bigInteger[1].intValue() + 1;
        node.setIsSettle(isSettle);
        node.setIsInit(status.isInit);
        node.setStakingTxIndex(tx.getIndex());
        node.setStakingAddr(randomAddress());
        node.setStatVerifierTime(EpochUtil.getEpoch(BigInteger.valueOf(tx.getNum()), BigInteger.TEN).intValue());

        if (ADDRESS.size() < 5000 && !ADDRESS.contains(tx.getFrom())) {
            ADDRESS.add(tx.getFrom());
        }
        List<Staking> stakingList = new ArrayList<>();
        for (int i = 1; i <= multiple; i++) {
            Staking staking = JSON.parseObject(stakingStr, Staking.class);
            BeanUtils.copyProperties(node, staking);
            staking.setStakingBlockNum(staking.getStakingBlockNum() + i);
            staking.setNodeId(nodeId);
            staking.setStatus(i);
            stakingList.add(staking);
        }
        StakeResult stakeResult = new StakeResult();
        stakeResult.setNode(node);
        //stakeResult.setStaking(staking);
        stakeResult.setStakingList(stakingList);
        NODE_IDS.add(node.getNodeId());
        return stakeResult;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Delegation getDelegation(Transaction tx) {
        Delegation copy = JSON.parseObject(delegationStr, Delegation.class);
        if (copy == null) {
            System.out.println("打印结果为：" + 1111);
        }
        copy.setStakingBlockNum(tx.getNum());
        //copy.setNodeId(randomNodeId());
        copy.setNodeId(BlockResult.createNodeId(BlockResult.getRandom(1, 1000)));
        copy.setDelegateAddr(rAddress());
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Proposal getProposal(Transaction tx) {
        Proposal copy = JSON.parseObject(proposalStr, Proposal.class);
        copy.setHash(tx.getHash());
        copy.setNodeId(BlockResult.createNodeId(BlockResult.getRandom(1, 1000)));
        copy.setNodeName(copy.getNodeId().substring(7, 10));
        copy.setName(copy.getNodeId().substring(0, 6));
        // 1文本提案,2升级提案,3参数提案,4取消提案
        switch (tx.getTypeEnum()) {
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
    public Vote getVote(Transaction tx) {
        Vote copy = JSON.parseObject(voteStr, Vote.class);
        copy.setNodeId(BlockResult.createNodeId(BlockResult.getRandom(1, 1000)));
        copy.setNodeName(copy.getNodeId().substring(7, 10));
        copy.setHash(tx.getHash());
        copy.setProposalHash(randomProposalHash());
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public RpPlan getRpPlan(Transaction tx) throws BlockNumberException {
        RpPlan copy = JSON.parseObject(rpPlanStr, RpPlan.class);
        copy.setId(null);
        copy.setEpoch(EpochUtil.getEpoch(BigInteger.valueOf(tx.getNum()), BigInteger.TEN));
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
    public Slash getSlash(Transaction tx) {
        Slash copy = JSON.parseObject(slashStr, Slash.class);
        copy.setTxHash(tx.getHash());
        copy.setBenefitAddress(randomAddress());
        copy.setNodeId(BlockResult.createNodeId(BlockResult.getRandom(1, 1000)));
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Token getToken(Transaction tx) {
        Token token = new Token();
        token.setAddress(tx.getContractAddress());
        if (tx.getType() == Transaction.TypeEnum.ERC20_CONTRACT_CREATE.getCode()) {
            token.setType("erc20");
            token.setIsSupportErc165(false);
            token.setIsSupportErc20(true);
            token.setIsSupportErc721(false);
            token.setIsSupportErc721Enumeration(false);
            token.setIsSupportErc721Metadata(false);
            token.setName("erc20Test");
        } else if (tx.getType() == Transaction.TypeEnum.ERC721_CONTRACT_CREATE.getCode()) {
            token.setType("erc721");
            token.setIsSupportErc165(false);
            token.setIsSupportErc20(false);
            token.setIsSupportErc721(true);
            token.setIsSupportErc721Enumeration(true);
            token.setIsSupportErc721Metadata(true);
            token.setName("erc721Test");
        }
        token.setTokenTxQty(1);
        token.setHolder(1);
        token.setSymbol("aLAT");
        token.setTotalSupply("100000000000000000000");
        token.setDecimal(18);
        return token;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public TokenHolder getTokenHolder(Transaction tx) {
        TokenHolder tokenHolder = new TokenHolder();
        tokenHolder.setTokenAddress(tx.getContractAddress());
        tokenHolder.setAddress(tx.getTo());
        tokenHolder.setBalance("0");
        tokenHolder.setCreateTime(new Date());
        tokenHolder.setUpdateTime(new Date());
        tokenHolder.setTokenTxQty(1);
        return tokenHolder;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public TokenInventory getTokenInventory(Transaction tx) {
        TokenInventoryWithBLOBs tokenInventory = new TokenInventoryWithBLOBs();
        tokenInventory.setName("");
        tokenInventory.setDescription("");
        tokenInventory.setImage("");
        tokenInventory.setCreateTime(new Date());
        tokenInventory.setUpdateTime(new Date());
        tokenInventory.setTokenTxQty(1);
        tokenInventory.setTokenOwnerTxQty(1);
        tokenInventory.setTokenAddress(tx.getContractAddress());
        List<BigInteger> list = new ArrayList() {{
            add(new BigInteger("1000000"));
            add(new BigInteger("1000001"));
            add(new BigInteger("1000002"));
            add(new BigInteger("1000003"));
            add(new BigInteger("1000004"));
            add(new BigInteger("1000005"));
        }};
        BigInteger tokenId = list.get((int) (Math.random() * list.size()));
        tokenInventory.setTokenId(tokenId.toString());
        tokenInventory.setOwner(tx.getTo());
        return tokenInventory;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public ErcTx getTokenTX(Transaction tx) {
        ErcTx ercTx = new ErcTx();
        if (tx.getType() == Transaction.TypeEnum.ERC20_CONTRACT_EXEC.getCode()) {
            ercTx.setName("erc20Test");
        } else if (tx.getType() == Transaction.TypeEnum.ERC721_CONTRACT_EXEC.getCode()) {
            ercTx.setName("erc721Test");
        }
        ercTx.setSeq(new Random().nextLong());
        ercTx.setSymbol("aLAT");
        ercTx.setDecimal(18);
        ercTx.setContract(tx.getContractAddress());
        ercTx.setHash(tx.getHash());
        ercTx.setFrom(tx.getFrom());
        ercTx.setTo(tx.getTo());
        ercTx.setValue("100");
        ercTx.setBn(tx.getNum());
        ercTx.setBTime(new Date());
        ercTx.setToType(tx.getToType());
        ercTx.setFromType(1);
        ercTx.setTxFee("9744300");
        return ercTx;
    }

}
