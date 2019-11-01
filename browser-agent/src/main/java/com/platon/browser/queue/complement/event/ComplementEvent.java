package com.platon.browser.queue.complement.event;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import lombok.Data;

import java.util.List;

@Data
public class ComplementEvent {
    // 区块信息
    private CollectionBlock block;
    // 交易列表
    private List<CollectionTransaction> transactions;
    // 业务参数
    private List<BusinessParam> businessParams;
    private ComplementEvent(){}
    public static ComplementEvent newInstance(){
        return new ComplementEvent();
    }
    public ComplementEvent setBlock(CollectionBlock block){
        this.block=block;
        return this;
    }
    public ComplementEvent setTransactions(List<CollectionTransaction> transactions){
        this.transactions=transactions;
        return this;
    }
    public ComplementEvent setEpochMessage(List<BusinessParam> businessParams){
        this.businessParams=businessParams;
        return this;
    }
}
