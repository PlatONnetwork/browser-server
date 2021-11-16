package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.param.BusinessParam;
import org.springframework.transaction.annotation.Transactional;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface DelegateBusinessMapper {
    /**
     * 发起委托
     * @param param
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void create ( BusinessParam param );

    /**
     * 退出委托
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void exit ( BusinessParam param );

    /**
     * // TODO: 编写领取委托奖励入库业务逻辑SQL
     * 领取委托奖励
     * @param param
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void claim(BusinessParam param);
}