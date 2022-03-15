package com.platon.browser.v0152.contract;

import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.methods.response.Log;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import lombok.Data;
import org.apache.http.MethodNotSupportedException;

import java.math.BigInteger;
import java.util.List;

public interface ErcContract {
    RemoteCall<String> name();
    RemoteCall<String> symbol();
    RemoteCall<BigInteger> decimals();
    RemoteCall<BigInteger> totalSupply();
    RemoteCall<BigInteger> balanceOf(String who, BigInteger id);
    List<ErcTxEvent> getTxEvents(final TransactionReceipt transactionReceipt);
    RemoteCall<String> getTokenURI(BigInteger tokenId) throws MethodNotSupportedException;
    @Data
    class ErcTxEvent {
        private Log log;
        // erc1155的操作人
        private String operator;
        private String from;
        private String to;
        // erc 721 1155 的tokenId
        private BigInteger tokenId;
        // 如果是erc20,erc721,erc1155 value表示转账数量
        private BigInteger value;
    }
}
