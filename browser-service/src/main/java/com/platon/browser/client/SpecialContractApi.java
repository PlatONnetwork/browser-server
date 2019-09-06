package com.platon.browser.client;

import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.ContractInvokeException;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.PlatonCall;
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
 * @Date: 2019/8/31 11:00
 * @Description:
 */
public class SpecialContractApi {

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
    /**
     * 获取提案结果
     */
    public static final int GET_PROPOSALRES_FUNC_TYPE = 2105;

    /**
     * 根据区块号获取结算周期验证人列表
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public static final BaseResponse<List<Node>> getHistoryVerifierList(Web3j web3j, BigInteger blockNumber) throws Exception {
        BaseResponse<List<Node>> nodes = nodeCall(
                web3j,
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
    public static final BaseResponse<List<Node>> getHistoryValidatorList(Web3j web3j,BigInteger blockNumber) throws Exception {
        BaseResponse<List<Node>> nodes = nodeCall(
                web3j,
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static final RemoteCall<BaseResponse<List<Node>>> nodeCall(Web3j web3j,BigInteger blockNumber,int funcType) {
        final Function function = new Function(
                funcType,
                Collections.singletonList(new Uint256(blockNumber)),
                Collections.singletonList(new TypeReference<Utf8String>() {})
        );
        return new RemoteCall<>((Callable<BaseResponse<List<Node>>>) () -> {
            String encodedFunction = PlatOnUtil.invokeEncode(function);
            PlatonCall ethCall = web3j.platonCall(
                    Transaction.createEthCallTransaction(InnerContractAddrEnum.NODE_CONTRACT.address, InnerContractAddrEnum.NODE_CONTRACT.address, encodedFunction),
                    DefaultBlockParameter.valueOf(blockNumber)
            ).send();
            String value = ethCall.getValue();
            if("0x".equals(value)){
                // 证明没数据,返回空响应
                return new BaseResponse<>();
            }
            BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray(value)), BaseResponse.class);
            if(response==null||response.data==null){
                throw new ContractInvokeException("查询历史节点合约出错: 入参(blockNumber="+blockNumber+",funcType="+funcType+"),响应(ethCall.getValue()="+value+")");
            }
            String data = (String)response.data;
            data = data.replace("\"Shares\":null","\"Shares\":\"0x0\"");
            response.data = JSONUtil.parseArray(data, Node.class);
            return response;
        });
    }

    /**
     * 根据账户地址获取锁仓余额
     * @param addresses
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final BaseResponse<List<RestrictingBalance>> getRestrictingBalance(Web3j web3j, String addresses) throws Exception {
        final Function function = new Function(
                GET_RESTRICTINGBALANCE_FUNC_TYPE,
                //Collections.singletonList(new DynamicArray<>(Utils.typeMap(addresses, Utf8String.class))),
                Arrays.asList(new Utf8String(addresses)),
                Collections.emptyList());
        return new RemoteCall<>((Callable<BaseResponse<List<RestrictingBalance>>>) () -> {
            String encodedFunction = PlatOnUtil.invokeEncode(function);
            PlatonCall ethCall = web3j.platonCall(
                    Transaction.createEthCallTransaction(InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.address, InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.address, encodedFunction),
                    DefaultBlockParameterName.LATEST
            ).send();
            String value = ethCall.getValue();
            if("0x".equals(value)){
                // 证明没数据,返回空响应
                return new BaseResponse<>();
            }
            BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray(value)), BaseResponse.class);
            if(response==null||response.data==null){
                throw new ContractInvokeException("查询锁仓计划合约出错: 入参(addresses="+addresses+"),响应(ethCall.getValue()="+value+")");
            }
            String data = (String)response.data;
            data = data.replace("\"lockBalance\":null","\"lockBalance\":\"0x0\"");
            data = data.replace("\"pledgeBalance\":null","\"pledgeBalance\":\"0x0\"");
            response.data = JSONUtil.parseArray(data, RestrictingBalance.class);
            return response;
        }).send();
    }

    public static final BaseResponse getProposalAccuVerifiers ( Web3j web3j, String proposalHash, String blockHash)throws Exception{
        final Function function = new Function(
                GET_PROPOSALRES_FUNC_TYPE,
                Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(proposalHash)),
                        new BytesType(Numeric.hexStringToByteArray(blockHash))),
                Collections.emptyList());
        return new RemoteCall<>((Callable<BaseResponse>) () -> {
            String encodedFunction = PlatOnUtil.invokeEncode(function);
            PlatonCall ethCall = web3j.platonCall(
                    Transaction.createEthCallTransaction(InnerContractAddrEnum.PROPOSAL_CONTRACT.address, InnerContractAddrEnum.PROPOSAL_CONTRACT.address, encodedFunction),
                    DefaultBlockParameterName.LATEST
            ).send();
            String value = ethCall.getValue();
            if("0x".equals(value)){
                // 证明没数据,返回空响应
                return new BaseResponse<>();
            }
            BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray(value)), BaseResponse.class);
            return response;
        }).send();

    }

}
