package com.platon.browser.dao.custommapper;

import com.github.pagehelper.Page;
import com.platon.browser.bean.CustomTokenHolder;
import com.platon.browser.bean.TokenHolderCount;
import com.platon.browser.dao.entity.TokenHolder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomTokenHolderMapper {

    Page<CustomTokenHolder> selectListByParams(@Param("tokenAddress") String tokenAddress, @Param("address") String address, @Param("type") String type);

    Page<CustomTokenHolder> selectERC721Holder(@Param("tokenAddress") String tokenAddress);

    /**
     * 查询erc721令牌数量
     *
     * @param tokenAddress
     * @param address
     * @param type
     * @return com.github.pagehelper.Page<com.platon.browser.bean.CustomTokenHolder>
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/3
     */
    Page<CustomTokenHolder> findErc721TokenHolder(@Param("tokenAddress") String tokenAddress, @Param("address") String address, @Param("type") String type);

    int batchInsertOrUpdateSelective(@Param("list") List<TokenHolder> list, @Param("selective") TokenHolder.Column... selective);

    /**
     * 批量更新token持有者余额
     *
     * @param list
     * @return int
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/18
     */
    int batchUpdate(@Param("list") List<TokenHolder> list);

    /**
     * 查询token对应的持有人的数量
     *
     * @param
     * @return java.util.List<com.platon.browser.bean.TokenHolderCount>
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/17
     */
    List<TokenHolderCount> findTokenHolderCount();

    /**
     * 查询erc721的TokenHolderList
     *
     * @param tokenAddress
     * @param address
     * @return com.github.pagehelper.Page<com.platon.browser.bean.CustomTokenHolder>
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/19
     */
    Page<CustomTokenHolder> selectListByERC721(@Param("tokenAddress") String tokenAddress, @Param("address") String address);

    /**
     * 取余额为0的token holder
     * @param type
     * @return
     */
    List<TokenHolder> getZeroBalanceTokenHolderList(
        @Param("type")String type,
        @Param("offset")int offset,
        @Param("limit")int limit,
        @Param("orderby")String orderby
    );
}