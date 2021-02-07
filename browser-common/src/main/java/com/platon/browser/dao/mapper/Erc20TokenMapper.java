package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.entity.Erc20TokenExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface Erc20TokenMapper {
    long countByExample(Erc20TokenExample example);

    int deleteByExample(Erc20TokenExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Erc20Token record);

    int insertSelective(Erc20Token record);

    List<Erc20Token> selectByExample(Erc20TokenExample example);

    Erc20Token selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Erc20Token record, @Param("example") Erc20TokenExample example);

    int updateByExample(@Param("record") Erc20Token record, @Param("example") Erc20TokenExample example);

    int updateByPrimaryKeySelective(Erc20Token record);

    int updateByPrimaryKey(Erc20Token record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table erc20_token
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int batchInsert(@Param("list") List<Erc20Token> list);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table erc20_token
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int batchInsertSelective(@Param("list") List<Erc20Token> list, @Param("selective") Erc20Token.Column ... selective);

    int batchUpdateTxCount(@Param("list") List<Erc20Token> list);
    List<Erc20Token> listErc20Token(Map params);
    int totalErc20Token(Map params);
    Erc20Token selectByAddress(@Param("address") String address);
    List<Erc20Token> listErc20TokenIds(Map params);
    List<Erc20Token> listErc20TokenByIds(List<Long> list);
}