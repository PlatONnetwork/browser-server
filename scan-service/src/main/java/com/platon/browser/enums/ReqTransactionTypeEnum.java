package com.platon.browser.enums;

import com.platon.browser.elasticsearch.dto.Transaction.TypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易类型请求枚举
 *  @file ReqTransactionTypeEnum.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public enum ReqTransactionTypeEnum {

	/** 
    * 0：转账 1：合约发布  2：合约调用    5：MPC交易
	* 6：ERC20合约发布(合约创建)
	* 7：ERC20合约调用(合约执行)
	* 8：ERC721合约发布(合约创建)
	* 9：ERC721合约调用(合约执行)
    * 1000: 发起质押  1001: 修改质押信息  1002: 增持质押  1003: 撤销质押 1004: 发起委托  1005: 减持/撤销委托
    * 2000: 提交文本提案 2001: 提交升级提案 2002: 提交参数提案 2003: 给提案投票 2004: 版本声明
    * 3000: 举报多签
    * 4000: 创建锁仓计划
    */
	TRANSACTION_TRANSFER("transfer","0","转账"),
    TRANSACTION_DELEGATE("delegate","1","委托"),
    TRANSACTION_STAKING("staking","2","验证人"),
    TRANSACTION_PROPOSAL("proposal","3","提案");
	private ReqTransactionTypeEnum(String name,String code,String description) {
		this.code = code;
		this.name = name;
		this.description = description;
	}
	
	private String name;
	
	private String code;
	
	private String description;

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * 页面不同的类型进行转换，显示出需要显示的数据
	 * @method getTxType
	 * @param typeName
	 * @return
	 */
	public static List<Object> getTxType(String typeName){
		List<Object> list = new ArrayList<>();
		if(ReqTransactionTypeEnum.TRANSACTION_TRANSFER.getName().equals(typeName)) {
			list.add(String.valueOf(TypeEnum.TRANSFER.getCode()));
		}
		if(ReqTransactionTypeEnum.TRANSACTION_DELEGATE.getName().equals(typeName)) {
			list.add(String.valueOf(TypeEnum.DELEGATE_CREATE.getCode()));
			list.add(String.valueOf(TypeEnum.DELEGATE_EXIT.getCode()));
			list.add(String.valueOf(TypeEnum.CLAIM_REWARDS.getCode()));
		}
		if(ReqTransactionTypeEnum.TRANSACTION_STAKING.getName().equals(typeName)) {
			list.add(String.valueOf(TypeEnum.STAKE_CREATE.getCode()));
			list.add(String.valueOf(TypeEnum.STAKE_MODIFY.getCode()));
			list.add(String.valueOf(TypeEnum.STAKE_INCREASE.getCode()));
			list.add(String.valueOf(TypeEnum.STAKE_EXIT.getCode()));
			list.add(String.valueOf(TypeEnum.REPORT.getCode()));
		}
		if(ReqTransactionTypeEnum.TRANSACTION_PROPOSAL.getName().equals(typeName)) {
			list.add(String.valueOf(TypeEnum.PROPOSAL_TEXT.getCode()));
			list.add(String.valueOf(TypeEnum.PROPOSAL_UPGRADE.getCode()));
			list.add(String.valueOf(TypeEnum.PROPOSAL_PARAMETER.getCode()));
			list.add(String.valueOf(TypeEnum.PROPOSAL_CANCEL.getCode()));
			list.add(String.valueOf(TypeEnum.PROPOSAL_VOTE.getCode()));
			list.add(String.valueOf(TypeEnum.VERSION_DECLARE.getCode()));
		}
		return list;
	}
}
