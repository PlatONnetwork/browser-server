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
    RemoteCall<BigInteger> balanceOf(String who);
    List<ErcTxEvent> getTxEvents(final TransactionReceipt transactionReceipt);
    RemoteCall<String> getTokenURI(BigInteger tokenId) throws MethodNotSupportedException;
    @Data
    class ErcTxEvent {
        private Log log;
        private String from;
        private String to;
        // 如果是erc20，value表示转账数量；erc721，value表示tokenId
        private BigInteger value;
    }
}
