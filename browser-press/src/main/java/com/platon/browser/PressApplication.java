package com.platon.browser;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.exception.GracefullyShutdownException;
import com.platon.browser.queue.publisher.*;
import com.platon.browser.service.BlockResult;
import com.platon.browser.service.DataGenService;
import com.platon.browser.service.StakeResult;
import com.platon.browser.utils.CounterBean;
import com.platon.browser.utils.GracefullyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@EnableRetry
@EnableScheduling
@MapperScan(basePackages = {"com.platon.browser.dao.mapper"})
@SpringBootApplication
public class PressApplication implements ApplicationRunner {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(1);

    @Autowired
    private BlockPublisher blockPublisher;
    @Autowired
    private TransactionPublisher transactionPublisher;
    @Autowired
    private NodeOptPublisher nodeOptPublisher;
    @Autowired
    private AddressPublisher addressPublisher;
    @Autowired
    private NodePublisher nodePublisher;
    @Autowired
    private StakePublisher stakePublisher;
    @Autowired
    private DelegationPublisher delegationPublisher;
//    @Autowired
//    private Erc20TokenPublisher erc20TokenPublisher;
//    @Autowired
//    private ESTokenTransferRecordPublisher esTokenTransferRecordPublisher;
    @Autowired
    private ProposalPublisher proposalPublisher;
    @Autowired
    private VotePublisher votePublisher;
    @Autowired
    private RpPlanPublisher rpPlanPublisher;
    @Autowired
    private RewardPublisher rewardPublisher;
    @Autowired
    private EstimatePublisher estimatePublisher;
    @Autowired
    private SlashPublisher slashPublisher;
//    @Autowired
//    private Erc20TokenAddressRelPublisher erc20TokenAddressRelPublisher;

    @Autowired
    private DataGenService dataGenService;

    @Autowired
    private NetworkStatMapper networkStatMapper;
    @Autowired
    private ConfigMapper configMapper;

    @Value("${platon.nodeMaxCount}")
    private long nodeMaxCount;
    @Value("${platon.stakeMaxCount}")
    private long stakeMaxCount;
    @Value("${platon.delegateMaxCount}")
    private long delegateMaxCount;
    @Value("${platon.proposalMaxCount}")
    private long proposalMaxCount;
    @Value("${platon.voteMaxCount}")
    private long voteMaxCount;
    @Value("${platon.rpplanMaxCount}")
    private long rpplanMaxCount;
    @Value("${platon.rewardMaxCount}")
    private long rewardMaxCount;
    @Value("${platon.slashMaxCount}")
    private long slashMaxCount;
    @Value("${platon.blockMaxCount}")
    private long blockMaxCount;
    @Value("${platon.nodeoptMaxCount}")
    private long nodeoptMaxCount;
    @Value("${platon.estimateMaxCount}")
    private long estimateMaxCount;

    @Value("${platon.tokenMaxCount}")
    private long tokenMaxCount;
    @Value("${platon.addressCountPerToken}")
    private int addressCountPerToken;
    @Value("${platon.tokenTransferMaxCount}")
    private long tokenTransferMaxCount;

    private long currentNodeSum = 0L;
    private long currentStakeSum = 0L;
    private long currentDelegationSum = 0L;
    private long currentProposalSum = 0L;
    private long currentVoteSum = 0L;
    private long currentRpplanSum = 0L;
    private long currentRewardSum = 0L;
    private long currentSlashSum = 0L;
    private long currentEstimateSum = 0L;

    private long currentTokenCount = 0l;
    private long currentTokenTransferCount = 0l;

    public static void main ( String[] args ) {
        SpringApplication.run(PressApplication.class, args);
    }
    @Override
    public void run ( ApplicationArguments args ) throws IOException, BlockNumberException {
        BigInteger blockNumber = init();
        NetworkStatExample networkStatExample = new NetworkStatExample();
        networkStatExample.createCriteria().andIdEqualTo(1);
        while (true){

            if(blockPublisher.getTotalCount()>blockMaxCount){
                log.warn("区块已达到指定数量：{}",blockMaxCount);
                System.exit(0);
                break;
            }

            // 检查应用状态
            checkAppStatus(blockNumber);
            // 构造【区块&交易&操作日志】数据
            BlockResult blockResult = makeBlock(blockNumber);
            /*// 构造【节点&质押】数据
            makeStake(blockResult);
            // 构造【委托】数据
            makeDelegation(blockResult);
            // 构造【提案】数据
            makeProposal(blockResult);
            // 构造【投票】数据
            makeVote(blockResult);
            // 构造【rpplan】数据
            makeRpPlan(blockResult);
            // 构造【slash】数据
            makeSlash(blockResult);
            // 构造【委托奖励】数据
            makeReward(blockResult);
            // 构造【gas】数据
            makeEstimate(blockResult);*/

//            // 构造【代币】数据
//            makeErc20Token(blockResult);
//            // 构造【代币转账】数据
//            makeTokenTransferRecord(blockResult);

            // 区块号累加
            blockNumber=blockNumber.add(BigInteger.ONE);
            log.info("当前块高：" + blockNumber);

            if (blockNumber.intValue() % 1000 == 0) {
                flushCount(blockNumber);
            }

            /*// 更新网络统计表
            dataGenService.getNetworkStat().setCurNumber(blockNumber.longValue());
            EXECUTOR_SERVICE.submit(()->{
                NetworkStat networkStat=dataGenService.getNetworkStat();
                networkStatMapper.updateByExample(networkStat,networkStatExample);
            });*/
        }
    }

    private BigInteger init(){
        GracefullyUtil.updateStatus();
        log.warn("加载状态文件:counter.json");
        File counterFile = FileUtils.getFile(System.getProperty("user.dir"), "counter.json");
        CounterBean counterBean = new CounterBean();
        BigInteger blockNumber = BigInteger.ZERO;
        try {
            String status = FileUtils.readFileToString(counterFile,"UTF8");
            counterBean = JSON.parseObject(status,CounterBean.class);
            blockPublisher.setTotalCount(counterBean.getBlockCount());
            transactionPublisher.setTotalCount(counterBean.getTransactionCount());
            nodeOptPublisher.setTotalCount(counterBean.getNodeOptCount());
            dataGenService.setNodeOptMaxId(counterBean.getNodeOptMaxId());
            addressPublisher.setTotalCount(counterBean.getAddressCount());
            currentNodeSum=counterBean.getNodeCount();
            currentStakeSum=counterBean.getStakingCount();
            currentDelegationSum=counterBean.getDelegationCount();
            currentProposalSum=counterBean.getProposalCount();
            currentVoteSum=counterBean.getVoteCount();
            currentRpplanSum=counterBean.getRpplanCount();
            currentRewardSum=counterBean.getRewardCount();
            currentSlashSum=counterBean.getSlashCount();
            currentEstimateSum=counterBean.getEstimateCount();
            currentTokenCount = counterBean.getTokenCount();
            currentTokenTransferCount = counterBean.getTokenTransferCount();
            blockNumber=BigInteger.valueOf(counterBean.getLastBlockNumber());
        } catch (IOException e) {
            log.warn("没有状态文件,创建一个!");
        }
        log.warn("状态加载完成:{}",JSON.toJSONString(counterBean,true));

        // 初始化配置表
        configMapper.deleteByExample(null);
        configMapper.batchInsert(dataGenService.getConfigList());
        // 初始化网络统计表
        networkStatMapper.deleteByExample(null);
        dataGenService.getNetworkStat().setCurNumber(0L);
        networkStatMapper.insert(dataGenService.getNetworkStat());

        return blockNumber;
    }

    private void checkAppStatus(BigInteger blockNumber) throws IOException {
        try {
            GracefullyUtil.monitor();
        } catch (GracefullyShutdownException | InterruptedException e) {
            log.warn("检测到SHUTDOWN钩子,放弃执行业务逻辑,写入当前状态...");
            /*CounterBean counter = new CounterBean();
            counter.setBlockCount(blockPublisher.getTotalCount());
            counter.setTransactionCount(transactionPublisher.getTotalCount());
            counter.setNodeOptCount(nodeOptPublisher.getTotalCount());
            counter.setNodeOptMaxId(dataGenService.getNodeOptMaxId());
            counter.setAddressCount(addressPublisher.getTotalCount());
            counter.setLastBlockNumber(blockNumber.longValue());
            counter.setNodeCount(currentNodeSum);
            counter.setStakingCount(currentStakeSum);
            counter.setDelegationCount(currentDelegationSum);
            counter.setProposalCount(currentProposalSum);
            counter.setVoteCount(currentVoteSum);
            counter.setRpplanCount(currentRpplanSum);
            counter.setSlashCount(currentSlashSum);
            counter.setRewardCount(currentRewardSum);
            counter.setEstimateCount(currentEstimateSum);
            counter.setTokenCount(currentTokenCount);
            String status = JSON.toJSONString(counter,true);
            File counterFile = FileUtils.getFile(System.getProperty("user.dir"), "counter.json");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(counterFile))) {
                bw.write(status);
            }
            log.warn("状态写入完成,可安全停机:{}",status);*/
            flushCount(blockNumber);
            System.exit(0);
        }
    }

    private void flushCount(BigInteger blockNumber){
        CounterBean counter = new CounterBean();
        counter.setBlockCount(blockPublisher.getTotalCount());
        counter.setTransactionCount(transactionPublisher.getTotalCount());
        counter.setNodeOptCount(nodeOptPublisher.getTotalCount());
        counter.setNodeOptMaxId(dataGenService.getNodeOptMaxId());
        counter.setAddressCount(addressPublisher.getTotalCount());
        counter.setLastBlockNumber(blockNumber.longValue());
        counter.setNodeCount(currentNodeSum);
        counter.setStakingCount(currentStakeSum);
        counter.setDelegationCount(currentDelegationSum);
        counter.setProposalCount(currentProposalSum);
        counter.setVoteCount(currentVoteSum);
        counter.setRpplanCount(currentRpplanSum);
        counter.setSlashCount(currentSlashSum);
        counter.setRewardCount(currentRewardSum);
        counter.setEstimateCount(currentEstimateSum);
        counter.setTokenCount(currentTokenCount);
        counter.setTokenTransferCount(currentTokenTransferCount);
        String status = JSON.toJSONString(counter,true);
        File counterFile = FileUtils.getFile(System.getProperty("user.dir"), "counter.json");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(counterFile))) {
            bw.write(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.warn("状态写入完成,可安全停机:{}",status);
    }

    /**
     * 构造区块、交易、及日志
     * @param blockNumber
     * @return
     */
    private BlockResult makeBlock(BigInteger blockNumber){
        BlockResult blockResult = dataGenService.getBlockResult(blockNumber);
        blockPublisher.publish(Arrays.asList(blockResult.getBlock()));
        transactionPublisher.publish(blockResult.getTransactionList());
        if(nodeOptPublisher.getTotalCount()<=nodeoptMaxCount){
            //如果日志记录达到指定数量则停止入库ES
            nodeOptPublisher.publish(blockResult.getNodeOptList());
        }
        return blockResult;
    }

    /**
     * 构造节点&质押
     * @param blockResult
     */
    private boolean nodeSatisfied = false;
    private boolean stakeSatisfied = false;
    private void makeStake(BlockResult blockResult) throws BlockNumberException {
        List <Node> nodeList = new ArrayList<>();
        List <Staking> stakingList = new ArrayList<>();
        for (Transaction tx : blockResult.getTransactionList()) {
            if(
                (
                    tx.getTypeEnum()== Transaction.TypeEnum.STAKE_CREATE||
                    tx.getTypeEnum()==Transaction.TypeEnum.STAKE_MODIFY||
                    tx.getTypeEnum()==Transaction.TypeEnum.STAKE_INCREASE||
                    tx.getTypeEnum()==Transaction.TypeEnum.STAKE_EXIT
                ) &&
                (!nodeSatisfied||!stakeSatisfied)
            ){
                StakeResult stakeResult = dataGenService.getStakeResult(tx);
                if(currentNodeSum<nodeMaxCount){
                    // 构造指定数量的节点记录并入库
                    nodeList.add(stakeResult.getNode());
                    currentNodeSum++;
                }else {
                    nodeSatisfied=true;
                }
                if(currentStakeSum<stakeMaxCount){
                    // 构造指定数量的质押记录并入库
                    stakingList.add(stakeResult.getStaking());
                    currentStakeSum++;
                }else {
                    stakeSatisfied=true;
                }
            }
        }

        if(!nodeList.isEmpty()) nodePublisher.publish(nodeList);
        if(!stakingList.isEmpty()) stakePublisher.publish(stakingList);
    }

    /**
     * 构造委托
     * @param blockResult
     */
    private void makeDelegation(BlockResult blockResult){
        if(currentDelegationSum<delegateMaxCount){
            // 构造指定数量的委托记录并入库
            List <Delegation> delegationList = new ArrayList<>();
            for (Transaction tx : blockResult.getTransactionList()) {
                if(
                        tx.getTypeEnum()== Transaction.TypeEnum.DELEGATE_CREATE||
                                tx.getTypeEnum()==Transaction.TypeEnum.DELEGATE_EXIT
                ){
                    delegationList.add(dataGenService.getDelegation(tx));
                    currentDelegationSum++;
                }
            }
            delegationPublisher.publish(delegationList);
        }
    }

    /**
     * 构造提案
     * @param blockResult
     */
    private void makeProposal(BlockResult blockResult){
        if(currentProposalSum<proposalMaxCount){
            // 构造指定数量的提案记录并入库
            List <Proposal> proposalList = new ArrayList<>();
            for (Transaction tx : blockResult.getTransactionList()) {
                if(
                    tx.getTypeEnum()==Transaction.TypeEnum.PROPOSAL_CANCEL||
                    tx.getTypeEnum()==Transaction.TypeEnum.PROPOSAL_TEXT||
                    tx.getTypeEnum()==Transaction.TypeEnum.PROPOSAL_PARAMETER||
                    tx.getTypeEnum()==Transaction.TypeEnum.PROPOSAL_UPGRADE
                ){
                    proposalList.add(dataGenService.getProposal(tx));
                    currentProposalSum++;
                }
            }
            proposalPublisher.publish(proposalList);
        }
    }

    /**
     * 构造投票
     * @param blockResult
     */
    private void makeVote(BlockResult blockResult){
        if(currentVoteSum<voteMaxCount){
            // 构造指定数量的投票记录并入库
            List <Vote> voteList = new ArrayList<>();
            for (Transaction tx : blockResult.getTransactionList()) {
                if(
                    tx.getTypeEnum()== Transaction.TypeEnum.PROPOSAL_VOTE
                ){
                    voteList.add(dataGenService.getVote(tx));
                    currentVoteSum++;
                }
            }
            votePublisher.publish(voteList);
        }
    }

    /**
     * 构造Slash
     * @param blockResult
     */
    private void makeSlash(BlockResult blockResult) {
        if(currentSlashSum<slashMaxCount){
            // 构造指定数量的Slash记录并入库
            List <Slash> slashList = new ArrayList<>();
            for (Transaction tx : blockResult.getTransactionList()) {
                if(
                        tx.getTypeEnum()== Transaction.TypeEnum.REPORT
                ){
                    slashList.add(dataGenService.getSlash(tx));
                    currentSlashSum++;
                }
            }
            slashPublisher.publish(slashList);
        }
    }

    /**
     * 构造RpPlan
     * @param blockResult
     */
    private void makeRpPlan(BlockResult blockResult) throws BlockNumberException {
        if(currentRpplanSum<rpplanMaxCount){
            // 构造指定数量的RpPlan记录并入库
            List <RpPlan> rpPlanList = new ArrayList<>();
            for (Transaction tx : blockResult.getTransactionList()) {
                if(
                        tx.getTypeEnum()== Transaction.TypeEnum.RESTRICTING_CREATE
                ){
                    rpPlanList.add(dataGenService.getRpPlan(tx));
                    currentRpplanSum++;
                }
            }
            rpPlanPublisher.publish(rpPlanList);
        }
    }
    
    /**
     * 构造委托奖励
     * @param blockResult
     */
    private void makeReward(BlockResult blockResult) throws BlockNumberException {
        if(currentRewardSum<rewardMaxCount){
            // 构造指定数量的委托奖励记录并入库
            List <DelegationReward> delegationRewards = new ArrayList<>();
            for (Transaction tx : blockResult.getTransactionList()) {
                if(
                        tx.getTypeEnum()== Transaction.TypeEnum.CLAIM_REWARDS
                ){
                	delegationRewards.add(dataGenService.getReward(tx));
                    currentRewardSum++;
                }
            }
            rewardPublisher.publish(delegationRewards);
        }
    }
    
    /**
     * 构造gas
     * @param blockResult
     */
    private void makeEstimate(BlockResult blockResult) throws BlockNumberException {
        if(currentEstimateSum<estimateMaxCount){
            // 构造指定数量的委托奖励记录并入库
            List <GasEstimate> gasEstimates = new ArrayList<>();
            for (Transaction tx : blockResult.getTransactionList()) {
                if(
                        tx.getTypeEnum()== Transaction.TypeEnum.DELEGATE_CREATE
                ){
                	gasEstimates.add(dataGenService.getEstimate(tx));
                	currentEstimateSum++;
                }
            }
            estimatePublisher.publish(gasEstimates);
        }
    }

//    /**
//     * 构造代币合约
//     * @param blockResult
//     */
//    private void makeErc20Token(BlockResult blockResult){
//        if (currentTokenCount < tokenMaxCount) {
//            List<Erc20Token> tokenList = new ArrayList<>();
//            for (Transaction tx : blockResult.getTransactionList()) {
//                if (tx.getTypeEnum() == Transaction.TypeEnum.ERC20_CONTRACT_CREATE) {
//                    tokenList.add(dataGenService.getErc20Token(tx));
//                    currentTokenCount++;
//                }
//            }
////            erc20TokenPublisher.publish(tokenList);
//
//            // 代币与地址关系：1:1000000，按此关系来（一般）
//            List<Erc20TokenAddressRel> erc20TokenAddressRelList = new ArrayList<>();
//            tokenList.forEach(token -> {
//                erc20TokenAddressRelList.addAll(dataGenService.getErc20TokenAddressRel(token, addressCountPerToken));
//            });
////            erc20TokenAddressRelPublisher.publish(erc20TokenAddressRelList);
//        }
//    }
//
//    /**
//     * 构建代币转账交易
//     */
//    private void makeTokenTransferRecord(BlockResult blockResult) {
//        if (currentTokenTransferCount < tokenTransferMaxCount) {
//            List<OldErcTx> transferRecordList = new ArrayList<>();
//            for (Transaction tx : blockResult.getTransactionList()) {
//                if (tx.getTypeEnum() == Transaction.TypeEnum.ERC20_CONTRACT_EXEC) {
//                    transferRecordList.add(dataGenService.getESTokenTransferRecord(tx));
//                    currentTokenTransferCount++;
//                }
//            }
////            esTokenTransferRecordPublisher.publish(transferRecordList);
//        }
//    }
}
