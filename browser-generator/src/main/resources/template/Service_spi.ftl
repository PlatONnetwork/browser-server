package ${package_path}<#if (model?exists && model!="")>.${model}</#if>.service;

import ${package_path}<#if (model?exists && model!="")>.${model}</#if>.entity.${class_name};
import java.util.List;

/**
 * ${tableRemark!}
 * @author ${author}
 */
public interface ${class_name}Service  {

    /**
     * 插入数据
     *
     * @param entity
     *            需要插入的对象
     * @return 插入的记录条数
     */
    public int insert(${class_name} entity);

    /**
     * 批量插入
     *
     * @param list
     *            插入的列表
     */
    int insertBatch(List<${class_name}> list);

    /**
     * 删除数据
     *
     * @param id(数据类型要手动修改)
     *            删除的记录的id
     * @return 删除的条数
     */
    int delete(String id);

    /**
     * 批量删除
     *
     * @param ids(数据类型要手动修改)
     *            所有需要删除的记录的id
     * @return 删除的条数
     */
    int deleteBatch(String[] ids);

     /**
     * 删除数据列表
     *
     * @param entity
     *            需要删除的对象
     * @return 删除的条数
     */
    int deleteList(${class_name} entity);

    /**
     * 更新数据
     *
     * @param entity
     *            更新的记录的数据
     * @return 更新的条数
     */
    int update(${class_name} entity);

    /**
     * 批量更新
     *
     * @param list
     *            批量更新的记录
     * @return 更新的记录条数
     */
    int updateBatch(List<${class_name}> list);

    /**
     * 查询所有数据列表
     *
     * @return 所有表中的数据
     */
    List<${class_name}> findAll();

    /**
     * 查询数据列表，如果需要分页,请设置分页对象
     *
     * @param model
     *            查询列表的参数
     * @return 符合条件的列表数据
     */
    List<${class_name}> findList(${class_name} model);

    /**
     * 模糊查询数据列表
     *
     * @param model
     *            查询列表的参数
     * @return 符合条件的列表数据
     */
    List<${class_name}> findListByFuzzyQuery(${class_name} model);

    /**
     * 查询数据记录
     *
     * @param model
     *            查询参数
     * @return 符合条件的记录条数
     */
    int getCount(${class_name} model);

    /**
     * 获取单条数据
     *
     * @param id
     *            查询的id
     * @return 查询到的数据
     */
    ${class_name} get(String id);

}
