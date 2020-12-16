package com.platon.browser.complement.dao.mapper;

import com.platon.browser.param.sync.*;
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
    @Transactional
    void syncTokenTxCount(
            @Param("addressParams") List<AddressTokenQtyUpdateParam> addressParams,
            @Param("tokenParams") List<Erc20TokenTxCountUpdateParam> tokenParams,
            @Param("tokenAddressParams") List<Erc20TokenAddressRelTxCountUpdateParam> tokenAddressParams,
            @Param("networkParam") NetworkStatTokenQtyUpdateParam networkParam
    );

    @Transactional
    void updateTotalSupply(@Param("totalSupplyParams")List<TotalSupplyUpdateParam> totalSupplyParams);
}