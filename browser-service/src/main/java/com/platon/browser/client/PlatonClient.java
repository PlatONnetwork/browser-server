package com.platon.browser.client;

import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.ContractInvokeException;
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
import org.web3j.platon.ContractAddress;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
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
import java.util.Collections;
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
     * 查询结算周期历史验证人队列
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
        delegateContract = DelegateContract.load(currentValidWeb3j);
        nodeContract = NodeContract.load(currentValidWeb3j);
        proposalContract = ProposalContract.load(currentValidWeb3j);
        restrictingPlanContract = RestrictingPlanContract.load(currentValidWeb3j);
        slashContract = SlashContract.load(currentValidWeb3j);
        stakingContract = StakingContract.load(currentValidWeb3j);
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
     * 根据区块号获取节点列表
     * @return
     * @throws Exception
     */

    private RemoteCall<BaseResponse<List<Node>>> nodeCall(BigInteger blockNumber,int funcType) {
        final Function function = new Function(
                funcType,
                Collections.singletonList(new Uint256(blockNumber)),
                Collections.singletonList(new TypeReference<Utf8String>() {})
        );
        return new RemoteCall<>((Callable<BaseResponse<List<Node>>>) () -> {
            String encodedFunction = PlatOnUtil.invokeEncode(function);
            PlatonCall ethCall = currentValidWeb3j.platonCall(
                    Transaction.createEthCallTransaction(InnerContractAddrEnum.NODE_CONTRACT.address, InnerContractAddrEnum.NODE_CONTRACT.address, encodedFunction),
                    DefaultBlockParameter.valueOf(blockNumber)
            ).send();
            String value = ethCall.getValue();
            BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray(value)), BaseResponse.class);
            if(response==null||response.data==null){
                throw new ContractInvokeException("查询历史节点合约出错: 入参(blockNumber="+blockNumber+",funcType="+funcType+"),响应(ethCall.getValue()="+value+")");
            }
            response.data = JSONUtil.parseArray((String) response.data, Node.class);
            return response;
        });
    }

    /**
     * 根据账户地址获取锁仓余额
     * @param addresses
     * @return
     * @throws Exception
     */
    public BaseResponse<List<RestrictingBalance>> getRestrictingBalance(String addresses) throws Exception {
        final Function function = new Function(
                PlatonClient.GET_RESTRICTINGBALANCE_FUNC_TYPE,
                //Collections.singletonList(new DynamicArray<>(Utils.typeMap(addresses, Utf8String.class))),
                Arrays.asList(new Utf8String(addresses)),
                Collections.emptyList());
        return new RemoteCall<>((Callable<BaseResponse<List<RestrictingBalance>>>) () -> {
            String encodedFunction = PlatOnUtil.invokeEncode(function);
            PlatonCall ethCall = currentValidWeb3j.platonCall(
                    Transaction.createEthCallTransaction(InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.address, InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.address, encodedFunction),
                    DefaultBlockParameterName.LATEST
            ).send();
            String value = ethCall.getValue();
            BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray(value)), BaseResponse.class);
            if(response==null||response.data==null){
                throw new ContractInvokeException("查询锁仓计划合约出错: 入参(addresses="+addresses+"),响应(ethCall.getValue()="+value+")");
            }
            response.data = JSONUtil.parseArray((String) response.data, RestrictingBalance.class);
            return response;
        }).send();
    }
}
