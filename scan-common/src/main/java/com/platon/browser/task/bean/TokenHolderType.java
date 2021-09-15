package com.platon.browser.task.bean;

import com.platon.browser.dao.entity.TokenHolder;
import lombok.Data;

@Data
public class TokenHolderType extends TokenHolder {

    /**
     * 合约类型
     */
    private String type;

}
