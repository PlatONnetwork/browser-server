package com.platon.browser.client.result;

import lombok.Data;

import java.util.List;
@Data
public class RpcTxReceiptResult {
    private String jsonrpc;
    private int id;
    private List<Result> result;
}