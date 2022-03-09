package com.platon.browser.dao.custommapper;

import com.platon.browser.bean.CustomTokenInventory;
import com.platon.browser.bean.Erc721ContractDestroyBalanceVO;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryKey;
import com.platon.browser.dao.entity.TokenInventoryWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomTokenInventoryMapper {

    int batchInsertOrUpdateSelective(@Param("list") List<TokenInventoryWithBLOBs> list, @Param("selective") TokenInventory.Column... selective);

    void burnAndDelTokenInventory(@Param("list") List<TokenInventoryKey> list);

    CustomTokenInventory selectTokenInventory(TokenInventoryKey key);

    List<Erc721ContractDestroyBalanceVO> findErc721ContractDestroyBalance(@Param("tokenAddress") String tokenAddress);

    /**
     * 查找token_inventory中销毁的合约记录
     *
     * @param minId:
     * @param maxId:
     * @param retryNum:
     * @return: java.util.List<com.platon.browser.dao.entity.TokenInventoryWithBLOBs>
     * @date: 2022/2/10
     */
    List<TokenInventoryWithBLOBs> findDestroyContracts(@Param("minId") Long minId, @Param("maxId") Long maxId, @Param("retryNum") Integer retryNum);

    void batchUpdateTokenInfo(@Param("list") List<TokenInventoryWithBLOBs> list);
}