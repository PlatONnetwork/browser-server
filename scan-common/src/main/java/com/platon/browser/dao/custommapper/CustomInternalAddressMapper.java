package com.platon.browser.dao.custommapper;

import com.github.pagehelper.Page;
import com.platon.browser.bean.CountBalance;
import com.platon.browser.dao.entity.InternalAddress;
import com.platon.browser.dao.entity.InternalAddressExample;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface CustomInternalAddressMapper {

    int batchInsertOrUpdateSelective(@Param("list") Collection<InternalAddress> list, @Param("selective") InternalAddress.Column... selective);

    /**
     * 查询统计的余额
     *
     * @param
     * @return java.util.List<com.platon.browser.bean.CountBalance>
     * @date 2021/5/15
     */
    List<CountBalance> countBalance();

    /**
     * 根据条件查询列表
     *
     * @param example
     * @return
     * @method selectByExample
     */
    Page<InternalAddress> selectListByExample(InternalAddressExample example);

    /**
     * 返回参与计算的基金会地址
     * @return
     */
    List<String> listCalculableFoundationAddress();

    /**
     * 查询内部地址列表，固定按address排列
     * @param internalAddressType
     * @param offset
     * @param pageSize
     * @return
     */
    List<InternalAddress> listInternalAddress(@Param("internalAddressType") int internalAddressType, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 更新余额和锁仓金额
     * @param values
     */
    void updateBalanceAndRestrictingBalance(Collection<InternalAddress> values);
}
