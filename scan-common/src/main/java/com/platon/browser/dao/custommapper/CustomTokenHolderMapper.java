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
     * @date 2021/4/3
     */
    Page<CustomTokenHolder> findErc721TokenHolder(@Param("tokenAddress") String tokenAddress, @Param("address") String address, @Param("type") String type);

    int batchInsertOrUpdateSelective(@Param("list") List<TokenHolder> list, @Param("selective") TokenHolder.Column... selective);

    int batchInsert(@Param("list") List<TokenHolder> list);

    int batchUpdateTokenTxQty(@Param("list")  List<TokenHolder> insert);

    /**
     * 批量更新token持有者余额
     *
     * @param list
     * @return int
     * @date 2021/3/18
     */
    int batchUpdate(@Param("list") List<TokenHolder> list);


    /**
     * 批量改变token持有者余额，交易次数
     * 参数中，TokenHolder.increment保存的是变动量，如果是增加余额，此值为正数；如果是减少余额，此值为负数。
     * 这样，在sql中的逻辑，可以统一为：新余额 = 原余额+变动量
     * 由于INSERT INTO ON DUPLICATE KEY UPDATE容易引起死锁，
     * 因此，实际的SQL使有两个SQL组成：首先update，把存在的记录的变动量更新；再insert into ignore，把新的记录插入而忽略已经存在的记录
     * @param list
     * @return
     */
    int batchChange(@Param("list") List<TokenHolder> list);
    /**
     * 查询token对应的持有人的数量
     *
     * @param
     * @return java.util.List<com.platon.browser.bean.TokenHolderCount>
     * @date 2021/3/17
     */
    List<TokenHolderCount> findTokenHolderCount();

    /**
     * 查询erc721的TokenHolderList
     *
     * @param tokenAddress
     * @param address
     * @return com.github.pagehelper.Page<com.platon.browser.bean.CustomTokenHolder>
     * @date 2021/3/19
     */
    Page<CustomTokenHolder> selectListByERC721(@Param("tokenAddress") String tokenAddress, @Param("address") String address);

    /**
     * 取余额为0的token holder
     *
     * @param type
     * @return
     */
    List<TokenHolder> getZeroBalanceTokenHolderList(@Param("type") String type, @Param("offset") int offset, @Param("limit") int limit, @Param("orderby") String orderby);

}
