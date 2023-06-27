package com.platon.browser.dao.custommapper;

import com.github.pagehelper.Page;
import com.platon.browser.bean.*;
import com.platon.browser.dao.entity.Token1155Holder;
import com.platon.browser.dao.entity.Token1155HolderKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomToken1155HolderMapper {

    /**
     * 根据唯一索引查询
     *
     * @param token1155HolderKey:
     * @return: com.platon.browser.dao.entity.Token1155Holder
     * @date: 2022/8/3
     */
    Token1155Holder selectByUK(@Param("token1155HolderKey") Token1155HolderKey token1155HolderKey);

    /**
     * 批量新增或更新
     *
     * @param list:
     * @param selective:
     * @return: int
     * @date: 2022/8/3
     */
    int batchInsertOrUpdateSelective1155(@Param("list") List<Token1155Holder> list, @Param("selective") Token1155Holder.Column... selective);

    /**
     * 批量更新token持有者余额
     *
     * @param list:
     * @return: int
     * @date: 2022/8/3
     */
    int batchUpdate(@Param("list") List<Token1155Holder> list);

    /**
     * 查找销毁的合约
     *
     * @param type:
     * @return: java.util.List<com.platon.browser.bean.Erc1155ContractDestroyBean>
     * @date: 2022/8/3
     */
    List<Erc1155ContractDestroyBean> findDestroyContract(@Param("type") Integer type);

    /**
     * 查找合约下的持有者
     *
     * @param contract:
     * @return: com.github.pagehelper.Page<com.platon.browser.bean.Token1155HolderListBean>
     * @date: 2022/8/5
     */
    Page<Token1155HolderListBean> findToken1155HolderList(@Param("contract") String contract);

    /**
     * 库存列表
     *
     * @param key:
     * @return: com.github.pagehelper.Page<com.platon.browser.bean.TokenIdListBean>
     * @date: 2022/8/5
     */
    Page<TokenIdListBean> findTokenIdList(@Param("key") Token1155HolderKey key);

    /**
     * 查询erc1155的TokenHolderList
     *
     * @param tokenAddress
     * @param address
     * @return com.github.pagehelper.Page<com.platon.browser.bean.CustomTokenHolder>
     * @author dexin.y@digquant.com
     * @date 2022/2/12
     */
    Page<CustomTokenHolder> selectListByERC1155(@Param("tokenAddress") String tokenAddress, @Param("address") String address);

    Page<CustomTokenHolder> findErc1155TokenHolder(@Param("address") String address);

    Page<TokenHolderCount> findToken1155Holder();

    /**
     * 批量改变token持有者余额，交易次数
     * 参数中，TokenHolder.increment保存的是变动量，如果是增加余额，此值为正数；如果是减少余额，此值为负数。
     * 这样，在sql中的逻辑，可以统一为：新余额 = 原余额+变动量
     * @param list
     * @return
     */
    void batchChange(@Param("list") List<Token1155Holder> list);
}
