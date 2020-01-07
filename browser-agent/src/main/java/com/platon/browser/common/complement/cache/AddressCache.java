package com.platon.browser.common.complement.cache;

import com.platon.browser.common.enums.AddressTypeEnum;
import com.platon.browser.complement.dao.param.delegate.DelegateRewardClaim;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ContractDescEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.param.claim.Reward;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地址统计缓存
 */
@Component
public class AddressCache {
    private Map<String,Address> addressMap = new HashMap<>();
    
    public void update(Transaction tx) {
    	String from = tx.getFrom();
    	String to = tx.getTo();
    	updateAddress(tx,from);
    	updateAddress(tx,to);
    }
    
    public Collection<Address> getAll(){
    	return addressMap.values();
    }
    
    public void cleanAll() {
    	addressMap.clear();
    }
    
    private void updateAddress(Transaction tx, String addr) {
    	Address address = addressMap.get(addr);
    	if(address == null) {
    		address = createDefaultAddress(addr);
    		addressMap.put(addr, address);
    	}
    	
		address.setTxQty(address.getTxQty() + 1);
		switch (tx.getTypeEnum()){
		    case TRANSFER: // 转账交易
			 address.setTransferQty(address.getTransferQty() + 1);
		    break;
		case STAKE_CREATE:// 创建验证人
		case STAKE_INCREASE:// 增加自有质押
		case STAKE_MODIFY:// 编辑验证人
		case STAKE_EXIT:// 退出验证人
		case REPORT:// 举报验证人
			 address.setStakingQty(address.getStakingQty()+1);
		    break;
		case DELEGATE_CREATE:// 发起委托
		case DELEGATE_EXIT:// 撤销委托
			case CLAIM_REWARDS:// 领取委托奖励
			 address.setDelegateQty(address.getDelegateQty()+1);
		    break;
		case PROPOSAL_TEXT:// 创建文本提案
		case PROPOSAL_UPGRADE:// 创建升级提案
		case PROPOSAL_PARAMETER:// 创建参数提案
		case PROPOSAL_VOTE:// 提案投票
		case PROPOSAL_CANCEL:// 取消提案
		case VERSION_DECLARE:// 版本声明
		   	 address.setProposalQty(address.getProposalQty()+1); 
		        break;
		    default:
		}    	
    }
    
    private Address createDefaultAddress(String addr) {
    	Address address = new Address();
    	address.setAddress(addr);
        // 设置地址类型
        if(InnerContractAddrEnum.getAddresses().contains(addr)){
            // 内置合约地址
        	address.setType(AddressTypeEnum.INNER_CONTRACT.getCode());
        }else{
            // 主动发起交易的都认为是账户地址因为当前川陀版本无wasm
        	address.setType(AddressTypeEnum.ACCOUNT.getCode());
        }
        
        ContractDescEnum cde = ContractDescEnum.getMap().get(addr);
        if(cde!=null){
        	address.setContractName(cde.getContractName());
        	address.setContractCreate(cde.getCreator());
        	address.setContractCreatehash(cde.getContractHash());
        } else {
        	address.setContractName("");
        	address.setContractCreate("");
        	address.setContractCreatehash("");
		}    	
    	
        address.setTxQty(0);
        address.setTransferQty(0);
        address.setStakingQty(0);
        address.setDelegateQty(0);
        address.setProposalQty(0);
    	return address;
    }

	/**
	 * 初始化
	 * @param addressList 地址实体列表
	 */
	public void init(List<Address> addressList) {
		if(addressList.isEmpty()) return;
		addressMap.clear();
		addressList.forEach(address -> addressMap.put(address.getAddress(),address));
	}
	
	/**
	 * 第一次启动初始化
	 */
	public void initOnFirstStart() {
		for(ContractDescEnum contractDescEnum : ContractDescEnum.values()) {
			addressMap.put(contractDescEnum.getAddress(), createDefaultAddress(contractDescEnum.getAddress()));
		}
	}

	/**
	 * 更新已领取的委托奖励字段
	 * @param drc
	 */
	public void update(DelegateRewardClaim drc) {
		Address cache = addressMap.get(drc.getAddress());
		if(cache == null) {
			cache = createDefaultAddress(drc.getAddress());
			addressMap.put(drc.getAddress(), cache);
		}
		// 统计当前交易from地址的【已领取委托奖励】
		BigDecimal totalAmount = BigDecimal.ZERO;
		for (Reward reward : drc.getRewardList()) {
			totalAmount = totalAmount.add(reward.getReward());
		}
		cache.setHaveReward(cache.getHaveReward().add(totalAmount));
	}
}
