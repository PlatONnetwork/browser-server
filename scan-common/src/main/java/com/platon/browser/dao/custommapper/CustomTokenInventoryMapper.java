package com.platon.browser.dao.custommapper;

import com.platon.browser.bean.CustomTokenInventory;
import com.platon.browser.bean.Erc721ContractDestroyBalanceVO;
import com.platon.browser.dao.entity.NftObject;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryKey;
import com.platon.browser.dao.entity.TokenInventoryWithBLOBs;
import com.platon.browser.elasticsearch.dto.ErcTx;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomTokenInventoryMapper {

    int batchInsertOrUpdateSelective(@Param("list") List<TokenInventoryWithBLOBs> list, @Param("selective") TokenInventory.Column... selective);

    void burnAndDelTokenInventory(@Param("list") List<TokenInventoryKey> list);

    CustomTokenInventory selectTokenInventory(TokenInventoryKey key);

    List<Erc721ContractDestroyBalanceVO> findErc721ContractDestroyBalance(@Param("tokenAddress") String tokenAddress);

    void batchUpdateTokenInfo(@Param("list") List<com.platon.browser.dao.entity.NftObject> list);

    long findMaxId();

    void batchUpdateTokenUrl(List<NftObject> nftList);

    void burn(List<ErcTx> tobeDeletedTokenList);

    /**
     * 如果on duplicate, 则说明这个NFT转给了其它人。owner发生了改变
     * @param tobeInsertOnDuplicateUpdateList
     */
    void insertOnDuplicateUpdate(List<ErcTx> tobeInsertOnDuplicateUpdateList);
}
