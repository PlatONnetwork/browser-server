package com.platon.browser.util;

import com.github.pagehelper.PageHelper;
import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.PendingTxExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
public class DatabaseTool extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseTool.class);
    private static final int pageNum = 1;
    private static final int pageSize = 100;


}
