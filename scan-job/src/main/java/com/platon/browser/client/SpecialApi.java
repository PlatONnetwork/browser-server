package com.platon.browser.client;

import com.alibaba.fastjson.JSON;
import com.platon.abi.solidity.datatypes.Utf8String;
import com.platon.browser.bean.RestrictingBalance;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.contracts.ppos.BaseContract;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.common.ErrorCode;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.methods.request.Transaction;
import com.platon.protocol.core.methods.response.PlatonCall;
import com.platon.tx.exceptions.ContractCallException;
import com.platon.utils.JSONUtil;
import com.platon.utils.Numeric;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/31 11:00
 * @Description:
 */
@Slf4j
@Component
public class SpecialApi {

    /**
     * 获取可用和锁仓余额
     */
    public static final int GET_RESTRICTING_BALANCE_FUNC_TYPE = 4101;

    private static final String BLANK_RES = "结果为空!";

    /**
     * rpc调用接口
     * @param web3j
     * @param function
     * @param from
     * @param to
     * @return
     * @throws Exception
     */
	private CallResponse<String> rpc(Web3j web3j, Function function, String from, String to) throws ContractInvokeException {
        CallResponse<String> br;
        try {
            br = new RemoteCall<>(() -> {
                PlatonCall ethCall = web3j.platonCall(
                        Transaction.createEthCallTransaction(from, to, EncoderUtils.functionEncoder(function)),
                        DefaultBlockParameterName.LATEST)
                        .send();
                if(ethCall.hasError()) {
                    throw new ContractInvokeException(ethCall.getError().getMessage());
                }
                String value = ethCall.getValue();
                if("0x".equals(value)){
                    // 证明没数据,返回空响应
                    CallResponse<String> data = new CallResponse<>();
                    data.setData(null);
                    data.setErrMsg(null);
                    data.setCode(ErrorCode.SUCCESS);
                    return data;
                }
                String decodedValue = new String(Numeric.hexStringToByteArray(value));
                BaseContract.CallRet callRet = JSONUtil.parseObject(decodedValue, BaseContract.CallRet.class);
                if (callRet == null) {
                    throw new ContractCallException("Unable to convert response: " + decodedValue);
                }
                CallResponse<String> callResponse = new CallResponse<>();
                if (callRet.isStatusOk()) {
                    callResponse.setCode(callRet.getCode());
                    callResponse.setData(JSONUtil.toJSONString(callRet.getRet()));
                } else {
                    callResponse.setCode(callRet.getCode());
                    callResponse.setErrMsg(callRet.getRet().toString());
                }
                return callResponse;
            }).send();
        } catch (Exception e) {
        	log.error("get rpc error", e);
            throw new ContractInvokeException(e.getMessage());
        }
        return br;
    }

    /**
     * 根据账户地址获取锁仓余额
     * @param addresses
     * @return
     * @throws Exception
     */
    public List<RestrictingBalance> getRestrictingBalance(Web3j web3j, String addresses) throws ContractInvokeException, BlankResponseException {
        final Function function = new Function(GET_RESTRICTING_BALANCE_FUNC_TYPE,Collections.singletonList(new Utf8String(addresses)));
        CallResponse<String> br = rpc(web3j,function,InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.getAddress(),InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.getAddress());
        if(br==null||br.getData()==null){
            throw new BlankResponseException(String.format("查询锁仓余额出错【addresses:%s)】,返回为空!",addresses));
        }
        if(br.isStatusOk()){
            String data = br.getData();
            if(data==null){
                throw new BlankResponseException(BLANK_RES);
            }
            return JSON.parseArray(data,RestrictingBalance.class);
        }else{
            String msg = JSON.toJSONString(br,true);
            throw new ContractInvokeException(String.format("【查询锁仓余额出错】地址:%s,返回数据:%s",addresses,msg));
        }
    }

}
