package com.platon.browser.dao.custommapper;

import com.github.pagehelper.Page;
import com.platon.browser.bean.*;
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
     * @param type: {@link com.platon.browser.enums.ErcTypeEnum}
     * @return: java.util.List<com.platon.browser.bean.DestroyContract>
     * @date: 2021/10/25
     */
    List<DestroyContract> findDestroyContract(@Param("type") String type);

    /**
     * 批量更新token的交易数
     *
     * @param list:
     * @return: int
     * @date: 2021/12/6
     */
    int batchUpdateTokenQty(@Param("list") List<TokenQty> list);

    /**
     * 更新token总供应量
     *
     * @param list:
     * @return: int
     * @date: 2021/12/14
     */
    int batchUpdateTokenTotalSupply(@Param("list") List<Token> list);

    int updateErc20TokenHolderCount();
    int updateErc1155TokenHolderCount();


    /**
     * 查询所有没有被销毁的，可以更新totalSupply的token(即合约是erc20或者是erc721并且isSupportErc721Enumeration==true)
     * 注意：scan-agent可能落后于特殊节点，即在db中，合约还没有销毁，但是特殊节点已经是销毁状态了。
     * 所以，获取此结果后，如果需要到特殊节点查询此合约的某些信息，就会返回0x
     * @return
     */
    List<Token> listNotDestroyedToUpdateTotalSupply(@Param("offset") int offset, @Param("pageSize") int pageSize);

    void updateProxyToken(@Param("offset")ContractInfo proxy, @Param("impl")ContractInfo impl);
}
