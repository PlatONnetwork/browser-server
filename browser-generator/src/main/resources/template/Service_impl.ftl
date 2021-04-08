package ${package_path}<#if (model?exists && model!="")>.${model}</#if>.service.impl;

import ${package_path}<#if (model?exists && model!="")>.${model}</#if>.service.${class_name}Service;
import ${package_path}<#if (model?exists && model!="")>.${model}</#if>.mapper.${class_name}Mapper;
import ${package_path}<#if (model?exists && model!="")>.${model}</#if>.entity.${class_name};
import ${package_path}<#if (model?exists && model!="")>.${model}</#if>.service.*;


import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
/**
 * ${tableRemark!}
 * @author ${author}
 */
@Service("${class_name?uncap_first}Service")
public class ${class_name}ServiceImpl implements ${class_name}Service {
    @Resource
    private ${class_name}Mapper ${class_name?uncap_first}Mapper;

    /**
     * 插入数据
     *
     * @param entity
     *            需要插入的对象
     * @return 插入的记录条数
     */
    public int insert(${class_name} entity){
      return ${class_name?uncap_first}Mapper.insert(entity);
    }

    /**
     * 批量插入
     *
     * @param list
     *            插入的列表
     */
    public int insertBatch(List<${class_name}> list){
     return ${class_name?uncap_first}Mapper.insertBatch(list);
    }

    /**
     * 删除数据
     *
     * @param id(数据类型要手动修改)
     *            删除的记录的id
     * @return 删除的条数
     */
    public int delete(String id){
    return ${class_name?uncap_first}Mapper.delete(id);
    }

    /**
     * 批量删除
     *
     * @param ids(数据类型要手动修改)
     *            所有需要删除的记录的id
     * @return 删除的条数
     */
    public int deleteBatch(String[] ids){
    return ${class_name?uncap_first}Mapper.deleteBatch(ids);
    }

     /**
     * 删除数据列表
     *
     * @param entity
     *            需要删除的对象
     * @return 删除的条数
     */
    public int deleteList(${class_name} entity){
    return ${class_name?uncap_first}Mapper.deleteList(entity);
    }

    /**
     * 更新数据
     *
     * @param entity
     *            更新的记录的数据
     * @return 更新的条数
     */
    public int update(${class_name} entity){
    return ${class_name?uncap_first}Mapper.update(entity);
    }

    /**
     * 批量更新
     *
     * @param list
     *            批量更新的记录
     * @return 更新的记录条数
     */
    public int updateBatch(List<${class_name}> list){
    return ${class_name?uncap_first}Mapper.updateBatch(list);
    }

    /**
     * 查询所有数据列表
     *
     * @return 所有表中的数据
     */
    public List<${class_name}> findAll(){
    return ${class_name?uncap_first}Mapper.findAll();
    }

    /**
     * 查询数据列表，如果需要分页,请设置分页对象
     *
     * @param model
     *            查询列表的参数
     * @return 符合条件的列表数据
     */
    public List<${class_name}> findList(${class_name} model){
    return ${class_name?uncap_first}Mapper.findList(model);
    }

    /**
     * 模糊查询数据列表
     *
     * @param model
     *            查询列表的参数
     * @return 符合条件的列表数据
     */
     public List<${class_name}> findListByFuzzyQuery(${class_name} model){
     return ${class_name?uncap_first}Mapper.findListByFuzzyQuery(model);
    }

    /**
     * 查询数据记录
     *
     * @param model
     *            查询参数
     * @return 符合条件的记录条数
     */
    public int getCount(${class_name} model){
    return ${class_name?uncap_first}Mapper.getCount(model);
    }

    /**
     * 获取单条数据
     *
     * @param id
     *            查询的id
     * @return 查询到的数据
     */
    public ${class_name} get(String id){
    return ${class_name?uncap_first}Mapper.get(id);
    }

}
