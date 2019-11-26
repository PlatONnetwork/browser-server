package com.platon.browser.client;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

@Data
public class Receipt {
    private static final int SUCCESS = 1;
    private static final int FAILURE = 2;

    private Long blockNumber;
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
        return BigInteger.ONE.equals(statusQuantity)?SUCCESS:FAILURE;
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
        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(data));
        List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);
        if(statusCode==0){
            logStatus=SUCCESS;
        }else {
            logStatus=FAILURE;
        }
    }
}