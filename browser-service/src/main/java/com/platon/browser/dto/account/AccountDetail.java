package com.platon.browser.dto.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper=false)
public class AccountDetail extends AddressDetail {
    private String votePledge;
    private int nodeCount;
    public void init(AddressDetail initData){
        BeanUtils.copyProperties(initData,this);
    }
}
