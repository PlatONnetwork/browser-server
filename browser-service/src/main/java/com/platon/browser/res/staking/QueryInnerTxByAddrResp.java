package com.platon.browser.res.staking;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;
import lombok.Data;

import java.math.BigDecimal;

/***
*@program: QueryInnerTxByAddrResp.java
*@description: 
*@author: Rongjin Zhang
*@create: 2020/9/22
*/
@Data
public class QueryInnerTxByAddrResp {

	private String hash;    //交易hash
	private String from;          //from地址
	private String to;    //接收方
	private Long time;    //时间
	private Long nowTime;   //现在时间
	private String transValue;      //转账金额
	private String tokenName;     //token名称
	private String tokenAddr;     //token地址
	private String symbol;     //符号

}
