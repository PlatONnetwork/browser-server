package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockPage;
import com.platon.browser.dto.app.transaction.TxIncomeSumDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomBlockMapper {
    List<Block> selectByPage(@Param("page") BlockPage page);

    List<Block> selectBlockByNumber(@Param("chainId") String chainId, @Param("start") long start, @Param("end") long end);

    void updateBlockNodeName(@Param("chainId") String chainId);

    List<TxIncomeSumDto> getIncomeByVoteHash(@Param("chainId") String chainId, @Param("txHashes") List<String> txHashes);
}
