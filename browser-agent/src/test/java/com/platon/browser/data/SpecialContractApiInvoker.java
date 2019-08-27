package com.platon.browser.data;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.RestrictingBalance;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.util.Resolver;
import jnr.ffi.annotations.In;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.TypeReference;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.ContractAddress;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.NodeContract;
import org.web3j.platon.contracts.RestrictingPlanContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.PlatonCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.*;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultWasmGasProvider;
import org.web3j.utils.JSONUtil;
import org.web3j.utils.Numeric;
import org.web3j.utils.PlatOnUtil;

import java.math.BigInteger;
import java.util.ArrayList;
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
//    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.21.138:6789"));
    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.120.76:6797"));

    // 特殊合约接口测试
    @Test
    public void getCandidates() throws Exception {
        NodeContract nodeContract = NodeContract.load(currentValidWeb3j);

/*        // 当前区块号
        BigInteger blockNumber = currentValidWeb3j.platonBlockNumber().send().getBlockNumber();
        logger.error("{}",blockNumber);
        // 查询区块号为1时的结算周期历史验证人列表
        BaseResponse<List<Node>> historyVerifierList = getHistoryVerifierList(BigInteger.valueOf(0));
        logger.error("{}",historyVerifierList);
        // 查询实时结算验证人列表
        BaseResponse<List<Node>> verifierList = nodeContract.getVerifierList().send();
        logger.error("{}",verifierList);
        // 查询区块号为1时的共识周期历史验证人列表
        BaseResponse<List<Node>> historyValidatorList = getHistoryValidatorList(BigInteger.valueOf(0));
        logger.error("{}",historyValidatorList);
        // 查询实时共识验证人列表
        BaseResponse<List<Node>> validatorList = nodeContract.getValidatorList().send();
        logger.error("{}",validatorList);*/

        // 获取可用和锁仓余额
        List<String> addresses = new ArrayList<>();
        addresses.add("0x60ceca9c1290ee56b98d4e160ef0453f7c40d219");
        BaseResponse<List<RestrictingBalance>> balances = getRestrictingBalance("0x60ceca9c1290ee56b98d4e160ef0453f7c40d219;0x60ceca9c1290ee56b98d4e160ef0453f7c40d211");
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
                Collections.singletonList(new Uint256(blockNumber)),
                Collections.singletonList(new TypeReference<Utf8String>() {
                })
        );
        return new RemoteCall<>((Callable<BaseResponse<List<Node>>>) () -> {
            String encodedFunction = PlatOnUtil.invokeEncode(function);
            PlatonCall ethCall = currentValidWeb3j.platonCall(
                    Transaction.createEthCallTransaction(InnerContractAddrEnum.NODE_CONTRACT.address, InnerContractAddrEnum.NODE_CONTRACT.address, encodedFunction),
                    DefaultBlockParameter.valueOf(blockNumber)
            ).send();
            String value = ethCall.getValue();
            BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray(value)), BaseResponse.class);
            response.data = JSONUtil.parseArray((String) response.data, Node.class);
            return response;
        });
    }

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
            response.data = JSONUtil.parseArray((String) response.data, RestrictingBalance.class);
            return response;
        }).send();
    }


    public BytesType getRlpEncodeData( List<String> list) {

        if (list != null && !list.isEmpty()) {
            List<RlpType> rlpListList = new ArrayList <>();
            for (String t : list) {
                rlpListList.add(RlpString.create(RlpEncoder.encode(RlpString.create((t)))));
            }
            return new BytesType(RlpString.create(RlpEncoder.encode(new RlpList(rlpListList))).getBytes());
        }
        throw new RuntimeException("unsupported types");
    }

    public static void main(String args[]){
       // String a = new String(Numeric.hexStringToByteArray("0xf848b8467b22537461747573223a66616c73652c2244617461223a22222c224572724d7367223a22546869732063616e64696461746520697320616c726561647920657869737473227d"));
        String input = "0xf84e838203ec8180b842b84000cc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba41000838203e8";
        RlpList rlpList = RlpDecoder.decode(Hex.decode(input.replace("0x", "")));
        List <RlpType> rlpTypes = rlpList.getValues();
        RlpList rlpList1 = (RlpList) rlpTypes.get(0);
        RlpString rlpString = (RlpString) rlpList1.getValues().get(0);
        String stringValue = Numeric.toHexString(rlpString.getBytes());
        String res = new String(Numeric.hexStringToByteArray(stringValue));
        System.out.println(res);
        BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray("0xf848b8467b22537461747573223a66616c73652c2244617461223a22222c224572724d7367223a22546869732063616e64696461746520697320616c726561647920657869737473227d")), BaseResponse.class);
        System.out.println(response.status);
        //System.out.println(a);
    }
}
