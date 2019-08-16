package com.platon.browser.data;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.RestrictingBalance;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.NodeContract;
import org.web3j.platon.contracts.RestrictingPlanContract;
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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 21:00
 * @Description:
 */
public class SpecialContractApiInvoker {
    private static Logger logger = LoggerFactory.getLogger(SpecialContractApiInvoker.class);
    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.112.171:6789"));

    // 特殊合约接口测试
    @Test
    public void getCandidates() throws Exception {
        NodeContract nodeContract = NodeContract.load(currentValidWeb3j,new ReadonlyTransactionManager(currentValidWeb3j, NodeContract.NODE_CONTRACT_ADDRESS),new DefaultWasmGasProvider());

        // 当前区块号
        BigInteger blockNumber = currentValidWeb3j.platonBlockNumber().send().getBlockNumber();
        logger.error("{}",blockNumber);
        // 查询区块号为1时的结算周期历史验证人列表
        BaseResponse<List<Node>> historyVerifierList = getHistoryVerifierList(BigInteger.ONE);
        logger.error("{}",historyVerifierList);
        // 查询实时结算验证人列表
        BaseResponse<List<Node>> verifierList = nodeContract.getVerifierList().send();
        logger.error("{}",verifierList);
        // 查询区块号为1时的共识周期历史验证人列表
        BaseResponse<List<Node>> historyValidatorList = getHistoryValidatorList(BigInteger.ONE);
        logger.error("{}",historyValidatorList);
        // 查询实时共识验证人列表
        BaseResponse<List<Node>> validatorList = nodeContract.getValidatorList().send();
        logger.error("{}",validatorList);

        // 获取可用和锁仓余额
        BaseResponse<List<RestrictingBalance>> balances = getRestrictingBalance("0x8b77ac9fabb6fe247ee91ca07ea4f62c6761e79b");
        logger.error("{}",balances);
    }


    public BaseResponse<List<Node>> getHistoryVerifierList(BigInteger blockNumber) throws Exception {
        BaseResponse<List<Node>> nodes = nodeCall(
                blockNumber,
                PlatonClient.GET_HISTORY_VERIFIERLIST_FUNC_TYPE
        ).send();
        return nodes;
    }

    public BaseResponse<List<Node>> getHistoryValidatorList(BigInteger blockNumber) throws Exception {
        BaseResponse<List<Node>> nodes = nodeCall(
                blockNumber,
                PlatonClient.GET_HISTORY_VALIDATORLIST_FUNC_TYPE
        ).send();
        return nodes;
    }


    public RemoteCall<BaseResponse<List<Node>>> nodeCall(BigInteger blockNumber, int funcType) {
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

    public BaseResponse<List<RestrictingBalance>> getRestrictingBalance(String account) throws Exception {
        final Function function = new Function(
                PlatonClient.GET_RESTRICTINGBALANCE_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(account))),
                Collections.emptyList());
        return new RemoteCall<>((Callable<BaseResponse<List<RestrictingBalance>>>) () -> {
            String encodedFunction = PlatOnUtil.invokeEncode(function);
            PlatonCall ethCall = currentValidWeb3j.platonCall(
                    Transaction.createEthCallTransaction(RestrictingPlanContract.RESTRICTING_PLAN_CONTRACT_ADDRESS, RestrictingPlanContract.RESTRICTING_PLAN_CONTRACT_ADDRESS, encodedFunction),
                    DefaultBlockParameterName.LATEST
            ).send();
            String value = ethCall.getValue();
            BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray(value)), BaseResponse.class);
            response.data = JSONUtil.parseArray((String) response.data, RestrictingBalance.class);
            return response;
        }).send();
    }
}
