package com.platon.browser.client;

import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:42
 */
@Component
public class PlatOnClient {
    private static Logger logger = LoggerFactory.getLogger(PlatOnClient.class);
    private static final ReentrantReadWriteLock WEB3J_CONFIG_LOCK = new ReentrantReadWriteLock();

    private Map<Web3j,String> web3jMap=new HashMap<>();
    private Web3j currentValidWeb3j;
    private String currentValidAddress;
    // 委托合约接口
    private DelegateContract delegateContract;
    public DelegateContract getDelegateContract(){return delegateContract;}
    // 节点合约接口
    private NodeContract nodeContract;
    public NodeContract getNodeContract(){return nodeContract;}
    // 提案合约接口
    private ProposalContract proposalContract;
    public ProposalContract getProposalContract(){return proposalContract;}
    // 锁仓合约接口
    private RestrictingPlanContract restrictingPlanContract;
    public RestrictingPlanContract getRestrictingPlanContract(){return restrictingPlanContract;}
    // 惩罚合约接口
    private SlashContract slashContract;
    public SlashContract getSlashContract(){return slashContract;}
    // 质押合约接口
    private StakingContract stakingContract;
    public StakingContract getStakingContract(){return stakingContract;}

    @Value("${platon.web3j.addresses}")
    private List<String> web3jAddresses;

    @PostConstruct
    public void init(){
        // 初始化所有web3j实例
        try {
            WEB3J_CONFIG_LOCK.writeLock().lock();
            web3jAddresses.forEach(address->{
                Web3j web3j = Web3j.build(new HttpService(address));
                web3jMap.put(web3j,address);
                if(currentValidWeb3j==null) currentValidWeb3j=web3j;
                if(currentValidAddress==null) currentValidAddress=address;
            });
        }catch (Exception e){
        	logger.error("web3j error{}", e);
        }finally {
            WEB3J_CONFIG_LOCK.writeLock().unlock();
        }

        // 更新合约
        updateContract();
        // 更新有效web3j实例列表
        updateCurrentValidWeb3j();
    }

    public Web3j getWeb3j(){
        WEB3J_CONFIG_LOCK.readLock().lock();
        try{
            return currentValidWeb3j;
        }catch (Exception e){
            logger.error("web3j error{}", e);
        }finally {
            WEB3J_CONFIG_LOCK.readLock().unlock();
        }
        return null;
    }

    public String getWeb3jAddress(){
        return currentValidAddress;
    }

    private void updateContract(){
        delegateContract = DelegateContract.load(currentValidWeb3j);
        nodeContract = NodeContract.load(currentValidWeb3j);
        proposalContract = ProposalContract.load(currentValidWeb3j);
        restrictingPlanContract = RestrictingPlanContract.load(currentValidWeb3j);
        slashContract = SlashContract.load(currentValidWeb3j);
        stakingContract = StakingContract.load(currentValidWeb3j);
    }

    public void updateCurrentValidWeb3j(){
        WEB3J_CONFIG_LOCK.writeLock().lock();
        try {
            // 检查currentValidWeb3j连通性, 取块高最高的作为当前web3j
            long maxBlockNumber = 0;
            for (Map.Entry<Web3j, String> entry : web3jMap.entrySet()) {
                Web3j web3j = entry.getKey();
                String address = entry.getValue();
                try {
                    BigInteger blockNumber = web3j.platonBlockNumber().send().getBlockNumber();
                    if (blockNumber.longValue() > maxBlockNumber) {
                        maxBlockNumber = blockNumber.longValue();
                        currentValidWeb3j = web3j;
                        currentValidAddress = address;
                    }
                } catch (IOException e2) {
                    logger.info("候选Web3j实例({})无效！", web3j);
                }
            }
            if(maxBlockNumber==0){
                logger.info("当前所有候选Web3j实例均无法连通！");
            }
        }finally {
            WEB3J_CONFIG_LOCK.writeLock().unlock();
        }
    }

    /**
     * web3j实例保活
     */
    @Scheduled(cron = "0/20 * * * * ?")
    protected void keepAlive () {
        logger.debug("*** In the detect task *** ");
        try {
            updateCurrentValidWeb3j();
        } catch (Exception e) {
            logger.error("detect exception:{}", e);
        }
        logger.debug("*** End the detect task *** ");
    }


    /**
     * 根据区块号取交易回执
     * @param blockNumber
     */
    private RpcParam rpcParam = new RpcParam();
    public CompletableFuture<ReceiptResult> getReceiptAsync(long blockNumber){
        rpcParam.setMethod("platon_getTransactionByBlock");
        rpcParam.getParams().clear();
        rpcParam.getParams().add(blockNumber);
        CompletableFuture<ReceiptResult> cf = com.platon.browser.util.HttpUtil.postAsync(getWeb3jAddress(),rpcParam.toJsonString(), ReceiptResult.class);
        while (cf.isCompletedExceptionally()){
            cf = HttpUtil.postAsync(getWeb3jAddress(),rpcParam.toJsonString(), ReceiptResult.class);
        }
        return cf;
    }
}
