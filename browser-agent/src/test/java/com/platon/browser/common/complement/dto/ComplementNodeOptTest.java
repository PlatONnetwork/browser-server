package com.platon.browser.common.complement.dto;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.web3j.utils.Collection;

import java.util.Date;
import java.util.List;
@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class ComplementNodeOptTest extends AgentTestBase {

    @Test
    public void Test(){
        List <CollectionTransaction> list = transactionList;
        ComplementNodeOpt complementNodeOpt = ComplementNodeOpt.newInstance();
        complementNodeOpt.setBNum(list.get(0).getNum());
        complementNodeOpt.setCreTime(new Date());
        complementNodeOpt.setDesc("test");
        complementNodeOpt.setId(1L);
        complementNodeOpt.setNodeId("0x12fdsfj1984h498r14r");
        complementNodeOpt.setTime(new Date());
        complementNodeOpt.setTxHash(list.get(0).getHash());
        complementNodeOpt.setType(list.get(0).getType());
        complementNodeOpt.setUpdTime(new Date());
    }
}
