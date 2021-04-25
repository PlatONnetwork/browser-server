package com.platon.browser.service;

import com.platon.contracts.ppos.BaseContract;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.common.ErrorCode;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.methods.request.Transaction;
import com.platon.protocol.core.methods.response.PlatonCall;
import com.platon.tx.exceptions.ContractCallException;
import com.platon.utils.JSONUtil;
import com.platon.utils.Numeric;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public abstract class DeContract extends BaseContract {

	protected DeContract(String contractAddress, Web3j web3j) {
		super(contractAddress, web3j);
		// TODO Auto-generated constructor stub
	}
	
	protected <T> RemoteCall<CallResponse<List<T>>> executeRemoteCallListValueReturnDe(Function function, Class<T> returnType,BigInteger block) {
        return new RemoteCall<>(() -> executeCallListValueReturnDe(function, returnType, block));
    }
	
	private <T> CallResponse<List<T>> executeCallListValueReturnDe(Function function, Class<T> returnType,BigInteger block) throws IOException {
    	PlatonCall ethCall = web3j.platonCall(
                Transaction.createEthCallTransaction(
                		transactionManager.getFromAddress(), contractAddress, EncoderUtils.functionEncoder(function)),
                DefaultBlockParameter.valueOf(block))
                .send();
    	
    	String result = Numeric.cleanHexPrefix(ethCall.getValue());
    	if(result==null || "".equals(result)){
    		  throw new ContractCallException("Empty value (0x) returned from contract");
    	}
    	
    	CallRet callRet = JSONUtil.parseObject(new String(Hex.decode(result)), CallRet.class);
        if (callRet == null) {
        	throw new ContractCallException("Unable to convert response: " + result);
        }
        
        CallResponse<List<T>> callResponse = new CallResponse<List<T>>();
        if (callRet.isStatusOk()) {
        	callResponse.setCode(callRet.getCode());
        	callResponse.setData(JSONUtil.parseArray(JSONUtil.toJSONString(callRet.getRet()), returnType));
        } else {
        	callResponse.setCode(callRet.getCode());
        	callResponse.setErrMsg(callRet.getRet().toString());
        }

        if(callRet.getCode() == ErrorCode.OBJECT_NOT_FOUND){
            callResponse.setCode(ErrorCode.SUCCESS);
            callResponse.setData(Collections.emptyList());
        }

        return callResponse;
    }

}
