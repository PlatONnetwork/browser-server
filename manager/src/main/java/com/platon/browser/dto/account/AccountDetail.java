package com.platon.browser.dto.account;

import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class AccountDetail extends AddressDetail {
    private String votePledge;
    private int nodeCount;
    public void init(AddressDetail initData){
        BeanUtils.copyProperties(initData,this);
    }
}
