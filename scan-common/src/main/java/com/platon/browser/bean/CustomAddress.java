package com.platon.browser.bean;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.enums.AddressTypeEnum;
import com.platon.browser.enums.ContractDescEnum;
import com.platon.browser.enums.ContractTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:02
 * @Description: 地址实体扩展类
 */
@Data
public class CustomAddress extends Address {

    private ContractTypeEnum contractType;
    private int option;

    private CustomAddress() {
    }

    private static CustomAddress initDefaultAccountAddress(String addr){
        CustomAddress address = new CustomAddress();
        address.setAddress(addr);
        // 先默认置为账户地址，具体是什么类型，由调用此方法的后续逻辑决定并设置
        address.setType(AddressTypeEnum.ACCOUNT.getCode());
        ContractDescEnum cde = ContractDescEnum.getMap().get(addr);
        if (cde != null) {
            address.setContractName(cde.getContractName());
            address.setContractCreate(cde.getCreator());
            address.setContractCreatehash(cde.getContractHash());
        } else {
            address.setContractName("");
            address.setContractCreate("");
            address.setContractCreatehash("");
        }

        address.setTxQty(0);
        address.setErc20TxQty(0);
        address.setErc721TxQty(0);
        address.setErc1155TxQty(0);
        address.setTransferQty(0);
        address.setStakingQty(0);
        address.setDelegateQty(0);
        address.setProposalQty(0);
        address.setHaveReward(BigDecimal.ZERO);
        return address;
    }


    public static CustomAddress createDefaultAccountAddress(String addr, Option option){
        CustomAddress address = initDefaultAccountAddress(addr);
        address.setOption(option);
        return address;
    }


    /*public enum Option {
        NEW, SUICIDED, REWARD_CLAIM, PENDING
    }*/

    public enum Option {
        PENDING(0), //没什么用
        NEW(1),
        REWARD_CLAIM(2),
        SUICIDED(4),

        RESET_TYPE(8);

        private int mask;

        private Option(int mask) {
            this.mask = mask;
        }
    }

    private int setting=0x0;
    public void setOption(Option ...flags) {
        for(Option flag:flags){
            setting|=flag.mask;
        }
    }

    public void unsetOption(Option ...flags) {
        for(Option flag:flags){
            setting &=~flag.mask;
        }
    }

    public boolean hasOption(Option flag) {
        return (setting & flag.mask)==flag.mask;
    }
}
