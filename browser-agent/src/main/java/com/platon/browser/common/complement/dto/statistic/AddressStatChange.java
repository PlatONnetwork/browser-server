package com.platon.browser.common.complement.dto.statistic;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * 地址统计变更消息 <br/>
 * <pre>
insert into `address` 
	(`address`, 
	`type`, 
	`tx_qty`, 
	`transfer_qty`, 
	`delegate_qty`, 
	`staking_qty`, 
	`proposal_qty`, 
	`contract_name`, 
	`contract_create`, 
	`contract_createHash`
	)
	values
	('address', 
	'type', 
	'tx_qty', 
	'transfer_qty', 
	'delegate_qty', 
	'staking_qty', 
	'proposal_qty', 
	'contract_name', 
	'contract_create', 
	'contract_createHash'
	)
	on duplicate key update
	`tx_qty` = `tx_qty` + @, 
	`transfer_qty` = `transfer_qty` + @, 
	`delegate_qty` = `delegate_qty` + @, 
	`staking_qty` = `staking_qty` + @, 
	`proposal_qty` = `proposal_qty` + @;
 * <pre/>
 * @author chendai
 */
@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class AddressStatChange extends BusinessParam {

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 地址类型 1:账号 2:合约 3:内置合约
	 */
    private Integer type;

	/**
	 * 交易总数
	 */
    private Integer txQty;

	/**
	 * 转账交易总数
	 */
    private Integer transferQty;

	/**
	 * 委托交易总数
	 */
    private Integer delegateQty;

	/**
	 * 质押交易总数
	 */
    private Integer stakingQty;

	/**
	 * 治理交易总数
	 */
    private Integer proposalQty;

	/**
	 * 合约名称
	 */
    private String contractName;

	/**
	 * 合约创建者地址
	 */
    private String contractCreate;

	/**
	 * 创建合约的交易Hash
	 */
    private String contractCreatehash;

    @Override
	public BusinessType getBusinessType() {
		return BusinessType.ADDRESS_STATISTIC;
	}
}
