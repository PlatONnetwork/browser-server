package com.platon.browser.complement.dao.mapper;

import com.platon.browser.param.boostrapsync.AddressTokenQtyUpdateParam;
import com.platon.browser.param.boostrapsync.Erc20TokenAddressRelTxCountUpdateParam;
import com.platon.browser.param.boostrapsync.Erc20TokenTxCountUpdateParam;
import com.platon.browser.param.boostrapsync.NetworkStatTokenQtyUpdateParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface SyncTokenTxCountMapper {
    /**
     * 同步Token交易数量
     * @param param
     */
    @Transactional
    void syncTokenTxCount(
            @Param("addressParams") List<AddressTokenQtyUpdateParam> addressParams,
            @Param("tokenParams") List<Erc20TokenTxCountUpdateParam> tokenParams,
            @Param("tokenAddressParams") List<Erc20TokenAddressRelTxCountUpdateParam> tokenAddressParams,
            @Param("networkParam") NetworkStatTokenQtyUpdateParam networkParam
    );
}