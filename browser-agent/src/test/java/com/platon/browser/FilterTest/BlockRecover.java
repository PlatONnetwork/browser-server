package com.platon.browser.FilterTest;

import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/1/21
 * Time: 15:27
 */
@RunWith(SpringRunner.class)
public class BlockRecover extends TestBase {

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Test
    public void blockRecover(){
        List<String> numberList = new ArrayList <>();
        BlockExample blockExample = new BlockExample();
        blockExample.createCriteria().andChainIdEqualTo("1");
        blockExample.setOrderByClause("number desc");
        List<Block> blockList = blockMapper.selectByExample(blockExample);
        blockList.forEach(block -> {

        });
    }

}