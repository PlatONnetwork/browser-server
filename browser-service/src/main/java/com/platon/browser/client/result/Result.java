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
public class Result {
    private String gasUsed;
    private List<Log> logs;
    private String transactionHash;
    private String transactionIndex;


    private int receiptStatus;
    private String failReason;

    public BigInteger getGasUsed() {
        return Numeric.decodeQuantity(gasUsed);
    }

    /**
     * 解码log
     */
    public void decodeLogs(){
        if(logs==null||logs.isEmpty()){
            receiptStatus=0;
        }else {
            Log log = logs.get(0);
            String data = log.getData();
            if(StringUtils.isNotBlank(data)) {
                data=data.replace("0x","");
            }else{
                receiptStatus=0;
            }
            RlpList b = RlpDecoder.decode(Hex.decode(data));
            RlpList group = (RlpList) b.getValues().get(0);
            RlpString out = (RlpString) group.getValues().get(0);
            String res = new String(out.getBytes());
            BaseResponse response = JSONUtil.parseObject(res, BaseResponse.class);
            if(response==null) receiptStatus=0;
            if(response!=null){
                if(response.isStatusOk()) {
                    receiptStatus=1;
                }else{
                    receiptStatus=0;
                    failReason=response.errMsg;
                }
            }
        }
    }
}