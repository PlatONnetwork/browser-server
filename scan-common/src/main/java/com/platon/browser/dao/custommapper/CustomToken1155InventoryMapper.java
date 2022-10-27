package com.platon.browser.dao.custommapper;

import com.platon.browser.bean.CustomToken1155Inventory;
import com.platon.browser.bean.Erc1155ContractDestroyBalanceVO;
import com.platon.browser.dao.entity.Token1155Inventory;
import com.platon.browser.dao.entity.Token1155InventoryKey;
import com.platon.browser.dao.entity.Token1155InventoryWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomToken1155InventoryMapper {

    int batchInsertOrUpdateSelective(@Param("list") List<Token1155InventoryWithBLOBs> list, @Param("selective") Token1155Inventory.Column... selective);

    void burnAndDelTokenInventory(@Param("list") List<Token1155InventoryKey> list);

    CustomToken1155Inventory selectTokenInventory(Token1155InventoryKey key);

    List<Erc1155ContractDestroyBalanceVO> findErc1155ContractDestroyBalance(@Param("tokenAddress") String tokenAddress);


    /**
     * 查找token_inventory中销毁的合约记录
     *
     * @param minId:
     * @param maxId:
     * @param retryNum:
     * @return: java.util.List<com.platon.browser.dao.entity.TokenInventoryWithBLOBs>
     * @date: 2022/2/10
     */
    List<Token1155InventoryWithBLOBs> findDestroyContracts(@Param("minId") Long minId, @Param("maxId") Long maxId, @Param("retryNum") Integer retryNum);

    Token1155InventoryWithBLOBs findOneByUK(@Param("key") Token1155InventoryKey key);

    long findMaxId();

}