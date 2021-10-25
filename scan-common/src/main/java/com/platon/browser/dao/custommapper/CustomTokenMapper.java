package com.platon.browser.dao.custommapper;

import com.github.pagehelper.Page;
import com.platon.browser.bean.CustomToken;
import com.platon.browser.bean.CustomTokenDetail;
import com.platon.browser.bean.DestroyContract;
import com.platon.browser.dao.entity.Token;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomTokenMapper {

    Page<CustomToken> selectListByType(@Param("type") String type);

    CustomTokenDetail selectDetailByAddress(@Param("address") String address);

    int batchInsertOrUpdateSelective(@Param("list") List<Token> list, @Param("selective") Token.Column... selective);

    /**
     * 查找销毁的合约
     *
     * @param type: {@link com.platon.browser.v0152.enums.ErcTypeEnum}
     * @return: java.util.List<com.platon.browser.bean.DestroyContract>
     * @date: 2021/10/25
     */
    List<DestroyContract> findDestroyContract(@Param("type") String type);

}