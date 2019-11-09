//package com.platon.browser.dao.mapper.mapper_old;
//
//import com.platon.browser.dto.CustomSlash;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//
//import java.util.List;
//import java.util.Set;
//
//@Mapper
//public interface CustomSlashMapper {
//    List<CustomSlash> selectByNodeId ( @Param("nodeId") String nodeId );
//    List<CustomSlash> selectByNodeIdList ( @Param("nodeIds") List <String> nodeIds );
//    int batchInsertOrUpdateSelective ( @Param("list") Set <Slash> list, @Param("selective") Slash.Column... selective );
//}
