package com.platon.browser.bean;

import com.platon.browser.dao.entity.Token;
import com.platon.browser.enums.ErcTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class ErcToken extends Token {
    private ErcTypeEnum typeEnum;
    private boolean dirty = false;
    public synchronized void setDirty(boolean bol){
        this.dirty=bol;
    }
    public ErcToken() {
        setTokenTxQty(0);
        setHolder(0);
        Date date = new Date();
        setCreateTime(date);
        setUpdateTime(date);
    }
}
