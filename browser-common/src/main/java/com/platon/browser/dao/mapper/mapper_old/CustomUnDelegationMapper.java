//package com.platon.browser.dao.mapper.mapper_old;
//
//import com.platon.browser.dto.CustomUnDelegation;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//
//import java.util.List;
//import java.util.Set;
//
//@Mapper
//public interface CustomUnDelegationMapper {
//
//    List<CustomUnDelegation> selectByNodeId ( @Param("nodeId") String nodeId );
//    List<CustomUnDelegation> selectByNodeIdList ( @Param("nodeIds") List <String> nodeIds );
//    int batchInsertOrUpdateSelective ( @Param("list") Set <UnDelegation> list, @Param("selective") UnDelegation.Column... selective );
//}
