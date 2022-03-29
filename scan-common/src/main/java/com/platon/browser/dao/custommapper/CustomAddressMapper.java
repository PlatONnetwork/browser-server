package com.platon.browser.dao.custommapper;

import com.platon.browser.bean.AddressErcQty;
import com.platon.browser.bean.AddressQty;
import com.platon.browser.bean.CustomAddressDetail;
import com.platon.browser.bean.RecoveredDelegationAmount;
import com.platon.browser.dao.entity.Address;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CustomAddressMapper {

    CustomAddressDetail findAddressDetail(String address);

    /**
     * 批量更新
     */
    int batchUpdateByAddress(@Param("list") List<RecoveredDelegationAmount> list);

    /**
     * 查找不同类型下的销毁合约
     *
     * @param type:
     * @return: java.util.List<java.lang.String>
     * @date: 2021/9/27
     */
    List<String> findContractDestroy(@Param("type") Integer type);

    /**
     * 更新地址已领取委托奖励
     *
     * @param address:
     * @param amount:
     * @return: void
     * @date: 2021/12/2
     */
    void updateAddressHaveReward(@Param("address") String address, @Param("amount") BigDecimal amount);

    /**
     * 批量更新地址表的erc交易数
     *
     * @param list:
     * @return: int
     * @date: 2021/12/6
     */
    int batchUpdateAddressErcQty(@Param("list") List<AddressErcQty> list);

    /**
     * 批量更新地址表的交易数
     *
     * @param list:
     * @return: int
     * @date: 2021/12/6
     */
    int batchUpdateAddressQty(@Param("list") List<AddressQty> list);

    /**
     * 批量更新地址信息
     *
     * @param list:
     * @return: int
     * @date: 2021/12/13
     */
    int batchUpdateAddressInfo(@Param("list") List<Address> list);

}
