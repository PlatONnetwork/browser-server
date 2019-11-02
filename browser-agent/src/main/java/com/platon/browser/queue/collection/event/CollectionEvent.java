package com.platon.browser.queue.collection.event;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.collection.dto.EpochMessage;
import lombok.Getter;

import java.util.List;

@Getter
public class CollectionEvent {
    // 区块信息
    private CollectionBlock block;
    // 交易列表
    private List<CollectionTransaction> transactions;
    private EpochMessage epochMessage;
    private CollectionEvent(){}
    public static CollectionEvent newInstance(){
        return new CollectionEvent();
    }
    public CollectionEvent setBlock(CollectionBlock block){
        this.block=block;
        return this;
    }
    public CollectionEvent setTransactions(List<CollectionTransaction> transactions){
        this.transactions=transactions;
        return this;
    }
    public CollectionEvent setEpochMessage(EpochMessage epochMessage){
        this.epochMessage=epochMessage;
        return this;
    }
}
