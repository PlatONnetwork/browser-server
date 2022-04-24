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

    void batchUpdateTokenInfo(@Param("list") List<TokenInventoryWithBLOBs> list);

}