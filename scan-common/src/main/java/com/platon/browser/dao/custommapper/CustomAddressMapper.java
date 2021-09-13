package com.platon.browser.dao.custommapper;

import com.platon.browser.bean.CustomAddressDetail;
import com.platon.browser.bean.RecoveredDelegationAmount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomAddressMapper {

    CustomAddressDetail findAddressDetail(String address);
    /**
     * 批量更新
     */
    int batchUpdateByAddress(@Param("list") List<RecoveredDelegationAmount> list);
}
