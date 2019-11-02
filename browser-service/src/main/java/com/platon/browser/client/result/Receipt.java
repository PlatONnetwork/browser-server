package com.platon.browser.client.result;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.platon.BaseResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.JSONUtil;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

@Data
public class Receipt {
    private static final int SUCCESS = 1;
    private static final int FAILURE = 2;

    private String gasUsed;
    private List<Log> logs;
    private String transactionHash;
    private String transactionIndex;
    private String status;

    private int logStatus;

    private String failReason;

    public int getStatus() {
        if (null == status) return SUCCESS;
        BigInteger statusQuantity = Numeric.decodeQuantity(status);
        return BigInteger.ONE.equals(statusQuantity)?1:0;
    }

    public BigInteger getGasUsed() {
        return Numeric.decodeQuantity(gasUsed);
    }

    /**
     * 解码log
     */
    public void decodeLogs(){
        if(logs==null||logs.isEmpty()){
            logStatus=FAILURE;
            return;
        }
        Log log = logs.get(0);
        String data = log.getData();
        if(StringUtils.isBlank(data)){
            logStatus=FAILURE;
            return;
        }
        data=data.replace("0x","");
        RlpList b = RlpDecoder.decode(Hex.decode(data));
        RlpList group = (RlpList) b.getValues().get(0);
        RlpString out = (RlpString) group.getValues().get(0);
        String res = new String(out.getBytes());
        BaseResponse response = JSONUtil.parseObject(res, BaseResponse.class);
        if(response==null) {
            logStatus=FAILURE;
            return;
        }
        if(!response.isStatusOk()) {
            logStatus=FAILURE;
            failReason=response.errMsg;
            return;
        }
        logStatus=SUCCESS;
    }
}