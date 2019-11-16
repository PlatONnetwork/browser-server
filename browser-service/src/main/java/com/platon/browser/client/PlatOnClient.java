package com.platon.browser.client;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.exception.ConfigLoadingException;
import com.platon.browser.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.bean.EconomicConfig;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
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
@Slf4j
@Component
public class PlatOnClient {
    private static final ReentrantReadWriteLock WEB3J_CONFIG_LOCK = new ReentrantReadWriteLock();
    @Value("${platon.web3j.protocol}")
    private String protocol;
    @Value("${platon.web3j.addresses}")
    private List<String> web3jAddresses;

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

    @PostConstruct
    public void init(){
        WEB3J_CONFIG_LOCK.writeLock().lock();
        try {
            web3jMap.clear();
            if(protocol.startsWith("ws")){
                web3jAddresses.forEach(address->{
                    WebSocketService wss = new WebSocketService(protocol+address,true);
                    try {
                        wss.connect();
                        web3jMap.put(Web3j.build(wss),protocol+address);
                    } catch (ConnectException e) {
                        log.error("Websocket地址({})无法连通:",protocol+address,e);
                    }
                });
                if(web3jMap.size()==0){
                    log.error("没有可用Web3j实例!");
                    System.exit(1);
                }
            }else
            if(protocol.startsWith("http")){
                web3jAddresses.forEach(address->web3jMap.put(Web3j.build(new HttpService(protocol+address)),protocol+address));
            }else{
                throw new ConfigLoadingException("Web3j连接协议不合法!");
            }
            updateCurrentValidWeb3j();
        }catch (Exception e){
            log.error("加载Web3j配置错误:", e);
        }finally {
            WEB3J_CONFIG_LOCK.writeLock().unlock();
        }
    }

    public Web3j getWeb3j(){
        WEB3J_CONFIG_LOCK.readLock().lock();
        try{
            return currentValidWeb3j;
        }catch (Exception e){
            log.error("加载Web3j配置错误:", e);
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

    private void updateCurrentValidWeb3j(){
        WEB3J_CONFIG_LOCK.writeLock().lock();
        try {
            Web3j preWeb3j = currentValidWeb3j;
            // 检查所有Web3j的连通性, 取块高最高的作为当前web3j
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
                    log.info("候选Web3j实例({})无效！", web3j);
                }
            }
            if(preWeb3j==null||preWeb3j!=currentValidWeb3j){
                // 前任web3j为空或Web3j有变动,则更新合约变量
                updateContract();
            }
            if(maxBlockNumber==0){
                log.info("当前所有候选Web3j实例均无法连通!");
                if(protocol.startsWith("ws")){
                    log.info("重新初始化websocket连接!");
                    init();
                }
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
        log.debug("*** In the detect task *** ");
        try {
            updateCurrentValidWeb3j();
        } catch (Exception e) {
            log.error("detect exception:{}", e);
        }
        log.debug("*** End the detect task *** ");
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

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,backoff=@Backoff(value=3000L))
    public EconomicConfig getEconomicConfig() throws Exception {
        try {
            EconomicConfig ec = getWeb3j().getEconomicConfig().send().getEconomicConfig();
            String msg = JSON.toJSONString(ec,true);
            log.info("链上配置:{}",msg);
            return ec;
        } catch (Exception e) {
            updateCurrentValidWeb3j();
            log.error("获取链上配置出错({}),将重试!", e.getMessage());
            throw e;
        }
    }

    public BigInteger getLatestBlockNumber() throws IOException {
        return getWeb3j().platonBlockNumber().send().getBlockNumber();
    }

    public List<Node> getLatestValidators() throws Exception {
        return getNodeContract().getValidatorList().send().data;
    }

    public List<Node> getLatestVerifiers() throws Exception {
        return getNodeContract().getVerifierList().send().data;
    }
}
