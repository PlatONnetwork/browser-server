package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.param.BusinessParam;
import org.springframework.transaction.annotation.Transactional;

/*
 * @Auther: dongqile
 * @Date:  2019/11/2
 * @Description:
 */
public interface NewBlockMapper {
    /**
     * 新区块相关数据更新
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void newBlock(BusinessParam param);
}