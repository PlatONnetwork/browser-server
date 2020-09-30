package com.platon.browser.client;

import com.alaya.protocol.core.methods.response.Log;
import com.alaya.rlp.solidity.RlpDecoder;
import com.alaya.rlp.solidity.RlpList;
import com.alaya.rlp.solidity.RlpString;
import com.alaya.rlp.solidity.RlpType;
import com.alaya.utils.Numeric;
import org.apache.commons.lang3.StringUtils;

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
    private List<String> topics;

    private int logStatus;

    private String failReason;

    public int getStatus() {
        if (null == this.status)
            return SUCCESS;
        BigInteger statusQuantity = Numeric.decodeQuantity(this.status);
        return BigInteger.ONE.equals(statusQuantity) ? SUCCESS : FAILURE;
    }

    public BigInteger getGasUsed() {
        return Numeric.decodeQuantity(this.gasUsed);
    }

    /**
     * 解码log
     */
    public void decodeLogs() {
        if (this.logs == null || this.logs.isEmpty()) {
            this.logStatus = FAILURE;
            return;
        }
        Log log = this.logs.get(0);
        String data = log.getData();
        if (StringUtils.isBlank(data)) {
            this.logStatus = FAILURE;
            return;
        }
        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(data));
        List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);
        if (statusCode == 0) {
            this.logStatus = SUCCESS;
        } else {
            this.failReason = decodedStatus;
            this.logStatus = FAILURE;
        }
    }

    public Long getBlockNumber() {
        return this.blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public List<Log> getLogs() {
        return this.logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public String getTransactionHash() {
        return this.transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getTransactionIndex() {
        return this.transactionIndex;
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public int getLogStatus() {
        return this.logStatus;
    }

    public void setLogStatus(int logStatus) {
        this.logStatus = logStatus;
    }

    public String getFailReason() {
        return this.failReason;
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
        return this.contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public List<String> getTopics() {
        return this.topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}