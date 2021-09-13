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

}