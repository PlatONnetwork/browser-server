package com.platon.browser.client;

import com.platon.browser.job.Web3DetectJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.PlatonCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultWasmGasProvider;
import org.web3j.utils.JSONUtil;
import org.web3j.utils.Numeric;
import org.web3j.utils.PlatOnUtil;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:42
 */
@Component
public class PlatonClient {
    private static Logger logger = LoggerFactory.getLogger(Web3DetectJob.class);
    private static final ReentrantReadWriteLock WEB3J_CONFIG_LOCK = new ReentrantReadWriteLock();

    /**
     * 查询区块历史的验证人队列
     */
    public static final int GET_HISTORY_VERIFIERLIST_FUNC_TYPE = 1106;
    /**
     * 查询历史共识周期的验证人列
     */
    public static final int GET_HISTORY_VALIDATORLIST_FUNC_TYPE = 1107;
    /**
     * 获取可用和锁仓余额
     */
    public static final int GET_RESTRICTINGBALANCE_FUNC_TYPE = 4101;

    private List<Web3j> allWeb3jList=new ArrayList<>();
    private Web3j currentValidWeb3j;
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
                allWeb3jList.add(web3j);
                if(currentValidWeb3j==null) currentValidWeb3j=web3j;
            });
        }catch (Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
        }finally {
            WEB3J_CONFIG_LOCK.readLock().unlock();
        }
        return null;
    }

    private void updateContract(){
        delegateContract = DelegateContract.load(currentValidWeb3j,new ReadonlyTransactionManager(currentValidWeb3j, DelegateContract.DELEGATE_CONTRACT_ADDRESS),new DefaultWasmGasProvider());
        nodeContract = NodeContract.load(currentValidWeb3j,new ReadonlyTransactionManager(currentValidWeb3j, NodeContract.NODE_CONTRACT_ADDRESS),new DefaultWasmGasProvider());
        proposalContract = ProposalContract.load(currentValidWeb3j,new ReadonlyTransactionManager(currentValidWeb3j, ProposalContract.PROPOSAL_CONTRACT_ADDRESS),new DefaultWasmGasProvider());
        restrictingPlanContract = RestrictingPlanContract.load(currentValidWeb3j,new ReadonlyTransactionManager(currentValidWeb3j, RestrictingPlanContract.RESTRICTING_PLAN_CONTRACT_ADDRESS),new DefaultWasmGasProvider());
        slashContract = SlashContract.load(currentValidWeb3j,new ReadonlyTransactionManager(currentValidWeb3j, SlashContract.SLASH_CONTRACT_ADDRESS),new DefaultWasmGasProvider());
        stakingContract = StakingContract.load(currentValidWeb3j,new ReadonlyTransactionManager(currentValidWeb3j, StakingContract.STAKING_CONTRACT_ADDRESS),new DefaultWasmGasProvider());
    }

    public void updateCurrentValidWeb3j(){
        WEB3J_CONFIG_LOCK.writeLock().lock();
        Web3j originWeb3j = currentValidWeb3j;
        try {
            // 检查currentValidWeb3j连通性
            try {
                if(currentValidWeb3j==null) throw new RuntimeException("currentValidWeb3j需要初始化！");
                currentValidWeb3j.platonBlockNumber().send();
                updateContract();
            } catch (Exception e1) {
                logger.info("当前Web3j实例({})无效，重新选举Web3j实例！",currentValidWeb3j);
                // 连不通，则需要更新
                for(Web3j web3j:allWeb3jList){
                    try {
                        web3j.platonBlockNumber().send();
                        currentValidWeb3j=web3j;
                    } catch (IOException e2) {
                        logger.info("候选Web3j实例({})无效！",web3j);
                    }
                }
                if(originWeb3j==currentValidWeb3j){
                    logger.info("当前所有候选Web3j实例均无法连通！");
                }
            }
        }finally {
            WEB3J_CONFIG_LOCK.writeLock().unlock();
        }
    }

    /**
     * web3j实例保活
     */
    @Scheduled(cron = "0/30 * * * * ?")
    protected void keepAlive () {
        logger.debug("*** In the detect task *** ");
        try {
            updateCurrentValidWeb3j();
        } catch (Exception e) {
            logger.error("detect exception:{}", e.getMessage());
            e.printStackTrace();
        }
        logger.debug("*** End the detect task *** ");
    }


    /**
     * 根据区块号获取结算周期验证人列表
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public BaseResponse<List<Node>> getHistoryVerifierList(BigInteger blockNumber) throws Exception {
        BaseResponse<List<Node>> nodes = nodeCall(
                blockNumber,
                GET_HISTORY_VERIFIERLIST_FUNC_TYPE
        ).send();
        return nodes;
    }

    /**
     * 根据区块号获取共识周期验证人列表
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public BaseResponse<List<Node>> getHistoryValidatorList(BigInteger blockNumber) throws Exception {
        BaseResponse<List<Node>> nodes = nodeCall(
                blockNumber,
                GET_HISTORY_VALIDATORLIST_FUNC_TYPE
        ).send();
        return nodes;
    }


    /**
     * 根据account获取可用余额和锁仓余额
     * @param account
     * @return
     * @throws Exception
     */

    private RemoteCall<BaseResponse<List<Node>>> nodeCall(BigInteger blockNumber,int funcType) {
        final Function function = new Function(
                funcType,
                Arrays.asList(new Uint256(blockNumber)),
                Arrays.asList(new TypeReference<Utf8String>() {})
        );
        return new RemoteCall<>((Callable<BaseResponse<List<Node>>>) () -> {
            String encodedFunction = PlatOnUtil.invokeEncode(function);
            PlatonCall ethCall = currentValidWeb3j.platonCall(
                    Transaction.createEthCallTransaction(NodeContract.NODE_CONTRACT_ADDRESS, NodeContract.NODE_CONTRACT_ADDRESS, encodedFunction),
                    DefaultBlockParameter.valueOf(blockNumber)
            ).send();
            String value = ethCall.getValue();
            BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray(value)), BaseResponse.class);
            response.data = JSONUtil.parseArray((String) response.data, Node.class);
            return response;
        });
    }
}
