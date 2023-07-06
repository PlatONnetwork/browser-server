package com.platon.browser;

import com.platon.protocol.core.methods.response.PlatonBlock;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-14 13:45:31
 **/
@Data
@Slf4j
public class BlockBean {
    private String number;
    private String hash;
    private String parentHash;
    private String nonce;
    private String sha3Uncles;
    private String logsBloom;
    private String transactionsRoot;
    private String stateRoot;
    private String receiptsRoot;
    private String author;
    private String miner;
    private String mixHash;
    private String difficulty;
    private String totalDifficulty;
    private String extraData;
    private String size;
    private String gasLimit;
    private String gasUsed;
    private String timestamp;
    private List<PlatonBlock.TransactionObject> transactions;
    private List<String> uncles;
    private List<String> sealFields;
}
