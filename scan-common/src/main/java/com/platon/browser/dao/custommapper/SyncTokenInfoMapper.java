package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.TokenHolder;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.param.sync.*;
import com.platon.browser.task.bean.TokenHolderNum;
import com.platon.browser.task.bean.TokenHolderType;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * token信息同步
 */
public interface SyncTokenInfoMapper {

    /**
     * 同步Token交易数量
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void syncTokenTxCount(@Param("addressParams") List<AddressTokenQtyUpdateParam> addressParams, @Param("tokenParams") List<Erc20TokenTxCountUpdateParam> tokenParams, @Param("tokenAddressParams") List<Erc20TokenAddressRelTxCountUpdateParam> tokenAddressParams, @Param("networkParam") NetworkStatTokenQtyUpdateParam networkParam);

    @Transactional(rollbackFor = {Exception.class, Error.class})
    void updateTotalSupply(@Param("totalSupplyParams") List<TotalSupplyUpdateParam> totalSupplyParams);

    /**
     * 更新供应总量
     *
     * @param totalSupplyParams 需要批量更新的list
     * @return void
     * @date 2021/1/18
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void updateTokenTotalSupply(@Param("totalSupplyParams") List<TotalSupplyUpdateParam> totalSupplyParams);

    /**
     * 更新地址代币余额
     *
     * @param list 需要批量更新的list
     * @return void
     * @date 2021/1/18
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void updateAddressBalance(@Param("list") List<TokenHolder> list);

    /**
     * 更新token_inventory
     *
     * @param list 需要批量更新的list
     * @return void
     * @date 2021/1/18
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void updateTokenInventory(@Param("list") List<TokenInventory> list);

    /**
     * 查询合约地址的用户统计数
     *
     * @return java.util.List<com.platon.browser.task.bean.TokenHolderNum>
     * @date 2021/1/19
     */
    List<TokenHolderNum> findTokenHolder();

    /**
     * 更新token对应的持有人的数量
     *
     * @param list 需要批量更新的list
     * @return void
     * @date 2021/1/19
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void updateTokenHolder(@Param("list") List<TokenHolderNum> list);

    /**
     * 查询token_holder并返回合约类型
     *
     * @return java.util.List<com.platon.browser.task.bean.TokenHolderType>
     * @date 2021/1/20
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    List<TokenHolderType> findTokenHolderType();

}