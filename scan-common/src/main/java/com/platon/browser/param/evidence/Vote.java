
package com.platon.browser.param.evidence;

import lombok.Data;

@Data
public class Vote {

    private int epoch;
    private int viewNumber;
    private String blockHash;
    private int blockNumber;
    private int blockIndex;
    private ValidateNode validateNode;
    private String signature;


}