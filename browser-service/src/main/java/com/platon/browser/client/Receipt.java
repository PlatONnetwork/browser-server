package com.platon.browser.client;

import org.apache.commons.lang3.StringUtils;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

public class Receipt {
    private static final int SUCCESS = 1;
    private static final int FAILURE = 2;

    private Long blockNumber;
    private String gasUsed;
    private List<Log> logs;
    private String transactionHash;
    private String transactionIndex;
    private String status;
    private String contractAddress;

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
            failReason=decodedStatus;
            logStatus=FAILURE;
        }
    }

	public Long getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(Long blockNumber) {
		this.blockNumber = blockNumber;
	}

	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public String getTransactionHash() {
		return transactionHash;
	}

	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}

	public String getTransactionIndex() {
		return transactionIndex;
	}

	public void setTransactionIndex(String transactionIndex) {
		this.transactionIndex = transactionIndex;
	}

	public int getLogStatus() {
		return logStatus;
	}

	public void setLogStatus(int logStatus) {
		this.logStatus = logStatus;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public static int getSuccess() {
		return SUCCESS;
	}

	public static int getFailure() {
		return FAILURE;
	}

	public void setGasUsed(String gasUsed) {
		this.gasUsed = gasUsed;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}
}