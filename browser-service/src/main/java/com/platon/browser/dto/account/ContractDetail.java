package com.platon.browser.dto.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper=false)
public class ContractDetail extends AddressDetail {
    private String developer;
    private int ownerCount;
    public void init(AddressDetail initData){
        BeanUtils.copyProperties(initData,this);
    }
}
